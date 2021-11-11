package info.u_team.u_team_core.api.gui;

import net.minecraft.util.Identifier;

public interface TextureProvider {
	
	Identifier getTexture();
	
	int getU();
	
	int getV();
	
	int getWidth();
	
	int getHeight();
	
}
