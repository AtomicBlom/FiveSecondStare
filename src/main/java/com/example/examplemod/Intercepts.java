package com.example.examplemod;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.minecraft.block.BlockDirectional.FACING;

@Mod.EventBusSubscriber
public class Intercepts {
    private static boolean staring = false;
    private static long fiveSecondStareTime = -1;
    private static boolean activated;

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        ResourceLocation registryName = event.getWorld().getBlockState(event.getPos()).getBlock().getRegistryName();
        if (registryName.getResourceDomain().startsWith("rftool")) {
            ItemStack itemStack = event.getItemStack();
            if (itemStack != null) {
                NBTTagCompound tagCompound = itemStack.getTagCompound();
                if (tagCompound != null && tagCompound.hasKey("override")) {
                    return;
                }
            }
            event.setUseBlock(Event.Result.DENY);
            //event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBlockHighlightEvent(DrawBlockHighlightEvent event) {
        RayTraceResult target = event.getTarget();
        if (target.typeOfHit != RayTraceResult.Type.BLOCK) {
            staring = false;
            activated= false;
            return;
        }

        EntityPlayer player = event.getPlayer();
        World world = player.getEntityWorld();
        BlockPos pos = target.getBlockPos();
        IBlockState blockState = world.getBlockState(pos);
        if (!blockState.getBlock().getRegistryName().getResourceDomain().startsWith("rftool")) {
            staring = false;
            activated= false;
            return;
        }

        try {
            EnumFacing direction = blockState.getValue(BlockDirectional.FACING);
            if (target.sideHit != direction) {
                staring = false;
                activated= false;
                return;
            }
        } catch (Exception e) {
            try {
                EnumFacing direction = blockState.getValue(BlockHorizontal.FACING);
                if (target.sideHit != direction) {
                    staring = false;
                    activated= false;
                    return;
                }
            } catch (Exception e2) {

            }
        }

        Vec3d vec = target.hitVec;

        float f = (float)(vec.xCoord - (double)pos.getX());
        float f1 = (float)(vec.yCoord - (double)pos.getY());
        float f2 = (float)(vec.zCoord - (double)pos.getZ());

        float x = 0;
        float y = 0;

        y = f1;

        if (target.sideHit == EnumFacing.NORTH) {
            x = f;
        } else if (target.sideHit == EnumFacing.WEST) {
            x = 1- f2;
        } else if (target.sideHit == EnumFacing.SOUTH) {
            x = 1 - f;
        }  else if (target.sideHit == EnumFacing.EAST) {
            x = f2;
        } else if (target.sideHit == EnumFacing.UP) {
            x = f;
            y = f2;
        } else if (target.sideHit == EnumFacing.DOWN) {
            x = f;
            y = 1 - f2;
        }

        float one_pixel = 1/16f;

        float minX = 12 * one_pixel;
        float minY = 13 * one_pixel;
        float maxX = 13 * one_pixel;
        float maxY = 14 * one_pixel;

        if (x > minX && x < maxX && y > minY && y < maxY) {
            if (activated) {
                return;
            }
            if (!staring) {
                staring = true;
                fiveSecondStareTime = world.getTotalWorldTime() + 20 * 5;
            } else if (world.getTotalWorldTime() >= fiveSecondStareTime) {
                activated = true;

                if (player instanceof EntityPlayerSP && world instanceof WorldClient) {
                    ItemStack overrideStack = new ItemStack(Blocks.COBBLESTONE, 1, 0);

                    NBTTagCompound nbtTagCompound = new NBTTagCompound();
                    nbtTagCompound.setBoolean("override", true);
                    overrideStack.setTagCompound(nbtTagCompound);
                    //Minecraft.getMinecraft().playerController.processRightClickBlock((EntityPlayerSP)player, (WorldClient) world, overrideStack, pos, target.sideHit, vec, EnumHand.MAIN_HAND);
                    FiveSecondStare.CHANNEL.sendToServer(new OverrideOpenRequestMessage(pos, target.sideHit, f, f1, f2));
                }

                //if (player instanceof PlayerControllerMP)

                //pl\ayer.processRigh
                //player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, target.sideHit, EnumHand.MAIN_HAND, f, f1, f2));
                //blockState.getBlock().onBlockActivated(world, pos, blockState, player, EnumHand.MAIN_HAND, player.getHeldItemMainhand(), target.sideHit, f, f1, f2 );
            }
        }
    }
}
