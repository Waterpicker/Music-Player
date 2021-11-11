package info.u_team.music_player.init;

import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_CATEGORY;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_OPEN;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_PAUSE;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_SKIP_BACK;
import static info.u_team.music_player.init.MusicPlayerLocalization.KEY_SKIP_FORWARD;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.option.KeyBinding;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class MusicPlayerKeys {
	
	public static final KeyBinding OPEN = new KeyBinding(KEY_OPEN, GLFW.GLFW_KEY_F8, KEY_CATEGORY);
	
	public static final KeyBinding PAUSE = new KeyBinding(KEY_PAUSE, GLFW.GLFW_KEY_KP_8, KEY_CATEGORY);
	public static final KeyBinding SKIP_FORWARD = new KeyBinding(KEY_SKIP_FORWARD, GLFW.GLFW_KEY_KP_9, KEY_CATEGORY);
	public static final KeyBinding SKIP_BACK = new KeyBinding(KEY_SKIP_BACK, GLFW.GLFW_KEY_KP_5, KEY_CATEGORY);
	
	public static void init() {
		KeyBindingHelper.registerKeyBinding(OPEN);
		KeyBindingHelper.registerKeyBinding(PAUSE);
		KeyBindingHelper.registerKeyBinding(SKIP_FORWARD);
		KeyBindingHelper.registerKeyBinding(SKIP_BACK);
	}
}
