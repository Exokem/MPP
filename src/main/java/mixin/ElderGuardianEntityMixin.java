package mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElderGuardianEntity.class)
public class ElderGuardianEntityMixin extends GuardianEntity
{
    public ElderGuardianEntityMixin(EntityType<? extends GuardianEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @ModifyArg
    (
        method = "mobTick",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z"), index = 0
    )
    private StatusEffectInstance reduceMiningFatigue(StatusEffectInstance intercepted)
    {
        return new StatusEffectInstance(intercepted.getEffectType(), 1800, 1);
    }
}
