package info.u_team.u_team_core.gui.elements;

import info.u_team.u_team_core.util.RGBA;
import info.u_team.u_team_core.util.RenderUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class ImageButton extends UButton {
	
	protected Identifier image;
	
	protected RGBA imageColor;
	
	public ImageButton(int x, int y, int width, int height, Identifier image) {
		this(x, y, width, height, image, EMTPY_PRESSABLE);
	}
	
	public ImageButton(int x, int y, int width, int height, Identifier image, PressAction pessable) {
		this(x, y, width, height, image, pessable, EMPTY_TOOLTIP);
	}
	
	public ImageButton(int x, int y, int width, int height, Identifier image, TooltipSupplier tooltip) {
		this(x, y, width, height, image, EMTPY_PRESSABLE, tooltip);
	}
	
	public ImageButton(int x, int y, int width, int height, Identifier image, PressAction pessable, TooltipSupplier tooltip) {
		super(x, y, width, height, LiteralText.EMPTY, pessable, tooltip);
		this.image = image;
		imageColor = WHITE;
	}
	
	public Identifier getImage() {
		return image;
	}
	
	public void setImage(Identifier image) {
		this.image = image;
	}
	
	public RGBA getImageColor() {
		return imageColor;
	}
	
	public void setImageColor(RGBA imageColor) {
		this.imageColor = imageColor;
	}
	
	@Override
	public void renderForeground(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		RenderUtil.drawTexturedQuad(poseStack, x + 2, x + width - 2, y + 2, y + height - 2, 0, 1, 0, 1, 0, getCurrentImage(poseStack, mouseX, mouseY, partialTicks), getCurrentImageColor(poseStack, mouseX, mouseY, partialTicks));
	}
	
	public Identifier getCurrentImage(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return image;
	}
	
	public RGBA getCurrentImageColor(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		return imageColor;
	}
}
