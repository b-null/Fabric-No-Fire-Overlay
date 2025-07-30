package vvbj.modding.no_fire_overlay.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Config(name = "no-fire-overlay")
public class ModConfig implements ConfigData {
    public boolean enabled = true;

    public OverlayMode mode = OverlayMode.HIDE;

    public boolean showCrosshairFireIcon = true;

    public int fireIconSize = 10;

    public enum OverlayMode{
        HIDE,
        SHRINK
    }
}
