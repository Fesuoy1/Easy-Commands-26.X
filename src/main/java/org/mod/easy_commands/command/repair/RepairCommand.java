package org.mod.easy_commands.command.repair;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

public class RepairCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            if (stack.isDamageableItem()) {
                stack.setDamageValue(0);
                ServerLevel world = player.level();
                world.playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            } else {
                context.getSource().sendFailure(Component.literal("The item you are holding cannot be repaired or damaged."));
                return Command.SINGLE_SUCCESS;
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("repair")
                .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                .executes(new RepairCommand()));
    }
}
