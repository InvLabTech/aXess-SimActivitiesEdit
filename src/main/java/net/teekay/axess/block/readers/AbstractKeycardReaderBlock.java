package net.teekay.axess.block.readers;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.teekay.axess.block.AccessBlockPowerState;
import net.teekay.axess.utilities.VoxelShapeUtilities;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class AbstractKeycardReaderBlock extends FaceAttachedHorizontalDirectionalBlock implements EntityBlock {
    public final VoxelShape VOXEL_SHAPE_1 = Block.box(3, 1, 15, 13, 15, 16);
    public final VoxelShape VOXEL_SHAPE_2 = Block.box(3, 5, 14, 13, 13, 15);

    public final VoxelShape VOXEL_SHAPE = Shapes.join(VOXEL_SHAPE_1, VOXEL_SHAPE_2, BooleanOp.OR);

    public final VoxelShape VOXEL_SHAPE_SOUTH = VoxelShapeUtilities.rotateShape(VOXEL_SHAPE, Direction.NORTH, Direction.SOUTH);
    public final VoxelShape VOXEL_SHAPE_WEST = VoxelShapeUtilities.rotateShape(VOXEL_SHAPE, Direction.NORTH, Direction.WEST);
    public final VoxelShape VOXEL_SHAPE_EAST = VoxelShapeUtilities.rotateShape(VOXEL_SHAPE, Direction.NORTH, Direction.EAST);

    public final VoxelShape VOXEL_SHAPE_FLOOR_X = VoxelShapeUtilities.rotateShape(VOXEL_SHAPE, Direction.NORTH, Direction.UP);
    public final VoxelShape VOXEL_SHAPE_FLOOR_Z = VoxelShapeUtilities.rotateShape(VOXEL_SHAPE_FLOOR_X, Direction.NORTH, Direction.WEST);

    public final VoxelShape VOXEL_SHAPE_CEILING_X = VoxelShapeUtilities.rotateShape(VOXEL_SHAPE, Direction.NORTH, Direction.DOWN);
    public final VoxelShape VOXEL_SHAPE_CEILING_Z = VoxelShapeUtilities.rotateShape(VOXEL_SHAPE_CEILING_X, Direction.NORTH, Direction.WEST);

    public static final EnumProperty<AccessBlockPowerState> POWER_STATE = EnumProperty.create("power_state", AccessBlockPowerState.class);

    public AbstractKeycardReaderBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction facing = pState.getValue(FACING);
        AttachFace face = pState.getValue(FACE);
        return switch (face) {
            case WALL -> switch (facing) {
                case SOUTH -> VOXEL_SHAPE_SOUTH;
                case WEST -> VOXEL_SHAPE_WEST;
                case EAST -> VOXEL_SHAPE_EAST;
                default -> VOXEL_SHAPE;
            };
            case CEILING -> switch (facing) {
                case SOUTH, NORTH -> VOXEL_SHAPE_CEILING_X;
                case WEST, EAST -> VOXEL_SHAPE_CEILING_Z;
                default -> VOXEL_SHAPE;
            };
            case FLOOR -> switch (facing) {
                case SOUTH, NORTH -> VOXEL_SHAPE_FLOOR_X;
                case WEST, EAST -> VOXEL_SHAPE_FLOOR_Z;
                default -> VOXEL_SHAPE;
            };
            default -> VOXEL_SHAPE;
        };
    }

    @Override
    @NotNull
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(FACE);
        builder.add(POWER_STATE);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        for(Direction direction : pContext.getNearestLookingDirections()) {
            BlockState blockstate;
            if (direction.getAxis() == Direction.Axis.Y) {
                blockstate = this.defaultBlockState()
                        .setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
                        .setValue(FACING, direction == Direction.UP ? pContext.getHorizontalDirection() : pContext.getHorizontalDirection().getOpposite())
                        .setValue(POWER_STATE, AccessBlockPowerState.NORMAL);
            } else {
                blockstate = this.defaultBlockState()
                        .setValue(FACE, AttachFace.WALL)
                        .setValue(FACING, direction.getOpposite())
                        .setValue(POWER_STATE, AccessBlockPowerState.NORMAL);
            }

            if (blockstate.canSurvive(pContext.getLevel(), pContext.getClickedPos())) {
                return blockstate;
            }
        }

        return null;
    }

}
