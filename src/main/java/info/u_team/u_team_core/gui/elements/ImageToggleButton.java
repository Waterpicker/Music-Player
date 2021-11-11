package info.u_team.u_team_core.gui.elements;

import info.u_team.u_team_core.util.RGBA;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ImageToggleButton extends ImageButton {
	
	protected Identifier toggleImage;
	
	protected RGBA toggleImageColor;
	
	protected boolean toggled;
	
	public ImageToggleButton(int x, int y, int width, int height, Identifier image, Identifier toggleImage, boolean toggled) {
		this(x, y, width, height, image, toggleImage, toggled, EMTPY_PRESSABLE);
	}
	
	public ImageToggleButton(int x, int y, int width, int height, Identifier image, Identifier toggleImage, boolean toggled, PressAction pessable) {
		this(x, y, width, height, image, toggleImage, toggled, pessable, EMPTY_TOOLTIP);
	}
	
	public ImageToggleButton(int x, int y, int width, int height, Identifier image, Identifier toggleImage, boolean toggled, TooltipSupplier tooltip) {
		this(x, y, width, height, image, toggleImage, toggled, EMTPY_PRESSABLE, tooltip);
	}
	
	public ImageToggleButton(int x, int y, int width, int height, Identifier image, Identifier toggleImage, boolean toggled, PressAction pessable, TooltipSupplier tooltip) {
		super(x, y, width, height, image, pessable, tooltip);
		this.toggleImage = toggleImage;
		toggleImageColor = WHITE;
		this.toggled = toggled;
	}
	
	public Identifier getToggleImage() {
		return toggleImage;
	}
	
	public void setToggleImage(Identifier toggleImage) {
		this.toggleImage = toggleImage;
	}
	
	public RGBA getToggleImageColor() {
		return toggleImageColor;
	}
	
	public void setToggleImageColor(RGBA toggleImageColor) {
		this.toggleImageColor = toggleImageColor;
	}
	
	public boolean isToggled() {
		return toggled;
	}
	
	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}
	
	public void toggle() {
		toggled = !toggled;
	}
	
	@Override
	public void onPress() {
		toggle();
		super.onPress();
	}
	
	@Override
	public Identifier getCurrentImage(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return toggled ? toggleImage : image;
	}
	
	@Override
	public RGBA getCurrentImageColor(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return toggled ? toggleImageColor : imageColor;
	}
}
