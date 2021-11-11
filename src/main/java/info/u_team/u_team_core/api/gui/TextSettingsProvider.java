package info.u_team.u_team_core.api.gui;

import info.u_team.u_team_core.util.RGBA;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public interface TextSettingsProvider {
	
	default TextRenderer getCurrentTextFont() {
		return MinecraftClient.getInstance().textRenderer;
	}
	
	RGBA getCurrentTextColor(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks);
	
}
