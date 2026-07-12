package org.mod.easy_commands.mixin;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.mod.easy_commands.ModGameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public class ProjectileMixin {

    @Inject(at = @At("HEAD"), method = "onHitEntity", cancellable = true)
    public void onHitEntity(EntityHitResult hitResult, CallbackInfo ci) {
        Level level = ((Projectile) (Object) this).level();
        if (ModGameRules.isExplosiveProjectilesEnabled(level)) {
            ModGameRules.explode(((Projectile) (Object) this));
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onHitBlock", cancellable = true)
    public void onHitBlock(BlockHitResult hitResult, CallbackInfo ci) {
        Level level = ((Projectile) (Object) this).level();
        if (ModGameRules.isExplosiveProjectilesEnabled(level)) {
            ModGameRules.explode(((Projectile) (Object) this));
            ci.cancel();
        }
    }

    @Unique
    private static final boolean particlesEnabled = false;

    @Inject(at = @At("HEAD"), method = "tick")
    public void onTick(CallbackInfo ci) {
        // For now, disable particles since they lag the server if there are too many
        if (!particlesEnabled) return;

        Projectile projectile = (Projectile) (Object) this;
        Level level = projectile.level();
        if (ModGameRules.isExplosiveProjectilesEnabled(level)) { // already checks if it's a server, etc.
            Vec3 pos = projectile.position();
            boolean isOwnerPlayer = projectile.getOwner() instanceof ServerPlayer;
            if (isOwnerPlayer)
                ((ServerLevel)level).sendParticles((ServerPlayer)projectile.getOwner(), ParticleTypes.SMOKE, false, false, pos.x(), pos.y() + 0.1, pos.z(), 1, 0.0, 0.0, 0.0, 0.0);
            else
                ((ServerLevel)level).sendParticles(ParticleTypes.SMOKE, pos.x(), pos.y() + 0.1, pos.z(), 1, 0.0, 0.0, 0.0, 0.0);
        }
    }
}
