package net.playwright.magitacz.attachment_modifiers;

import com.google.gson.annotations.SerializedName;
import io.redspace.ironsspellbooks.IronsSpellbooks;

import java.util.List;
import java.util.Map;


public class AttachedSpell {

    public enum CastType {
        ONHIT,
        ONCHANCE,
        ONKILL,
        ONSHOOT,
    }

    public enum SpellRegistryEnum {
        IRONS(IronsSpellbooks.MODID);
//        TOS(TravelopticsMod.MODID);

        public final String registryName;

        SpellRegistryEnum(String registryName) {
            this.registryName = registryName;
        }
    }

    @SerializedName("mod")
    private SpellRegistryEnum spell_registry = SpellRegistryEnum.IRONS;


    @SerializedName("spell_name")
    private String spell_name = "chain_lightning";

    @SerializedName("spell_level")
    private int spell_level = 1;

    @SerializedName("cast_type")
    private CastType cast_type = CastType.ONHIT;

    @SerializedName("cast_type_parameters")
    private Map<String, Double> cast_type_parameters = null;

    // per_x for ONHIT


    // ...existing methods...

    public Map<String, Double> getCastTypeParameters() {
        return this.cast_type_parameters;
    }

    public String getSpellName() {
        return this.spell_name;
    }

    public SpellRegistryEnum getSpellRegistry() {
        return spell_registry;
    }

    public Integer getSpellLevel() {
        return this.spell_level;
    }

    public CastType getCastType() {
        return this.cast_type;
    }

    public static AttachedSpell of(AttachedSpell attachedSpell, List<AttachedSpell> attachedSpells) {
        AttachedSpell result = new AttachedSpell();
        result.spell_name = attachedSpell.spell_name;
        result.spell_level = attachedSpell.spell_level;
        result.cast_type = attachedSpell.cast_type;
        result.spell_registry = attachedSpell.spell_registry;
        result.cast_type_parameters = attachedSpell.cast_type_parameters;

        for(AttachedSpell listAttachedSpell : attachedSpells) {
            result.spell_name = listAttachedSpell.spell_name;
            result.spell_level = listAttachedSpell.spell_level;
            result.cast_type = listAttachedSpell.cast_type;
            result.spell_registry = listAttachedSpell.spell_registry;
            result.cast_type_parameters = listAttachedSpell.cast_type_parameters;
        }
        return result;
    }

    @Override
    public String toString() {
        return "AttachedSpell{" +
                "spell_name='" + spell_name + '\'' +
                ", spell_level=" + spell_level +
                ", cast_type=" + cast_type +
                ", cast_type_parameters=" + cast_type_parameters +
                ", spell_registry='" + spell_registry + '\'' +
                '}';
    }
}
