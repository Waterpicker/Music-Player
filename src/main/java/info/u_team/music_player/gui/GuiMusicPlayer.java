package info.u_team.music_player.gui;

import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_CREATE_PLAYLIST_ADD_LIST;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_CREATE_PLAYLIST_INSERT_NAME;
import static info.u_team.music_player.init.MusicPlayerLocalization.getTranslation;

import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.u_team_core.gui.elements.ImageButton;
import info.u_team.u_team_core.gui.renderer.ScrollingTextRenderer;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class GuiMusicPlayer extends BetterScreen {
	
	private TextFieldWidget namePlaylistField;
	
	private GuiMusicPlayerList playlistsList;
	
	private GuiControls controls;
	
	public GuiMusicPlayer() {
		super(new LiteralText("musicplayer"));
	}
	
	@Override
	protected void init() {
		addDrawableChild(new ImageButton(1, 1, 15, 15, MusicPlayerResources.TEXTURE_BACK, button -> client.setScreen(null)));
		
		namePlaylistField = new TextFieldWidget(textRenderer, 100, 60, width - 150, 20, Text.of(null));
		namePlaylistField.setMaxLength(500);
		children.add(namePlaylistField);
		
		final ImageButton addPlaylistButton = addDrawableChild(new ImageButton(width - 41, 59, 22, 22, MusicPlayerResources.TEXTURE_CREATE));
		addPlaylistButton.setPressable(() -> {
			final String name = namePlaylistField.getText();
			if (StringUtils.isBlank(name) || name.equals(getTranslation(GUI_CREATE_PLAYLIST_INSERT_NAME))) {
				namePlaylistField.setText(getTranslation(GUI_CREATE_PLAYLIST_INSERT_NAME));
				return;
			}
			playlistsList.addPlaylist(name);
			namePlaylistField.setText("");
		});
		
		playlistsList = new GuiMusicPlayerList(width - 24, height, 90, height - 10, 12, width - 12);
		addDrawableChild(playlistsList);
		
		controls = new GuiControls(this, 5, width);
		children.add(controls);
	}
	
	@Override
	public void resize(MinecraftClient minecraft, int width, int height) {
		final String text = namePlaylistField.getText();
		final ScrollingTextRenderer titleRender = controls.getTitleRender();
		final ScrollingTextRenderer authorRender = controls.getAuthorRender();
		this.init(minecraft, width, height);
		namePlaylistField.setText(text);
		controls.copyTitleRendererState(titleRender);
		controls.copyAuthorRendererState(authorRender);
	}
	
	@Override
	public void tick() {
		namePlaylistField.tick();
		controls.tick();
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackgroundTexture(0);
		playlistsList.render(matrixStack, mouseX, mouseY, partialTicks);
		textRenderer.draw(matrixStack, getTranslation(GUI_CREATE_PLAYLIST_ADD_LIST), 20, 65, 0xFFFFFF);
		namePlaylistField.render(matrixStack, mouseX, mouseY, partialTicks);
		controls.render(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	public GuiMusicPlayerList getPlaylistsList() {
		return playlistsList;
	}
	
}
