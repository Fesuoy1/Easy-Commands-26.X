package org.mod.easy_commands.command.repair;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

public class RepairAllCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        ServerLevel world = source.getLevel();
        final boolean[] repaired = {false};

        boolean repairInventory = false;
        try {
            repairInventory = BoolArgumentType.getBool(context, "repairInventory");
        } catch (IllegalArgumentException _) {}

        if (repairInventory) {
            world.players().forEach(player -> {
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (stack.isDamageableItem()) {
                        stack.setDamageValue(0);
                        repaired[0] = true;
                    }
                }
                world.playSound(null, player.position().x(), player.position().y(), player.position().z(), SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            });
        } else {
            world.players().forEach(player -> {
                ItemStack stack = player.getMainHandItem();
                if (stack.isDamageableItem()) {
                    stack.setDamageValue(0);
                    repaired[0] = true;
                }
                world.playSound(null, player.position().x(), player.position().y(), player.position().z(), SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            });
        }
        if (!repaired[0]) {
            context.getSource().sendFailure(Component.literal("All players have no items that can be repaired or damaged."));
        }
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("repairall")
                .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                .executes(new RepairAllCommand())
                .then(Commands.argument("repairInventory", BoolArgumentType.bool())
                        .executes(new RepairAllCommand())));
    }
}
