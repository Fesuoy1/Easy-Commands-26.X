package org.mod.easy_commands.command.kill;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class KillAllCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        CommandDispatcher<CommandSourceStack> dispatcher = source.dispatcher();
        boolean shouldAlsoKillPlayers = false;
        try {
            shouldAlsoKillPlayers = BoolArgumentType.getBool(context, "shouldAlsoKillPlayers");
        } catch (IllegalArgumentException _) {} // optional
        final ParseResults<CommandSourceStack> parsed;
        if (shouldAlsoKillPlayers) {
            parsed = dispatcher.parse("kill @e", source);
        } else {
            parsed = dispatcher.parse("kill @e[type=!player]", source);
        }
        dispatcher.execute(parsed);
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("killall")
                .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                    .executes(new KillAllCommand())
                .then(Commands.argument("shouldAlsoKillPlayers", BoolArgumentType.bool())
                    .executes(new KillAllCommand())));
    }
}
