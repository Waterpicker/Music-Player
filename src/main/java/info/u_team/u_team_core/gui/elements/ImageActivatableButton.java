package info.u_team.u_team_core.gui.elements;

import info.u_team.u_team_core.util.RGBA;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ImageActivatableButton extends ImageButton {
	
	protected boolean activated;
	
	protected RGBA activatedColor;
	
	public ImageActivatableButton(int x, int y, int width, int height, Identifier image, boolean activated, RGBA activatedColor) {
		this(x, y, width, height, image, activated, activatedColor, EMTPY_PRESSABLE);
	}
	
	public ImageActivatableButton(int x, int y, int width, int height, Identifier image, boolean activated, RGBA activatedColor, PressAction pessable) {
		this(x, y, width, height, image, activated, activatedColor, pessable, EMPTY_TOOLTIP);
	}
	
	public ImageActivatableButton(int x, int y, int width, int height, Identifier image, boolean activated, RGBA activatedColor, TooltipSupplier tooltip) {
		this(x, y, width, height, image, activated, activatedColor, EMTPY_PRESSABLE, tooltip);
	}
	
	public ImageActivatableButton(int x, int y, int width, int height, Identifier image, boolean activated, RGBA activatedColor, PressAction pessable, TooltipSupplier tooltip) {
		super(x, y, width, height, image, pessable, tooltip);
		this.activated = activated;
		this.activatedColor = activatedColor;
	}
	
	public boolean isActivated() {
		return activated;
	}
	
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	
	public RGBA getActivatedColor() {
		return activatedColor;
	}
	
	public void setActivatedColor(RGBA activatedColor) {
		this.activatedColor = activatedColor;
	}
	
	@Override
	public RGBA getCurrentBackgroundColor(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return activated ? activatedColor : buttonColor;
	}
	
}
