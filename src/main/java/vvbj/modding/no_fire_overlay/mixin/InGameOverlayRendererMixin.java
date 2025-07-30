package vvbj.modding.no_fire_overlay.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vvbj.modding.no_fire_overlay.config.ModConfig;

@Mixin(InGameOverlayRenderer.class)
public abstract class InGameOverlayRendererMixin {

    @Unique
    private static ModConfig config;

    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void cancelFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci){
        if(config == null)
            config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if(!config.enabled) return;
        ci.cancel();

        if(config.mode == ModConfig.OverlayMode.SHRINK)
        {
            // Copied from vanilla
            BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
            RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
            RenderSystem.depthFunc(519);
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            Sprite sprite = ModelLoader.FIRE_1.getSprite();
            RenderSystem.setShaderTexture(0, sprite.getAtlasId());
            float f = sprite.getMinU();
            float g = sprite.getMaxU();
            float h = (f + g) / 2.0F;
            float i = sprite.getMinV();
            float j = sprite.getMaxV();
            float k = (i + j) / 2.0F;
            float l = sprite.getAnimationFrameDelta();
            float m = MathHelper.lerp(l, f, h);
            float n = MathHelper.lerp(l, g, h);
            float o = MathHelper.lerp(l, i, k);
            float p = MathHelper.lerp(l, j, k);
            float q = 1.0F;

            float bottom = -0.5F;
            float top = MathHelper.lerp(0.65f, bottom, 0.5F); // Shrink the top toward the bottom

            for (int r = 0; r < 2; ++r) {
                matrices.push();
                matrices.translate((float)(-(r * 2 - 1)) * 0.24F, -0.3F, 0.0F);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)(r * 2 - 1) * 10.0F));
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();

                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
                bufferBuilder.vertex(matrix4f, -0.5F, bottom, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).texture(n, p).next();
                bufferBuilder.vertex(matrix4f,  0.5F, bottom, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).texture(m, p).next();
                bufferBuilder.vertex(matrix4f,  0.5F, top,    -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).texture(m, o).next();
                bufferBuilder.vertex(matrix4f, -0.5F, top,    -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).texture(n, o).next();
                BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());

                matrices.pop();
            }

            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.depthFunc(515);
        }
    }
}