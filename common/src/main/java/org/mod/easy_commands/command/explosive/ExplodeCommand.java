package org.mod.easy_commands.command.explosive;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExplodeCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        ServerLevel world = source.getLevel();
        Vec3 pos = Vec3Argument.getVec3(context, "position");
        float explosionPower = FloatArgumentType.getFloat(context, "explosionPower");
        boolean createFire = false;
        try {
            createFire = BoolArgumentType.getBool(context, "createFire");
        } catch (IllegalArgumentException _) {} // optional
        world.explode(null, pos.x(), pos.y(), pos.z(), explosionPower, createFire, Level.ExplosionInteraction.TNT);
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("explode")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("position", Vec3Argument.vec3())
                .then(Commands.argument("explosionPower", FloatArgumentType.floatArg(0.1f))
                    .suggests((_, builder) -> {
                        builder.suggest(1);
                        builder.suggest(3);
                        builder.suggest(5);
                        builder.suggest(10);
                        return builder.buildFuture();
                    })
                        .executes(new ExplodeCommand())
                    .then(Commands.argument("createFire", BoolArgumentType.bool())
                        .executes(new ExplodeCommand())))));
    }
}
