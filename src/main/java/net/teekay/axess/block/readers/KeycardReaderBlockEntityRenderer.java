package net.teekay.axess.block.readers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.Vec3;
import net.teekay.axess.Axess;
import net.teekay.axess.block.AccessBlockPowerState;
import net.teekay.axess.registry.AxessIconRegistry;
import net.teekay.axess.utilities.RotationUtilities;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class KeycardReaderBlockEntityRenderer implements BlockEntityRenderer<KeycardReaderBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public KeycardReaderBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.context = ctx;
    }

    public static final ResourceLocation TEXTURE = AxessIconRegistry.FILLED;

    @Override
    public void render(KeycardReaderBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        Direction facing = pBlockEntity.getBlockState().getValue(AbstractKeycardReaderBlock.FACING);
        AttachFace face = pBlockEntity.getBlockState().getValue(AbstractKeycardReaderBlock.FACE);
        AccessBlockPowerState powerState = pBlockEntity.getBlockState().getValue(AbstractKeycardReaderBlock.POWER_STATE);

        pPoseStack.pushPose();

        Vector3f rot = RotationUtilities.rotationFromDirAndFace(facing, face);

        pPoseStack.translate(0.5, 0.5, 0.5);

        pPoseStack.mulPose(Axis.XP.rotationDegrees((float) rot.x));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees((float) rot.z));
        pPoseStack.mulPose(Axis.YP.rotationDegrees((float) rot.y));

        pPoseStack.translate(0f, -2f/16f, 6f/16f - 0.001f);

        pPoseStack.scale(6/16f, 6/16f, 6/16f);

        VertexConsumer consumer = pBuffer.getBuffer(RenderType.eyes(TEXTURE));
        Matrix4f matrix = pPoseStack.last().pose();

        consumer.vertex(matrix, 0.5f, 0f, 0f).color(255, 255, 255, 255).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.pack(15, 15)).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, -0.5f, 0f, 0f).color(255, 255, 255, 255).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.pack(15, 15)).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, -0.5f, 1f, 0f).color(255, 255, 255, 255).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.pack(15, 15)).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, 0.5f, 1f, 0f).color(255, 255, 255, 255).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.pack(15, 15)).normal(0, 1, 0).endVertex();


        /*
        Vector3f rot = RotationUtilities.rotationFromDirAndFace(facing, face);

        pPoseStack.translate(0.5, 0.5, 0.5);


        pPoseStack.mulPose(Axis.XP.rotationDegrees((float) rot.x));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees((float) rot.z));
        pPoseStack.mulPose(Axis.YP.rotationDegrees((float) rot.y));

        pPoseStack.translate(0f, 1f/16f, 0.449f - 1f/16f);

        pPoseStack.scale(0.38f, 0.38f, 0.38f);

        this.context.getItemRenderer().

        this.context.getItemRenderer().renderStatic(
                (switch (powerState) {
                    default -> AxessIconRegistry.FILLED;
                    //case NORMAL -> AxessIconRegistry.SCAN;
                    //case ALLOW -> AxessIconRegistry.ACCEPT;
                    //case DENY -> AxessIconRegistry.DENY;
                    //case DISABLED -> AxessIconRegistry.DENY;
                }).get().getDefaultInstance(),
                ItemDisplayContext.FIXED,
                LightTexture.pack(15, 15),
                OverlayTexture.NO_OVERLAY,
                pPoseStack,
                pBuffer,
                pBlockEntity.getLevel(),
                1
        );*/

        pPoseStack.popPose();
    }
}
