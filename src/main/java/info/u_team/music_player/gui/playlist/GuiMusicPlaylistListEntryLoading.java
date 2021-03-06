package info.u_team.music_player.gui.playlist;

import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_PLAYLIST_LOADING;
import static info.u_team.music_player.init.MusicPlayerLocalization.getTranslation;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class GuiMusicPlaylistListEntryLoading extends GuiMusicPlaylistListEntry {
	
	@Override
	public void render(MatrixStack matrixStack, int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
		final String text = getTranslation(GUI_PLAYLIST_LOADING);
		minecraft.textRenderer.draw(matrixStack, text, entryX + (entryWidth / 2) - (minecraft.textRenderer.getWidth(text) / 2), entryY + 20, 0xFF0000);
	}

	@Override
	public Text getNarration() {
		return Text.of(null);
	}
}
