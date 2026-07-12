package org.mod.easy_commands.fabric;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import org.mod.easy_commands.ModGameRules;

public class ModGameRulesFabric {
    private static boolean registered = false;

    public static void register() {
        if (registered) return;
        registered = true;

        ModGameRules.TREE_HEIGHT = GameRuleBuilder.forInteger(1)
                .category(GameRuleCategory.MISC)
                .buildAndRegister(Identifier.fromNamespaceAndPath("easy_commands", "tree_height"));

        ModGameRules.EXPLOSIVE_PROJECTILES_ENABLED = GameRuleBuilder.forBoolean(false)
                .category(GameRuleCategory.MISC)
                .buildAndRegister(Identifier.fromNamespaceAndPath("easy_commands", "explosive_projectiles"));

        ModGameRules.EXPLOSION_POWER = GameRuleBuilder.forDouble(2.5D)
                .category(GameRuleCategory.MISC)
                .buildAndRegister(Identifier.fromNamespaceAndPath("easy_commands", "explosion_power"));
    }
}
