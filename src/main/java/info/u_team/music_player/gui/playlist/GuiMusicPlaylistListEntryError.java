package info.u_team.music_player.gui.playlist;

import info.u_team.music_player.musicplayer.playlist.LoadedTracks;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.music_player.musicplayer.playlist.Playlists;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class GuiMusicPlaylistListEntryError extends GuiMusicPlaylistListEntryFunctions {
	
	private final String error;
	
	public GuiMusicPlaylistListEntryError(GuiMusicPlaylistList guilist, Playlists playlists, Playlist playlist, LoadedTracks loadedTrack, String error) {
		super(guilist, playlists, playlist, loadedTrack, null);
		this.error = error;
	}
	
	@Override
	public void drawEntryExtended(MatrixStack matrixStack, int entryX, int entryY, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean mouseInList, float partialTicks) {
		minecraft.textRenderer.draw(matrixStack, error, entryX + 5, entryY + 5, 0xFF0000);
		minecraft.textRenderer.draw(matrixStack, uri.get(), entryX + 5, entryY + 25, 0xFF0000);
	}

	@Override
	public Text getNarration() {
		return Text.of(null);
	}
}
