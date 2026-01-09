package com.licht_meilleur.resurrection_ark.block;

import com.licht_meilleur.resurrection_ark.block.entity.ResurrectionArkBlockEntity;
import com.licht_meilleur.resurrection_ark.data.ResurrectionData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;

public class ResurrectionArkBlock extends Block {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public ResurrectionArkBlock() {
        super(AbstractBlock.Settings.copy(Blocks.STONE).strength(4f));
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof ResurrectionArkBlockEntity arkEntity)) {
            return ActionResult.PASS;
        }

        // GUIを開く
        player.openHandledScreen(arkEntity);

        return ActionResult.SUCCESS;
    }
    /*@Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        if (!(world.getBlockEntity(pos) instanceof ResurrectionArkBlockEntity arkBe)) {
            return ActionResult.PASS;
        }

        ResurrectionData data = ResurrectionData.get(world);

        if (player.isSneaking()) {
            // 登録モード切り替え
            arkBe.setRegistering(!arkBe.isRegistering());
            player.sendMessage(Text.literal(
                    arkBe.isRegistering() ? "登録モードになりました" : "登録モードを解除しました"), false);
            return ActionResult.SUCCESS;
        } else {
            // 蘇生処理
            Map<UUID, String> mobs = data.getAllMobs();
            if (!mobs.isEmpty()) {
                UUID mobId = mobs.keySet().iterator().next(); // とりあえず最初の1体
                String typeId = mobs.get(mobId);
                EntityType<?> type = net.minecraft.registry.Registries.ENTITY_TYPE.get(
                        new net.minecraft.util.Identifier(typeId));

                if (type != null && player.getInventory().contains(new ItemStack(Items.EMERALD))) {
                    // エメラルドを消費
                    player.getInventory().removeStack(player.getInventory().getSlotWithStack(new ItemStack(Items.EMERALD)), 1);

                    Entity entity = type.create(world);
                    if (entity != null) {
                        entity.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0);
                        world.spawnEntity(entity);
                        player.sendMessage(Text.literal("Mobを蘇生しました！"), false);
                    }
                } else {
                    player.sendMessage(Text.literal("エメラルドが必要です！"), false);
                }
            } else {
                player.sendMessage(Text.literal("登録されたMobがいません"), false);
            }
            return ActionResult.SUCCESS;*/
        }