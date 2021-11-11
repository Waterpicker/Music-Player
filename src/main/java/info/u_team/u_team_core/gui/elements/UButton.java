package info.u_team.u_team_core.gui.elements;

import info.u_team.u_team_core.api.gui.BackgroundColorProvider;
import info.u_team.u_team_core.api.gui.PerspectiveRenderable;
import info.u_team.u_team_core.api.gui.TextProvider;
import info.u_team.u_team_core.api.gui.TextureProvider;
import info.u_team.u_team_core.util.RGBA;
import info.u_team.u_team_core.util.WidgetUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * A button that fixes vanilla not drawing the continuous border if the button is smaller than 20. Also adds utility
 * methods to add an IPressable and ITooltip
 *
 * @author HyCraftHD
 */
public class UButton extends ButtonWidget implements PerspectiveRenderable, BackgroundColorProvider, TextProvider {
	
	protected static final PressAction EMTPY_PRESSABLE = button -> {
	};
	
	protected static final TooltipSupplier EMPTY_TOOLTIP = ButtonWidget.EMPTY;
	
	protected static final RGBA WHITE = RGBA.WHITE;
	protected static final RGBA LIGHT_GRAY = new RGBA(0xA0A0A0FF);
	
	protected TextureProvider buttonTextureProvider;
	protected RGBA buttonColor;
	
	protected RGBA textColor;
	protected RGBA disabledTextColor;
	
	public UButton(int x, int y, int width, int height, Text text) {
		this(x, y, width, height, text, EMTPY_PRESSABLE);
	}
	
	public UButton(int x, int y, int width, int height, Text text, PressAction pessable) {
		this(x, y, width, height, text, pessable, EMPTY_TOOLTIP);
	}
	
	public UButton(int x, int y, int width, int height, Text text, TooltipSupplier tooltip) {
		this(x, y, width, height, text, EMTPY_PRESSABLE, tooltip);
	}
	
	public UButton(int x, int y, int width, int height, Text text, PressAction pessable, TooltipSupplier tooltip) {
		super(x, y, width, height, text, pessable);
		tooltipSupplier = tooltip;
		buttonTextureProvider = new WidgetTextureProvider(this, this::getYImage);
		buttonColor = WHITE;
		textColor = WHITE;
		disabledTextColor = LIGHT_GRAY;
	}
	
	public void setPressable(PressAction pressable) {
		onPress = pressable;
	}
	
	public void setPressable(Runnable runnable) {
		onPress = button -> runnable.run();
	}
	
	public void setTooltip(TooltipSupplier tooltip) {
		tooltipSupplier = tooltip;
	}
	
	public RGBA getButtonColor() {
		return buttonColor;
	}
	
	public void setButtonColor(RGBA buttonColor) {
		this.buttonColor = buttonColor;
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
	
	@Override
	public void renderButton(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		WidgetUtil.renderButtonLikeWidget(this, buttonTextureProvider, poseStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void renderBackground(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(poseStack, MinecraftClient.getInstance(), mouseX, mouseY);
	}
	
	@Override
	public void renderForeground(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		WidgetUtil.renderText(this, poseStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void renderToolTip(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		renderTooltip(poseStack, mouseX, mouseY);
	}
	
	@Override
	public RGBA getCurrentBackgroundColor(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return buttonColor;
	}
	
	@Override
	public Text getCurrentText() {
		return getMessage();
	}
	
	@Override
	public RGBA getCurrentTextColor(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return active ? textColor : disabledTextColor;
	}
	
}
