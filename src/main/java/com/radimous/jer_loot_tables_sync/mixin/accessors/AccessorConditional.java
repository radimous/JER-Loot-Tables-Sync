package com.radimous.jer_loot_tables_sync.mixin.accessors;

import jeresources.api.conditionals.Conditional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Conditional.class, remap = false)
public interface AccessorConditional {

    @Accessor
    String getText();

    @Accessor
    String getColour();

    @Accessor
    void setColour(String color);
}
