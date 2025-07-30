package vvbj.modding.no_fire_overlay.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import vvbj.modding.no_fire_overlay.NoFireOverlay;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
public class ConfigHandler {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("no-fire-overlay.json");

    public static ModConfig config = new ModConfig();

    public static void loadConfig(){
        try {
            if (Files.exists(CONFIG_PATH)) {
                Reader reader = Files.newBufferedReader(CONFIG_PATH);
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                config.loadFromJson(json);
                reader.close();
            } else {
                config.reset();
                saveConfig(); // Write default
            }
        } catch (IOException e) {
            NoFireOverlay.LOGGER.error("Error loading config from path: {}", e.getMessage());
        }
    }

    public static void saveConfig(){
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(config.toJson(), writer);
        } catch (IOException e) {
            NoFireOverlay.LOGGER.error("Error saving config to path: {}", e.getMessage());
        }
    }
}
