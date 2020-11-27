package io.github.kilobytz.sa.misc;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.util.Vector;

import io.netty.handler.codec.AsciiHeadersEncoder.NewlineType;

public class Pistons implements Listener {

    @EventHandler
    public void pistonMove(BlockPistonExtendEvent event) {
        switch(event.getDirection()) {
            case UP :
            event.setCancelled(launch(BlockFace.UP, event.getBlock()));
            return;
            case EAST :
            event.setCancelled(launch(BlockFace.EAST, event.getBlock()));
            return;
            case WEST :
            event.setCancelled(launch(BlockFace.WEST, event.getBlock()));
            return;
            case NORTH :
            event.setCancelled(launch(BlockFace.NORTH, event.getBlock()));
            return;
            case SOUTH :
            event.setCancelled(launch(BlockFace.SOUTH, event.getBlock()));
            return;
            default :
            return;

        }
    }
    

    public boolean launch(BlockFace direction, Block block) {
        for(Entity e : block.getLocation().getChunk().getEntities()) {
            Location loc1 = block.getLocation();
            loc1 = getCenter(loc1, direction);
            Location loc2 = loc1.clone();
            loc2 = getSideCenter(loc2, direction);
            if(e.getLocation().distanceSquared(loc1) <= 0.36 || e.getLocation().distanceSquared(loc2) <= 0.36) {
                e.setVelocity(getPlayerVec(direction));
                return true;
            }
        }
        return false;
    }

    public Location getCenter(Location loc, BlockFace dir) {
        switch(dir) {
            case UP :
            loc.setY(loc.getY()+1);
            loc.setX(loc.getX()+0.5);
            loc.setZ(loc.getZ()+0.5);
            return loc;
            case EAST :
            loc.setY(loc.getY()+0.5);
            loc.setX(loc.getX()+1.5);
            loc.setZ(loc.getZ()+0.5);
            return loc;
            case WEST :
            loc.setY(loc.getY()+0.5);
            loc.setX(loc.getX()-0.5);
            loc.setZ(loc.getZ()+0.5);
            return loc;
            case NORTH :
            loc.setY(loc.getY()+0.5);
            loc.setX(loc.getX()+0.5);
            loc.setZ(loc.getZ()-0.5);
            return loc;
            case SOUTH :
            loc.setY(loc.getY()+0.5);
            loc.setX(loc.getX()+0.5);
            loc.setZ(loc.getZ()+1.5);
            return loc;
            default :
            return loc;
        }
    }

    public Location getSideCenter(Location loc, BlockFace dir) {
        switch(dir) {
            case UP :
            return loc;
            case EAST :
            loc.setY(loc.getY()-1);
            return loc;
            case WEST :
            loc.setY(loc.getY()-1);
            return loc;
            case NORTH :
            loc.setY(loc.getY()-1);
            return loc;
            case SOUTH :
            loc.setY(loc.getY()-1);
            return loc;
            default :
            return loc;
        }
    }

    public Vector getPlayerVec(BlockFace dir) {
        switch(dir) {
            case UP :
            return new Vector(0,1.5,0);
            case EAST :
            return new Vector(1.5,0,0);
            case WEST :
            return new Vector(-1.5,0,0);
            case NORTH :
            return new Vector(0,0,-1.5);
            case SOUTH :
            return new Vector(0,0,1.5);
            default :
            return new Vector(0,0,0);

        }
    }
}