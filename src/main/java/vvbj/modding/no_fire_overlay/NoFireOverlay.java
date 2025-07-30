package vvbj.modding.no_fire_overlay;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vvbj.modding.no_fire_overlay.config.ModConfig;

public class NoFireOverlay implements ModInitializer {
	public static final String MOD_ID = "no-fire-overlay";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("No Fire overlay loaded. Say bye bye to that annoying fire!");
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
	}
}