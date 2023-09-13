package pers.roinflam.futurediary.handlers;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pers.roinflam.futurediary.FutureDiary;
import pers.roinflam.futurediary.init.FutureDiaryItems;
import pers.roinflam.futurediary.init.FutureDiaryPotion;
import pers.roinflam.futurediary.utils.IHasModel;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber
public class RegistryHandler {

    @SubscribeEvent
    public static void onPotionRegister(@Nonnull RegistryEvent.Register<Potion> evt) {
        evt.getRegistry().registerAll(FutureDiaryPotion.POTIONS.toArray(new Potion[0]));
    }

    @SubscribeEvent
    public static void onItemRegister(@Nonnull RegistryEvent.Register<Item> evt) {
        evt.getRegistry().registerAll(FutureDiaryItems.ITEMS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent evt) {
        for (Item item : FutureDiaryItems.ITEMS) {
            if (item instanceof IHasModel) {
                FutureDiary.proxy.registerItemRenderer(item, 0, "inventory");
            }
        }
    }
}
