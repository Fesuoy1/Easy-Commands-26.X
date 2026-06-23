package org.mod.easy_commands.command.tree;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.mod.easy_commands.ModGameRules;

public class ModifyTreeHeightCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        int height = IntegerArgumentType.getInteger(context, "height");
        context.getSource().getLevel().getGameRules().set(ModGameRules.TREE_HEIGHT, height, context.getSource().getServer());
        context.getSource().sendSuccess(() -> Component.literal("Tree height set to: " + height), true);
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("modifyTreeHeight")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("height", IntegerArgumentType.integer(1))
                        .executes(new ModifyTreeHeightCommand())));
    }
}
