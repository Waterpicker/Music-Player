package info.u_team.music_player.init;

import java.util.List;

import info.u_team.music_player.gui.GuiMusicPlayer;
import info.u_team.music_player.gui.controls.GuiControls;
import info.u_team.music_player.lavaplayer.api.queue.ITrackManager;
import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.music_player.musicplayer.MusicPlayerUtils;
import info.u_team.music_player.musicplayer.SettingsManager;
import info.u_team.music_player.musicplayer.settings.IngameOverlayPosition;
import info.u_team.music_player.render.RenderOverlayMusicDisplay;
import info.u_team.u_team_core.gui.renderer.ScrollingTextRenderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;

public class MusicPlayerEventHandler {
	
	private static final SettingsManager SETTINGS_MANAGER = MusicPlayerManager.getSettingsManager();

	private static boolean handleKeyboard() {
		if(SETTINGS_MANAGER.getSettings().isKeyWorkInGui());

		final boolean handled;
		final ITrackManager manager = MusicPlayerManager.getPlayer().getTrackManager();
		if (MusicPlayerKeys.OPEN.isPressed()) {
			final MinecraftClient mc = MinecraftClient.getInstance();
			if (!(mc.currentScreen instanceof GuiMusicPlayer)) {
				mc.setScreen(new GuiMusicPlayer());
			}
			handled = true;
		} else if (MusicPlayerKeys.PAUSE.isPressed()) {
			if (manager.getCurrentTrack() != null) {
				manager.setPaused(!manager.isPaused());
			}
			handled = true;
		} else if (MusicPlayerKeys.SKIP_FORWARD.isPressed()) {
			if (manager.getCurrentTrack() != null) {
				MusicPlayerUtils.skipForward();
			}
			handled = true;
		} else if (MusicPlayerKeys.SKIP_BACK.isPressed()) {
			MusicPlayerUtils.skipBack();
			handled = true;
		} else {
			handled = false;
		}
		return handled;
	}

	private static RenderOverlayMusicDisplay overlayRender;
	
	// Render overlay
	
	private static void onRenderGameOverlay(MatrixStack matrixStack, float delta) {
		final MinecraftClient mc = MinecraftClient.getInstance();
		// if (event.getType() == ElementType.TEXT && !mc.gameSettings.showDebugInfo && mc.currentScreen == null) {

			if (SETTINGS_MANAGER.getSettings().isShowIngameOverlay()) {
				final IngameOverlayPosition position = SETTINGS_MANAGER.getSettings().getIngameOverlayPosition();
				
				if (overlayRender == null) {
					overlayRender = new RenderOverlayMusicDisplay();
				}
				
				final Window window = mc.getWindow();
				final int screenWidth = window.getScaledWidth();
				final int screenHeight = window.getScaledHeight();
				
				final int height = overlayRender.getHeight();
				final int width = overlayRender.getWidth();
				
				final int x;
				if (position.isLeft()) {
					x = 3;
				} else {
					x = screenWidth - 3 - width;
				}
				
				final int y;
				if (position.isUp()) {
					y = 3;
				} else {
					y = screenHeight - 3 - height;
				}
				
				matrixStack.push();
				matrixStack.translate(x, y, 500);
				overlayRender.render(matrixStack, 0, 0, delta);
				matrixStack.pop();
			}

	}
	
	// Used to add buttons and gui controls to main ingame gui
	
	private static ScrollingTextRenderer titleRender, authorRender;
	
	private static void onInitGuiPre(Screen screen) {
		if (screen instanceof GameMenuScreen) {
			if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
				screen.children().stream() //
						.filter(element -> element instanceof GuiControls) //
						.map(element -> ((GuiControls) element)).findAny() //
						.ifPresent(controls -> {
							titleRender = controls.getTitleRender();
							authorRender = controls.getAuthorRender();
						});
			}
		}
	}
	
	private static void onInitGuiPost(Screen screen) {
		if (screen instanceof GameMenuScreen) {
			if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
				final GuiControls controls = new GuiControls(screen, 3, screen.width);
				if (titleRender != null) {
					controls.copyTitleRendererState(titleRender);
					titleRender = null;
				}
				if (authorRender != null) {
					controls.copyAuthorRendererState(authorRender);
					authorRender = null;
				}
				@SuppressWarnings("unchecked")
				final List<Element> list = (List<Element>) screen.children();
				list.add(controls);
			}
		}
	}
	
	private static void onDrawScreenPost(Screen screen, MatrixStack matrices, int mouseX, int mouseY, float tickDelta) {
		if (screen instanceof GameMenuScreen) {
			if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
				screen.children().stream() //
						.filter(element -> element instanceof GuiControls) //
						.map(element -> ((GuiControls) element)).findAny() //
						.ifPresent(controls -> controls.render(matrices, mouseX, mouseY, tickDelta));
			}
		}
	}
	
	private static void onMouseReleasePre(Screen screen, double mouseX, double mouseY, int button) {
		if (screen instanceof GameMenuScreen) {
			if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
				screen.children().stream() //
						.filter(element -> element instanceof GuiControls) //
						.map(element -> ((GuiControls) element)).findAny() //
						.ifPresent(controls -> controls.mouseReleased(mouseX, mouseY, button));
			}
		}
	}
	
	private static void onClientTick() {
			final Screen gui = MinecraftClient.getInstance().currentScreen;
			if (gui instanceof GameMenuScreen) {
				if (SETTINGS_MANAGER.getSettings().isShowIngameMenueOverlay()) {
					gui.children().stream() //
							.filter(element -> element instanceof GuiControls) //
							.map(element -> ((GuiControls) element)).findAny() //
							.ifPresent(GuiControls::tick);
				}
			}
		}

	
	public static void init() {

		HudRenderCallback.EVENT.register(MusicPlayerEventHandler::onRenderGameOverlay);


		ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			onInitGuiPre(screen);
			ScreenEvents.afterRender(screen).register(MusicPlayerEventHandler::onDrawScreenPost);
			ScreenMouseEvents.afterMouseRelease(screen).register(MusicPlayerEventHandler::onMouseReleasePre);
		});

		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> onInitGuiPost(screen));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			handleKeyboard();
			onClientTick();
		});

	}
}
