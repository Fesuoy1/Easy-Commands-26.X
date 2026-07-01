package org.mod.easy_commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.mod.easy_commands.command.enchant.EnchantAxeCommand;
import org.mod.easy_commands.command.enchant.EnchantBowCommand;
import org.mod.easy_commands.command.enchant.EnchantCrossbowCommand;
import org.mod.easy_commands.command.enchant.EnchantFishingRodCommand;
import org.mod.easy_commands.command.enchant.EnchantHoeCommand;
import org.mod.easy_commands.command.enchant.EnchantMaceCommand;
import org.mod.easy_commands.command.enchant.EnchantPickaxeCommand;
import org.mod.easy_commands.command.enchant.EnchantShovelCommand;
import org.mod.easy_commands.command.enchant.EnchantSpearCommand;
import org.mod.easy_commands.command.enchant.EnchantSwordCommand;
import org.mod.easy_commands.command.enchant.EnchantTridentCommand;
import org.mod.easy_commands.command.enchant.KnockbackCommand;
import org.mod.easy_commands.command.enchant.KnockbackStickCommand;
import org.mod.easy_commands.command.explosive.ExplodeCommand;
import org.mod.easy_commands.command.explosive.SetExplosionPowerCommand;
import org.mod.easy_commands.command.explosive.ToggleExplosiveProjectilesCommand;
import org.mod.easy_commands.command.health.FeedCommand;
import org.mod.easy_commands.command.health.HealCommand;
import org.mod.easy_commands.command.kill.KillAllCommand;
import org.mod.easy_commands.command.repair.RepairAllCommand;
import org.mod.easy_commands.command.repair.RepairCommand;
import org.mod.easy_commands.command.repair.RepairInventoryCommand;
import org.mod.easy_commands.command.time.DayCommand;
import org.mod.easy_commands.command.time.MidnightCommand;
import org.mod.easy_commands.command.time.NightCommand;
import org.mod.easy_commands.command.time.NoonCommand;
import org.mod.easy_commands.command.tree.ModifyTreeHeightCommand;
import org.mod.easy_commands.command.tree.ResetTreeHeightCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyCommands implements ModInitializer {
    public static final String MOD_ID = "easy_commands";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        try {
            ModGameRules.register();
            LOGGER.info("Easy Commands Initialized");
            CommandRegistrationCallback.EVENT.register(this::registerCommands);
            ServerLifecycleEvents.SERVER_STARTED.register(server -> ModGameRules.worlds = server.getAllLevels());
            ServerLifecycleEvents.SERVER_STOPPED.register(_ -> ModGameRules.worlds = null);
        } catch (Exception e) {
            LOGGER.error("Easy Commands failed to initialize: {}", e.getMessage());
        }
    }

    private void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection environment) {
        // Repair Commands
        RepairCommand.register(dispatcher);
        RepairInventoryCommand.register(dispatcher);
        RepairAllCommand.register(dispatcher);

        // Enchant Commands
        EnchantSwordCommand.register(dispatcher);
        EnchantPickaxeCommand.register(dispatcher);
        EnchantAxeCommand.register(dispatcher);
        EnchantShovelCommand.register(dispatcher);
        EnchantHoeCommand.register(dispatcher);
        EnchantBowCommand.register(dispatcher);
        EnchantCrossbowCommand.register(dispatcher);
        EnchantMaceCommand.register(dispatcher);
        EnchantSpearCommand.register(dispatcher);
        EnchantTridentCommand.register(dispatcher);
        EnchantFishingRodCommand.register(dispatcher);
        KnockbackCommand.register(dispatcher);
        KnockbackStickCommand.register(dispatcher);

        // Kill Commands
        KillAllCommand.register(dispatcher);
        
        // Health Commands
        HealCommand.register(dispatcher);
        FeedCommand.register(dispatcher);

        // Time Commands
        DayCommand.register(dispatcher);
        NoonCommand.register(dispatcher);
        NightCommand.register(dispatcher);
        MidnightCommand.register(dispatcher);

        // Explosion Commands
        SetExplosionPowerCommand.register(dispatcher);
        ToggleExplosiveProjectilesCommand.register(dispatcher);
        ExplodeCommand.register(dispatcher);

        // Tree Commands
        ModifyTreeHeightCommand.register(dispatcher);
        ResetTreeHeightCommand.register(dispatcher);
    }
}
