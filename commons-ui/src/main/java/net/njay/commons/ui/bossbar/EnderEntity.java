package net.njay.commons.ui.bossbar;

import net.njay.commons.nms.NMSUtils;
import net.njay.commons.nms.ReflectionUtils;
import org.bukkit.Location;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    /**
     * Constructor
     *
     * @param UIBossBar information for the bar of this entity.
     */
    public EnderEntity(UIBossBar UIBossBar) {
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
        Class<?> Entity = NMSUtils.getNMSClass("Entity");
        Class<?> EntityLiving = NMSUtils.getNMSClass("EntityLiving");
        Class<?> EntityEnderDragon = NMSUtils.getNMSClass("EntityEnderDragon");
        Object packet = null;

        try {
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
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return packet;
    }

    /**
     * Get the destroy packet for this entity.
     */
    public Object getDestroyPacket() {
        Class<?> PacketPlayOutEntityDestroy = NMSUtils.getNMSClass("PacketPlayOutEntityDestroy");

        Object packet = null;
        try {
            packet = PacketPlayOutEntityDestroy.newInstance();
            Field a = PacketPlayOutEntityDestroy.getDeclaredField("a");
            a.setAccessible(true);
            a.set(packet, new int[]{this.entityId});
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
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
        Class<?> DataWatcher = NMSUtils.getNMSClass("DataWatcher");

        Class<?> PacketPlayOutEntityMetadata = NMSUtils.getNMSClass("PacketPlayOutEntityMetadata");

        Object packet = null;
        try {
            packet = PacketPlayOutEntityMetadata.getConstructor(new Class<?>[]{int.class, DataWatcher, boolean.class}).newInstance(this.entityId, watcher, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
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
        Class<?> PacketPlayOutEntityTeleport = NMSUtils.getNMSClass("PacketPlayOutEntityTeleport");
        Object packet = null;

        try {
            packet = PacketPlayOutEntityTeleport.getConstructor(new Class<?>[]{int.class, int.class, int.class, int.class, byte.class, byte.class, boolean.class}).newInstance(this.entityId, loc.getBlockX() * 32, BossManager.elevation, loc.getBlockZ() * 32, (byte) ((int) loc.getYaw() * 256 / 360), (byte) ((int) loc.getPitch() * 256 / 360), false);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return packet;
    }

    /**
     * Get the data watcher for the entity
     */
    public Object getWatcher() {
        Class<?> Entity = NMSUtils.getNMSClass("Entity");
        Class<?> DataWatcher = NMSUtils.getNMSClass("DataWatcher");

        Object watcher = null;
        try {
            watcher = DataWatcher.getConstructor(new Class<?>[]{Entity}).newInstance(entity);
            Method a = ReflectionUtils.getMethod(DataWatcher, "a", new Class<?>[]{int.class, Object.class});

            a.invoke(watcher, 5, (byte) 0);
            a.invoke(watcher, 6, health);
            a.invoke(watcher, 7, 0);
            a.invoke(watcher, 8, (byte) 0);
            a.invoke(watcher, 10, this.UIBossBar.getText());
            a.invoke(watcher, 11, (byte) 1);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
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
