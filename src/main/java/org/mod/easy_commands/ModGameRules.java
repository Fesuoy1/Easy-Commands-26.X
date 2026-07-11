package org.mod.easy_commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = EasyCommands.MOD_ID)
public class ModGameRules {
    public static GameRule<Integer> TREE_HEIGHT;
    public static GameRule<Boolean> EXPLOSIVE_PROJECTILES_ENABLED;
    public static GameRule<Double> EXPLOSION_POWER;

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(Registries.GAME_RULE, helper -> {
            TREE_HEIGHT = Registry.register(
                BuiltInRegistries.GAME_RULE,
                Identifier.fromNamespaceAndPath("easy_commands", "tree_height"),
                new GameRule<>(
                    GameRuleCategory.MISC,
                    GameRuleType.INT,
                    IntegerArgumentType.integer(1),
                    GameRuleTypeVisitor::visitInteger,
                    Codec.intRange(1, 256),
                    i -> i,
                    1,
                    FeatureFlagSet.of()
                )
            );

            EXPLOSIVE_PROJECTILES_ENABLED = Registry.register(
                BuiltInRegistries.GAME_RULE,
                Identifier.fromNamespaceAndPath("easy_commands", "explosive_projectiles"),
                new GameRule<>(
                    GameRuleCategory.MISC,
                    GameRuleType.BOOL,
                    BoolArgumentType.bool(),
                    GameRuleTypeVisitor::visitBoolean,
                    Codec.BOOL,
                    b -> b ? 1 : 0,
                    false,
                    FeatureFlagSet.of()
                )
            );

            EXPLOSION_POWER = Registry.register(
                BuiltInRegistries.GAME_RULE,
                Identifier.fromNamespaceAndPath("easy_commands", "explosion_power"),
                new GameRule<>(
                    GameRuleCategory.MISC,
                    GameRuleType.INT,
                    DoubleArgumentType.doubleArg(0.1),
                    (visitor, rule) -> visitor.visit(rule),
                    Codec.DOUBLE,
                    d -> (int) Math.round(d),
                    2.5,
                    FeatureFlagSet.of()
                )
            );
        });
    }

    public static Iterable<ServerLevel> worlds;

    public static void explode(@NotNull Entity entity) {
        entity.discard();
        Vec3 pos = entity.position();
        Level level = entity.level();
        level.explode(entity, pos.x(), pos.y(), pos.z(), (float) getExplosionPower(), Level.ExplosionInteraction.TNT);
    }

    public static Boolean isExplosiveProjectilesEnabled(@NotNull Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getGameRules().get(EXPLOSIVE_PROJECTILES_ENABLED);
        }
        return false;
    }

    public static Integer getTreeHeight() {
        if (worlds != null) {
            for (ServerLevel world : worlds) {
                if (world.getGameRules().get(TREE_HEIGHT) > 0) {
                    return world.getGameRules().get(TREE_HEIGHT);
                }
            }
        }
        return 1;
    }

    public static double getExplosionPower() {
        if (worlds != null) {
            for (ServerLevel world : worlds) {
                if (world.getGameRules().get(EXPLOSION_POWER) > 0) {
                    return world.getGameRules().get(EXPLOSION_POWER);
                }
            }
        }
        return 2.5;
    }
}
