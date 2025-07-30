package vvbj.modding.no_fire_overlay.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModConfig {
    public boolean enabled = true;

    public OverlayMode mode = OverlayMode.HIDE;

    public boolean showCrosshairFireIcon = true;

    public int fireIconSize = 10;

    public enum OverlayMode{
        HIDE,
        SHRINK
    }
}
