package hu.telcat.servercore.entity.npc;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SheepNMS extends EntitySheep {
    public SheepNMS(World world) {
        super(EntityTypes.SHEEP, world);
    }

    @Override
    protected void initPathfinder(){
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(9, new PathfinderGoalInteract(this, EntityHuman.class, 3.0f, 1.0f));
        this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0f));
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    public static Sheep spawnSheep(String name, Location loc){
        WorldServer worldServer = ((CraftWorld)loc.getWorld()).getHandle();
        SheepNMS sheepNMS = new SheepNMS((World) worldServer);
        sheepNMS.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        worldServer.addEntity((Entity) sheepNMS, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (Sheep) sheepNMS.getBukkitEntity();
    }

    @Override
    public void movementTick() {

    }
}
