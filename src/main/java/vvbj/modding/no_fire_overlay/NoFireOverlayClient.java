package vvbj.modding.no_fire_overlay;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import vvbj.modding.no_fire_overlay.config.ModConfig;

public class NoFireOverlayClient implements ClientModInitializer {

    private ModConfig config;
    private static final Identifier FIRE_TEXTURE = new Identifier(NoFireOverlay.MOD_ID, "fire.png");

    private static int counter = 0;

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((drawContext, delta) -> {
            if(config == null)
                config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
            if(!config.enabled) return;
            MinecraftClient client = MinecraftClient.getInstance();
            if(config.mode == ModConfig.OverlayMode.HIDE && config.showCrosshairFireIcon && client.player != null && client.player.isOnFire() && !client.options.hudHidden && client.options.getPerspective().isFirstPerson()) {
                float scale = config.fireIconSize / 10f;

                int centerX = drawContext.getScaledWindowWidth() / 2;
                int centerY = drawContext.getScaledWindowHeight() / 2;

                float offset = (4 * scale - 4) / 2;

                drawContext.getMatrices().push();
                drawContext.getMatrices().translate(centerX + 8 + (int)scale - offset,centerY - 2 - offset,0);
                drawContext.getMatrices().scale(scale, scale, 1);

                float u = 0;
                float v = 16 * counter;

                drawContext.drawTexture(FIRE_TEXTURE, 0, 0, 4, 4, u, v, 16, 16, 16, 512);

                drawContext.getMatrices().pop();
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(client.player != null){
                if(client.player.isOnFire()){
                    counter++;
                    if(counter > 32)
                        counter = 0;
                }else
                    counter = 0;
            }
        });
    }
}
