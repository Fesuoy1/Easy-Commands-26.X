package org.mod.easy_commands.command.explosive;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.mod.easy_commands.ModGameRules;

public class ToggleExplosiveProjectilesCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        context.getSource().getLevel().getGameRules().set(ModGameRules.EXPLOSIVE_PROJECTILES_ENABLED, !ModGameRules.isExplosiveProjectilesEnabled(context.getSource().getLevel()), context.getSource().getServer());
        boolean enabled = context.getSource().getLevel().getGameRules().get(ModGameRules.EXPLOSIVE_PROJECTILES_ENABLED);
        String message = enabled ? "enabled" : "disabled";
        context.getSource().sendSuccess(() -> Component.literal("Explosive projectiles " + message), true);
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("toggleExplosiveProjectiles")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .executes(new ToggleExplosiveProjectilesCommand()));
    }
}
