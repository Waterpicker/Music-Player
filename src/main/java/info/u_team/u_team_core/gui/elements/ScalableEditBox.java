package info.u_team.u_team_core.gui.elements;

import info.u_team.u_team_core.api.gui.Scalable;
import info.u_team.u_team_core.api.gui.ScaleProvider;
import info.u_team.u_team_core.util.RGBA;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class ScalableEditBox extends UEditBox implements Scalable, ScaleProvider {
	
	protected float scale;
	
	public ScalableEditBox(TextRenderer font, int x, int y, int width, int height, UEditBox previousEditBox, Text title, float scale) {
		this(font, x, y, width, height, previousEditBox, title, scale, EMPTY_TOOLTIP);
	}
	
	public ScalableEditBox(TextRenderer font, int x, int y, int width, int height, UEditBox previousEditBox, Text title, float scale, OnTooltip tooltip) {
		super(font, x, y, width, height, previousEditBox, title, tooltip);
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
		final float currentScale = getCurrentScale(poseStack, mouseX, mouseY, partialTicks);
		
		final float positionFactor = 1 / scale;
		
		poseStack.push();
		poseStack.scale(currentScale, currentScale, 0);
		
		final RGBA currentTextColor = getCurrentTextColor(poseStack, mouseX, mouseY, partialTicks);
		
		final String currentText = textRenderer.trimToWidth(text.substring(firstCharacterIndex), getInnerWidth());
		
		final int cursorOffset = selectionStart - firstCharacterIndex;
		final int selectionOffset = Math.min(selectionEnd - firstCharacterIndex, currentText.length());
		
		final boolean isCursorInText = cursorOffset >= 0 && cursorOffset <= currentText.length();
		final boolean shouldCursorBlink = isFocused() && focusedTicks / 6 % 2 == 0 && isCursorInText;
		final boolean isCursorInTheMiddle = selectionStart < text.length() || text.length() >= maxLength;
		
		final int xOffset = (int) ((drawsBackground ? x + 4 : x) * positionFactor);
		final int yOffset = (int) ((drawsBackground ? y + (int) (height - 8 * scale) / 2 : y) * positionFactor);
		
		int leftRenderedTextX = xOffset;
		
		if (!currentText.isEmpty()) {
			final String firstTextPart = isCursorInText ? currentText.substring(0, cursorOffset) : currentText;
			leftRenderedTextX = textRenderer.drawWithShadow(poseStack, renderTextProvider.apply(firstTextPart, firstCharacterIndex), xOffset, yOffset, currentTextColor.getColorARGB());
		}
		
		int rightRenderedTextX = leftRenderedTextX;
		
		if (!isCursorInText) {
			rightRenderedTextX = cursorOffset > 0 ? xOffset + width : xOffset;
		} else if (isCursorInTheMiddle) {
			rightRenderedTextX = leftRenderedTextX - 1;
			--leftRenderedTextX;
		}
		
		if (!currentText.isEmpty() && isCursorInText && cursorOffset < currentText.length()) {
			textRenderer.drawWithShadow(poseStack, renderTextProvider.apply(currentText.substring(cursorOffset), selectionStart), leftRenderedTextX, yOffset, currentTextColor.getColorARGB());
		}
		
		if (!isCursorInTheMiddle && suggestion != null) {
			textRenderer.drawWithShadow(poseStack, suggestion, rightRenderedTextX - 1, yOffset, getCurrentSuggestionTextColor(poseStack, mouseX, mouseY, partialTicks).getColorARGB());
		}
		
		if (shouldCursorBlink) {
			if (isCursorInTheMiddle) {
				DrawableHelper.fill(poseStack, rightRenderedTextX, yOffset - 1, rightRenderedTextX + 1, yOffset + 1 + 9, getCurrentCursorColor(poseStack, mouseX, mouseY, partialTicks).getColorARGB());
			} else {
				textRenderer.drawWithShadow(poseStack, "_", rightRenderedTextX, yOffset, currentTextColor.getColorARGB());
			}
		}
		
		if (selectionOffset != cursorOffset) {
			final int selectedX = xOffset + textRenderer.getWidth(currentText.substring(0, selectionOffset));
			drawSelectionHighlight((int) (rightRenderedTextX * currentScale), (int) ((yOffset - 1) * currentScale), (int) ((selectedX - 1) * currentScale), (int) ((yOffset + 1 + 9) * currentScale));
		}
		
		poseStack.pop();
	}
	
	@Override
	public float getCurrentScale(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return getCurrentScale(mouseX, mouseY);
	}
	
	public float getCurrentScale(double mouseX, double mouseY) {
		return scale;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (!visible) {
			return false;
		} else {
			final boolean clicked = clicked(mouseX, mouseY);
			
			if (focusUnlocked) {
				setTextFieldFocused(clicked);
			}
			
			if (isFocused() && clicked && button == 0) {
				int clickOffset = MathHelper.floor(mouseX) - x;
				if (drawsBackground) {
					clickOffset -= 4;
				}
				
				clickOffset /= getCurrentScale(mouseX, mouseY);
				
				final String currentText = textRenderer.trimToWidth(text.substring(firstCharacterIndex), getInnerWidth());
				setCursor(textRenderer.trimToWidth(currentText, clickOffset).length() + firstCharacterIndex);
				return true;
			} else {
				return false;
			}
		}
	}
	
}
