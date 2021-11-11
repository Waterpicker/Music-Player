package info.u_team.u_team_core.api.gui;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;

public interface PerspectiveRenderable extends TooltipRenderable, Drawable {
	
	@Override
	void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks);
	
	void renderBackground(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks);
	
	void renderForeground(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks);
	
}
