package info.u_team.u_team_core.api.gui;

import net.minecraft.client.util.math.MatrixStack;

public interface ScaleProvider {
	
	float getCurrentScale(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks);
	
}
