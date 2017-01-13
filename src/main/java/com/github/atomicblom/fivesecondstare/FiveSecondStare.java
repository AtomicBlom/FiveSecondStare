package com.github.atomicblom.fivesecondstare;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = FiveSecondStare.MODID, version = FiveSecondStare.VERSION)
public class FiveSecondStare
{
    public static final String MODID = "five_second_stare";
    public static final String VERSION = "1.0";


    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)

    {
        CHANNEL.registerMessage(OverrideOpenMessageHandler.class, OverrideOpenRequestMessage.class, 0, Side.SERVER);

        // some example code
        ///System.out.println("DIRT BLOCK >> "+Blocks.DIRT.getUnlocalizedName());
    }
}

