package pers.roinflam.futurediary.event.catastrophic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.utils.java.random.RandomUtil;

import javax.annotation.Nonnull;

//手软
public class SlipperyHandEvent extends FutureEvent {

    @SubscribeEvent
    public void onPlayerTick(@Nonnull TickEvent.PlayerTickEvent evt) {
        if (!evt.player.world.isRemote && evt.phase.equals(TickEvent.Phase.START)) {
            World world = evt.player.getEntityWorld();
            if (!this.isEnableEvent(world)) {
                return;
            }
            if (world.getTotalWorldTime() % 1500 == 0 && RandomUtil.percentageChance(25)) {
                EntityPlayer entityPlayer = evt.player;
                boolean dropItem = false;
                if (!entityPlayer.getHeldItemMainhand().isEmpty()) {
                    entityPlayer.dropItem(entityPlayer.getHeldItemMainhand(), true, true);
                    entityPlayer.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    dropItem = true;
                }
                if (!entityPlayer.getHeldItemOffhand().isEmpty()) {
                    entityPlayer.dropItem(entityPlayer.getHeldItemOffhand(), true, true);
                    entityPlayer.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
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
        return "slippery_hand";
    }
}
