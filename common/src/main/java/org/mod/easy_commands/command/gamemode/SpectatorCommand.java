package org.mod.easy_commands.command.gamemode;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.level.GameType;

public class SpectatorCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return CommandUtils.setGameMode(context, GameType.SPECTATOR);
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spectator")
                .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                .executes(new SpectatorCommand())
                .then(Commands.argument("players", EntityArgument.players())
                        .executes(new SpectatorCommand())));
    }
}
