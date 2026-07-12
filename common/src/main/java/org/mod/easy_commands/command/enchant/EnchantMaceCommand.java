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

public class EnchantMaceCommand implements Command<CommandSourceStack> {

    private static final Map<String, ResourceKey<Enchantment>> ENCHANTMENT_MAP = Map.of(
            "density", Enchantments.DENSITY,
            "breach", Enchantments.BREACH,
            "windBurst", Enchantments.WIND_BURST,
            "smite", Enchantments.SMITE,
            "baneOfArthropods", Enchantments.BANE_OF_ARTHROPODS,
            "fireAspect", Enchantments.FIRE_ASPECT,
            "mending", Enchantments.MENDING,
            "unbreaking", Enchantments.UNBREAKING
    );

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            Item item = stack.getItem();
            if (item.getName(stack).getString().toLowerCase().contains("mace")) {
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
                context.getSource().sendFailure(Component.literal("You must hold a mace to enchant."));
                return Command.SINGLE_SUCCESS;
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("enchantmace")
                .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                .then(Commands.argument("density", IntegerArgumentType.integer(-1))
                    .executes(new EnchantMaceCommand())
                .then(Commands.argument("breach", IntegerArgumentType.integer(-1))
                    .executes(new EnchantMaceCommand())
                .then(Commands.argument("windBurst", IntegerArgumentType.integer(-1))
                    .executes(new EnchantMaceCommand())
                .then(Commands.argument("smite", IntegerArgumentType.integer(-1))
                    .executes(new EnchantMaceCommand())
                .then(Commands.argument("baneOfArthropods", IntegerArgumentType.integer(-1))
                    .executes(new EnchantMaceCommand())
                .then(Commands.argument("fireAspect", IntegerArgumentType.integer(-1))
                    .executes(new EnchantMaceCommand())
                .then(Commands.argument("mending", IntegerArgumentType.integer(-1))
                    .executes(new EnchantMaceCommand())
                .then(Commands.argument("unbreaking", IntegerArgumentType.integer(-1))
                    .executes(new EnchantMaceCommand())
                )))))))));
    }
}
