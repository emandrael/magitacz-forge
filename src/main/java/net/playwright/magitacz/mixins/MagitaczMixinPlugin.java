package net.playwright.magitacz.mixins;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;


import java.util.List;
import java.util.Set;

public class MagitaczMixinPlugin implements IMixinConfigPlugin {
    @Override
public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
    if (mixinClassName.endsWith("CaptainCorneliaMixin")) {
        // Check for the class as a resource instead of loading it
        return getClass().getClassLoader().getResource(
            "com/obscuria/aquamirae/common/entities/CaptainCornelia.class") != null;
    }
    return true;
}

    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}