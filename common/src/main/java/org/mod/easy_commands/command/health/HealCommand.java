package org.mod.easy_commands.command.health;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.Collection;

public class HealCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
        if (!players.isEmpty()) {
            for (ServerPlayer player : players) {
                player.setHealth(player.getMaxHealth());
                try {
                    if (BoolArgumentType.getBool(context, "alsoFeed")) {
                        player.getFoodData().eat(20, 20);
                    }
                } catch (IllegalArgumentException _) {} // optional
                ServerLevel world = player.level();
                world.playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("heal")
                .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                .then(Commands.argument("players", EntityArgument.players())
                        .then(Commands.argument("alsoFeed", BoolArgumentType.bool())
                                .executes(new HealCommand()))
                        .executes(new HealCommand())));
    }
}
