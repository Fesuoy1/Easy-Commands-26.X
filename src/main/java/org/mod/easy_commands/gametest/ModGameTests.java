package org.mod.easy_commands.gametest;

import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.mod.easy_commands.EasyCommands;

@EventBusSubscriber(modid = EasyCommands.MOD_ID)
public class ModGameTests {
    @SubscribeEvent
    public static void registerFunctions(RegisterEvent event) {
        event.register(Registries.TEST_FUNCTION, helper -> {
            registerFunction(helper, "test_repair", EasyCommandsGameTests::testRepair);
            registerFunction(helper, "test_repair_with_selector", EasyCommandsGameTests::testRepairWithSelector);
            registerFunction(helper, "test_repair_non_damageable", EasyCommandsGameTests::testRepairNonDamageable);
            registerFunction(helper, "test_repair_inventory", EasyCommandsGameTests::testRepairInventory);
            registerFunction(helper, "test_repair_all", EasyCommandsGameTests::testRepairAll);
            registerFunction(helper, "test_repair_all_inventory", EasyCommandsGameTests::testRepairAllInventory);

            registerFunction(helper, "test_enchant_sword", EasyCommandsGameTests::testEnchantSword);
            registerFunction(helper, "test_enchant_pickaxe", EasyCommandsGameTests::testEnchantPickaxe);
            registerFunction(helper, "test_enchant_axe", EasyCommandsGameTests::testEnchantAxe);
            registerFunction(helper, "test_enchant_shovel", EasyCommandsGameTests::testEnchantShovel);
            registerFunction(helper, "test_enchant_hoe", EasyCommandsGameTests::testEnchantHoe);
            registerFunction(helper, "test_enchant_bow", EasyCommandsGameTests::testEnchantBow);
            registerFunction(helper, "test_enchant_crossbow", EasyCommandsGameTests::testEnchantCrossbow);
            registerFunction(helper, "test_enchant_mace", EasyCommandsGameTests::testEnchantMace);
            registerFunction(helper, "test_enchant_spear", EasyCommandsGameTests::testEnchantSpear);
            registerFunction(helper, "test_enchant_trident", EasyCommandsGameTests::testEnchantTrident);
            registerFunction(helper, "test_enchant_fishing_rod", EasyCommandsGameTests::testEnchantFishingRod);
            registerFunction(helper, "test_enchant_wrong_item", EasyCommandsGameTests::testEnchantWrongItem);

            registerFunction(helper, "test_knockback", EasyCommandsGameTests::testKnockback);
            registerFunction(helper, "test_knockback_level", EasyCommandsGameTests::testKnockbackLevel);
            registerFunction(helper, "test_knockback_stick", EasyCommandsGameTests::testKnockbackStick);
            registerFunction(helper, "test_knockback_stick_level", EasyCommandsGameTests::testKnockbackStickLevel);

            registerFunction(helper, "test_heal", EasyCommandsGameTests::testHeal);
            registerFunction(helper, "test_heal_and_feed", EasyCommandsGameTests::testHealAndFeed);
            registerFunction(helper, "test_feed", EasyCommandsGameTests::testFeed);
            registerFunction(helper, "test_feed_players", EasyCommandsGameTests::testFeedPlayers);

            registerFunction(helper, "test_kill_all", EasyCommandsGameTests::testKillAll);

            registerFunction(helper, "test_day", EasyCommandsGameTests::testDay);
            registerFunction(helper, "test_noon", EasyCommandsGameTests::testNoon);
            registerFunction(helper, "test_night", EasyCommandsGameTests::testNight);
            registerFunction(helper, "test_midnight", EasyCommandsGameTests::testMidnight);

            registerFunction(helper, "test_explode", EasyCommandsGameTests::testExplode);
            registerFunction(helper, "test_set_explosion_power", EasyCommandsGameTests::testSetExplosionPower);
            registerFunction(helper, "test_query_explosion_power", EasyCommandsGameTests::testQueryExplosionPower);
            registerFunction(helper, "test_toggle_explosive_projectiles_on", EasyCommandsGameTests::testToggleExplosiveProjectilesOn);
            registerFunction(helper, "test_toggle_explosive_projectiles_off", EasyCommandsGameTests::testToggleExplosiveProjectilesOff);
            registerFunction(helper, "test_toggle_explosive_projectiles_toggle", EasyCommandsGameTests::testToggleExplosiveProjectilesToggle);

            registerFunction(helper, "test_modify_tree_height", EasyCommandsGameTests::testModifyTreeHeight);
            registerFunction(helper, "test_query_tree_height", EasyCommandsGameTests::testQueryTreeHeight);
            registerFunction(helper, "test_reset_tree_height", EasyCommandsGameTests::testResetTreeHeight);
        });
    }

