package pers.roinflam.futurediary.event.bad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.base.potion.hide.PrivateHideBase;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;

import javax.annotation.Nonnull;

import static pers.roinflam.futurediary.event.bad.MingdaoEvent.MobEffectAttackDamageBoost.ATTACK_DAMAGE_BOOST;

//Ãûµ¶
public class MingdaoEvent extends FutureEvent {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLivingDeath(@Nonnull LivingDeathEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (RandomUtil.percentageChance(10)) {
                EntityLivingBase entityLivingBase = evt.getEntityLiving();
                evt.setCanceled(true);
                entityLivingBase.setHealth(entityLivingBase.getMaxHealth());
                entityLivingBase.addPotionEffect(new PotionEffect(ATTACK_DAMAGE_BOOST, 200, 0));

                DamageSource damageSource = evt.getSource();
                this.sendReminder(entityLivingBase);
                if (damageSource.getTrueSource() instanceof EntityLivingBase) {
                    this.sendReminder((EntityLivingBase) damageSource.getTrueSource());
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
        return "ming_dao";
    }

    static class MobEffectAttackDamageBoost extends PrivateHideBase {
        public static final MobEffectAttackDamageBoost ATTACK_DAMAGE_BOOST = new MobEffectAttackDamageBoost(false, 0, "ming_dao_MobEffectAttackDamageBoost");

        protected MobEffectAttackDamageBoost(boolean isBadEffectIn, int liquidColorIn, @Nonnull String name) {
            super(isBadEffectIn, liquidColorIn, name);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "bf1a787b-feb7-1b20-9723-7d476b678bcf", 1, 2);
        }

    }
}
