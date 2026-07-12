package org.mod.easy_commands.neoforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;
import net.minecraft.gametest.framework.GameTestServer;
import net.minecraft.server.Main;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class GameTestMixin {
    private static final String PROPERTY = "easy_commands.gametest";
    private static final String UNIVERSE_DIR = "gametestserver";

    @ModifyExpressionValue(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/Eula;hasAgreedToEULA()Z"))
    private static boolean isEulaAgreedTo(boolean isEulaAgreedTo) {
        return System.getProperty(PROPERTY) != null || isEulaAgreedTo;
    }

    @Inject(method = "main", cancellable = true, at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/packs/repository/ServerPacksSource;createPackRepository(Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;)Lnet/minecraft/server/packs/repository/PackRepository;"))
    private static void main(String[] args, CallbackInfo info) throws Exception {
        if (System.getProperty(PROPERTY) != null) {
            startGameTestServer(args);
            info.cancel();
        }
    }

    private static void startGameTestServer(String[] args) throws Exception {
        Path universeDir = Paths.get(UNIVERSE_DIR);
        if (Files.exists(universeDir)) {
            try (var stream = Files.walk(universeDir)) {
                stream.sorted(java.util.Comparator.reverseOrder()).forEach(path -> {
                    try { Files.deleteIfExists(path); } catch (IOException e) {}
                });
            }
        }
        Files.createDirectories(universeDir);

        LevelStorageSource.LevelStorageAccess levelStorageAccess = LevelStorageSource.createDefault(universeDir).createAccess("gametestworld");
        PackRepository packRepository = net.minecraft.server.packs.repository.ServerPacksSource.createPackRepository(levelStorageAccess);

        GameTestServer gameTestServer = GameTestServer.create(
            new Thread("GameTestServer"),
            levelStorageAccess,
            packRepository,
            Optional.empty(),
            false,
            1
        );
        MinecraftServer.spin(thread -> gameTestServer);
    }
}
