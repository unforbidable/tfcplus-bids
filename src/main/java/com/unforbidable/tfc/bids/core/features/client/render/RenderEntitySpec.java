package com.unforbidable.tfc.bids.core.features.client.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

import java.util.List;

public class RenderEntitySpec {

    public final Render renderer;
    public final List<Class<? extends Entity>> entities;

    public RenderEntitySpec(Render renderer, List<Class<? extends Entity>> entities) {
        this.renderer = renderer;
        this.entities = entities;
    }

}
