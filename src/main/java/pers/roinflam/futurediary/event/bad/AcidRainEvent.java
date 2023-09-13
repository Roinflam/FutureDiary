package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pers.roinflam.futurediary.base.potion.hide.PrivateHideBase;
import pers.roinflam.futurediary.event.FutureEvent;

import javax.annotation.Nonnull;

import static pers.roinflam.futurediary.event.bad.AcidRainEvent.MobEffectArmorReduced.ARMOR_REDUCED;

//À·”Í
public class AcidRainEvent extends FutureEvent {

    @SubscribeEvent
    public void onWorldTick(@Nonnull TickEvent.WorldTickEvent evt) {
        if (!evt.world.isRemote && evt.phase.equals(TickEvent.Phase.START)) {
            World world = evt.world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            world.getWorldInfo().setRaining(true);
            world.getWorldInfo().setRainTime(1200);
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(@Nonnull LivingEvent.LivingUpdateEvent evt) {
        if (!evt.getEntityLiving().world.isRemote) {
            World world = evt.getEntityLiving().getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityLivingBase entityLivingBase = evt.getEntityLiving();
            if (world.getTotalWorldTime() % 50 == 0) {
                if (world.isRaining() && entityLivingBase.isWet() && world.canSeeSky(entityLivingBase.getPosition())) {
                    entityLivingBase.addPotionEffect(new PotionEffect(ARMOR_REDUCED, 100, 0));
                    entityLivingBase.attackEntityFrom(DamageSource.GENERIC.setMagicDamage(), entityLivingBase.getMaxHealth() * 0.1f);
                    this.sendReminder(entityLivingBase);
                }
            }
        }
    }

    @Override
    public boolean isBadEvent() {
        return true;
    }

    @Override
    public boolean isGoodEvent() {
        return false;
    }

    @Override
    public boolean isCatastrophicEvent() {
        return false;
    }

    @Nonnull
    @Override
    public String getEventName() {
        return "acid_rain";
    }

    static class MobEffectArmorReduced extends PrivateHideBase {
        public static final MobEffectArmorReduced ARMOR_REDUCED = new MobEffectArmorReduced(true, 0xacb615, "acid_rain_pressure_MobEffectArmorReduced");

        protected MobEffectArmorReduced(boolean isBadEffectIn, int liquidColorIn, @Nonnull String name) {
            super(isBadEffectIn, liquidColorIn, name);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR, "01e45c9f-cc8a-a3da-45cb-f72bd2d6c17f", -0.75, 2);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR, "2d168c87-22f4-0365-cf0f-d71d0ba6d337", -0.75, 2);
        }

    }
}
