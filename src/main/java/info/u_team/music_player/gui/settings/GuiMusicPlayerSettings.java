package info.u_team.music_player.gui.settings;

import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_MIXER_DEVICE_SELECTION;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_POSITION_OVERLAY;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_TOGGLE_INGAME_OVERLAY;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_TOGGLE_KEY_IN_GUI;
import static info.u_team.music_player.init.MusicPlayerLocalization.GUI_SETTINGS_TOGGLE_MENUE_OVERLAY;
import static info.u_team.music_player.init.MusicPlayerLocalization.getTranslation;

import info.u_team.music_player.gui.BetterScreen;
import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.music_player.init.MusicPlayerColors;
import info.u_team.music_player.init.MusicPlayerResources;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.settings.IngameOverlayPosition;
import info.u_team.music_player.musicplayer.settings.Settings;
import info.u_team.u_team_core.gui.elements.ImageButton;
import info.u_team.u_team_core.gui.elements.ScalableActivatableButton;
import info.u_team.u_team_core.gui.elements.UButton;
import info.u_team.u_team_core.gui.renderer.ScrollingTextRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class GuiMusicPlayerSettings extends BetterScreen {
	
	private final Screen previousGui;
	
	private GuiMusicPlayerSettingsMixerDeviceList mixerDeviceList;
	
	private GuiControls controls;
	
	public GuiMusicPlayerSettings(Screen previousGui) {
		super(new LiteralText("musicplayersettings"));
		this.previousGui = previousGui;
	}
	
	@Override
	protected void init() {
		addDrawableChild(new ImageButton(1, 1, 15, 15, MusicPlayerResources.TEXTURE_BACK, button -> client.setScreen(previousGui)));
		
		final Settings settings = MusicPlayerManager.getSettingsManager().getSettings();
		
		final ScalableActivatableButton toggleKeyWorkInGuiButton = addDrawableChild(new ScalableActivatableButton(12, 60, width / 2 - 24, 20, Text.of(getTranslation(GUI_SETTINGS_TOGGLE_KEY_IN_GUI)), 1, settings.isKeyWorkInGui(), MusicPlayerColors.LIGHT_GREEN));
		toggleKeyWorkInGuiButton.setPressable(() -> {
			settings.setKeyWorkInGui(!settings.isKeyWorkInGui());
			toggleKeyWorkInGuiButton.setActivated(settings.isKeyWorkInGui());
		});
		
		final ScalableActivatableButton toggleIngameMenueDisplayButton = addDrawableChild(new ScalableActivatableButton(width / 2 + 14, 60, width / 2 - 24, 20, Text.of(getTranslation(GUI_SETTINGS_TOGGLE_MENUE_OVERLAY)), 1, settings.isShowIngameMenueOverlay(), MusicPlayerColors.LIGHT_GREEN));
		toggleIngameMenueDisplayButton.setPressable(() -> {
			settings.setShowIngameMenueOverlay(!settings.isShowIngameMenueOverlay());
			toggleIngameMenueDisplayButton.setActivated(settings.isShowIngameMenueOverlay());
		});
		
		final ScalableActivatableButton toggleIngameDisplayButton = addDrawableChild(new ScalableActivatableButton(12, 90, width / 2 - 24, 20, Text.of(getTranslation(GUI_SETTINGS_TOGGLE_INGAME_OVERLAY)), 1, settings.isShowIngameOverlay(), MusicPlayerColors.LIGHT_GREEN));
		toggleIngameDisplayButton.setPressable(() -> {
			settings.setShowIngameOverlay(!settings.isShowIngameOverlay());
			toggleIngameDisplayButton.setActivated(settings.isShowIngameOverlay());
		});
		
		final UButton ingameOverlayPositionButton = addDrawableChild(new UButton(width / 2 + 14, 90, width / 2 - 24, 20, Text.of(getTranslation(GUI_SETTINGS_POSITION_OVERLAY) + ": " + getTranslation(settings.getIngameOverlayPosition().getLocalization()))));
		ingameOverlayPositionButton.setPressable(() -> {
			settings.setIngameOverlayPosition(IngameOverlayPosition.forwardCycle(settings.getIngameOverlayPosition()));
			ingameOverlayPositionButton.setMessage(Text.of(getTranslation(GUI_SETTINGS_POSITION_OVERLAY) + ": " + getTranslation(settings.getIngameOverlayPosition().getLocalization())));
		});
		
		mixerDeviceList = new GuiMusicPlayerSettingsMixerDeviceList(width - 24, height, 133, 183, 12, width - 12);
		children.add(mixerDeviceList);
		
		controls = new GuiControls(this, 5, width);
		children.add(controls);
	}
	
	@Override
	public void tick() {
		controls.tick();
	}
	
	@Override
	public void resize(MinecraftClient minecraft, int width, int height) {
		final ScrollingTextRenderer titleRender = controls.getTitleRender();
		final ScrollingTextRenderer authorRender = controls.getAuthorRender();
		this.init(minecraft, width, height);
		controls.copyTitleRendererState(titleRender);
		controls.copyAuthorRendererState(authorRender);
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderBackgroundTexture(0);
		mixerDeviceList.render(matrixStack, mouseX, mouseY, partialTicks);
		textRenderer.draw(matrixStack, getTranslation(GUI_SETTINGS_MIXER_DEVICE_SELECTION), 13, 117, 0xFFFFFF);
		controls.render(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
}
