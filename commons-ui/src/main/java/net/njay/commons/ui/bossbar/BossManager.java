package net.njay.commons.ui.bossbar;

import com.google.common.collect.Maps;
import net.njay.commons.nms.PacketUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;

/**
 * @author Austin Mayes
 */
public class BossManager implements Listener {
    public static double elevation = -500D;
    public static int renderDistance = 32;
    private HashMap<Player, EnderEntity> bosses = Maps.newHashMap();

    @EventHandler
    public void handlePlayerRespawn(PlayerRespawnEvent event) {
        // On respawn, all fake entities are removed. So, let's re-add our fake entity!
        if (bosses.containsKey(event.getPlayer()))
            spawnBoss(bosses.get(event.getPlayer()), event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void join(PlayerJoinEvent e) {
        if (this.bosses.containsKey(e.getPlayer()))
            spawnBoss(bosses.get(e.getPlayer()), e.getPlayer()); // Persistent boss.
    }

    public void spawnBoss(EnderEntity enderEntity, Player player) {
        PacketUtils.sendPacket(player, enderEntity.getSpawnPacket(player.getLocation()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void keepBossVisible(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        EnderEntity enderEntity = bosses.get(player);
        if (enderEntity != null) {
            Location delta = player.getLocation().subtract(enderEntity.getLocation());
            if (delta.getX() * delta.getX() + delta.getZ() * delta.getZ() > renderDistance * renderDistance) {
                moveBoss(player, enderEntity);
            }
        }
    }

    public void updateBoss(EnderEntity enderEntity, Player player) {
        PacketUtils.sendPacket(player, enderEntity.getMetaPacket(enderEntity.getWatcher()));
        moveBoss(player, enderEntity); // Client update.
    }

    public void moveBoss(Player player, EnderEntity enderEntity) {
        PacketUtils.sendPacket(player, enderEntity.getTeleportPacket(player.getLocation()));
    }

    public void destroyBoss(Player player, EnderEntity enderEntity) {
        PacketUtils.sendPacket(player, enderEntity.getDestroyPacket());
    }
}
