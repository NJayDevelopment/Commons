package net.njay.commons.nms;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSUtils {

    public static String version;

    /**
     * Get the current minecraft server version.
     * This needs to be called FIRST for any reflection methods to work properly.
     *
     * @param plugin plugin to get the bukkit server instance from.
     */
    public static void getBukkitVersion(Plugin plugin) {
        String name = plugin.getServer().getClass().getPackage().getName();
        String mcVersion = name.substring(name.lastIndexOf('.') + 1);

        version = mcVersion + ".";
    }

    /**
     * Get the minecraft server class for a string.
     *
     * @param name name of the class without .class
     * @return the referenced class.
     */
    public static Class<?> getNMSClass(String name) {
        String className = "net.minecraft.server." + version + name;
        Class<?> c = null;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * Get a craft handle for a bukkit world.
     *
     * @param world world to get the handle for.
     * @return the craft handle.
     */
    public static Object getHandle(World world) {
        Object nms_entity = null;
        Method entity_getHandle = ReflectionUtils.getMethod(world.getClass(), "getHandle");
        try {
            nms_entity = entity_getHandle.invoke(world);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return nms_entity;
    }

    /**
     * Get a craft handle for a bukkit entity.
     *
     * @param entity entity to get the handle for.
     * @return the craft handle.
     */
    public static Object getHandle(Entity entity) {
        Object nms_entity = null;
        Method entity_getHandle = ReflectionUtils.getMethod(entity.getClass(), "getHandle");
        try {
            nms_entity = entity_getHandle.invoke(entity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return nms_entity;
    }

    /**
     * Gets a {@link Class} object representing a type contained within the {@code org.bukkit.craftbukkit} versioned package.
     * The class instances returned by this method are cached, such that no lookup will be done twice (unless multiple threads are accessing this method simultaneously).
     *
     * @param className The name of the class, excluding the package, within OBC. This name may contain a subpackage name, such as {@code inventory.CraftItemStack}.
     * @return The class instance representing the specified CraftBukkit class, or {@code null} if it could not be loaded.
     */
    public synchronized static Class<?> getCraftClass(String className) {
        String fullName = "org.bukkit.craftbukkit." + version + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return clazz;
    }
}
