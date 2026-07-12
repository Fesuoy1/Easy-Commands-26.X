package org.mod.easy_commands.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.mod.easy_commands.ModGameRules.getTreeHeight;

@Mixin(TrunkPlacer.class)
public class TrunkPlacerMixin {

    @Final
    @Shadow
    protected int baseHeight;

    @Final
    @Shadow
    protected int heightRandA;

    @Final
    @Shadow
    protected int heightRandB;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"), method = "getTreeHeight", cancellable = true)
    public void onGetHeight(RandomSource random, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(this.baseHeight + random.nextInt(this.heightRandA + getTreeHeight()) + random.nextInt(this.heightRandB + getTreeHeight()));
    }
}
