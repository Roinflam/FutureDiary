package pers.roinflam.futurediary.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pers.roinflam.futurediary.event.FutureEvent;
import pers.roinflam.futurediary.init.FutureDiaryItems;
import pers.roinflam.futurediary.utils.IHasModel;
import pers.roinflam.futurediary.utils.util.ItemUtil;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.UUID;

@Mod.EventBusSubscriber
public class RandomDiary extends Item implements IHasModel {
    private final static HashSet<UUID> uuid = new HashSet<>();

    public RandomDiary(@Nonnull String name, @Nonnull CreativeTabs creativeTabs) {
        ItemUtil.registerItem(this, name, creativeTabs);

        setMaxStackSize(1);

        FutureDiaryItems.ITEMS.add(this);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onItemTooltip(@Nonnull ItemTooltipEvent evt) {
        ItemStack itemStack = evt.getItemStack();
        Item item = itemStack.getItem();
        if (item instanceof RandomDiary) {
            evt.getToolTip().add(1, TextFormatting.DARK_GRAY + String.valueOf(TextFormatting.ITALIC) + I18n.format("item.random_diary.tooltip"));
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(@Nonnull EntityJoinWorldEvent evt) {
        if (!evt.getWorld().isRemote) {
            if (evt.getEntity() instanceof EntityPlayer && !uuid.contains(evt.getEntity().getUniqueID())) {
                EntityPlayer entityPlayer = (EntityPlayer) evt.getEntity();
                entityPlayer.getCooldownTracker().setCooldown(FutureDiaryItems.RANDOM_DIARY, 1200);
                uuid.add(entityPlayer.getUniqueID());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(@Nonnull net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent evt) {
        if (!evt.player.world.isRemote) {
            uuid.remove(evt.player.getUniqueID());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(@Nonnull PlayerEvent.Clone evt) {
        if (!evt.getEntity().world.isRemote && evt.isWasDeath()) {
            EntityPlayer entityPlayer = evt.getEntityPlayer();
            entityPlayer.getCooldownTracker().setCooldown(FutureDiaryItems.RANDOM_DIARY, 1200);
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, @Nonnull EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote) {
            FutureEvent.show(playerIn);
            playerIn.getCooldownTracker().setCooldown(itemstack.getItem(), 1200);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

    @Nonnull
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
