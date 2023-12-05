package hu.telcat.servercore.entity.npc;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class WanderingTraderNMS  extends EntityVillagerTrader {
    public WanderingTraderNMS(World world) {
        super(EntityTypes.WANDERING_TRADER, world);
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

    @Override
    public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) { return EnumInteractionResult.a(false); }

    @Override
    public void g(Vec3D vec){}

    public static WanderingTrader spawnWanderingTrader(Location loc){
        WorldServer worldServer = ((CraftWorld)loc.getWorld()).getHandle();
        WanderingTraderNMS villagerNMS = new WanderingTraderNMS((World) worldServer);
        villagerNMS.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        worldServer.addEntity((Entity) villagerNMS, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (WanderingTrader) villagerNMS.getBukkitEntity();
    }
}