    private static void registerFunction(RegisterEvent.RegisterHelper<Consumer<GameTestHelper>> helper, String name, Consumer<GameTestHelper> function) {
        helper.register(ResourceKey.create(Registries.TEST_FUNCTION, Identifier.fromNamespaceAndPath("easy_commands", name)), function);
    }

    @SubscribeEvent
    public static void registerTests(RegisterGameTestsEvent event) {
        var env = event.registerEnvironment(
            Identifier.fromNamespaceAndPath("easy_commands", "default_test"),
            new TestEnvironmentDefinition.AllOf());

        var structure = Identifier.withDefaultNamespace("empty");

        register(event, "test_repair", env, structure, 200);
        register(event, "test_repair_with_selector", env, structure, 200);
        register(event, "test_repair_non_damageable", env, structure, 200);
        register(event, "test_repair_inventory", env, structure, 200);
        register(event, "test_repair_all", env, structure, 200);
        register(event, "test_repair_all_inventory", env, structure, 200);

        register(event, "test_enchant_sword", env, structure, 200);
        register(event, "test_enchant_pickaxe", env, structure, 200);
        register(event, "test_enchant_axe", env, structure, 200);
        register(event, "test_enchant_shovel", env, structure, 200);
        register(event, "test_enchant_hoe", env, structure, 200);
        register(event, "test_enchant_bow", env, structure, 200);
        register(event, "test_enchant_crossbow", env, structure, 200);
        register(event, "test_enchant_mace", env, structure, 200);
        register(event, "test_enchant_spear", env, structure, 200);
        register(event, "test_enchant_trident", env, structure, 200);
        register(event, "test_enchant_fishing_rod", env, structure, 200);
        register(event, "test_enchant_wrong_item", env, structure, 200);

        register(event, "test_knockback", env, structure, 200);
        register(event, "test_knockback_level", env, structure, 200);
        register(event, "test_knockback_stick", env, structure, 200);
        register(event, "test_knockback_stick_level", env, structure, 200);

        register(event, "test_heal", env, structure, 200);
        register(event, "test_heal_and_feed", env, structure, 200);
        register(event, "test_feed", env, structure, 200);
        register(event, "test_feed_players", env, structure, 200);

        register(event, "test_kill_all", env, structure, 200);

        register(event, "test_day", env, structure, 200);
        register(event, "test_noon", env, structure, 200);
        register(event, "test_night", env, structure, 200);
        register(event, "test_midnight", env, structure, 200);

        register(event, "test_explode", env, structure, 200);
        register(event, "test_set_explosion_power", env, structure, 200);
        register(event, "test_query_explosion_power", env, structure, 200);
        register(event, "test_toggle_explosive_projectiles_on", env, structure, 200);
        register(event, "test_toggle_explosive_projectiles_off", env, structure, 200);
        register(event, "test_toggle_explosive_projectiles_toggle", env, structure, 200);

        register(event, "test_modify_tree_height", env, structure, 200);
        register(event, "test_query_tree_height", env, structure, 200);
        register(event, "test_reset_tree_height", env, structure, 200);
    }

    private static void register(RegisterGameTestsEvent event, String name,
                                 Holder<TestEnvironmentDefinition<?>> env,
                                 Identifier structure, int maxTicks) {
        var data = new TestData<>(env, structure, maxTicks, 0, true);
        var functionKey = ResourceKey.create(Registries.TEST_FUNCTION, Identifier.fromNamespaceAndPath("easy_commands", name));
        event.registerTest(Identifier.fromNamespaceAndPath("easy_commands", name),
            new FunctionGameTestInstance(functionKey, data));
    }
}
