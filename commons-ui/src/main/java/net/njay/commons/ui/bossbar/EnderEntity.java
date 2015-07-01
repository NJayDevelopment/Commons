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

    public EnderEntity(UIBossBar UIBossBar) {
        this.health = Math.max(0F, Math.min(1F, UIBossBar.getPercent())) * 99.9F + 0.1F; // Fake 0
    }

    public Object getSpawnPacket(Location location) {
        this.location = location;
        Class<?> Entity = NMSUtils.getCraftClass("Entity");
        Class<?> EntityLiving = NMSUtils.getCraftClass("EntityLiving");
        Class<?> EntityEnderDragon = NMSUtils.getCraftClass("EntityEnderDragon");
        Object packet = null;

        try {
            entity = EntityEnderDragon.getConstructor(NMSUtils.getCraftClass("World")).newInstance(location.getWorld());

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

            Class<?> PacketPlayOutSpawnEntityLiving = NMSUtils.getCraftClass("PacketPlayOutSpawnEntityLiving");

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

    public Object getDestroyPacket() {
        Class<?> PacketPlayOutEntityDestroy = NMSUtils.getCraftClass("PacketPlayOutEntityDestroy");

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

    public Object getMetaPacket(Object watcher) {
        Class<?> DataWatcher = NMSUtils.getCraftClass("DataWatcher");

        Class<?> PacketPlayOutEntityMetadata = NMSUtils.getCraftClass("PacketPlayOutEntityMetadata");

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

    public Object getTeleportPacket(Location loc) {
        this.location = loc;
        Class<?> PacketPlayOutEntityTeleport = NMSUtils.getCraftClass("PacketPlayOutEntityTeleport");
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

    public Object getWatcher() {
        Class<?> Entity = NMSUtils.getCraftClass("Entity");
        Class<?> DataWatcher = NMSUtils.getCraftClass("DataWatcher");

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

    public void setHealth(float health) {
        this.health = health;
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

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }
}
