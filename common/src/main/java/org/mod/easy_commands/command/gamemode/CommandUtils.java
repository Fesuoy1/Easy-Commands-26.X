package org.mod.easy_commands.command.gamemode;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import java.util.Collection;
import java.util.List;

public final class CommandUtils {
    public static int setGameMode(CommandContext<CommandSourceStack> context, GameType gameType, String gamemode) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        Collection<ServerPlayer> players;
        try {
            players = EntityArgument.getPlayers(context, "players");
        } catch (IllegalArgumentException _) {
            ServerPlayer sender = source.getPlayer();
            if (sender != null) {
                players = List.of(sender);
            } else {
                source.sendFailure(Component.literal("No players found."));
                return 0;
            }
        }
        if (!players.isEmpty()) {
            int count = 0;
            for (ServerPlayer player : players) {
                if (player.gameMode() != gameType) {
                    player.setGameMode(gameType);
                    count++;
                }
            }
            final int finalCount = count;
            if (finalCount > 0) {
                source.sendSuccess(() -> Component.literal("Successfully set " + finalCount + " player(s) to " + gamemode), true);
            } else {
                source.sendFailure(Component.literal("No players found."));
                return 0;
            }
        } else {
            source.sendFailure(Component.literal("No players found."));
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }
}
