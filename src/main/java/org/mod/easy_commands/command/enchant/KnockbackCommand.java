package org.mod.easy_commands.command.enchant;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class KnockbackCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        int level = 10;
        try {
            level = IntegerArgumentType.getInteger(context, "level");
        } catch (IllegalArgumentException _) {}

        final int finalLevel = level;
        ServerPlayer player = context.getSource().getPlayer();
        try {
            ServerPlayer target = EntityArgument.getPlayer(context, "target");
            if (target != null) player = target;
        } catch (IllegalArgumentException | CommandSyntaxException _) {}
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            if (!stack.isEmpty()) {
                HolderLookup.Provider registries = context.getSource().getServer().registryAccess();
                EnchantmentHelper.updateEnchantments(stack, mutable ->
                    mutable.set(registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK), finalLevel));
                player.playSound(SoundEvents.ANVIL_USE, 1.0F, 1.0F);
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("knockback")
                .executes(new KnockbackCommand())
                .then(Commands.argument("level", IntegerArgumentType.integer())
                        .suggests((_, builder) -> {
                            builder.suggest(5);
                            builder.suggest(10);
                            builder.suggest(30);
                            builder.suggest(50);
                            return builder.buildFuture();
                        })
                        .executes(new KnockbackCommand())
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(new KnockbackCommand()))));
    }
}
