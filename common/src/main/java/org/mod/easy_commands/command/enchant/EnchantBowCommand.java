package org.mod.easy_commands.command.enchant;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Map;

public class EnchantBowCommand implements Command<CommandSourceStack> {

    private static final Map<String, ResourceKey<Enchantment>> ENCHANTMENT_MAP = Map.of(
            "power", Enchantments.POWER,
            "punch", Enchantments.PUNCH,
            "flame", Enchantments.FLAME,
            "infinity", Enchantments.INFINITY,
            "mending", Enchantments.MENDING,
            "unbreaking", Enchantments.UNBREAKING
    );

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            Item item = stack.getItem();
            String name = item.getName(stack).getString().toLowerCase();
            if (name.contains("bow") && !name.contains("crossbow") && !name.contains("bowl")) {
                HolderLookup.Provider registries = context.getSource().getServer().registryAccess();
                HolderLookup<Enchantment> lookup = registries.lookupOrThrow(Registries.ENCHANTMENT);
                final boolean[] enchanted = {false};
                EnchantmentHelper.updateEnchantments(stack, mutable -> {
                    for (Map.Entry<String, ResourceKey<Enchantment>> entry : ENCHANTMENT_MAP.entrySet()) {
                        try {
                            mutable.set(lookup.getOrThrow(entry.getValue()), IntegerArgumentType.getInteger(context, entry.getKey()));
                            enchanted[0] = true;
                        } catch (IllegalArgumentException _) {}
                    }
                });
                ServerLevel world = player.level();
                if (enchanted[0])
                    world.playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            } else {
                context.getSource().sendFailure(Component.literal("You must hold a bow to enchant."));
                return Command.SINGLE_SUCCESS;
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("enchantbow")
                .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                .then(Commands.argument("power", IntegerArgumentType.integer(-1))
                    .executes(new EnchantBowCommand())
                .then(Commands.argument("punch", IntegerArgumentType.integer(-1))
                    .executes(new EnchantBowCommand())
                .then(Commands.argument("flame", IntegerArgumentType.integer(-1))
                    .executes(new EnchantBowCommand())
                .then(Commands.argument("infinity", IntegerArgumentType.integer(-1))
                    .executes(new EnchantBowCommand())
                .then(Commands.argument("mending", IntegerArgumentType.integer(-1))
                    .executes(new EnchantBowCommand())
                .then(Commands.argument("unbreaking", IntegerArgumentType.integer(-1))
                    .executes(new EnchantBowCommand())
                )))))));
    }
}
