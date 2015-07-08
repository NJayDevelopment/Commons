package net.njay.commons.nms;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

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
    public static Class<?> getNMSClass(String name) throws Exception {
        String className = "net.minecraft.server." + version + name;
        Class<?> c = null;
        c = Class.forName(className);
        return c;
    }

    /**
     * Get a craft handle for a bukkit world.
     *
     * @param world world to get the handle for.
     * @return the craft handle.
     */
    public static Object getHandle(World world) throws Exception {
        Object nms_entity = null;
        Method entity_getHandle = ReflectionUtils.getMethod(world.getClass(), "getHandle");
        nms_entity = entity_getHandle.invoke(world);
        return nms_entity;
    }

    /**
     * Get a craft handle for a bukkit entity.
     *
     * @param entity entity to get the handle for.
     * @return the craft handle.
     */
    public static Object getHandle(Entity entity) throws Exception {
        Object nms_entity = null;
        Method entity_getHandle = ReflectionUtils.getMethod(entity.getClass(), "getHandle");
        nms_entity = entity_getHandle.invoke(entity);
        return nms_entity;
    }

    /**
     * Gets a {@link Class} object representing a type contained within the {@code org.bukkit.craftbukkit} versioned package.
     * The class instances returned by this method are cached, such that no lookup will be done twice (unless multiple threads are accessing this method simultaneously).
     *
     * @param className The name of the class, excluding the package, within OBC. This name may contain a subpackage name, such as {@code inventory.CraftItemStack}.
     * @return The class instance representing the specified CraftBukkit class, or {@code null} if it could not be loaded.
     */
    public synchronized static Class<?> getCraftClass(String className) throws Exception {
        String fullName = "org.bukkit.craftbukkit." + version + className;
        Class<?> clazz = null;
        clazz = Class.forName(fullName);
        return clazz;
    }
}
