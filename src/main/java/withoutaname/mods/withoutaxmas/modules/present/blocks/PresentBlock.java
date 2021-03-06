package withoutaname.mods.withoutaxmas.modules.present.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import withoutaname.mods.withoutaxmas.modules.present.setup.PresentRegistration;
import withoutaname.mods.withoutaxmas.modules.present.tools.Color;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock.Properties;

public class PresentBlock extends Block {

	public static final EnumProperty<Color> COLOR_PROPERTY = EnumProperty.create("color", Color.class);
	public static final IntegerProperty SIZE_PROPERTY = IntegerProperty.create("size", 0, 2);

	public static final VoxelShape SHAPE_0 = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D);
	public static final VoxelShape SHAPE_1 = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D);
	public static final VoxelShape SHAPE_2 = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

	public PresentBlock() {
		super(Properties.of(Material.WOOD)
				.sound(SoundType.WOOD)
				.strength(2.5F));
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
				.setValue(COLOR_PROPERTY, Color.BLUE)
				.setValue(SIZE_PROPERTY, 0));
	}

	@Override
	public Item asItem() {
		return PresentRegistration.PRESENT_BLUE_ITEM.get();
	}


	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new PresentTile();
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING, COLOR_PROPERTY, SIZE_PROPERTY);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.getValue(SIZE_PROPERTY)) {
			default:
			case 0:
				return SHAPE_0;
			case 1:
				return SHAPE_1;
			case 2:
				return SHAPE_2;
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
		if (!world.isClientSide) {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity instanceof PresentTile) {
				PresentTile presentTile = ((PresentTile) tileEntity);
				presentTile.setLevelAndPosition(world, pos);

				if (player.getUUID().equals(presentTile.getPlacer())) {
					INamedContainerProvider containerProvider = new INamedContainerProvider() {
						@Override
						public ITextComponent getDisplayName() {
							return new TranslationTextComponent("screen.withoutaxmas.present");
						}

						@Override
						public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
							return new PresentContainer(i, world, pos, playerInventory, playerEntity);
						}
					};
					NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getBlockPos());
				} else {
					presentTile.openPresent(world, pos);
				}
			} else {
				throw new IllegalStateException("No tile entity found!");
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
		((PresentTile) world.getBlockEntity(pos)).dropInventory(world, pos);
		super.onBlockExploded(state, world, pos, explosion);
	}

	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		((PresentTile) worldIn.getBlockEntity(pos)).dropInventory(worldIn, pos);
		super.playerWillDestroy(worldIn, pos, state, player);
	}

}