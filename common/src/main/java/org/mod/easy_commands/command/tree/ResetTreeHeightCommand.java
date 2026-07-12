package org.mod.easy_commands.command.tree;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.mod.easy_commands.ModGameRules;

public class ResetTreeHeightCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        context.getSource().getLevel().getGameRules().set(ModGameRules.TREE_HEIGHT, 1, context.getSource().getServer());
        context.getSource().sendSuccess(() -> Component.literal("Tree height reset to default"), true);
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("resetTreeHeight")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .executes(new ResetTreeHeightCommand()));
    }
}
