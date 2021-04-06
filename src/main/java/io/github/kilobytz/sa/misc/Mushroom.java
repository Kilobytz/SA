package io.github.kilobytz.sa.misc;

import net.minecraft.server.v1_12_R1.BlockMushroom;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.World;

public class Mushroom extends BlockMushroom{

    @Override
    public boolean f(World world, BlockPosition blockposition, IBlockData iblockdata) {
        return true;
    }
    @Override
    public boolean canPlace(World world, BlockPosition blockposition) {
        return this.f(world, blockposition, this.getBlockData());
    }
}