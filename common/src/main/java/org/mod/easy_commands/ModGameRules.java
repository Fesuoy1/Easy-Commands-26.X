package org.mod.easy_commands;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ModGameRules {
    public static GameRule<Integer> TREE_HEIGHT;
    public static GameRule<Boolean> EXPLOSIVE_PROJECTILES_ENABLED;
    public static GameRule<Double> EXPLOSION_POWER;

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
