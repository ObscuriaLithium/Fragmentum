package dev.obscuria.fragmentum.neoforge.service;

import dev.obscuria.fragmentum.api.Deferred;
import dev.obscuria.fragmentum.api.DeferredEntity;
import dev.obscuria.fragmentum.api.DeferredItem;
import dev.obscuria.fragmentum.api.DeferredParticle;
import dev.obscuria.fragmentum.api.v1.client.IClientRegistrar;
import dev.obscuria.fragmentum.neoforge.NeoFragmentum;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;

@ApiStatus.Internal
public record NeoClientRegistrar(String modId) implements IClientRegistrar
{
    @Override
    public <T extends Entity> void registerEntityRenderer(
            DeferredEntity<T> type,
            EntityRendererProvider<T> provider)
    {
        NeoFragmentum.eventBus(modId).addListener((EntityRenderersEvent.RegisterRenderers event) ->
                event.registerEntityRenderer(type.value(), provider));
    }

    @Override
    public <T extends BlockEntity> void registerBlockEntityRenderer(
            Deferred<BlockEntityType<?>, BlockEntityType<T>> type,
            BlockEntityRendererProvider<T> provider)
    {
        NeoFragmentum.eventBus(modId).addListener((EntityRenderersEvent.RegisterRenderers event) ->
                event.registerBlockEntityRenderer(type.value(), provider));
    }

    @Override
    public <T extends ParticleOptions> void registerParticleRenderer(
            DeferredParticle<T> type,
            ParticleProvider<T> provider)
    {
        NeoFragmentum.eventBus(modId).addListener((RegisterParticleProvidersEvent event) ->
                event.registerSpecial(type.value(), provider));
    }

    @Override
    public <T extends ParticleOptions> void registerTexturedParticleRenderer(
            DeferredParticle<T> type,
            TexturedParticleProvider<T> provider)
    {
        NeoFragmentum.eventBus(modId).addListener((RegisterParticleProvidersEvent event) ->
                event.registerSpriteSet(type.value(), provider::create));
    }

    @Override
    public void registerModelLayer(
            ModelLayerLocation layerLocation,
            ModelLayerProvider provider)
    {
        NeoFragmentum.eventBus(modId).addListener((EntityRenderersEvent.RegisterLayerDefinitions event) ->
                event.registerLayerDefinition(layerLocation, provider::create));
    }

    @Override
    public void registerItemColor(
            ItemColorProvider provider,
            DeferredItem<?>... items)
    {
        NeoFragmentum.eventBus(modId).addListener((RegisterColorHandlersEvent.Item event) ->
                event.register(provider::getColor, Arrays.stream(items).map(Deferred::value).toArray(Item[]::new)));
    }

    @Override
    public void registerItemProperty(
            ResourceLocation key,
            ClampedItemPropertyFunction function)
    {
        ItemProperties.registerGeneric(key, function);
    }

    @Override
    public void registerKeyMapping(KeyMapping keyMapping)
    {
        NeoFragmentum.eventBus(modId).addListener((RegisterKeyMappingsEvent event) ->
                event.register(keyMapping));
    }
}
