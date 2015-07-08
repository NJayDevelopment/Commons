package net.njay.commons.ui.bossbar;

import net.njay.commons.nms.NMSUtils;
import net.njay.commons.nms.ReflectionUtils;
import org.bukkit.Location;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Class to represent a dragon.
 * Packet methods adopted from BarAPI.
 *
 * @author Austin Mayes
 * @author James Mortemore
 */
public class EnderEntity {
    private UIBossBar UIBossBar;
    private float health;
    private Location location;
    private Object entity;
    private int entityId;
    private BossManager manager;

    /**
     * Constructor
     *
     * @param UIBossBar information for the bar of this entity.
     */
    public EnderEntity(UIBossBar UIBossBar, BossManager manager) {
        this.manager = manager;
        this.health = Math.max(0F, Math.min(1F, UIBossBar.getPercent())) * 99.9F + 0.1F; // Fake 0
    }

    /**
     * Get the constructed spawn packet for this entity.
     *
     * @param location location to spawn the entity at. NOTE: Will spawn in void.
     * @return a constructed spawn packet.
     */
    public Object getSpawnPacket(Location location) {
        this.location = location;
        Object packet = null;

        try {
            Class<?> Entity = NMSUtils.getNMSClass("Entity");
            Class<?> EntityLiving = NMSUtils.getNMSClass("EntityLiving");
            Class<?> EntityEnderDragon = NMSUtils.getNMSClass("EntityEnderDragon");
            entity = EntityEnderDragon.getConstructor(NMSUtils.getNMSClass("World")).newInstance(location.getWorld());

            Method setLocation = ReflectionUtils.getMethod(EntityEnderDragon, "setLocation", new Class<?>[]{double.class, double.class, double.class, float.class, float.class});
            setLocation.invoke(entity, location.getX(), BossManager.elevation, location.getZ(), location.getPitch(), location.getYaw());

            Method setInvisible = ReflectionUtils.getMethod(EntityEnderDragon, "setInvisible", new Class<?>[]{boolean.class});
            setInvisible.invoke(entity, true);

            Method setCustomName = ReflectionUtils.getMethod(EntityEnderDragon, "setCustomName", new Class<?>[]{String.class});
            setCustomName.invoke(entity, this.UIBossBar.getText());

            Method setHealth = ReflectionUtils.getMethod(EntityEnderDragon, "setHealth", new Class<?>[]{float.class});
            setHealth.invoke(entity, health);

            Field motX = ReflectionUtils.getField(Entity, "motX");
            motX.set(entity, (float) 0);

            Field motY = ReflectionUtils.getField(Entity, "motY");
            motY.set(entity, (float) 0);

            Field motZ = ReflectionUtils.getField(Entity, "motZ");
            motZ.set(entity, (float) 0);

            Method getId = ReflectionUtils.getMethod(EntityEnderDragon, "getId", new Class<?>[]{});
            this.entityId = (Integer) getId.invoke(entity);

            Class<?> PacketPlayOutSpawnEntityLiving = NMSUtils.getNMSClass("PacketPlayOutSpawnEntityLiving");

            packet = PacketPlayOutSpawnEntityLiving.getConstructor(new Class<?>[]{EntityLiving}).newInstance(entity);
        } catch (Exception e) {
            this.manager.getService().log(e);
        }

        return packet;
    }

    /**
     * Get the destroy packet for this entity.
     */
    public Object getDestroyPacket() {

        Object packet = null;
        try {
            Class<?> PacketPlayOutEntityDestroy = NMSUtils.getNMSClass("PacketPlayOutEntityDestroy");

            packet = PacketPlayOutEntityDestroy.newInstance();
            Field a = PacketPlayOutEntityDestroy.getDeclaredField("a");
            a.setAccessible(true);
            a.set(packet, new int[]{this.entityId});
        } catch (Exception e) {
            manager.getService().log(e);
        }

        return packet;
    }

    /**
     * Get the entity's meta packet.
     *
     * @param watcher DataWatcher for the entity.
     * @return the meta packet.
     */
    public Object getMetaPacket(Object watcher) {


        Object packet = null;
        try {
            Class<?> DataWatcher = NMSUtils.getNMSClass("DataWatcher");

            Class<?> PacketPlayOutEntityMetadata = NMSUtils.getNMSClass("PacketPlayOutEntityMetadata");
            packet = PacketPlayOutEntityMetadata.getConstructor(new Class<?>[]{int.class, DataWatcher, boolean.class}).newInstance(this.entityId, watcher, true);
        } catch (Exception e) {
            manager.getService().log(e);
        }

        return packet;
    }

    /**
     * Get the entity's teleport packet.
     *
     * @param loc location to teleport to.
     * @return the entity's teleport packet.
     */
    public Object getTeleportPacket(Location loc) {
        this.location = loc;
        Object packet = null;

        try {
            Class<?> PacketPlayOutEntityTeleport = NMSUtils.getNMSClass("PacketPlayOutEntityTeleport");

            packet = PacketPlayOutEntityTeleport.getConstructor(new Class<?>[]{int.class, int.class, int.class, int.class, byte.class, byte.class, boolean.class}).newInstance(this.entityId, loc.getBlockX() * 32, BossManager.elevation, loc.getBlockZ() * 32, (byte) ((int) loc.getYaw() * 256 / 360), (byte) ((int) loc.getPitch() * 256 / 360), false);
        } catch (Exception e) {
            manager.getService().log(e);
        }

        return packet;
    }

    /**
     * Get the data watcher for the entity
     */
    public Object getWatcher() {


        Object watcher = null;
        try {
            Class<?> Entity = NMSUtils.getNMSClass("Entity");
            Class<?> DataWatcher = NMSUtils.getNMSClass("DataWatcher");
            watcher = DataWatcher.getConstructor(new Class<?>[]{Entity}).newInstance(entity);
            Method a = ReflectionUtils.getMethod(DataWatcher, "a", new Class<?>[]{int.class, Object.class});

            a.invoke(watcher, 5, (byte) 0);
            a.invoke(watcher, 6, health);
            a.invoke(watcher, 7, 0);
            a.invoke(watcher, 8, (byte) 0);
            a.invoke(watcher, 10, this.UIBossBar.getText());
            a.invoke(watcher, 11, (byte) 1);
        } catch (Exception e) {
            manager.getService().log(e);
        }

        return watcher;
    }

    public float getHealth() {
        return health;
    }

    public UIBossBar getUIBossBar() {
        return UIBossBar;
    }

    public void setUIBossBar(UIBossBar UIBossBar) {
        this.UIBossBar = UIBossBar;
    }

    public Location getLocation() {
        return location;
    }

    public Object getEntity() {
        return entity;
    }

    public int getEntityId() {
        return entityId;
    }
}
