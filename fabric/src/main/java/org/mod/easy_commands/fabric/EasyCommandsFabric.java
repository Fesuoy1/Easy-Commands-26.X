package org.mod.easy_commands.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.mod.easy_commands.EasyCommands;
import org.mod.easy_commands.ModGameRules;

public class EasyCommandsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        try {
            ModGameRulesFabric.register();
            EasyCommands.LOGGER.info("Easy Commands Initialized");
            CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) ->
                EasyCommands.registerCommands(dispatcher));
            ServerLifecycleEvents.SERVER_STARTED.register(server -> ModGameRules.worlds = server.getAllLevels());
            ServerLifecycleEvents.SERVER_STOPPED.register(_ -> ModGameRules.worlds = null);
        } catch (Exception e) {
            EasyCommands.LOGGER.error("Easy Commands failed to initialize: {}", e.getMessage());
        }
    }
}
