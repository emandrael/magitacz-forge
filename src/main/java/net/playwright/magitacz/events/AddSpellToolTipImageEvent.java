package net.playwright.magitacz.events;




import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.item.ModernKineticGunItem;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.Utils.MagitaczDataUtils;
import net.playwright.magitacz.Utils.MagitaczTooltipUtils;
import net.playwright.magitacz.attachment_modifiers.AttachedSpell;


@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AddSpellToolTipImageEvent {
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack gunItemStack = event.getItemStack();

        ModernKineticGunScriptAPI api = new ModernKineticGunScriptAPI();
        api.setItemStack(gunItemStack);

        if (gunItemStack.getItem() instanceof ModernKineticGunItem gunItem) {

            AttachedSpell attachedSpell = MagitaczDataUtils.getAttachmentSpellData(gunItemStack, api.getGunIndex().getGunData(),AttachmentType.STOCK);

            Component component = MagitaczTooltipUtils.getGunAttachmentTooltip(attachedSpell);

            if (component != null) {
                event.getToolTip().add(component);
            }


        }}
}
