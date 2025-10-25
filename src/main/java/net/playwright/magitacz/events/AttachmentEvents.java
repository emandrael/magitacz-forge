package net.playwright.magitacz.events;

import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.item.ModernKineticGunItem;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.socket.SocketHelper;
import dev.shadowsoffire.apotheosis.adventure.socket.SocketedGems;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.MagitaczMod;

import java.sql.Time;
import java.util.Map;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AttachmentEvents {

    @SubscribeEvent
    public static void onItemAttributes(ItemAttributeModifierEvent event) {

        ItemStack gunItemStack = event.getItemStack();

        IGun iGun = IGun.getIGunOrNull(gunItemStack);


        if (event.getSlotType() == EquipmentSlot.MAINHAND && iGun != null ) {

            for (AttachmentType type : AttachmentType.values())
            {
                var attachment = iGun.getAttachment(gunItemStack,type);

                if (attachment.hasTag()) {
                    SocketedGems socketedGems = SocketHelper.getGems(attachment);
                    LootCategory lootCategory = LootCategory.forItem(attachment);
                    EquipmentSlot slotType = event.getSlotType();
                    Objects.requireNonNull(event);
                    socketedGems.addModifiers(lootCategory, slotType, event::addModifier);
                    Map<DynamicHolder<? extends Affix>, AffixInstance> affixes = AffixHelper.getAffixes(attachment);
                    affixes.forEach((afx, inst) -> {
                        EquipmentSlot handSlot = event.getSlotType();
                        Objects.requireNonNull(event);
                        inst.addModifiers(handSlot, event::addModifier);
                    });
                }

            }









        }

    }
}
