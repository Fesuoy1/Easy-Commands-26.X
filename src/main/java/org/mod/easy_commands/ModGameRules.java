package org.mod.easy_commands;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ModGameRules {
    public static void loaded() {
        // Forces static initializer to run so that it registers game rules early
    }

    public static final GameRule<Integer> TREE_HEIGHT = GameRuleBuilder.forInteger(1)
            .category(GameRuleCategory.MISC)
            .buildAndRegister(Identifier.fromNamespaceAndPath("easy_commands", "tree_height"));

    public static final GameRule<Boolean> EXPLOSIVE_PROJECTILES_ENABLED = GameRuleBuilder.forBoolean(false)
            .category(GameRuleCategory.MISC)
            .buildAndRegister(Identifier.fromNamespaceAndPath("easy_commands", "explosive_projectiles"));

    public static final GameRule<Double> EXPLOSION_POWER = GameRuleBuilder.forDouble(2.5D)
            .category(GameRuleCategory.MISC)
            .buildAndRegister(Identifier.fromNamespaceAndPath("easy_commands", "explosion_power"));

    public static Iterable<ServerLevel> worlds;

    public static void explode(@NotNull Entity entity) {
        entity.discard();
        Vec3 pos = entity.position();
        Level level = entity.level();
        level.explode(entity, pos.x(), pos.y(), pos.z(), (float) getExplosionPower(), Level.ExplosionInteraction.TNT);
    }

    public static Boolean isExplosiveProjectilesEnabled(@NotNull Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getGameRules().get(EXPLOSIVE_PROJECTILES_ENABLED);
        }
        return false;
    }

    public static Integer getTreeHeight() {
        if (worlds != null) {
            for (ServerLevel world : worlds) {
                if (world.getGameRules().get(TREE_HEIGHT) > 0) {
                    return world.getGameRules().get(TREE_HEIGHT);
                }
            }
        }
        return 1;
    }

    public static double getExplosionPower() {
        if (worlds != null) {
            for (ServerLevel world : worlds) {
                if (world.getGameRules().get(EXPLOSION_POWER) > 0) {
                    return world.getGameRules().get(EXPLOSION_POWER);
                }
            }
        }
        return 2.5;
    }
}
