package pers.roinflam.futurediary.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.base.potion.hide.HideBase;

import javax.annotation.Nonnull;


public class MobEffectPerceivedLapse extends HideBase {

    public MobEffectPerceivedLapse(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn, "perceived_lapse");
        this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "bf52e63b-c730-9f7a-cb4d-ec62355f6c64", -0.75, 2);
        this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "413d5c62-033b-1118-a91d-7fbba822a0a6", -0.75, 2);
        this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "8983586e-80f8-f0e2-3976-f86ada2a0868", -0.75, 2);
    }


    @SubscribeEvent
    public void onLivingHeal(@Nonnull LivingHealEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            EntityLivingBase healer = evt.getEntityLiving();
            if (healer.getActivePotionEffect(this) != null) {
                evt.setAmount(evt.getAmount() * 0.25f);
            }
        }
    }

}
