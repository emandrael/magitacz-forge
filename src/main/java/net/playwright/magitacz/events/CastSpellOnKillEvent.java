package net.playwright.magitacz.events;

import com.tacz.guns.api.event.common.EntityKillByGunEvent;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.Utils.MagitaczDataUtils;
import net.playwright.magitacz.apoth.affix.SpellAffix;
import net.playwright.magitacz.attributes.MagitaczAttributes;

import java.util.Map;

import static net.playwright.magitacz.Utils.SpellCastUtils.doSpellCastOnEntity;
import static net.playwright.magitacz.events.CastSpellOnHurtEvent.random;

@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CastSpellOnKillEvent {
    @SubscribeEvent
    public static void onEntityKill(EntityKillByGunEvent event) {
        LivingEntity shooter = event.getAttacker();
        LivingEntity victim = event.getKilledEntity();


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
                        String key = String.format("magitacz_kills_with_gun_%s_with_%s",itemId,spellAffix.getSpell());

                        CompoundTag tag = shooter.getPersistentData();

                        int count = tag.getInt(key) + 1;

                        tag.putInt(key, count);

                        AbstractSpell spell = MagitaczDataUtils.getSpellOnAffix(spellAffix);

                        if (spell == null) return; // bad id

                        Level world = shooter.level();
                        MagicData shooterData = MagicData.getPlayerMagicData(shooter);

                        shooterData.setAdditionalCastData(new TargetEntityCastData(victim));


                        int spellLevel = (int) Math.round(spellAffix.getValue(attachment,inst));

                        AttributeInstance spell_shot_reduction = shooter.getAttribute(MagitaczAttributes.CASTING_SHOT_AMPLIFICATION.get());


                        switch (spellAffix.getCastType()){
                            case ONKILL: {
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

                            case ONKILLCHANCE: {

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
