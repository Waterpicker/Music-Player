package info.u_team.u_team_core.gui.elements;

import info.u_team.u_team_core.api.gui.Scalable;
import info.u_team.u_team_core.api.gui.ScaleProvider;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class ScalableCheckboxButton extends CheckboxButton implements Scalable, ScaleProvider {
	
	protected float scale;
	
	public ScalableCheckboxButton(int x, int y, int width, int height, Text text, boolean checked, boolean drawText, float scale) {
		this(x, y, width, height, text, checked, drawText, scale, EMTPY_PRESSABLE);
	}
	
	public ScalableCheckboxButton(int x, int y, int width, int height, Text text, boolean checked, boolean drawText, float scale, PressAction pessable) {
		this(x, y, width, height, text, checked, drawText, scale, pessable, EMPTY_TOOLTIP);
	}
	
	public ScalableCheckboxButton(int x, int y, int width, int height, Text text, boolean checked, boolean drawText, float scale, TooltipSupplier tooltip) {
		this(x, y, width, height, text, checked, drawText, scale, EMTPY_PRESSABLE, tooltip);
	}
	
	public ScalableCheckboxButton(int x, int y, int width, int height, Text text, boolean checked, boolean drawText, float scale, PressAction pessable, TooltipSupplier tooltip) {
		super(x, y, width, height, text, checked, drawText, pessable, tooltip);
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
		if (drawText) {
			final TextRenderer font = getCurrentTextFont();
			
			final Text message = getCurrentText();
			if (message != LiteralText.EMPTY) {
				final float currentScale = getCurrentScale(poseStack, mouseX, mouseY, partialTicks);
				
				final float positionFactor = 1 / currentScale;
				
				final float xStart;
				final float yStart = (y + ((int) (height - 8 * currentScale)) / 2) * positionFactor;
				
				if (leftSideText) {
					xStart = (x - ((font.getWidth(message) * currentScale) + 4)) * positionFactor;
				} else {
					xStart = (x + width + 4) * positionFactor;
				}
				
				final int color = getCurrentTextColor(poseStack, mouseX, mouseY, partialTicks).getColorARGB();
				
				poseStack.push();
				poseStack.scale(currentScale, currentScale, 0);
				
				if (dropShadow) {
					font.drawWithShadow(poseStack, getCurrentText(), xStart, yStart, color);
				} else {
					font.draw(poseStack, getCurrentText(), xStart, yStart, color);
				}
				
				poseStack.pop();
			}
		}
	}
	
	@Override
	public float getCurrentScale(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return scale;
	}
}
