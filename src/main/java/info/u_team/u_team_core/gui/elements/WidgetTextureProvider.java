package info.u_team.u_team_core.gui.elements;

import java.util.function.Function;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.util.Identifier;
import info.u_team.u_team_core.api.gui.TextureProvider;

public class WidgetTextureProvider implements TextureProvider {
	
	protected ClickableWidget widget;
	protected Function<Boolean, Integer> yImage;
	
	public WidgetTextureProvider(ClickableWidget widget, Function<Boolean, Integer> yImage) {
		this.widget = widget;
		this.yImage = yImage;
	}
	
	@Override
	public Identifier getTexture() {
		return ClickableWidget.WIDGETS_TEXTURE;
	}
	
	@Override
	public int getU() {
		return 0;
	}
	
	@Override
	public int getV() {
		return 46 + yImage.apply(widget.isHovered()) * 20;
	}
	
	@Override
	public int getWidth() {
		return 200;
	}
	
	@Override
	public int getHeight() {
		return 20;
	}
	
}
