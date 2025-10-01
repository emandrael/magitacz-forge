package net.playwright.magitacz.attachment_modifiers;

import com.google.gson.annotations.SerializedName;
import com.tacz.guns.api.modifier.CacheValue;
import com.tacz.guns.api.modifier.IAttachmentModifier;
import com.tacz.guns.api.modifier.JsonProperty;
import com.tacz.guns.resource.CommonAssetsManager;
import com.tacz.guns.resource.pojo.data.gun.GunData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.playwright.magitacz.MagitaczMod;
import net.playwright.magitacz.Utils.MagitaczDataUtils;


import javax.annotation.Nullable;
import java.util.List;

public class SpellModifier implements IAttachmentModifier<AttachedSpell, AttachedSpell> {

    public SpellModifier() {

    }

    public static final String ID = "spell";


    @Override
    public String getId() {
        return "spell";
    }

    @Override
    public JsonProperty<AttachedSpell> readJson(String json) {
        Data data = (Data) CommonAssetsManager.GSON.fromJson(json, Data.class);
        AttachedSpell spell = data.getAttachedSpell();
        return new ExtraSpellJsonProperty(spell);
    }

    @Override
    public CacheValue<AttachedSpell> initCache(ItemStack itemStack, GunData gunData) {
        var attachedSpells = MagitaczDataUtils.getAttachedSpells(itemStack,gunData);

        if (attachedSpells.isEmpty()) {
            return new CacheValue<>(new AttachedSpell());
        }

        CacheValue<AttachedSpell> cacheValue = new CacheValue<AttachedSpell>(attachedSpells.get(0));
        return cacheValue;
    }

    @Override
    public void eval(List<AttachedSpell> modifiers, CacheValue<AttachedSpell> cache) {
        try {
            cache.setValue(AttachedSpell.of((AttachedSpell) cache.getValue(), modifiers));

        }
        catch (Exception e) {
            MagitaczMod.LOGGER.info("wht hao",e.getMessage());
        }
    }

    public static class ExtraSpellJsonProperty extends JsonProperty<AttachedSpell> {
        public ExtraSpellJsonProperty(AttachedSpell spell) {
            super(spell);
        }

        public void initComponents() {
            AttachedSpell attachedSpell = (AttachedSpell) this.getValue();
            if (attachedSpell != null) {
                this.resolveComponent(attachedSpell);

            }
        }

        private void resolveComponent(AttachedSpell spell) {

            var per_x = spell.getCastTypeParameters().get("per_x").intValue();

            boolean singular = per_x == 1;
            String spellName = I18n.get(("spell."+ spell.getSpellRegistry().registryName + "." + spell.getSpellName()));

            Component component;


            switch (spell.getCastType()) {

                case ONKILL -> {

                    if (singular) {
                        component = Component.translatable("guns.magitacz:spell_attachment_on_kill_singular", spellName, "takedown").withStyle(ChatFormatting.GREEN);
                    } else {
                        component = Component.translatable("guns.magitacz:spell_attachment_on_kill", spellName, per_x, "takendown").withStyle(ChatFormatting.RED);
                    }
                }
                case ONHIT -> {
                    if (singular) {
                        component = Component.translatable("guns.magitacz:spell_attachment_on_hit_singular", spellName, "lands").withStyle(ChatFormatting.LIGHT_PURPLE);
                    } else {
                        component = Component.translatable("guns.magitacz:spell_attachment_on_hit", spellName, per_x, "lands").withStyle(ChatFormatting.LIGHT_PURPLE);
                    }
                }
                case ONSHOOT -> {
                    if (singular) {
                        component = Component.translatable("guns.magitacz:spell_attachment_on_shoot_singular", spellName, "fired").withStyle(ChatFormatting.GREEN);
                    } else {
                        component = Component.translatable("guns.magitacz:spell_attachment_on_shoot", spellName, per_x, "fired").withStyle(ChatFormatting.GREEN);
                    }
                }

                default -> {
                    component = Component.literal("Something went wrong, please check TaCz config for this attachment");
                }
            }

            this.components.add(Component.literal(""));
            this.components.add(Component.literal("Spell Attachement")
                    .withStyle(ChatFormatting.GREEN));
            this.components.add(component);
            this.components.add(Component.literal(""));

        }


    }

    public static class Data {
        @SerializedName("spell")
        @Nullable
        private AttachedSpell attachedSpell = null;

        public Data() {
        }
        @Nullable
        public AttachedSpell getAttachedSpell() {
            return this.attachedSpell;
        }
    }
}