package info.u_team.u_team_core.gui.elements;

import info.u_team.u_team_core.util.RenderUtil;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CheckboxButton extends UButton {
	
	protected static final Identifier TEXTURE = new Identifier("textures/gui/checkbox.png");
	
	protected boolean checked;
	
	protected boolean drawText;
	protected boolean leftSideText;
	protected boolean dropShadow;
	
	public CheckboxButton(int x, int y, int width, int height, Text text, boolean checked, boolean drawText) {
		this(x, y, width, height, text, checked, drawText, EMTPY_PRESSABLE);
	}
	
	public CheckboxButton(int x, int y, int width, int height, Text text, boolean checked, boolean drawText, PressAction pessable) {
		this(x, y, width, height, text, checked, drawText, pessable, EMPTY_TOOLTIP);
	}
	
	public CheckboxButton(int x, int y, int width, int height, Text text, boolean checked, boolean drawText, TooltipSupplier tooltip) {
		this(x, y, width, height, text, checked, drawText, EMTPY_PRESSABLE, tooltip);
	}
	
	public CheckboxButton(int x, int y, int width, int height, Text text, boolean checked, boolean drawText, PressAction pessable, TooltipSupplier tooltip) {
		super(x, y, width, height, text, pessable, tooltip);
		this.checked = checked;
		this.drawText = drawText;
		leftSideText = false;
		dropShadow = false;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public boolean isDrawText() {
		return drawText;
	}
	
	public void setDrawText(boolean drawText) {
		this.drawText = drawText;
	}
	
	public boolean isLeftSideText() {
		return leftSideText;
	}
	
	public void setLeftSideText(boolean leftSideText) {
		this.leftSideText = leftSideText;
	}
	
	public void toggle() {
		checked = !checked;
	}
	
	@Override
	public void onPress() {
		toggle();
		super.onPress();
	}
	
	@Override
	public void renderButton(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		RenderUtil.drawTexturedQuad(poseStack, x, y, width, height, 20, 20, isHovered() ? 20 : 0, checked ? 20 : 0, 64, 64, 0, TEXTURE, getCurrentBackgroundColor(poseStack, mouseX, mouseY, partialTicks));
		
		renderBackground(poseStack, mouseX, mouseY, partialTicks);
		renderForeground(poseStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void renderForeground(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		if (drawText) {
			final TextRenderer font = getCurrentTextFont();
			
			final Text message = getCurrentText();
			if (message != LiteralText.EMPTY) {
				final float xStart;
				final float yStart = y + (height - 8) / 2;
				
				if (leftSideText) {
					xStart = x - (font.getWidth(message) + 4);
				} else {
					xStart = x + width + 4;
				}
				
				final int color = getCurrentTextColor(poseStack, mouseX, mouseY, partialTicks).getColorARGB();
				
				if (dropShadow) {
					font.drawWithShadow(poseStack, getCurrentText(), xStart, yStart, color);
				} else {
					font.draw(poseStack, getCurrentText(), xStart, yStart, color);
				}
			}
		}
	}
}
