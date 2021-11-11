package info.u_team.u_team_core.gui.elements;

import info.u_team.u_team_core.api.gui.BackgroundColorProvider;
import info.u_team.u_team_core.api.gui.PerspectiveRenderable;
import info.u_team.u_team_core.api.gui.TextProvider;
import info.u_team.u_team_core.api.gui.TextureProvider;
import info.u_team.u_team_core.util.RGBA;
import info.u_team.u_team_core.util.RenderUtil;
import info.u_team.u_team_core.util.WidgetUtil;
import net.minecraftforge.fmlclient.gui.widget.Slider;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class USlider extends Slider implements PerspectiveRenderable, BackgroundColorProvider, TextProvider {
	
	protected static final ISlider EMTPY_SLIDER = slider -> {
	};
	
	protected static final TooltipSupplier EMPTY_TOOLTIP = UButton.EMPTY_TOOLTIP;
	
	protected static final RGBA WHITE = UButton.WHITE;
	protected static final RGBA LIGHT_GRAY = UButton.LIGHT_GRAY;
	
	protected final boolean isInContainer;
	
	protected TextureProvider sliderBackgroundTextureProvider;
	protected RGBA sliderBackgroundColor;
	
	protected TextureProvider sliderTextureProvider;
	protected RGBA sliderColor;
	
	protected RGBA textColor;
	protected RGBA disabledTextColor;
	
	public USlider(int x, int y, int width, int height, Text prefix, Text suffix, double minValue, double maxValue, double value, boolean decimalPrecision, boolean drawDescription, boolean isInContainer) {
		this(x, y, width, height, prefix, suffix, minValue, maxValue, value, decimalPrecision, drawDescription, isInContainer, EMTPY_SLIDER);
	}
	
	public USlider(int x, int y, int width, int height, Text prefix, Text suffix, double minValue, double maxValue, double value, boolean decimalPrecision, boolean drawDescription, boolean isInContainer, ISlider slider) {
		this(x, y, width, height, prefix, suffix, minValue, maxValue, value, decimalPrecision, drawDescription, isInContainer, slider, EMPTY_TOOLTIP);
	}
	
	public USlider(int x, int y, int width, int height, Text prefix, Text suffix, double minValue, double maxValue, double value, boolean decimalPrecision, boolean drawDescription, boolean isInContainer, TooltipSupplier tooltip) {
		this(x, y, width, height, prefix, suffix, minValue, maxValue, value, decimalPrecision, drawDescription, isInContainer, EMTPY_SLIDER, tooltip);
	}
	
	public USlider(int x, int y, int width, int height, Text prefix, Text suffix, double minValue, double maxValue, double value, boolean decimalPrecision, boolean drawDescription, boolean isInContainer, ISlider slider, TooltipSupplier tooltip) {
		super(x, y, width, height, prefix, suffix, minValue, maxValue, value, decimalPrecision, drawDescription, UButton.EMTPY_PRESSABLE, slider);
		this.isInContainer = isInContainer;
		tooltipSupplier = tooltip;
		sliderBackgroundTextureProvider = new WidgetTextureProvider(this, hovered -> 0);
		sliderBackgroundColor = WHITE;
		sliderTextureProvider = new WidgetTextureProvider(this, hovered -> hovered ? 2 : 1);
		sliderColor = WHITE;
		textColor = WHITE;
		disabledTextColor = LIGHT_GRAY;
	}
	
	public void setSlider(ISlider slider) {
		parent = slider;
	}
	
	public void setSlider(Runnable runnable) {
		parent = slider -> runnable.run();
	}
	
	public void setTooltip(TooltipSupplier tooltip) {
		tooltipSupplier = tooltip;
	}
	
	public RGBA getSliderBackgroundColor() {
		return sliderBackgroundColor;
	}
	
	public void setSliderBackgroundColor(RGBA sliderBackgroundColor) {
		this.sliderBackgroundColor = sliderBackgroundColor;
	}
	
	public RGBA getSliderColor() {
		return sliderColor;
	}
	
	public void setSliderColor(RGBA sliderColor) {
		this.sliderColor = sliderColor;
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
		WidgetUtil.renderButtonLikeWidget(this, sliderBackgroundTextureProvider, poseStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void renderBackground(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(poseStack, MinecraftClient.getInstance(), mouseX, mouseY);
		if (visible) {
			RenderUtil.drawContinuousTexturedBox(poseStack, x + (int) (sliderValue * (width - 8)), y, sliderTextureProvider.getU(), sliderTextureProvider.getV(), 8, height, sliderBackgroundTextureProvider.getWidth(), sliderTextureProvider.getHeight(), 2, 3, 2, 2, getZOffset(), sliderTextureProvider.getTexture(), getCurrentSliderColor(poseStack, mouseX, mouseY, partialTicks));
		}
	}
	
	@Override
	public void renderForeground(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		WidgetUtil.renderText(this, poseStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void renderToolTip(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		super.renderTooltip(poseStack, mouseX, mouseY);
	}
	
	@Override
	public RGBA getCurrentBackgroundColor(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return sliderBackgroundColor;
	}
	
	public RGBA getCurrentSliderColor(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return sliderColor;
	}
	
	@Override
	public Text getCurrentText() {
		return getMessage();
	}
	
	@Override
	public RGBA getCurrentTextColor(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return active ? textColor : disabledTextColor;
	}
	
	@Override
	public void onClick(double mouseX, double mouseY) {
		changeSliderValue(mouseX);
		if (isInContainer) {
			dragging = true;
		}
	}
	
	@Override
	protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
		if (!isInContainer) {
			changeSliderValue(mouseX);
		}
	}
	
	@Override
	public void onRelease(double mouseX, double mouseY) {
		if (isHovered()) {
			super.playDownSound(MinecraftClient.getInstance().getSoundManager());
		}
		if (isInContainer) {
			dragging = false;
		}
	}
	
	@Override
	protected void renderBackground(MatrixStack poseStack, MinecraftClient minecraft, int mouseX, int mouseY) {
		if (isInContainer && visible && dragging) {
			changeSliderValue(mouseX);
		}
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		final boolean flag = keyCode == 263;
		if (flag || keyCode == 262) {
			final float direction = flag ? -1.0F : 1.0F;
			setSliderValue(sliderValue + direction / (width - 8));
		}
		return false;
	}
	
	@Override
	public void playDownSound(SoundManager handler) {
	}
	
	protected void changeSliderValue(double mouseX) {
		setSliderValue((mouseX - (x + 4)) / (width - 8));
	}
	
	protected void setSliderValue(double value) {
		final double oldValue = sliderValue;
		sliderValue = MathHelper.clamp(value, 0, 1);
		if (oldValue != sliderValue) {
			updateSlider();
		}
	}
}
