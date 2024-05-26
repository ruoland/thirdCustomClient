package com.example;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

public interface ICustomRenderable {
    <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T pWidget);
}
