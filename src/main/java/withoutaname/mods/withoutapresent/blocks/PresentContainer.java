package withoutaname.mods.withoutapresent.blocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import withoutaname.mods.withoutalib.blocks.BaseContainer;
import withoutaname.mods.withoutapresent.setup.Registration;

public class PresentContainer extends BaseContainer {

	private TileEntity tileEntity;
	private PlayerEntity playerEntity;

	public PresentContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
		super(Registration.PRESENT_CONTAINER.get(), windowId);
		tileEntity = world.getTileEntity(pos);
		this.playerEntity = player;

		if (tileEntity != null) {
			tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
				addSlotRow(h, 0, 44, 20, 3, 36);
			});
		}
		addPlayerInventorySlots(new InvWrapper(playerInventory), 8, 51);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, Registration.PRESENT_BLOCK.get());
	}

}