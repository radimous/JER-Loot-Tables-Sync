package com.radimous.jer_loot_tables_sync.mixin.accessors;

import jeresources.api.conditionals.LightLevel;
import jeresources.api.drop.LootDrop;
import jeresources.compatibility.minecraft.ExperienceRange;
import jeresources.entry.MobEntry;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

@Mixin(value = MobEntry.class, remap = false)
public interface AccessorMobEntry {

    @Invoker("<init>")
    static MobEntry createMobEntry(Supplier<LivingEntity> entitySupplier, @Nullable LightLevel lightLevel, @Nullable ExperienceRange experience, List<String> biomes, List<LootDrop> drops) {throw new UnsupportedOperationException();}

    @Accessor
    static List<String> getANY_BIOMES() {throw new UnsupportedOperationException();}
}
