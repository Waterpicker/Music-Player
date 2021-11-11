package info.u_team.music_player.init;

import info.u_team.music_player.musicplayer.MusicPlayerManager;

import net.fabricmc.api.ClientModInitializer;

public class MusicPlayerClientConstruct implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		System.setProperty("http.agent", "Chrome");

//		ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.CONFIG);
		
		MusicPlayerFiles.load();

		MusicPlayerManager.init();
		MusicPlayerKeys.init();
		
		MusicPlayerEventHandler.init();
	}
}
