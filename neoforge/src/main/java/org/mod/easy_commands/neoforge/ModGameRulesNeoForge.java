package org.mod.easy_commands.neoforge;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import net.minecraft.world.flag.FeatureFlagSet;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.mod.easy_commands.EasyCommands;
import org.mod.easy_commands.ModGameRules;

@EventBusSubscriber(modid = EasyCommands.MOD_ID)
public class ModGameRulesNeoForge {
    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(Registries.GAME_RULE, helper -> {
            ModGameRules.TREE_HEIGHT = Registry.register(
                BuiltInRegistries.GAME_RULE,
                Identifier.fromNamespaceAndPath("easy_commands", "tree_height"),
                new GameRule<>(
                    GameRuleCategory.MISC,
                    GameRuleType.INT,
                    IntegerArgumentType.integer(1),
                    GameRuleTypeVisitor::visitInteger,
                    Codec.intRange(1, 256),
                    i -> i,
                    1,
                    FeatureFlagSet.of()
                )
            );

            ModGameRules.EXPLOSIVE_PROJECTILES_ENABLED = Registry.register(
                BuiltInRegistries.GAME_RULE,
                Identifier.fromNamespaceAndPath("easy_commands", "explosive_projectiles"),
                new GameRule<>(
                    GameRuleCategory.MISC,
                    GameRuleType.BOOL,
                    BoolArgumentType.bool(),
                    GameRuleTypeVisitor::visitBoolean,
                    Codec.BOOL,
                    b -> b ? 1 : 0,
                    false,
                    FeatureFlagSet.of()
                )
            );

            ModGameRules.EXPLOSION_POWER = Registry.register(
                BuiltInRegistries.GAME_RULE,
                Identifier.fromNamespaceAndPath("easy_commands", "explosion_power"),
                new GameRule<>(
                    GameRuleCategory.MISC,
                    GameRuleType.INT,
                    DoubleArgumentType.doubleArg(0.1),
                    (visitor, rule) -> visitor.visit(rule),
                    Codec.DOUBLE,
                    d -> (int) Math.round(d),
                    2.5,
                    FeatureFlagSet.of()
                )
            );
        });
    }
}
