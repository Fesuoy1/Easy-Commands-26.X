package org.mod.easy_commands.neoforge.gametest;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import org.mod.easy_commands.ModGameRules;

public class EasyCommandsGameTests {

    private static ServerPlayer createPlayer(GameTestHelper helper) {
        return helper.makeMockServerPlayerInLevel();
    }

    private static void giveMainHand(ServerPlayer player, ItemStack stack) {
        player.getInventory().setItem(player.getInventory().getSelectedSlot(), stack);
    }

    private static void runCmd(CommandSourceStack source, String command) {
        source.getServer().getCommands().performPrefixedCommand(source, command);
    }

    private static CommandSourceStack asPlayer(ServerPlayer player) {
        return player.createCommandSourceStack();
    }

    private static CommandSourceStack asAdmin(ServerPlayer player) {
        return player.createCommandSourceStack().withPermission(LevelBasedPermissionSet.ADMIN);
    }

    private static CommandSourceStack asGM(ServerPlayer player) {
        return player.createCommandSourceStack().withPermission(LevelBasedPermissionSet.GAMEMASTER);
    }

    private static CommandSourceStack console(GameTestHelper helper) {
        return helper.getLevel().getServer().createCommandSourceStack().withPermission(LevelBasedPermissionSet.ADMIN);
    }

    private static HolderLookup<Enchantment> enchLookup(GameTestHelper helper) {
        return helper.getLevel().getServer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
    }

    private static Holder<Enchantment> ench(GameTestHelper helper, ResourceKey<Enchantment> key) {
        return enchLookup(helper).getOrThrow(key);
    }

    private static int enchLevel(ItemStack stack, Holder<Enchantment> holder) {
        ItemEnchantments ench = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        return ench.getLevel(holder);
    }

    private static long getClockTime(GameTestHelper helper) {
        var dimType = helper.getLevel().dimensionTypeRegistration();
        var clock = dimType.value().defaultClock().orElseThrow();
        return helper.getLevel().clockManager().getTotalTicks(clock);
    }

    public static void testRepair(GameTestHelper helper) {
        var player = createPlayer(helper);
        var stack = new ItemStack(Items.DIAMOND_PICKAXE);
        stack.setDamageValue(50);
        giveMainHand(player, stack);

        runCmd(asAdmin(player), "repair");

        helper.assertTrue(stack.getDamageValue() == 0, "Item should be fully repaired");
        helper.succeed();
    }

    public static void testRepairWithSelector(GameTestHelper helper) {
        var player = createPlayer(helper);
        var stack = new ItemStack(Items.DIAMOND_PICKAXE);
        stack.setDamageValue(50);
        giveMainHand(player, stack);

        runCmd(asAdmin(player), "repair @s");

        helper.assertTrue(stack.getDamageValue() == 0, "Item should be repaired via @s");
        helper.succeed();
    }

    public static void testRepairNonDamageable(GameTestHelper helper) {
        var player = createPlayer(helper);
        giveMainHand(player, new ItemStack(Items.STICK));

        runCmd(asAdmin(player), "repair");
        helper.succeed();
    }

    public static void testRepairInventory(GameTestHelper helper) {
        var player = createPlayer(helper);
        var pick = new ItemStack(Items.DIAMOND_PICKAXE);
        pick.setDamageValue(50);
        player.getInventory().setItem(0, pick);
        var sword = new ItemStack(Items.DIAMOND_SWORD);
        sword.setDamageValue(30);
        player.getInventory().setItem(1, sword);

        runCmd(asAdmin(player), "repairinventory");

        helper.assertTrue(pick.getDamageValue() == 0, "Pickaxe should be repaired");
        helper.assertTrue(sword.getDamageValue() == 0, "Sword should be repaired");
        helper.succeed();
    }

    public static void testRepairAll(GameTestHelper helper) {
        var p1 = createPlayer(helper);
        var p2 = createPlayer(helper);
        var s1 = new ItemStack(Items.DIAMOND_PICKAXE);
        s1.setDamageValue(50);
        giveMainHand(p1, s1);
        var s2 = new ItemStack(Items.DIAMOND_SWORD);
        s2.setDamageValue(30);
        giveMainHand(p2, s2);

        runCmd(asAdmin(p1), "repairall");

        helper.assertTrue(s1.getDamageValue() == 0, "P1's item should be repaired");
        helper.assertTrue(s2.getDamageValue() == 0, "P2's item should be repaired by repairall");
        helper.succeed();
    }

