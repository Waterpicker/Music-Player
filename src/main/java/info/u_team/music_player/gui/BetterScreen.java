package info.u_team.music_player.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class BetterScreen extends Screen implements BetterNestedGui {
	
	public BetterScreen(Text title) {
		super(title);
	}
	
}
