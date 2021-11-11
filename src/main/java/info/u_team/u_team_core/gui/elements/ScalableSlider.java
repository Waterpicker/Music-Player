package info.u_team.u_team_core.gui.elements;

import info.u_team.u_team_core.api.gui.Scalable;
import info.u_team.u_team_core.api.gui.ScaleProvider;
import info.u_team.u_team_core.util.WidgetUtil;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class ScalableSlider extends USlider implements Scalable, ScaleProvider {
	
	protected float scale;
	
	public ScalableSlider(int x, int y, int width, int height, Text prefix, Text suffix, double minValue, double maxValue, double value, boolean decimalPrecision, boolean drawDescription, boolean isInContainer, float scale) {
		this(x, y, width, height, prefix, suffix, minValue, maxValue, value, decimalPrecision, drawDescription, isInContainer, scale, EMTPY_SLIDER);
	}
	
	public ScalableSlider(int x, int y, int width, int height, Text prefix, Text suffix, double minValue, double maxValue, double value, boolean decimalPrecision, boolean drawDescription, boolean isInContainer, float scale, ISlider slider) {
		this(x, y, width, height, prefix, suffix, minValue, maxValue, value, decimalPrecision, drawDescription, isInContainer, scale, slider, EMPTY_TOOLTIP);
	}
	
	public ScalableSlider(int x, int y, int width, int height, Text prefix, Text suffix, double minValue, double maxValue, double value, boolean decimalPrecision, boolean drawDescription, boolean isInContainer, float scale, TooltipSupplier tooltip) {
		this(x, y, width, height, prefix, suffix, minValue, maxValue, value, decimalPrecision, drawDescription, isInContainer, scale, EMTPY_SLIDER, tooltip);
	}
	
	public ScalableSlider(int x, int y, int width, int height, Text prefix, Text suffix, double minValue, double maxValue, double value, boolean decimalPrecision, boolean drawDescription, boolean isInContainer, float scale, ISlider slider, TooltipSupplier tooltip) {
		super(x, y, width, height, prefix, suffix, minValue, maxValue, value, decimalPrecision, drawDescription, isInContainer, slider, tooltip);
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
