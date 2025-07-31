package vvbj.modding.no_fire_overlay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import vvbj.modding.no_fire_overlay.config.ConfigHandler;
import vvbj.modding.no_fire_overlay.config.ModConfig;

public class NoFireOverlayClient implements ClientModInitializer {

    private ModConfig config;
    private static final Identifier FIRE_TEXTURE = Identifier.of(NoFireOverlay.MOD_ID, "fire.png");

    private static int counter = 0;

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((drawContext, delta) -> {
            if(config == null)
                config = ConfigHandler.config;
            if(!config.enabled) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if(config.mode == ModConfig.OverlayMode.HIDE && config.showCrosshairFireIcon && client.player != null && client.player.isOnFire() && !client.options.hudHidden && client.options.getPerspective().isFirstPerson()) {
                int centerX = drawContext.getScaledWindowWidth() / 2;
                int centerY = drawContext.getScaledWindowHeight() / 2;

                float offset = (4 * config.fireIconSize - 4) / 2;

                drawContext.getMatrices().push();
                drawContext.getMatrices().translate(centerX + 8 + (int)config.fireIconSize - offset,centerY - 2 - offset,0);
                drawContext.getMatrices().scale(config.fireIconSize, config.fireIconSize, 1);

                int u = 0;
                int v = 16 * counter;

                drawContext.drawTexture(RenderLayer::getGuiTextured, FIRE_TEXTURE, 0, 0, u, v, 4, 4, 16, 16, 16, 512);

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

        ClientPlayConnectionEvents.JOIN.register((clientPlayNetworkHandler, packetSender, minecraftClient) -> {
            ConfigHandler.loadConfig(); // Refresh
        });
    }
}
