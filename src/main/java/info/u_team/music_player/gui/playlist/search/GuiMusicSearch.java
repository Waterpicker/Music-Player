package info.u_team.music_player.gui.playlist.search;

import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_ADDED_ALL;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_ADD_ALL;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_HEADER;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_LOAD_FILE;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_LOAD_FOLDER;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_MUSIC_FILES;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_SEARCH_FILE;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_SEARCH_SEARCH;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SEARCH_SEARCH_URI;
import static info.u_team.music_player.init.MusicPlayerLocalization.getTranslation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import info.u_team.music_player.gui.BetterScreen;
import info.u_team.music_player.gui.playlist.GuiMusicPlaylist;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrack;
import info.u_team.music_player.lavaplayer.api.audio.IAudioTrackList;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.playlist.Playlist;
import info.u_team.u_team_core.gui.elements.ImageButton;
import info.u_team.u_team_core.gui.elements.UButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class GuiMusicSearch extends BetterScreen {
	
	private final Playlist playlist;
	
	private TextFieldWidget urlField;
	private TextFieldWidget searchField;
	
	private final GuiMusicSearchList searchList;
	
	private SearchProvider searchProvider;
	
	private String information;
	private int informationTicks;
	private int maxTicksInformation;
	
	public GuiMusicSearch(Playlist playlist) {
		super(new LiteralText("musicsearch"));
		this.playlist = playlist;
		searchList = new GuiMusicSearchList();
		searchProvider = SearchProvider.YOUTUBE;
	}
	
	@Override
	protected void init() {
		final ImageButton backButton = addDrawableChild(new ImageButton(1, 1, 15, 15, MusicPlayerResources.TEXTURE_BACK));
		backButton.setPressable(() -> client.setScreen(new GuiMusicPlaylist(playlist)));
		
		urlField = new TextFieldWidget(textRenderer, 10, 35, width / 2 - 10, 20, Text.of("")) {
			
			@Override
			public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
				keyFromTextField(this, getText(), key);
				return super.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
			}
		};
		urlField.setMaxLength(10000);
		children.add(urlField);
		
		final UButton openFileButton = addDrawableChild(new UButton(width / 2 + 10, 34, width / 4 - 15, 22, Text.of(getTranslation(GUI_SEARCH_LOAD_FILE))));
		openFileButton.setPressable(() -> {
			final String response = TinyFileDialogs.tinyfd_openFileDialog(getTranslation(GUI_SEARCH_LOAD_FILE), null, null, getTranslation(GUI_SEARCH_MUSIC_FILES), false);
			if (response != null) {
				searchList.clear();
				addTrack(response);
			}
		});
		
		final UButton openFolderButton = addDrawableChild(new UButton((int) (width * 0.75) + 5, 34, width / 4 - 15, 22, Text.of(getTranslation(GUI_SEARCH_LOAD_FOLDER))));
		openFolderButton.setPressable(() -> {
			final String response = TinyFileDialogs.tinyfd_selectFolderDialog(getTranslation(GUI_SEARCH_LOAD_FOLDER), System.getProperty("user.home"));
			if (response != null) {
				searchList.clear();
				try (Stream<Path> stream = Files.list(Paths.get(response))) {
					stream.filter(path -> !Files.isDirectory(path)).forEach(path -> addTrack(path.toString()));
				} catch (final IOException ex) {
					setInformation(Formatting.RED + ex.getMessage(), 150);
				}
			}
		});
		
		final ImageButton searchButton = addDrawableChild(new ImageButton(10, 76, 24, 24, searchProvider.getLogo()));
		searchButton.setPressable(() -> {
			searchProvider = SearchProvider.toggle(searchProvider);
			searchButton.setImage(searchProvider.getLogo());
		});
		
		searchField = new TextFieldWidget(textRenderer, 40, 78, width - 51, 20, Text.of("")) {
			
			@Override
			public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
				keyFromTextField(this, searchProvider.getPrefix() + getText(), key);
				return super.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
			}
			
			@Override
			public boolean changeFocus(boolean p_changeFocus_1_) {
				System.out.println("CHANGEED FOR Search FIELD to " + p_changeFocus_1_);
				return super.changeFocus(p_changeFocus_1_);
			}
			
		};
		searchField.setMaxLength(1000);
		searchField.setTextFieldFocused(true);
		setFocused(searchField);
		children.add(searchField);
		
		final UButton addAllButton = addDrawableChild(new UButton(width - 110, 105, 100, 20, Text.of(getTranslation(GUI_SEARCH_ADD_ALL))));
		addAllButton.setPressable(() -> {
			final List<GuiMusicSearchListEntryPlaylist> list = searchList.children().stream().filter(entry -> entry instanceof GuiMusicSearchListEntryPlaylist).map(entry -> (GuiMusicSearchListEntryPlaylist) entry).collect(Collectors.toList());
			if (list.size() > 0) {
				list.forEach(entry -> {
					playlist.add(entry.getTrackList());
				});
			} else {
				searchList.children().stream().filter(entry -> entry instanceof GuiMusicSearchListEntryMusicTrack).map(entry -> (GuiMusicSearchListEntryMusicTrack) entry).filter(entry -> !entry.isPlaylistEntry()).forEach(entry -> {
					playlist.add(entry.getTrack());
				});
			}
			setInformation(Formatting.GREEN + getTranslation(GUI_SEARCH_ADDED_ALL), 150);
		});
		
		searchList.updateSettings(width - 24, height, 130, height - 10, 12, width - 12);
		children.add(searchList);
	}
	
	@Override
	public void resize(MinecraftClient minecraft, int width, int height) {
		final String urlFieldText = urlField.getText();
		final boolean urlFieldFocus = urlField.isFocused() && getFocused() == urlField;
		
		final String searchFieldText = searchField.getText();
		final boolean searchFieldFocus = searchField.isFocused() && getFocused() == searchField;
		
		init(minecraft, width, height);
		
		urlField.setText(urlFieldText);
		urlField.setTextFieldFocused(urlFieldFocus);
		if (urlFieldFocus) {
			setFocused(urlField);
		}
		
		searchField.setText(searchFieldText);
		searchField.setTextFieldFocused(searchFieldFocus);
		if (searchFieldFocus) {
			setFocused(searchField);
		}
		
	}
	
	@Override
	public void tick() {
		urlField.tick();
		searchField.tick();
		informationTicks++;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackgroundTexture(0);
		searchList.render(matrixStack, mouseX, mouseY, partialTicks);
		
		drawCenteredText(matrixStack, client.textRenderer, getTranslation(GUI_SEARCH_HEADER), width / 2, 5, 0xFFFFFF);
		drawStringWithShadow(matrixStack, client.textRenderer, getTranslation(GUI_SEARCH_SEARCH_URI), 10, 20, 0xFFFFFF);
		drawStringWithShadow(matrixStack, client.textRenderer, getTranslation(GUI_SEARCH_SEARCH_FILE), 10 + width / 2, 20, 0xFFFFFF);
		drawStringWithShadow(matrixStack, client.textRenderer, getTranslation(GUI_SEARCH_SEARCH_SEARCH), 10, 63, 0xFFFFFF);
		
		if (information != null && informationTicks <= maxTicksInformation) {
			drawStringWithShadow(matrixStack, client.textRenderer, information, 15, 110, 0xFFFFFF);
		}
		
		urlField.render(matrixStack, mouseX, mouseY, partialTicks);
		searchField.render(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (urlField.mouseClicked(mouseX, mouseY, button)) {
			setFocused(urlField);
			urlField.setTextFieldFocused(true);
			searchField.setTextFieldFocused(false);
			return true;
		} else if (searchField.mouseClicked(mouseX, mouseY, button)) {
			setFocused(searchField);
			searchField.setTextFieldFocused(true);
			urlField.setTextFieldFocused(false);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	public void setInformation(String information, int maxTicksInformation) {
		this.information = information;
		this.maxTicksInformation = maxTicksInformation;
		informationTicks = 0;
	}
	
	private void keyFromTextField(TextFieldWidget field, String text, int key) {
		if (field.isVisible() && field.isFocused() && (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER)) {
			searchList.clear();
			addTrack(text);
			field.setText("");
		}
	}
	
	private void addTrack(String uri) {
		MusicPlayerManager.getPlayer().getTrackSearch().getTracks(uri, result -> {
			client.execute(() -> {
				if (result.hasError()) {
					setInformation(Formatting.RED + result.getErrorMessage(), 150);
				} else if (result.isList()) {
					final IAudioTrackList list = result.getTrackList();
					if (!list.isSearch()) {
						searchList.add(new GuiMusicSearchListEntryPlaylist(this, playlist, list));
					}
					list.getTracks().forEach(track -> searchList.add(new GuiMusicSearchListEntryMusicTrack(this, playlist, track, !list.isSearch())));
				} else {
					final IAudioTrack track = result.getTrack();
					searchList.add(new GuiMusicSearchListEntryMusicTrack(this, playlist, track, false));
				}
			});
		});
	}
}