    public static void testRepairAllInventory(GameTestHelper helper) {
        var p1 = createPlayer(helper);
        var p2 = createPlayer(helper);
        var pick = new ItemStack(Items.DIAMOND_PICKAXE);
        pick.setDamageValue(50);
        p1.getInventory().setItem(0, pick);
        var sword = new ItemStack(Items.DIAMOND_SWORD);
        sword.setDamageValue(30);
        p2.getInventory().setItem(0, sword);

        runCmd(asAdmin(p1), "repairall true");

        helper.assertTrue(pick.getDamageValue() == 0, "P1 inventory items should be repaired");
        helper.assertTrue(sword.getDamageValue() == 0, "P2 inventory items should be repaired by repairall true");
        helper.succeed();
    }

    public static void testRepairOtherPlayer(GameTestHelper helper) {
        var executor = createPlayer(helper);
        var target = createPlayer(helper);
        var executorStack = new ItemStack(Items.DIAMOND_PICKAXE);
        executorStack.setDamageValue(50);
        giveMainHand(executor, executorStack);
        var targetStack = new ItemStack(Items.DIAMOND_SWORD);
        targetStack.setDamageValue(30);
        giveMainHand(target, targetStack);

        String tag = "repair_target_" + target.getStringUUID().replace("-", "");
        target.addTag(tag);
        runCmd(asAdmin(executor), "repair @p[tag=" + tag + "]");

        helper.assertTrue(targetStack.getDamageValue() == 0, "Target's item should be repaired");
        helper.assertTrue(executorStack.getDamageValue() == 50, "Executor's item should stay damaged");
        helper.succeed();
    }

    public static void testRepairInventoryOtherPlayer(GameTestHelper helper) {
        var executor = createPlayer(helper);
        var target = createPlayer(helper);
        var executorPick = new ItemStack(Items.DIAMOND_PICKAXE);
        executorPick.setDamageValue(50);
        executor.getInventory().setItem(0, executorPick);
        var targetSword = new ItemStack(Items.DIAMOND_SWORD);
        targetSword.setDamageValue(30);
        target.getInventory().setItem(0, targetSword);

        String tag = "repairinv_target_" + target.getStringUUID().replace("-", "");
        target.addTag(tag);
        runCmd(asAdmin(executor), "repairinventory @p[tag=" + tag + "]");

        helper.assertTrue(targetSword.getDamageValue() == 0, "Target inventory should be repaired");
        helper.assertTrue(executorPick.getDamageValue() == 50, "Executor inventory should stay damaged");
        helper.succeed();
    }

