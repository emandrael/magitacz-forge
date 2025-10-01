package net.playwright.magitacz.attachment_modifiers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ElementalSchoolBullet {
    @SerializedName("element")
    private String element = "none";

    @SerializedName("damage")
    private float damage = 0.0f;

    public String getElement() {
        return element;
    }

    public float getDamage() {
        return damage;
    }

    public static ElementalSchoolBullet of(ElementalSchoolBullet elementalSchoolBullet, List<ElementalSchoolBullet> elementalSchoolBullets) {
        ElementalSchoolBullet result = new ElementalSchoolBullet();
        result.element = elementalSchoolBullet.element;
        result.damage = elementalSchoolBullet.damage;


        for(ElementalSchoolBullet listElementalBullet : elementalSchoolBullets) {
            result.element = listElementalBullet.element;
            result.damage = listElementalBullet.damage;
        }
        return result;
    }
}
