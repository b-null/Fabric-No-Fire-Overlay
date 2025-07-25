package vvbj.modding.no_fire_overlay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class NoFireOverlayClient implements ClientModInitializer {

    private static final Identifier FIRE_TEXTURE = new Identifier(NoFireOverlay.MOD_ID, "fire.png");

    private static int counter = 0;

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((drawContext, delta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if(client.player != null && client.player.isOnFire() && client.options.getPerspective().isFirstPerson()) {
                int centerX = drawContext.getScaledWindowWidth() / 2;
                int centerY = drawContext.getScaledWindowHeight() / 2;

                float u = 0;
                float v = 16 * counter;

                drawContext.drawTexture(FIRE_TEXTURE, centerX + 8, centerY - 2, 4, 4, u, v, 16, 16, 16, 512);
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
