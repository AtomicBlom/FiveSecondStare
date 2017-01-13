package com.example.examplemod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by codew on 6/01/2017.
 */
public class OverrideOpenMessageHandler implements IMessageHandler<OverrideOpenRequestMessage, OverrideOpenRequestMessage> {
    @Override
    public OverrideOpenRequestMessage onMessage(OverrideOpenRequestMessage message, MessageContext ctx) {

        NetHandlerPlayServer serverHandler = ctx.getServerHandler();
        World entityWorld = serverHandler.playerEntity.getEntityWorld();
        BlockPos pos = message.getPos();

        IBlockState blockState = entityWorld.getBlockState(pos);
        blockState.getBlock().onBlockActivated(entityWorld,pos, blockState, serverHandler.playerEntity, EnumHand.MAIN_HAND, new ItemStack(Blocks.AIR, 0, 0), message.getSide(), message.hitX, message.hitY, message.hitZ );

        return null;

    }
}
