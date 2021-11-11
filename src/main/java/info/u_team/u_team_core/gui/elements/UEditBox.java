package info.u_team.u_team_core.gui.elements;

import info.u_team.u_team_core.api.gui.BackgroundColorProvider;
import info.u_team.u_team_core.api.gui.PerspectiveRenderable;
import info.u_team.u_team_core.api.gui.RenderTickable;
import info.u_team.u_team_core.api.gui.TextSettingsProvider;
import info.u_team.u_team_core.util.RGBA;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class UEditBox extends TextFieldWidget implements RenderTickable, PerspectiveRenderable, BackgroundColorProvider, TextSettingsProvider {
	
	protected static final OnTooltip EMPTY_TOOLTIP = (editBox, poseStack, mouseX, mouseY) -> {
	};
	
	protected static final RGBA BLACK = RGBA.BLACK;
	protected static final RGBA WHITE = RGBA.WHITE;
	
	protected static final RGBA LIGHT_GRAY = new RGBA(0xE0E0E0FF);
	protected static final RGBA LIGHTER_GRAY = new RGBA(0xD0D0D0FF);
	protected static final RGBA GRAY = new RGBA(0xA0A0A0FF);
	protected static final RGBA DARKER_GRAY = new RGBA(0x808080FF);
	protected static final RGBA DARK_GRAY = new RGBA(0x707070FF);
	
	protected OnTooltip onTooltip;
	
	protected RGBA backgroundFrameColor;
	protected RGBA unfocusedBackgroundFrameColor;
	protected RGBA backgroundColor;
	
	protected RGBA textColor;
	protected RGBA disabledTextColor;
	protected RGBA suggestionTextColor;
	
	protected RGBA cursorColor;
	
	public UEditBox(TextRenderer font, int x, int y, int width, int height, UEditBox previousEditBox, Text title) {
		this(font, x, y, width, height, previousEditBox, title, EMPTY_TOOLTIP);
	}
	
	public UEditBox(TextRenderer font, int x, int y, int width, int height, UEditBox previousEditBox, Text title, OnTooltip tooltip) {
		super(font, x, y, width, height, title);
		setPreviousText(previousEditBox);
		onTooltip = tooltip;
		backgroundFrameColor = WHITE;
		unfocusedBackgroundFrameColor = GRAY;
		backgroundColor = BLACK;
		textColor = LIGHT_GRAY;
		disabledTextColor = DARK_GRAY;
		suggestionTextColor = DARKER_GRAY;
		cursorColor = LIGHTER_GRAY;
	}
	
	public void setTooltip(OnTooltip tooltip) {
		onTooltip = tooltip;
	}
	
	public RGBA getBackgroundFrameColor() {
		return backgroundFrameColor;
	}
	
	public void setBackgroundFrameColor(RGBA backgroundFrameColor) {
		this.backgroundFrameColor = backgroundFrameColor;
	}
	
	public RGBA getUnfocusedBackgroundFrameColor() {
		return unfocusedBackgroundFrameColor;
	}
	
	public void setUnfocusedBackgroundFrameColor(RGBA unfocusedBackgroundFrameColor) {
		this.unfocusedBackgroundFrameColor = unfocusedBackgroundFrameColor;
	}
	
	public RGBA getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(RGBA backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public RGBA getTextColor() {
		return textColor;
	}
	
	public void setTextColor(RGBA textColor) {
		this.textColor = textColor;
	}
	
	public RGBA getDisabledTextColor() {
		return disabledTextColor;
	}
	
	public void setDisabledTextColor(RGBA disabledTextColor) {
		this.disabledTextColor = disabledTextColor;
	}
	
	public RGBA getSuggestionTextColor() {
		return suggestionTextColor;
	}
	
	public void setSuggestionTextColor(RGBA suggestionTextColor) {
		this.suggestionTextColor = suggestionTextColor;
	}
	
	public RGBA getCursorColor() {
		return cursorColor;
	}
	
	public void setCursorColor(RGBA cursorColor) {
		this.cursorColor = cursorColor;
	}
	
	@Override
	public void setEditableColor(int color) {
		super.setEditableColor(color);
		setTextColor(RGBA.fromARGB(color));
	}
	
	@Override
	public void setUneditableColor(int color) {
		super.setUneditableColor(color);
		setDisabledTextColor(RGBA.fromARGB(color));
	}
	
	public void setPreviousText(UEditBox textField) {
		if (textField != null) {
			text = textField.text;
			maxLength = textField.maxLength;
			firstCharacterIndex = textField.firstCharacterIndex;
			selectionStart = textField.selectionStart;
			selectionEnd = textField.selectionEnd;
		}
	}
	
	@Override
	public void renderTick() {
		tick();
	}
	
	@Override
	public void renderButton(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(poseStack, mouseX, mouseY, partialTicks);
		renderForeground(poseStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void renderBackground(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		if (drawsBackground) {
			fill(poseStack, x - 1, y - 1, x + width + 1, y + height + 1, getCurrentBackgroundFrameColor(poseStack, mouseX, mouseY, partialTicks).getColorARGB());
			fill(poseStack, x, y, x + width, y + height, getCurrentBackgroundColor(poseStack, mouseX, mouseY, partialTicks).getColorARGB());
		}
	}
	
	@Override
	public void renderForeground(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		final RGBA currentTextColor = getCurrentTextColor(poseStack, mouseX, mouseY, partialTicks);
		
		final String currentText = textRenderer.trimToWidth(text.substring(firstCharacterIndex), getInnerWidth());
		
		final int cursorOffset = selectionStart - firstCharacterIndex;
		final int selectionOffset = Math.min(selectionEnd - firstCharacterIndex, currentText.length());
		
		final boolean isCursorInText = cursorOffset >= 0 && cursorOffset <= currentText.length();
		final boolean shouldCursorBlink = isFocused() && focusedTicks / 6 % 2 == 0 && isCursorInText;
		final boolean isCursorInTheMiddle = selectionStart < text.length() || text.length() >= maxLength;
		
		final int xOffset = drawsBackground ? x + 4 : x;
		final int yOffset = drawsBackground ? y + (height - 8) / 2 : y;
		
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
			drawSelectionHighlight(rightRenderedTextX, yOffset - 1, selectedX - 1, yOffset + 1 + 9);
		}
	}
	
	@Override
	public void renderToolTip(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		renderTooltip(poseStack, mouseX, mouseY);
	}
	
	@Override
	public void renderTooltip(MatrixStack poseStack, int mouseX, int mouseY) {
		onTooltip.onTooltip(this, poseStack, mouseX, mouseY);
	}
	
	@Override
	public RGBA getCurrentBackgroundColor(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return backgroundColor;
	}
	
	public RGBA getCurrentBackgroundFrameColor(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return isFocused() ? backgroundFrameColor : unfocusedBackgroundFrameColor;
	}
	
	@Override
	public RGBA getCurrentTextColor(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return editable ? textColor : disabledTextColor;
	}
	
	public RGBA getCurrentSuggestionTextColor(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return suggestionTextColor;
	}
	
	public RGBA getCurrentCursorColor(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return cursorColor;
	}
	
	@FunctionalInterface
	public interface OnTooltip {
		
		void onTooltip(UEditBox editBox, MatrixStack poseStack, int mouseX, int mouseY);
	}
	
}
