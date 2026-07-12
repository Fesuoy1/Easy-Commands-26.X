package org.mod.easy_commands.command.explosive;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.mod.easy_commands.ModGameRules;

public class SetExplosionPowerCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        try {
            double power = DoubleArgumentType.getDouble(context, "power");
            context.getSource().getLevel().getGameRules().set(ModGameRules.EXPLOSION_POWER, power, context.getSource().getServer());
            context.getSource().sendSuccess(() -> Component.literal("Projectile explosion power set to: " + power), true);
            context.getSource().sendSuccess(() -> Component.literal("Note that this won't work if the gamerule 'easy_commands.explosiveProjectiles' is disabled"), false);
        } catch (IllegalArgumentException _) {
            double current = context.getSource().getLevel().getGameRules().get(ModGameRules.EXPLOSION_POWER);
            context.getSource().sendSuccess(() -> Component.literal("Current explosion power: " + current), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("setExplosionPower")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .executes(new SetExplosionPowerCommand())
                .then(Commands.argument("power", DoubleArgumentType.doubleArg(0.1))
                        .executes(new SetExplosionPowerCommand())));
    }
}
