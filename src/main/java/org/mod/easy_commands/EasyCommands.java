package org.mod.easy_commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.mod.easy_commands.command.enchant.EnchantSwordCommand;
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
            LOGGER.info("Easy Commands Initialized");
            ModGameRules.loaded();
            CommandRegistrationCallback.EVENT.register(this::registerCommands);
            ServerLifecycleEvents.SERVER_STARTED.register(server -> ModGameRules.worlds = server.getAllLevels());
            ServerLifecycleEvents.SERVER_STOPPED.register(_ -> ModGameRules.worlds = null);
        } catch (Exception e) {
            LOGGER.error("Easy Commands failed to initialize: {}", e.getMessage());
        }
    }

    private void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection environment) {
        RepairCommand.register(dispatcher);
        EnchantSwordCommand.register(dispatcher);
        RepairInventoryCommand.register(dispatcher);
        RepairAllCommand.register(dispatcher);
        KnockbackCommand.register(dispatcher);
        KnockbackStickCommand.register(dispatcher);
        KillAllCommand.register(dispatcher);
        HealCommand.register(dispatcher);
        FeedCommand.register(dispatcher);
        DayCommand.register(dispatcher);
        NoonCommand.register(dispatcher);
        NightCommand.register(dispatcher);
        MidnightCommand.register(dispatcher);
        SetExplosionPowerCommand.register(dispatcher);
        ExplodeCommand.register(dispatcher);
        ModifyTreeHeightCommand.register(dispatcher);
        ResetTreeHeightCommand.register(dispatcher);
        ToggleExplosiveProjectilesCommand.register(dispatcher);
    }
}
