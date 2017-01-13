package com.github.atomicblom.fivesecondstare;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by codew on 6/01/2017.
 */
public class OverrideOpenRequestMessage implements IMessage {
    private BlockPos pos;
    private EnumFacing side;
    public float hitX;
    public float hitY;
    public float hitZ;

    public OverrideOpenRequestMessage() {}

    public OverrideOpenRequestMessage(BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

        this.pos = pos;
        this.side = side;
        this.hitX = hitX;
        this.hitY = hitY;
        this.hitZ = hitZ;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new BlockPos(buf.readInt(),buf.readInt(), buf.readInt());
        side = EnumFacing.VALUES[buf.readByte()];
        hitX = buf.readFloat();
        hitY = buf.readFloat();
        hitZ = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeByte(side.ordinal());
        buf.writeFloat(hitX);
        buf.writeFloat(hitY);
        buf.writeFloat(hitZ);
    }

    public BlockPos getPos() {
        return pos;
    }

    public EnumFacing getSide() {
        return side;
    }
}
