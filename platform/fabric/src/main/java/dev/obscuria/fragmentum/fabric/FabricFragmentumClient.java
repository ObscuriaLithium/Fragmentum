package dev.obscuria.fragmentum.fabric;

import dev.obscuria.fragmentum.api.v1.client.ObscureClientEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public final class FabricFragmentumClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        WorldRenderEvents.START.register(context -> ObscureClientEvents.START_RENDER
                .broadcast(ObscureClientEvents.WorldRender::invoke));
        WorldRenderEvents.END.register(context -> ObscureClientEvents.END_RENDER
                .broadcast(ObscureClientEvents.WorldRender::invoke));

        ClientTickEvents.START_WORLD_TICK.register(level -> ObscureClientEvents.START_WORLD_TICK
                .broadcast(listener -> listener.invoke(level)));
        ClientTickEvents.END_WORLD_TICK.register(level -> ObscureClientEvents.END_WORLD_TICK
                .broadcast(listener -> listener.invoke(level)));

        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> ObscureClientEvents.START_CLIENT_TICK
                .broadcast(listener -> listener.invoke(minecraft)));
        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> ObscureClientEvents.END_CLIENT_TICK
                .broadcast(listener -> listener.invoke(minecraft)));
    }
}
