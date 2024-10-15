package dev.obscuria.fragmentum.fabric.service;

import dev.obscuria.fragmentum.api.v1.server.ObscureServerRegistry;
import dev.obscuria.fragmentum.api.v1.server.V1Server;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public final class FabricV1Server implements V1Server
{
    @Override
    public void registerCommand(ObscureServerRegistry.CommandRegistrar registrar)
    {
        CommandRegistrationCallback.EVENT.register(registrar::register);
    }
}
