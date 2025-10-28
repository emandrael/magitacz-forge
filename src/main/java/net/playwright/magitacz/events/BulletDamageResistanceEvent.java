package net.playwright.magitacz.events;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.spells.lightning.ChainLightningSpell;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.playwright.magitacz.MagitaczMod;

import static org.joml.Math.clamp;
import static org.joml.Math.max;

@Mod.EventBusSubscriber(modid = MagitaczMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BulletDamageResistanceEvent {


    @SubscribeEvent
    public static void onFinalDamage(net.minecraftforge.event.entity.living.LivingDamageEvent event) {

        var entity = event.getEntity();
        var src = event.getSource();

        Entity direct = src.getDirectEntity(); // e.g., an arrow or the same attacker for melee
        if (!(entity instanceof LivingEntity)) return;

        LivingEntity attacker;

        try {
             attacker = (LivingEntity) src.getEntity();     // the living attacker (shooter), if any
        }
        catch (ClassCastException e) {
            MagitaczMod.LOGGER.warn("Tried to cast an Arrow to LivingEntity", e);
            return;
        }

        if (attacker == null) return;


        String directStr = direct != null ? direct.getType().toShortString() : "null";
        String attackerStr = attacker.getType().toShortString();

        Holder<DamageType> holder = src.typeHolder();
        // 2) Turn the holder into a registry id like "minecraft:mob_attack" or "irons_spellbooks:frost"
        var typeId = holder.unwrapKey()
                .map(ResourceKey::location)
                .map(ResourceLocation::toString)
                .orElse("unknown").split(":");

        String namespace= typeId[0];
        String path = typeId[1] + "_resist";
        String elementPower = typeId[1].replaceFirst("_magic", "_spell_power");

        var amount = event.getAmount();


        if (directStr.equals("bullet") && namespace.equals(IronsSpellbooks.MODID)) {

            var bulletKey = new ResourceLocation(namespace, path);
            var spellResKey = new ResourceLocation(namespace, "spell_resist");
            var bulletElementalSpellPowerKey =  new ResourceLocation(namespace, elementPower);
            var spellPower = new ResourceLocation(namespace, "spell_power");


            var bulletAttr = ForgeRegistries.ATTRIBUTES.getValue(bulletKey);
            var spellResAttr = ForgeRegistries.ATTRIBUTES.getValue(spellResKey);
            var spellPowerAttr = ForgeRegistries.ATTRIBUTES.getValue(spellPower);
            var bulletElementalPowerAttr = ForgeRegistries.ATTRIBUTES.getValue(bulletElementalSpellPowerKey);

            if (spellPowerAttr != null && bulletElementalPowerAttr != null && attacker != null){

                float calculatedMultiplier = (float) (attacker.getAttributeValue(spellPowerAttr) * attacker.getAttributeValue(bulletElementalPowerAttr));

                float powerMultiplier = (float) max(1.0, calculatedMultiplier);
                amount *= powerMultiplier;
            }


            if (bulletAttr == null || spellResAttr == null) return;
            if (!entity.getAttributes().hasAttribute(bulletAttr) || !entity.getAttributes().hasAttribute(spellResAttr)) return;

            var schoolResistance =  entity.getAttributeValue(bulletAttr) - 1;
            var spellResistance = entity.getAttributeValue(spellResAttr) - 1;

            var totalResistance = schoolResistance + spellResistance;

            var clamped = clamp(-1, 0.85, totalResistance);

            float newDamageAmount = (float) (amount * (1 - clamped));

            event.setAmount(newDamageAmount);

            }

        }
    }

