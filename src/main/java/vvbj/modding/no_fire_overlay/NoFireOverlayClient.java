package vvbj.modding.no_fire_overlay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.util.Identifier;

public class NoFireOverlayClient implements ClientModInitializer {

    private static final Identifier FIRE_TEXTURE = Identifier.of(NoFireOverlay.MOD_ID, "fire.png");
    private static final Identifier OVERLAY_ID = Identifier.of(NoFireOverlay.MOD_ID, "fire_icon");

    private static int counter = 0;

    @Override
    public void onInitializeClient() {


        HudElementRegistry.addFirst(OVERLAY_ID, (context, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if(client.player != null && client.player.isOnFire() && !client.options.hudHidden && client.options.getPerspective().isFirstPerson()) {
                int centerX = context.getScaledWindowWidth() / 2;
                int centerY = context.getScaledWindowHeight() / 2;

                int u = 0;
                int v = 16 * counter;

                context.drawTexture(RenderPipelines.GUI_TEXTURED, FIRE_TEXTURE, centerX + 8, centerY - 2, u, v, 4, 4, 16, 16, 16, 512);
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
