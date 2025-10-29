package net.playwright.magitacz.events;

import com.tacz.guns.api.TimelessAPI;
import com.tacz.guns.api.event.common.GunFireEvent;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import com.tacz.guns.resource.index.CommonAttachmentIndex;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixRegistry;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.Config;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.Utils.MagitaczDataUtils;
import net.playwright.magitacz.apoth.affix.SpellAffix;
import net.playwright.magitacz.attributes.MagitaczAttributes;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static net.playwright.magitacz.Utils.SpellCastUtils.doSpellCastOnEntity;

@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CastSpellOnShootEvent {
    static Random random = new Random();



    @SubscribeEvent
    public static void onDebugShot(GunFireEvent event) {
        if (!Config.debug) return;

        ArrayList<String> attachments = new ArrayList<>();

        for (Map.Entry<ResourceLocation, CommonAttachmentIndex> attachment : TimelessAPI.getAllCommonAttachmentIndex().stream().toList()) {
            attachments.add(String.format("\"%s@10\"",attachment.getKey()));
        }

        MagitaczMod.LOGGER.info(String.format("%s",attachments));

    }

    @SubscribeEvent
    public static void onGunFire(GunFireEvent event) {



        LivingEntity shooter = event.getShooter();


        if (!(shooter instanceof LivingEntity) || event.getLogicalSide() != LogicalSide.SERVER) return;


        ItemStack gunItem = shooter.getMainHandItem();

        IGun iGun = IGun.getIGunOrNull(gunItem);

        for (AttachmentType type : AttachmentType.values())
        {
            var attachment = iGun.getAttachment(gunItem,type);

            if ( attachment != null && attachment.hasTag()) {
                Map<DynamicHolder<? extends Affix>, AffixInstance> affixes = AffixHelper.getAffixes(attachment);
                affixes.forEach((afx, inst) -> {

                    if ((inst.affix().get() instanceof SpellAffix spellAffix)) {


                        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(gunItem.getItem());
                        String key = String.format("magitacz_shots_fired_with_gun_%s_with_%s",itemId,spellAffix.getSpell());

                        CompoundTag tag = shooter.getPersistentData();

                        int count = tag.getInt(key) + 1;

                        tag.putInt(key, count);

                        AbstractSpell spell = MagitaczDataUtils.getSpellOnAffix(spellAffix);

                        if (spell == null) return; // bad id

                        Level world = shooter.level();

                        int spellLevel = (int) Math.round(spellAffix.getValue(attachment,inst));

                        AttributeInstance spell_shot_reduction = shooter.getAttribute(MagitaczAttributes.CASTING_SHOT_AMPLIFICATION.get());


                        switch (spellAffix.getCastType()){
                            case ONSHOOT: {
                                // Every X bullets, cast (if param is missing, default 1)

                                CompoundTag current_tag = shooter.getPersistentData();

                                var spell_shot_reduction_value = spell_shot_reduction.getValue();

                                var player_cast_type_parameter = (int) Math.round(spellAffix.getCastParameter() / spell_shot_reduction_value);

                                int current_count = current_tag.getInt(key);

                                boolean shouldTrigger = current_count % player_cast_type_parameter == 0;

                                if (shouldTrigger) {
                                    doSpellCastOnEntity( shooter, spell, world, spellLevel);
                                }

                            }

                            case ONSHOOTCHANCE: {

                                var randomNum = random.nextDouble();
                                double percent =  (spellAffix.getCastParameter() / 100.0);

                                boolean shouldTrigger = randomNum <= percent;

                                if (shouldTrigger) {
                                    doSpellCastOnEntity( shooter, spell, world, spellLevel);
                                }
                            }
                        }
                    }


                });
            }


        }

}
}