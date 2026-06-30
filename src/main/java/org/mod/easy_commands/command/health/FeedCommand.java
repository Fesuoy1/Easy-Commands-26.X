package org.mod.easy_commands.command.health;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.Collection;
import java.util.List;

public class FeedCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        Collection<ServerPlayer> players;
        try {
            players = EntityArgument.getPlayers(context, "allPlayers");
        } catch (IllegalArgumentException _) {
            ServerPlayer sender = source.getPlayer();
            if (sender != null) {
                players = List.of(sender);
            } else {
                source.sendFailure(Component.literal("No players found."));
                return Command.SINGLE_SUCCESS;
            }
        }

        if (!players.isEmpty()) {
            for (ServerPlayer player : players) {
                player.getFoodData().eat(20, 20);
                ServerLevel world = player.level();
                world.playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        } else {
            source.sendFailure(Component.literal("No players found."));
        }
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("feed")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .executes(new FeedCommand())
                .then(Commands.argument("allPlayers", EntityArgument.players())
                        .executes(new FeedCommand())));
    }
}
