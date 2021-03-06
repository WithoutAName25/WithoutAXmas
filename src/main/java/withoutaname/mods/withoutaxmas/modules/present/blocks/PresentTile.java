package withoutaname.mods.withoutaxmas.modules.present.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import withoutaname.mods.withoutaxmas.modules.present.setup.PresentRegistration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class PresentTile extends TileEntity {

	private UUID placer;

	private ItemStackHandler itemHandler = createItemHandler();

	private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

	public PresentTile() {
		super(PresentRegistration.PRESENT_TILE.get());
	}

	public void dropInventory(World world, BlockPos pos) {
		this.setLevelAndPosition(world, pos);
		ItemStack itemStack;
		for (int i = 0; i < 3; i++) {
			itemStack = itemHandler.getStackInSlot(i);
			world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack));
		}
		setChanged();
	}

	public void openPresent(World world, BlockPos pos) {
		this.setLevelAndPosition(world, pos);
		if (!this.isEmpty()) {
			world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			this.dropInventory(world, pos);
		}
	}

	public void setPlacer(UUID placer) {
		this.placer = placer;
	}

	public UUID getPlacer() {
		return this.placer;
	}

	public int getSize() {
		int size;
		switch (getStackAmount()) {
			default:
			case 0:
			case 1:
				size = 0;
				break;
			case 2:
				size = 1;
				break;
			case 3:
				size = 2;
				break;
		}
		return size;
	}

	public int getStackAmount() {
		int stackAmount = 0;
		for (int i = 0; i < itemHandler.getSlots(); i++) {
			if (!itemHandler.getStackInSlot(i).isEmpty()) {
				stackAmount++;
			}
		}
		return stackAmount;
	}

	public boolean isEmpty() {
		return getStackAmount() == 0;
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		handler.invalidate();
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		itemHandler.deserializeNBT(nbt.getCompound("inv"));
		this.placer = nbt.getUUID("placer");
		super.load(state, nbt);
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.put("inv", itemHandler.serializeNBT());
		nbt.putUUID("placer", this.placer);
		return super.save(nbt);
	}

	private ItemStackHandler createItemHandler() {
		return new ItemStackHandler(3) {

			@Override
			protected void onContentsChanged(int slot) {
				level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(PresentBlock.SIZE_PROPERTY, getSize()));
				setChanged();
			}
		};
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side == null) {
			return handler.cast();
		}
		return super.getCapability(cap, side);
	}

}
