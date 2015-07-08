package net.njay.commons.nms;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PacketUtils {
    /**
     * Send a packet to a player.
     *
     * @param p      player to be sent to.
     * @param packet packet to send.
     */
    public static void sendPacket(Player p, Object packet) throws Exception {
        Object nmsPlayer = NMSUtils.getHandle(p);
        Field con_field = nmsPlayer.getClass().getField("playerConnection");
        Object con = con_field.get(nmsPlayer);
        Method packet_method = ReflectionUtils.getMethod(con.getClass(), "sendPacket");
        packet_method.invoke(con, packet);
    }
}