    public static void testEnchantSword(GameTestHelper helper) {
        var player = createPlayer(helper);
        var stack = new ItemStack(Items.DIAMOND_SWORD);
        giveMainHand(player, stack);

        runCmd(asAdmin(player), "enchantsword 5 3 1 3 2 2 3");

        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.SHARPNESS)) == 5, "Sharpness V");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.SWEEPING_EDGE)) == 3, "Sweeping Edge III");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.MENDING)) == 1, "Mending I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.UNBREAKING)) == 3, "Unbreaking III");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.FIRE_ASPECT)) == 2, "Fire Aspect II");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.KNOCKBACK)) == 2, "Knockback II");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.LOOTING)) == 3, "Looting III");
        helper.succeed();
    }

    public static void testEnchantPickaxe(GameTestHelper helper) {
        var player = createPlayer(helper);
        var stack = new ItemStack(Items.DIAMOND_PICKAXE);
        giveMainHand(player, stack);

        runCmd(asAdmin(player), "enchantpickaxe 5 3 1 3 1");

        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.EFFICIENCY)) == 5, "Efficiency V");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.FORTUNE)) == 3, "Fortune III");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.MENDING)) == 1, "Mending I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.UNBREAKING)) == 3, "Unbreaking III");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.SILK_TOUCH)) == 1, "Silk Touch I");
        helper.succeed();
    }

    public static void testEnchantAxe(GameTestHelper helper) {
        var player = createPlayer(helper);
        var stack = new ItemStack(Items.DIAMOND_AXE);
        giveMainHand(player, stack);

        runCmd(asAdmin(player), "enchantaxe 5 3 1 5 1 3");

        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.EFFICIENCY)) == 5, "Efficiency V");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.FORTUNE)) == 3, "Fortune III");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.SILK_TOUCH)) == 1, "Silk Touch I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.SHARPNESS)) == 5, "Sharpness V");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.MENDING)) == 1, "Mending I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.UNBREAKING)) == 3, "Unbreaking III");
        helper.succeed();
    }

    public static void testEnchantShovel(GameTestHelper helper) {
        var player = createPlayer(helper);
        var stack = new ItemStack(Items.DIAMOND_SHOVEL);
        giveMainHand(player, stack);

        runCmd(asAdmin(player), "enchantshovel 5 3 1 1 3");

        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.EFFICIENCY)) == 5, "Efficiency V");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.FORTUNE)) == 3, "Fortune III");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.SILK_TOUCH)) == 1, "Silk Touch I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.MENDING)) == 1, "Mending I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.UNBREAKING)) == 3, "Unbreaking III");
        helper.succeed();
    }

    public static void testEnchantHoe(GameTestHelper helper) {
        var player = createPlayer(helper);
        var stack = new ItemStack(Items.DIAMOND_HOE);
        giveMainHand(player, stack);

        runCmd(asAdmin(player), "enchanthoe 5 3 1 1 3");

        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.EFFICIENCY)) == 5, "Efficiency V");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.FORTUNE)) == 3, "Fortune III");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.SILK_TOUCH)) == 1, "Silk Touch I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.MENDING)) == 1, "Mending I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.UNBREAKING)) == 3, "Unbreaking III");
        helper.succeed();
    }

    public static void testEnchantBow(GameTestHelper helper) {
        var player = createPlayer(helper);
        var stack = new ItemStack(Items.BOW);
        giveMainHand(player, stack);

        runCmd(asAdmin(player), "enchantbow 5 2 1 1 1 3");

        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.POWER)) == 5, "Power V");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.PUNCH)) == 2, "Punch II");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.FLAME)) == 1, "Flame I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.INFINITY)) == 1, "Infinity I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.MENDING)) == 1, "Mending I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.UNBREAKING)) == 3, "Unbreaking III");
        helper.succeed();
    }

    public static void testEnchantCrossbow(GameTestHelper helper) {
        var player = createPlayer(helper);
        var stack = new ItemStack(Items.CROSSBOW);
        giveMainHand(player, stack);

        runCmd(asAdmin(player), "enchantcrossbow 1 3 2 1 3");

        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.MULTISHOT)) == 1, "Multishot I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.PIERCING)) == 3, "Piercing III");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.QUICK_CHARGE)) == 2, "Quick Charge II");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.MENDING)) == 1, "Mending I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.UNBREAKING)) == 3, "Unbreaking III");
        helper.succeed();
    }

    public static void testEnchantMace(GameTestHelper helper) {
        var player = createPlayer(helper);
        var stack = new ItemStack(Items.MACE);
        giveMainHand(player, stack);

        runCmd(asAdmin(player), "enchantmace 5 4 2 4 4 2 1 3");

        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.DENSITY)) == 5, "Density V");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.BREACH)) == 4, "Breach IV");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.WIND_BURST)) == 2, "Wind Burst II");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.SMITE)) == 4, "Smite IV");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.BANE_OF_ARTHROPODS)) == 4, "Bane IV");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.FIRE_ASPECT)) == 2, "Fire Aspect II");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.MENDING)) == 1, "Mending I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.UNBREAKING)) == 3, "Unbreaking III");
        helper.succeed();
    }

    public static void testEnchantSpear(GameTestHelper helper) {
        var player = createPlayer(helper);
        var stack = new ItemStack(Items.DIAMOND_SPEAR);
        giveMainHand(player, stack);

        runCmd(asAdmin(player), "enchantspear 5 4 4 2 2 3 5 1 3");

        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.SHARPNESS)) == 5, "Sharpness V");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.SMITE)) == 4, "Smite IV");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.BANE_OF_ARTHROPODS)) == 4, "Bane IV");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.KNOCKBACK)) == 2, "Knockback II");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.FIRE_ASPECT)) == 2, "Fire Aspect II");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.LOOTING)) == 3, "Looting III");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.LUNGE)) == 5, "Lunge V");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.MENDING)) == 1, "Mending I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.UNBREAKING)) == 3, "Unbreaking III");
        helper.succeed();
    }

    public static void testEnchantTrident(GameTestHelper helper) {
        var player = createPlayer(helper);
        var stack = new ItemStack(Items.TRIDENT);
        giveMainHand(player, stack);

        runCmd(asAdmin(player), "enchanttrident 3 4 2 1 1 3");

        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.LOYALTY)) == 3, "Loyalty III");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.IMPALING)) == 4, "Impaling IV");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.RIPTIDE)) == 2, "Riptide II");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.CHANNELING)) == 1, "Channeling I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.MENDING)) == 1, "Mending I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.UNBREAKING)) == 3, "Unbreaking III");
        helper.succeed();
    }

    public static void testEnchantFishingRod(GameTestHelper helper) {
        var player = createPlayer(helper);
        var stack = new ItemStack(Items.FISHING_ROD);
        giveMainHand(player, stack);

        runCmd(asAdmin(player), "enchantfishingrod 3 2 1 3");

        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.LUCK_OF_THE_SEA)) == 3, "Luck III");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.LURE)) == 2, "Lure II");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.MENDING)) == 1, "Mending I");
        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.UNBREAKING)) == 3, "Unbreaking III");
        helper.succeed();
    }

    public static void testEnchantWrongItem(GameTestHelper helper) {
        var player = createPlayer(helper);
        giveMainHand(player, new ItemStack(Items.DIAMOND_PICKAXE));

        runCmd(asAdmin(player), "enchantsword 5");
        helper.succeed();
    }

    public static void testKnockback(GameTestHelper helper) {
        var player = createPlayer(helper);
        var stack = new ItemStack(Items.DIAMOND_SWORD);
        giveMainHand(player, stack);

        runCmd(asPlayer(player), "knockback");

        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.KNOCKBACK)) > 0, "Should have knockback");
        helper.succeed();
    }

    public static void testKnockbackLevel(GameTestHelper helper) {
        var player = createPlayer(helper);
        var stack = new ItemStack(Items.DIAMOND_SWORD);
        giveMainHand(player, stack);

        runCmd(asPlayer(player), "knockback 50");

        helper.assertTrue(enchLevel(stack, ench(helper, Enchantments.KNOCKBACK)) == 50, "Knockback 50");
        helper.succeed();
    }

    public static void testKnockbackStick(GameTestHelper helper) {
        var player = createPlayer(helper);

        runCmd(asPlayer(player), "knockbackstick");

        var inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            var s = inv.getItem(i);
            if (s.is(Items.STICK) && enchLevel(s, ench(helper, Enchantments.KNOCKBACK)) > 0) {
                helper.succeed();
                return;
            }
        }
        helper.fail("No knockback stick found in inventory");
    }

    public static void testKnockbackStickLevel(GameTestHelper helper) {
        var player = createPlayer(helper);

        runCmd(asPlayer(player), "knockbackstick 30");

        var inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            var s = inv.getItem(i);
            if (s.is(Items.STICK) && enchLevel(s, ench(helper, Enchantments.KNOCKBACK)) == 30) {
                helper.succeed();
                return;
            }
        }
        helper.fail("No knockback stick with level 30 found in inventory");
    }

    public static void testHeal(GameTestHelper helper) {
        var player = createPlayer(helper);
        player.setHealth(2.0f);

        runCmd(asAdmin(player), "heal @s");

        helper.assertTrue(player.getHealth() == player.getMaxHealth(), "Player should be at max health");
        helper.succeed();
    }

    public static void testHealAndFeed(GameTestHelper helper) {
        var player = createPlayer(helper);
        player.setHealth(2.0f);
        player.getFoodData().setFoodLevel(5);
        player.getFoodData().setSaturation(0);

        runCmd(asAdmin(player), "heal @s true");

        helper.assertTrue(player.getHealth() == player.getMaxHealth(), "Should be at max health");
        helper.assertTrue(player.getFoodData().getFoodLevel() == 20, "Food should be 20");
        helper.assertTrue(player.getFoodData().getSaturationLevel() > 0, "Should have saturation");
        helper.succeed();
    }

    public static void testFeed(GameTestHelper helper) {
        var player = createPlayer(helper);
        player.getFoodData().setFoodLevel(5);
        player.getFoodData().setSaturation(0);

        runCmd(asGM(player), "feed");

        helper.assertTrue(player.getFoodData().getFoodLevel() == 20, "Food should be 20");
        helper.assertTrue(player.getFoodData().getSaturationLevel() > 0, "Should have saturation");
        helper.succeed();
    }

    public static void testFeedPlayers(GameTestHelper helper) {
        var player = createPlayer(helper);
        player.getFoodData().setFoodLevel(5);
        player.getFoodData().setSaturation(0);

        runCmd(asGM(player), "feed @s");

        helper.assertTrue(player.getFoodData().getFoodLevel() == 20, "Food should be 20");
        helper.succeed();
    }

    public static void testKillAll(GameTestHelper helper) {
        helper.spawn(EntityTypes.ZOMBIE, 1, 2, 1);
        helper.spawn(EntityTypes.SKELETON, 2, 2, 1);

        runCmd(console(helper), "killall");

        helper.assertEntityNotPresent(EntityTypes.ZOMBIE);
        helper.assertEntityNotPresent(EntityTypes.SKELETON);
        helper.succeed();
    }

    public static void testDay(GameTestHelper helper) {
        runCmd(console(helper), "day");

        long time = getClockTime(helper);
        helper.assertTrue(time >= 1000 && time <= 1010, "Time should be ~1000, was " + time);
        helper.succeed();
    }

    public static void testNoon(GameTestHelper helper) {
        runCmd(console(helper), "noon");

        long time = getClockTime(helper);
        helper.assertTrue(time >= 6000 && time <= 6010, "Time should be ~6000, was " + time);
        helper.succeed();
    }

    public static void testNight(GameTestHelper helper) {
        runCmd(console(helper), "night");

        long time = getClockTime(helper);
        helper.assertTrue(time >= 13000 && time <= 13010, "Time should be ~13000, was " + time);
        helper.succeed();
    }

    public static void testMidnight(GameTestHelper helper) {
        runCmd(console(helper), "midnight");

        long time = getClockTime(helper);
        helper.assertTrue(time >= 18000 && time <= 18010, "Time should be ~18000, was " + time);
        helper.succeed();
    }

    public static void testExplode(GameTestHelper helper) {
        helper.setBlock(0, 2, 0, Blocks.STONE);
        var pos = helper.absolutePos(new BlockPos(0, 2, 0));

        runCmd(console(helper), "explode " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " 5");

        helper.assertBlockNotPresent(Blocks.STONE, new BlockPos(0, 2, 0));
        helper.succeed();
    }

    public static void testSetExplosionPower(GameTestHelper helper) {
        runCmd(console(helper), "setExplosionPower 7.5");

        double power = helper.getLevel().getGameRules().get(ModGameRules.EXPLOSION_POWER);
        helper.assertTrue(power == 7.5, "Explosion power should be 7.5");
        helper.succeed();
    }

    public static void testQueryExplosionPower(GameTestHelper helper) {
        runCmd(console(helper), "setExplosionPower");
        helper.succeed();
    }

    public static void testToggleExplosiveProjectilesOn(GameTestHelper helper) {
        runCmd(console(helper), "toggleExplosiveProjectiles true");

        boolean enabled = helper.getLevel().getGameRules().get(ModGameRules.EXPLOSIVE_PROJECTILES_ENABLED);
        helper.assertTrue(enabled, "Explosive projectiles should be enabled");
        helper.succeed();
    }

    public static void testToggleExplosiveProjectilesOff(GameTestHelper helper) {
        runCmd(console(helper), "toggleExplosiveProjectiles false");

        boolean enabled = helper.getLevel().getGameRules().get(ModGameRules.EXPLOSIVE_PROJECTILES_ENABLED);
        helper.assertTrue(!enabled, "Explosive projectiles should be disabled");
        helper.succeed();
    }

    public static void testToggleExplosiveProjectilesToggle(GameTestHelper helper) {
        runCmd(console(helper), "toggleExplosiveProjectiles");
        helper.succeed();
    }

    public static void testModifyTreeHeight(GameTestHelper helper) {
        runCmd(console(helper), "modifyTreeHeight 10");

        int height = helper.getLevel().getGameRules().get(ModGameRules.TREE_HEIGHT);
        helper.assertTrue(height == 10, "Tree height should be 10");
        helper.succeed();
    }

    public static void testQueryTreeHeight(GameTestHelper helper) {
        runCmd(console(helper), "modifyTreeHeight");
        helper.succeed();
    }

    public static void testResetTreeHeight(GameTestHelper helper) {
        helper.getLevel().getGameRules().set(ModGameRules.TREE_HEIGHT, 10, helper.getLevel().getServer());

        runCmd(console(helper), "resetTreeHeight");

        int height = helper.getLevel().getGameRules().get(ModGameRules.TREE_HEIGHT);
        helper.assertTrue(height == 1, "Tree height should be reset to 1");
        helper.succeed();
    }

    public static void testCreative(GameTestHelper helper) {
        var player = createPlayer(helper);

        runCmd(asAdmin(player), "creative");

        helper.assertTrue(player.gameMode.getGameModeForPlayer() == GameType.CREATIVE, "Expected CREATIVE");
        helper.succeed();
    }

    public static void testSurvival(GameTestHelper helper) {
        var player = createPlayer(helper);
        player.setGameMode(GameType.CREATIVE);

        runCmd(asAdmin(player), "survival");

        helper.assertTrue(player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL, "Expected SURVIVAL");
        helper.succeed();
    }

    public static void testAdventure(GameTestHelper helper) {
        var player = createPlayer(helper);

        runCmd(asAdmin(player), "adventure");

        helper.assertTrue(player.gameMode.getGameModeForPlayer() == GameType.ADVENTURE, "Expected ADVENTURE");
        helper.succeed();
    }

    public static void testSpectator(GameTestHelper helper) {
        var player = createPlayer(helper);

        runCmd(asAdmin(player), "spectator");

        helper.assertTrue(player.gameMode.getGameModeForPlayer() == GameType.SPECTATOR, "Expected SPECTATOR");
        helper.succeed();
    }

    public static void testHealMultiplePlayers(GameTestHelper helper) {
        var p1 = createPlayer(helper);
        var p2 = createPlayer(helper);
        p1.setHealth(2.0f);
        p2.setHealth(3.0f);
        String tag = "heal_multi_" + p1.getStringUUID().replace("-", "");
        p1.addTag(tag);
        p2.addTag(tag);

        runCmd(console(helper), "heal @a[tag=" + tag + "]");

        helper.assertTrue(p1.getHealth() == p1.getMaxHealth(), "P1 should be at max health");
        helper.assertTrue(p2.getHealth() == p2.getMaxHealth(), "P2 should be healed by @a[tag]");
        helper.succeed();
    }

    public static void testHealOtherPlayer(GameTestHelper helper) {
        var executor = createPlayer(helper);
        var target = createPlayer(helper);
        executor.setHealth(2.0f);
        target.setHealth(2.0f);

        String tag = "heal_target_" + target.getStringUUID().replace("-", "");
        target.addTag(tag);
        runCmd(asAdmin(executor), "heal @p[tag=" + tag + "]");

        helper.assertTrue(target.getHealth() == target.getMaxHealth(), "Target should be healed");
        helper.assertTrue(executor.getHealth() == 2.0f, "Executor should stay damaged");
        helper.succeed();
    }

    public static void testHealMultiplePlayersWithFeed(GameTestHelper helper) {
        var p1 = createPlayer(helper);
        var p2 = createPlayer(helper);
        p1.setHealth(2.0f);
        p1.getFoodData().setFoodLevel(5);
        p1.getFoodData().setSaturation(0);
        p2.setHealth(2.0f);
        p2.getFoodData().setFoodLevel(5);
        p2.getFoodData().setSaturation(0);
        String tag = "healfeed_multi_" + p1.getStringUUID().replace("-", "");
        p1.addTag(tag);
        p2.addTag(tag);

        runCmd(console(helper), "heal @a[tag=" + tag + "] true");

        helper.assertTrue(p1.getHealth() == p1.getMaxHealth(), "P1 health");
        helper.assertTrue(p2.getHealth() == p2.getMaxHealth(), "P2 health");
        helper.assertTrue(p1.getFoodData().getFoodLevel() == 20, "P1 food");
        helper.assertTrue(p2.getFoodData().getFoodLevel() == 20, "P2 food");
        helper.succeed();
    }

    public static void testFeedMultiplePlayers(GameTestHelper helper) {
        var p1 = createPlayer(helper);
        var p2 = createPlayer(helper);
        p1.getFoodData().setFoodLevel(5);
        p1.getFoodData().setSaturation(0);
        p2.getFoodData().setFoodLevel(5);
        p2.getFoodData().setSaturation(0);
        String tag = "feed_multi_" + p1.getStringUUID().replace("-", "");
        p1.addTag(tag);
        p2.addTag(tag);

        runCmd(console(helper), "feed @a[tag=" + tag + "]");

        helper.assertTrue(p1.getFoodData().getFoodLevel() == 20, "P1 food should be 20");
        helper.assertTrue(p2.getFoodData().getFoodLevel() == 20, "P2 food should be 20 via @a[tag]");
        helper.succeed();
    }

    public static void testFeedOtherPlayer(GameTestHelper helper) {
        var executor = createPlayer(helper);
        var target = createPlayer(helper);
        executor.getFoodData().setFoodLevel(5);
        executor.getFoodData().setSaturation(0);
        target.getFoodData().setFoodLevel(5);
        target.getFoodData().setSaturation(0);

        String tag = "feed_target_" + target.getStringUUID().replace("-", "");
        target.addTag(tag);
        runCmd(asGM(executor), "feed @p[tag=" + tag + "]");

        helper.assertTrue(target.getFoodData().getFoodLevel() == 20, "Target should be fed");
        helper.assertTrue(executor.getFoodData().getFoodLevel() == 5, "Executor should stay hungry");
        helper.succeed();
    }

    public static void testCreativeOtherPlayer(GameTestHelper helper) {
        var executor = createPlayer(helper);
        var target = createPlayer(helper);
        executor.setGameMode(GameType.SURVIVAL);

        String tag = "creative_target_" + target.getStringUUID().replace("-", "");
        target.addTag(tag);
        runCmd(asAdmin(executor), "creative @p[tag=" + tag + "]");

        helper.assertTrue(target.gameMode.getGameModeForPlayer() == GameType.CREATIVE, "Target should be CREATIVE");
        helper.assertTrue(executor.gameMode.getGameModeForPlayer() == GameType.SURVIVAL, "Executor should stay SURVIVAL");
        helper.succeed();
    }

    public static void testSurvivalOtherPlayer(GameTestHelper helper) {
        var executor = createPlayer(helper);
        var target = createPlayer(helper);
        target.setGameMode(GameType.CREATIVE);
        executor.setGameMode(GameType.CREATIVE);

        String tag = "survival_target_" + target.getStringUUID().replace("-", "");
        target.addTag(tag);
        runCmd(asAdmin(executor), "survival @p[tag=" + tag + "]");

        helper.assertTrue(target.gameMode.getGameModeForPlayer() == GameType.SURVIVAL, "Target should be SURVIVAL");
        helper.assertTrue(executor.gameMode.getGameModeForPlayer() == GameType.CREATIVE, "Executor should stay CREATIVE");
        helper.succeed();
    }

    public static void testAdventureOtherPlayer(GameTestHelper helper) {
        var executor = createPlayer(helper);
        var target = createPlayer(helper);
        executor.setGameMode(GameType.SURVIVAL);

        String tag = "adventure_target_" + target.getStringUUID().replace("-", "");
        target.addTag(tag);
        runCmd(asAdmin(executor), "adventure @p[tag=" + tag + "]");

        helper.assertTrue(target.gameMode.getGameModeForPlayer() == GameType.ADVENTURE, "Target should be ADVENTURE");
        helper.assertTrue(executor.gameMode.getGameModeForPlayer() == GameType.SURVIVAL, "Executor should stay SURVIVAL");
        helper.succeed();
    }

    public static void testSpectatorOtherPlayer(GameTestHelper helper) {
        var executor = createPlayer(helper);
        var target = createPlayer(helper);
        executor.setGameMode(GameType.SURVIVAL);

        String tag = "spectator_target_" + target.getStringUUID().replace("-", "");
        target.addTag(tag);
        runCmd(asAdmin(executor), "spectator @p[tag=" + tag + "]");

        helper.assertTrue(target.gameMode.getGameModeForPlayer() == GameType.SPECTATOR, "Target should be SPECTATOR");
        helper.assertTrue(executor.gameMode.getGameModeForPlayer() == GameType.SURVIVAL, "Executor should stay SURVIVAL");
        helper.succeed();
    }

    public static void testCreativeMultiplePlayers(GameTestHelper helper) {
        var p1 = createPlayer(helper);
        var p2 = createPlayer(helper);
        String tag = "creative_multi_" + p1.getStringUUID().replace("-", "");
        p1.addTag(tag);
        p2.addTag(tag);

        runCmd(console(helper), "creative @a[tag=" + tag + "]");

        helper.assertTrue(p1.gameMode.getGameModeForPlayer() == GameType.CREATIVE, "P1 should be CREATIVE");
        helper.assertTrue(p2.gameMode.getGameModeForPlayer() == GameType.CREATIVE, "P2 should be CREATIVE via @a[tag]");
        helper.succeed();
    }

    public static void testKnockbackOtherPlayer(GameTestHelper helper) {
        var executor = createPlayer(helper);
        var target = createPlayer(helper);
        var executorStack = new ItemStack(Items.DIAMOND_SWORD);
        giveMainHand(executor, executorStack);
        var targetStack = new ItemStack(Items.DIAMOND_SWORD);
        giveMainHand(target, targetStack);

        String tag = "knockback_target_" + target.getStringUUID().replace("-", "");
        target.addTag(tag);
        runCmd(asAdmin(executor), "knockback 15 @p[tag=" + tag + "]");

        helper.assertTrue(enchLevel(targetStack, ench(helper, Enchantments.KNOCKBACK)) == 15, "Target should have Knockback XV");
        helper.assertTrue(enchLevel(executorStack, ench(helper, Enchantments.KNOCKBACK)) == 0, "Executor should stay unenchanted");
        helper.succeed();
    }

    public static void testKnockbackStickOtherPlayer(GameTestHelper helper) {
        var executor = createPlayer(helper);
        var target = createPlayer(helper);

        String realTag = "kb_stick_target_" + target.getStringUUID().replace("-", "");
        target.addTag(realTag);
        runCmd(asAdmin(executor), "knockbackstick 20 @p[tag=" + realTag + "]");

        boolean foundInTarget = false;
        var targetInv = target.getInventory();
        for (int i = 0; i < targetInv.getContainerSize(); i++) {
            var s = targetInv.getItem(i);
            if (s.is(Items.STICK) && enchLevel(s, ench(helper, Enchantments.KNOCKBACK)) == 20) {
                foundInTarget = true;
                break;
            }
        }
        helper.assertTrue(foundInTarget, "Target should receive knockback XX stick");

        boolean foundInExecutor = false;
        var executorInv = executor.getInventory();
        for (int i = 0; i < executorInv.getContainerSize(); i++) {
            var s = executorInv.getItem(i);
            if (s.is(Items.STICK) && enchLevel(s, ench(helper, Enchantments.KNOCKBACK)) > 0) {
                foundInExecutor = true;
                break;
            }
        }
        helper.assertTrue(!foundInExecutor, "Executor should not receive a stick");
        helper.succeed();
    }
}
