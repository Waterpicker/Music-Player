package info.u_team.u_team_core.api.gui;

import info.u_team.u_team_core.util.RGBA;
import net.minecraft.client.util.math.MatrixStack;

public interface BackgroundColorProvider {
	
	RGBA getCurrentBackgroundColor(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks);
	
}
