package withoutaname.mods.withoutaxmas.datagen;

import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import withoutaname.mods.withoutalib.datagen.BaseLootTableProvider;
import withoutaname.mods.withoutaxmas.modules.other.setup.OtherRegistration;
import withoutaname.mods.withoutaxmas.modules.present.blocks.PresentBlock;
import withoutaname.mods.withoutaxmas.modules.present.setup.PresentRegistration;
import withoutaname.mods.withoutaxmas.modules.present.tools.Color;
import withoutaname.mods.withoutaxmas.modules.xmastree.setup.XmasTreeRegistration;

public class LootTables extends BaseLootTableProvider {

	public LootTables(DataGenerator dataGeneratorIn) {
		super(dataGeneratorIn);
	}

	@Override
	protected void addTables() {
		addOtherTables();
		addPresentTables();
		addXmasTreeTables();
	}

	private void addOtherTables() {
		createStandardTable(OtherRegistration.ADVENT_WREATH_BLOCK.get(), OtherRegistration.ADVENT_WREATH_ITEM.get());
		createStandardTable(OtherRegistration.CANDLE_BLOCK.get(), OtherRegistration.CANDLE_ITEM.get());
	}

	private void addPresentTables() {
		Block block = PresentRegistration.PRESENT_BLOCK.get();
		AlternativesLootEntry.Builder lootEntry = AlternativesLootEntry.alternatives(
				ItemLootEntry.lootTableItem(PresentRegistration.PRESENT_BLUE_ITEM.get())
					.when(BlockStateProperty.hasBlockStateProperties(block)
						.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PresentBlock.COLOR_PROPERTY, Color.BLUE))))
				.otherwise(
				ItemLootEntry.lootTableItem(PresentRegistration.PRESENT_GREEN_ITEM.get())
					.when(BlockStateProperty.hasBlockStateProperties(block)
						.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PresentBlock.COLOR_PROPERTY, Color.GREEN))))
				.otherwise(
				ItemLootEntry.lootTableItem(PresentRegistration.PRESENT_PURPLE_ITEM.get())
					.when(BlockStateProperty.hasBlockStateProperties(block)
						.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PresentBlock.COLOR_PROPERTY, Color.PURPLE))))
				.otherwise(
				ItemLootEntry.lootTableItem(PresentRegistration.PRESENT_RED_ITEM.get())
					.when(BlockStateProperty.hasBlockStateProperties(block)
						.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PresentBlock.COLOR_PROPERTY, Color.RED))))
				.otherwise(
				ItemLootEntry.lootTableItem(PresentRegistration.PRESENT_YELLOW_ITEM.get())
					.when(BlockStateProperty.hasBlockStateProperties(block)
						.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PresentBlock.COLOR_PROPERTY, Color.YELLOW))));
		lootTables.put(block, getStandardLootTable(getStandardLootPool(block.getRegistryName().toString(), lootEntry)));
	}

	private void addXmasTreeTables() {
		createStandardTable(XmasTreeRegistration.XMAS_TREE_BOTTOM_BLOCK.get(), XmasTreeRegistration.XMAS_TREE_ITEM.get());
		createStandardTable(XmasTreeRegistration.XMAS_TREE_MIDDLE_BLOCK.get(), XmasTreeRegistration.XMAS_TREE_ITEM.get());
		createStandardTable(XmasTreeRegistration.XMAS_TREE_TOP_BLOCK.get(), XmasTreeRegistration.XMAS_TREE_ITEM.get());
	}

}
