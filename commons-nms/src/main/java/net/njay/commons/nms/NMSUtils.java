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
}
