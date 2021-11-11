package info.u_team.u_team_core.gui.elements;

import info.u_team.u_team_core.api.gui.Scalable;
import info.u_team.u_team_core.api.gui.ScaleProvider;
import info.u_team.u_team_core.util.WidgetUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ScalableButton extends UButton implements Scalable, ScaleProvider {
	
	protected float scale;
	
	public ScalableButton(int x, int y, int width, int height, Text text, float scale) {
		this(x, y, width, height, text, scale, EMTPY_PRESSABLE);
	}
	
	public ScalableButton(int x, int y, int width, int height, Text text, float scale, PressAction pessable) {
		this(x, y, width, height, text, scale, pessable, EMPTY_TOOLTIP);
	}
	
	public ScalableButton(int x, int y, int width, int height, Text text, float scale, TooltipSupplier tooltip) {
		this(x, y, width, height, text, scale, EMTPY_PRESSABLE, tooltip);
	}
	
	public ScalableButton(int x, int y, int width, int height, Text text, float scale, PressAction pessable, TooltipSupplier tooltip) {
		super(x, y, width, height, text, pessable, tooltip);
		this.scale = scale;
	}
	
	@Override
	public float getScale() {
		return scale;
	}
	
	@Override
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	@Override
	public void renderForeground(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		WidgetUtil.renderScaledText(this, poseStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public float getCurrentScale(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return scale;
	}
}
