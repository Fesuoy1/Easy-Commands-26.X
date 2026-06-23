package org.mod.easy_commands.command.enchant;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public class KnockbackStickCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        int level = IntegerArgumentType.getInteger(context, "level");
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            ItemStack stack = enchantStick(context, level);
            int slot = player.getInventory().getFreeSlot();
            if (slot == -1) {
                player.drop(stack, false, false);
            } else {
                player.getInventory().setItem(slot, stack);
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("knockbackstick")
                .then(Commands.argument("level", IntegerArgumentType.integer())
                        .suggests((_, builder) -> {
                            builder.suggest(5);
                            builder.suggest(10);
                            builder.suggest(30);
                            builder.suggest(50);
                            return builder.buildFuture();
                        })
                        .executes(new KnockbackStickCommand())));
    }

    private ItemStack enchantStick(CommandContext<CommandSourceStack> context, int level) {
        ItemStack stack = new ItemStack(Items.STICK);
        HolderLookup.Provider registries = context.getSource().getServer().registryAccess();
        stack.enchant(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK), level);
        stack.set(DataComponents.CUSTOM_NAME, Component.translatable("item.easy_commands.knockback_stick"));
        return stack;
    }
}
