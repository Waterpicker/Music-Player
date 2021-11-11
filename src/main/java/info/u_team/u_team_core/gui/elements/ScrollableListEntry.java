package info.u_team.u_team_core.gui.elements;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;

public abstract class ScrollableListEntry<T extends ScrollableListEntry<T>> extends AlwaysSelectedEntryListWidget.Entry<T> {
	
	protected final MinecraftClient minecraft;
	
	private final List<Element> children;
	
	public ScrollableListEntry() {
		minecraft = MinecraftClient.getInstance();
		children = new ArrayList<>();
	}
	
	protected <B extends Element> B addChildren(B listener) {
		children.add(listener);
		return listener;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		children.forEach(listener -> listener.mouseClicked(mouseX, mouseY, button));
		return true;
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		for (final Element listener : children) {
			if (listener.mouseReleased(mouseX, mouseY, button)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		for (final Element listener : children) {
			if (listener.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public abstract void render(MatrixStack poseStack, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTicks);
	
	@SuppressWarnings("deprecation")
	protected EntryListWidget<T> getList() {
		return parentList;
	}
}
