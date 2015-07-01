package net.njay.commons.nms;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PacketUtils {
    public static void sendPacket(Player p, Object packet) {
        try {
            Object nmsPlayer = NMSUtils.getHandle(p);
            Field con_field = nmsPlayer.getClass().getField("playerConnection");
            Object con = con_field.get(nmsPlayer);
            Method packet_method = ReflectionUtils.getMethod(con.getClass(), "sendPacket");
            packet_method.invoke(con, packet);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
