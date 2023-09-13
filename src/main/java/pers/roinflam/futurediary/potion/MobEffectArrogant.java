package pers.roinflam.futurediary.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pers.roinflam.futurediary.base.potion.NetworkBase;

import javax.annotation.Nonnull;


public class MobEffectArrogant extends NetworkBase {

    public MobEffectArrogant(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn, "arrogant");
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderLiving(@Nonnull RenderLivingEvent.Pre evt) {
        EntityPlayerSP entityPlayerMP = Minecraft.getMinecraft().player;
        if (isAction(entityPlayerMP.getEntityId())) {
            evt.setCanceled(true);
        }
    }

    @Override
    public int getSerialNumber() {
        return 0;
    }

}
