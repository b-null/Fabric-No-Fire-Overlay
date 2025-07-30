package vvbj.modding.no_fire_overlay.config;

import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import vvbj.modding.no_fire_overlay.NoFireOverlay;

@Environment(EnvType.CLIENT)
public class ModConfig {
    public boolean enabled = true;

    public OverlayMode mode = OverlayMode.HIDE;

    public boolean showCrosshairFireIcon = true;

    public float fireIconSize = 1.0f;

    public void loadFromJson(JsonObject obj){
        try{
            enabled = obj.get("enabled").getAsBoolean();
            String strMode = obj.get("mode").getAsString();
            switch (strMode.trim().toLowerCase()){
                case "hide":
                    mode = OverlayMode.HIDE;
                    break;
                case "shrink":
                    mode = OverlayMode.SHRINK;
                    break;
                default:
                    mode = OverlayMode.HIDE;
                    break;
            }

            JsonObject crosshair = obj.getAsJsonObject("crosshair");
            showCrosshairFireIcon = crosshair.get("showFireIcon").getAsBoolean();
            fireIconSize = crosshair.get("fireIconSize").getAsFloat();
            if(fireIconSize < 0.5f)
                fireIconSize = 0.5f;
            if(fireIconSize > 3)
                fireIconSize = 3;
            NoFireOverlay.LOGGER.info("Config loaded");
        }catch (Exception e){
            NoFireOverlay.LOGGER.error("Error loading config: {}\nLoading defaults.", e.toString());
            reset(); // Unnecessary but meh
            ConfigHandler.saveConfig();
        }
    }

    public JsonObject toJson(){
        JsonObject configJson = new JsonObject();

        configJson.addProperty("_comment", "For the \'mode\' property, there are two modes: \'hide\' and \'shrink\'. hide: Removes vanilla's overlay and shows an icon next to crosshair (if enabled). shrink: Shows vanilla's overlay, but shorter (will only cover a very small portion of your screen)");
        configJson.addProperty("enabled", enabled);
        configJson.addProperty("mode", "hide");
        
        JsonObject fireIconJson = new JsonObject();
        fireIconJson.addProperty("_comment", "The fire icon will only be rendered in \'hide\' mode; Maximum size is 3.0, minimum 0.5");
        fireIconJson.addProperty("showFireIcon", showCrosshairFireIcon);
        fireIconJson.addProperty("fireIconSize", fireIconSize);

        configJson.add("crosshair", fireIconJson);

        return configJson;
    }

    public void reset(){
        enabled = true;
        mode = OverlayMode.HIDE;
        showCrosshairFireIcon = true;
        fireIconSize = 1.0f;
    }

    public enum OverlayMode{
        HIDE,
        SHRINK
    }
}
