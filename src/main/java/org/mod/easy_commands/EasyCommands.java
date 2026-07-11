package org.mod.easy_commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
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

@Mod(EasyCommands.MOD_ID)
public class EasyCommands {
    public static final String MOD_ID = "easy_commands";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public EasyCommands(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Easy Commands Initialized");
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        NeoForge.EVENT_BUS.addListener(this::onServerStarting);
        NeoForge.EVENT_BUS.addListener(this::onServerStopping);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

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

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        ModGameRules.worlds = event.getServer().getAllLevels();
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        ModGameRules.worlds = null;
    }
}
