package pers.roinflam.futurediary.event.catastrophic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;

import javax.annotation.Nonnull;

//底层突破
public class BottomBreakthroughEvent extends FutureEvent {

    @SubscribeEvent
    public void onLivingAttack(@Nonnull LivingAttackEvent evt) {
        if (!evt.getEntity().world.isRemote) {
            World world = evt.getEntity().world;
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (evt.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) evt.getEntityLiving();
                boolean dropItem = false;
                if (!entityPlayer.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty() && RandomUtil.percentageChance(2.5)) {
                    entityPlayer.dropItem(entityPlayer.getItemStackFromSlot(EntityEquipmentSlot.HEAD), true, true);
                    entityPlayer.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
                    dropItem = true;
                }
                if (!entityPlayer.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty() && RandomUtil.percentageChance(2.5)) {
                    entityPlayer.dropItem(entityPlayer.getItemStackFromSlot(EntityEquipmentSlot.CHEST), true, true);
                    entityPlayer.setItemStackToSlot(EntityEquipmentSlot.CHEST, ItemStack.EMPTY);
                    dropItem = true;
                }
                if (!entityPlayer.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty() && RandomUtil.percentageChance(2.5)) {
                    entityPlayer.dropItem(entityPlayer.getItemStackFromSlot(EntityEquipmentSlot.FEET), true, true);
                    entityPlayer.setItemStackToSlot(EntityEquipmentSlot.FEET, ItemStack.EMPTY);
                    dropItem = true;
                }
                if (!entityPlayer.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty() && RandomUtil.percentageChance(2.5)) {
                    entityPlayer.dropItem(entityPlayer.getItemStackFromSlot(EntityEquipmentSlot.LEGS), true, true);
                    entityPlayer.setItemStackToSlot(EntityEquipmentSlot.LEGS, ItemStack.EMPTY);
                    dropItem = true;
                }
                if (dropItem) {
                    this.sendReminder(entityPlayer);
                }
            }
        }
    }

    @Override
    public boolean isBadEvent() {
        return false;
    }

    @Override
    public boolean isGoodEvent() {
        return false;
    }

    @Override
    public boolean isCatastrophicEvent() {
        return true;
    }

    @Nonnull
    @Override
    public String getEventName() {
        return "bottom_breakthrough";
    }
}
