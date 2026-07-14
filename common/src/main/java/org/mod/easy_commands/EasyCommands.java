package org.mod.easy_commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
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
import org.mod.easy_commands.command.gamemode.AdventureCommand;
import org.mod.easy_commands.command.gamemode.CreativeCommand;
import org.mod.easy_commands.command.gamemode.SpectatorCommand;
import org.mod.easy_commands.command.gamemode.SurvivalCommand;
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

public class EasyCommands {
    public static final String MOD_ID = "easy_commands";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        RepairCommand.register(dispatcher);
        RepairInventoryCommand.register(dispatcher);
        RepairAllCommand.register(dispatcher);

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

        KillAllCommand.register(dispatcher);

        HealCommand.register(dispatcher);
        FeedCommand.register(dispatcher);

        DayCommand.register(dispatcher);
        NoonCommand.register(dispatcher);
        NightCommand.register(dispatcher);
        MidnightCommand.register(dispatcher);

        SetExplosionPowerCommand.register(dispatcher);
        ToggleExplosiveProjectilesCommand.register(dispatcher);
        ExplodeCommand.register(dispatcher);

        CreativeCommand.register(dispatcher);
        SurvivalCommand.register(dispatcher);
        AdventureCommand.register(dispatcher);
        SpectatorCommand.register(dispatcher);

        ModifyTreeHeightCommand.register(dispatcher);
        ResetTreeHeightCommand.register(dispatcher);
    }
}
