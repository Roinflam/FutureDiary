package pers.roinflam.futurediary.event.good;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pers.roinflam.futurediary.base.potion.hide.PrivateHideBase;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.helper.task.SynchronizationTask;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static pers.roinflam.futurediary.event.good.HardenedArmorEvent.MobEffectHardenedArmor.HARDENED_ARMOR;

//º¡¹Ç¶Æ²ã
public class HardenedArmorEvent extends FutureEvent {
    private final List<String> battle = new ArrayList<>();

    @SubscribeEvent
    public void onPlayerTick(@Nonnull TickEvent.PlayerTickEvent evt) {
        if (!evt.player.world.isRemote && evt.phase.equals(TickEvent.Phase.START)) {
            World world = evt.player.getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            EntityPlayer entityPlayer = evt.player;
            if (world.getTotalWorldTime() % 20 == 0 && !battle.contains(entityPlayer.getUniqueID().toString())) {
                entityPlayer.addPotionEffect(new PotionEffect(HARDENED_ARMOR, 100, 0));
                this.sendReminder(entityPlayer);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDamage(@Nonnull LivingDamageEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (evt.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) evt.getEntityLiving();
                String uuidString = entityPlayer.getUniqueID().toString();
                battle.add(uuidString);
                new SynchronizationTask(600) {

                    @Override
                    public void run() {
                        battle.remove(uuidString);
                    }

                }.start();
            }
        }
    }

    @Override
    public boolean isBadEvent() {
        return false;
    }

    @Override
    public boolean isGoodEvent() {
        return true;
    }

    @Override
    public boolean isCatastrophicEvent() {
        return false;
    }

    @Nonnull
    @Override
    public String getEventName() {
        return "hardened_armor";
    }

    static class MobEffectHardenedArmor extends PrivateHideBase {
        public static final MobEffectHardenedArmor HARDENED_ARMOR = new MobEffectHardenedArmor(false, 0x017a00, "hardened_armor_MobEffectHardenedArmor");

        protected MobEffectHardenedArmor(boolean isBadEffectIn, int liquidColorIn, @Nonnull String name) {
            super(isBadEffectIn, liquidColorIn, name);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR, "dffcd2e1-28b1-33b4-55c3-01f6c98530fa", 3.33, 2);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR_TOUGHNESS, "f484fe36-cc7d-044c-f5c4-97f153f6bd1f", 3.33, 2);
        }

    }
}
