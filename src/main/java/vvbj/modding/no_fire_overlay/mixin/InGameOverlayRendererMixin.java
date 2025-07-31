package vvbj.modding.no_fire_overlay.mixin;

import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vvbj.modding.no_fire_overlay.config.ConfigHandler;
import vvbj.modding.no_fire_overlay.config.ModConfig;

@Mixin(InGameOverlayRenderer.class)
public abstract class InGameOverlayRendererMixin {

    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void cancelFireOverlay(MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci){
        if(!ConfigHandler.config.enabled) return;
        ci.cancel();

        if(ConfigHandler.config.mode == ModConfig.OverlayMode.SHRINK){
            Sprite sprite = ModelBaker.FIRE_1.getSprite();
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getFireScreenEffect(sprite.getAtlasId()));
            float f = sprite.getMinU();
            float g = sprite.getMaxU();
            float h = (f + g) / 2.0F;
            float i = sprite.getMinV();
            float j = sprite.getMaxV();
            float k = (i + j) / 2.0F;
            float l = sprite.getUvScaleDelta();
            float m = MathHelper.lerp(l, f, h);
            float n = MathHelper.lerp(l, g, h);
            float o = MathHelper.lerp(l, i, k);
            float p = MathHelper.lerp(l, j, k);
            float q = 1.0F;

            float bottom = -0.5F;
            float top = MathHelper.lerp(0.65f, bottom, 0.5F);

            for(int r = 0; r < 2; ++r) {
                matrices.push();
                float s = -0.5F;
                float t = 0.5F;
                float u = -0.5F;
                float v = 0.5F;
                float w = -0.5F;
                matrices.translate((float)(-(r * 2 - 1)) * 0.24F, -0.3F, 0.0F);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)(r * 2 - 1) * 10.0F));
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                vertexConsumer.vertex(matrix4f, -0.5F, bottom, -0.5F).texture(n, p).color(1.0F, 1.0F, 1.0F, 0.9F);
                vertexConsumer.vertex(matrix4f, 0.5F, bottom, -0.5F).texture(m, p).color(1.0F, 1.0F, 1.0F, 0.9F);
                vertexConsumer.vertex(matrix4f, 0.5F, top, -0.5F).texture(m, o).color(1.0F, 1.0F, 1.0F, 0.9F);
                vertexConsumer.vertex(matrix4f, -0.5F, top, -0.5F).texture(n, o).color(1.0F, 1.0F, 1.0F, 0.9F);
                matrices.pop();
            }
        }
    }
}