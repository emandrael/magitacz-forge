package net.playwright.magitacz.events;

import com.tacz.guns.api.event.common.EntityHurtByGunEvent;
import com.tacz.guns.api.event.common.GunFireEvent;
import com.tacz.guns.api.item.IGun;
import com.tacz.guns.api.item.attachment.AttachmentType;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import dev.shadowsoffire.apotheosis.adventure.affix.Affix;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixHelper;
import dev.shadowsoffire.apotheosis.adventure.affix.AffixInstance;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.socket.SocketHelper;
import dev.shadowsoffire.apotheosis.adventure.socket.SocketedGems;
import dev.shadowsoffire.placebo.reload.DynamicHolder;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static net.playwright.magitacz.Utils.SpellCastUtils.doSpellCastOnEntity;

@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CastSpellOnHurtEvent {
    private static final Logger log = LoggerFactory.getLogger(CastSpellOnHurtEvent.class);
    static Random random = new Random();


    @SubscribeEvent
    public static void onEntityHurtByGunWithSpellAffix(EntityHurtByGunEvent.Post event) {

        LivingEntity shooter = event.getAttacker();
        Entity victim = event.getHurtEntity();


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
                        String key = String.format("magitacz_shots_landed_with_gun_%s_with_%s",itemId,spellAffix.getSpell());



                        CompoundTag tag = shooter.getPersistentData();


                        int count = tag.getInt(key) + 1;

                        MagitaczMod.LOGGER.info("{} {}",key,count);


                        tag.putInt(key, count);



                        AbstractSpell spell = MagitaczDataUtils.getSpellOnAffix(spellAffix);

                        if (spell == null) return; // bad id

                        Level world = shooter.level();
                        MagicData shooterData = MagicData.getPlayerMagicData(shooter);

                        if (victim instanceof LivingEntity livingVictim) {
                            shooterData.setAdditionalCastData(new TargetEntityCastData(livingVictim));
                        }

                        int spellLevel = (int) Math.round(spellAffix.getValue(attachment,inst));

                        AttributeInstance spell_shot_reduction = shooter.getAttribute(MagitaczAttributes.CASTING_SHOT_AMPLIFICATION.get());


                        switch (spellAffix.getCastType()){
                            case ONHIT: {
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

                            case ONHITCHANCE: {

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

