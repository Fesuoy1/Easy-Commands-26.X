package org.mod.easy_commands.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.mod.easy_commands.EasyCommands;
import org.mod.easy_commands.ModGameRules;

@Mod(EasyCommands.MOD_ID)
public class EasyCommandsNeoForge {
    public EasyCommandsNeoForge(IEventBus modEventBus, ModContainer modContainer) {
        EasyCommands.LOGGER.info("Easy Commands Initialized");
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        NeoForge.EVENT_BUS.addListener(this::onServerStarting);
        NeoForge.EVENT_BUS.addListener(this::onServerStopping);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        EasyCommands.registerCommands(event.getDispatcher());
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
