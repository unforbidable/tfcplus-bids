package com.unforbidable.tfc.bids.core.features.client.render;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

public class RenderEntityBuilder {

    private final Render renderer;
    private final List<Class<? extends Entity>> items = new ArrayList<>();

    public RenderEntityBuilder(Render renderer) {
        this.renderer = renderer;
    }

    public RenderEntityBuilder entity(Class<? extends Entity> entity) {
        items.add(entity);

        return this;
    }

    public RenderEntitySpec build() {
        return new RenderEntitySpec(renderer, items);
    }

}
