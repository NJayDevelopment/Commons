package net.njay.commons.ui.bossbar;

import com.google.common.collect.Maps;
import net.njay.commons.debug.DebuggingService;
import net.njay.commons.nms.PacketUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

/**
 * Class to manager player-boss interactions.
 *
 * @author Austin Mayes
 */
public class BossManager implements Listener {
    public static double elevation = -500D;
    public static int renderDistance = 32;
    private HashMap<Player, EnderEntity> bosses = Maps.newHashMap();
    private DebuggingService service;
    private Plugin owner;

    public BossManager(Plugin owner, DebuggingService service) {
        this.owner = owner;
        this.service = service == null ? new DebuggingService(owner.getLogger()) : service;
    }

    public static double getElevation() {
        return elevation;
    }

    public static int getRenderDistance() {
        return renderDistance;
    }

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

    /**
     * Spawn a boss for a player.
     *
     * @param enderEntity boss to spawn.
     * @param player      player to spawn the boss for.
     */
    public void spawnBoss(EnderEntity enderEntity, Player player) {
        try {
        PacketUtils.sendPacket(player, enderEntity.getSpawnPacket(player.getLocation()));
        } catch (Exception e) {
            this.service.log(e);
        }
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

    /**
     * Update a boss for a player.
     * You should use this to change text/health, not to only change location.
     *
     * @param enderEntity boss to update
     * @param player      player to send the update to.
     */
    public void updateBoss(EnderEntity enderEntity, Player player) {
        try {
            PacketUtils.sendPacket(player, enderEntity.getMetaPacket(enderEntity.getWatcher()));
        } catch (Exception e) {
            this.service.log(e);
        }
        moveBoss(player, enderEntity); // Client update.
    }

    /**
     * Teleport a boss to a nw location.
     *
     * @param player      player to send the packet to.
     * @param enderEntity boss to move.
     */
    public void moveBoss(Player player, EnderEntity enderEntity) {
        try {
            PacketUtils.sendPacket(player, enderEntity.getTeleportPacket(player.getLocation()));
        } catch (Exception e) {
            this.service.log(e);
        }
    }

    /**
     * Destroy a boss.
     *
     * @param player      player to send the packet to.
     * @param enderEntity boss to destroy.
     */
    public void destroyBoss(Player player, EnderEntity enderEntity) {
        try {
            PacketUtils.sendPacket(player, enderEntity.getDestroyPacket());
        } catch (Exception e) {
            this.service.log(e);
        }
        this.bosses.remove(player);
    }

    public HashMap<Player, EnderEntity> getBosses() {
        return bosses;
    }

    public DebuggingService getService() {
        return service;
    }

    public Plugin getOwner() {
        return owner;
    }
}
