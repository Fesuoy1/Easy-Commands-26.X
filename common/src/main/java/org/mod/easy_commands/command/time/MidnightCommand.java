package org.mod.easy_commands.command.time;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class MidnightCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        try {
            context.getSource().dispatcher().execute("time set 18000", context.getSource());
        } catch (com.mojang.brigadier.exceptions.CommandSyntaxException e) {
            context.getSource().sendFailure(Component.literal("Failed to set time"));
        }
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("midnight")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .executes(new MidnightCommand()));
    }
}
