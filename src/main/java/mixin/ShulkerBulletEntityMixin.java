package mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ShulkerBulletEntity.class)
public class ShulkerBulletEntityMixin
{
    @ModifyArg
    (
        method = "onEntityHit",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z"), index = 0
    )
    private StatusEffectInstance adjustLevitation(StatusEffectInstance intercepted)
    {
        return new StatusEffectInstance(intercepted.getEffectType(), 60);
    }
}
