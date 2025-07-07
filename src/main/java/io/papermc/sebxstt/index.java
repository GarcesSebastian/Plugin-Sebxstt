package io.papermc.sebxstt;

import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.sebxstt.enums.PlayerTypeGroup;
import io.papermc.sebxstt.functions.utils.InPlayer;
import io.papermc.sebxstt.instances.CheckPoint;
import io.papermc.sebxstt.instances.Main;
import io.papermc.sebxstt.instances.PlayerConfig;
import io.papermc.sebxstt.instances.PlayersGroup;
import io.papermc.sebxstt.functions.utils.Lib;
import io.papermc.sebxstt.managers.CommandManager;
import io.papermc.sebxstt.providers.ConfigurationProvider;
import io.papermc.sebxstt.providers.DataStoreProvider;
import io.papermc.sebxstt.providers.PlayerProvider;
import io.papermc.sebxstt.providers.PluginProvider;
import io.papermc.sebxstt.serialize.data.PlayerConfigData;
import io.papermc.sebxstt.serialize.data.PlayerGroupData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import static io.papermc.sebxstt.providers.DataStoreProvider.DS;

import java.util.*;

public class index extends JavaPlugin implements Listener {
    public static Main mainData = new Main();
    public static final Map<UUID, ArmorStand> nameTags = new HashMap<>();
    public static final MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        ConfigurationProvider.load(this);
        DataStoreProvider.init(this);
        PluginProvider.init(this);

        Bukkit.getPluginManager().registerEvents(this, this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            CommandManager.registerAll(event);
        });
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[Sebxstt] Guardando datos antes del apagado...");

        for (PlayerConfig pc : mainData.playerConfigs) {
            DS.edit("id", pc.id.toString(), PlayerConfigData.create(pc), PlayerConfigData.class);
        }

        for (PlayersGroup pg : mainData.playersGroups) {
            DS.edit("id", pg.id.toString(), PlayerGroupData.create(pg), PlayerGroupData.class);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Inventory inventory = event.getInventory();
        PlayerConfig pc = Lib.getPlayerConfig(player);
        PlayerTypeGroup playerTypeGroup = pc.getPlayerType();

        PlayersGroup grp = Lib.FindPlayerInGroup(player.getName());
        if (grp == null) return;

        if (!inventory.equals(grp.getStorage().instance)) return;

        int rawSlot = event.getRawSlot();
        InventoryView view = event.getView();

        if (rawSlot < view.getTopInventory().getSize() && rawSlot == 0) {
            event.setCancelled(true);
            player.sendMessage(Component.text("Este ítem no se puede mover."));
            return;
        }

        if (playerTypeGroup == PlayerTypeGroup.VIEWER) {
            event.setCancelled(true);
            return;
        }

        if (playerTypeGroup == PlayerTypeGroup.CONTROLLER) {
            if (event.getClickedInventory() != null
                    && event.getClickedInventory().equals(event.getView().getTopInventory())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        mainData = DS.load();

        if (mainData.playersGroups == null) {
            System.out.println("[Index] PlayersGroup is NULL");
        }

        if (mainData.playerConfigs == null) {
            System.out.println("[Index] PlayersConfig is NULL");
        }

        Player player = event.getPlayer();
        PlayerProvider.setup(player.getUniqueId());
        PlayersGroup grp = Lib.FindPlayerInGroup(player.getName());
        player.removePotionEffect(PotionEffectType.GLOWING);

        PlayerConfigData pcd = DS.get("player", player.getUniqueId().toString(), PlayerConfigData.class);

        if(pcd == null) {
            PlayerConfig pc = new PlayerConfig(player.getUniqueId());
            mainData.playerConfigs.add(pc);
            DS.create(PlayerConfigData.create(pc), PlayerConfigData.class);
            InPlayer.message(player.getUniqueId(),
                    "<blue><bold>Se ha creado tu PlayerConfig</bold></blue>"
            );
        }

        if (grp != null) {
            grp.TargetMembers();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        ArmorStand as = nameTags.remove(event.getPlayer().getUniqueId());
        if (as != null && !as.isDead()) {
            as.remove();
        }

        PlayerConfig pc = Lib.getPlayerConfig(player);
        if (pc == null) {
            this.getLogger().warning("No se encontró configuración para el jugador " + player.getName() + " al desconectarse.");
            return;
        }

        try {
            DS.edit("id", pc.id.toString(), PlayerConfigData.create(pc), PlayerConfigData.class);
        } catch (Exception e) {
            this.getLogger().severe("Error al guardar la configuración del jugador " + player.getName());
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ArmorStand as = nameTags.get(player.getUniqueId());
        if (as != null && !as.isDead()) {
            Location to = player.getLocation().clone();
            Location newLoc = to.clone().add(0, 2.6, 0);
            as.teleport(newLoc);
            player.showEntity(this, as);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(this, () -> {
            PlayerProvider.setup(player.getUniqueId());
            PlayersGroup grp = Lib.FindPlayerInGroup(player.getName());
            player.removePotionEffect(PotionEffectType.GLOWING);

            if (grp != null) {
                grp.TargetMembers();
            }
        }, 1L);
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String message = LegacyComponentSerializer.legacyAmpersand().serialize(event.message());
        var grp = Lib.FindPlayerInGroup(player.getName());
        event.setCancelled(true);

        String output = "<white>" + player.getName() + "</white>";

        if (grp != null) {
            String color = grp.getColor().name().toLowerCase();
            output = "<gray>" + player.getName() + "</gray> [<" + color + ">" + grp.getName() + "</" + color + ">]";
        }

        Bukkit.broadcast(mm.deserialize(output + ": " + message));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        var player = event.getEntity();
        var damage = event.getFinalDamage();
        if(player instanceof Player) {
            player.sendMessage(Component.text("Paisano te cacharon, y te quitaron " + Math.floor(damage)));
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player damager)) return;
        if(!(event.getEntity() instanceof Player victim)) return;

        PlayersGroup grpDamager = Lib.FindPlayerInGroup(damager.getName());
        PlayersGroup grpVictim = Lib.FindPlayerInGroup(victim.getName());

        if(grpVictim == null || grpDamager == null) return;
        if(!grpVictim.getName().equalsIgnoreCase(grpDamager.getName())) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onDeathEvent(EntityDeathEvent event) {
        if(!(event.getEntity() instanceof Player player)) return;

        PlayerConfig pc = Lib.getPlayerConfig(player);
        if(pc == null) return;

        Location loc = player.getLocation();
        CheckPoint cp = new CheckPoint(player, loc, "return");

        pc.saveLastDeath(cp);
    }

    @EventHandler
    public void onExpPickup(PlayerExpChangeEvent event) {
        Player player = event.getPlayer();
        int amount = event.getAmount();
        event.setAmount(0);

        player.sendMessage(mm.deserialize(
                "<gold>Se retuvo tu experiencia: <white>" + amount + "</white></gold>"
        ));

        PlayersGroup grp = Lib.FindPlayerInGroup(player.getName());
        if (grp == null) {
            player.giveExp(amount);
            player.sendMessage(mm.deserialize(
                    "<red>No perteneces a ningún grupo. Recuperaste toda tu experiencia.</red>"
            ));
            return;
        }

        List<Player> closest = Lib.ClosestMembers(grp, player, 30.0);

        Set<Player> recipients = new LinkedHashSet<>(closest);
        recipients.add(player);

        if (recipients.size() <= 1) {
            player.giveExp(amount);
            player.sendMessage(mm.deserialize(
                    "<yellow>No hay miembros cercanos. Recuperaste toda tu experiencia.</yellow>"
            ));
            return;
        }

        int count = recipients.size();
        float perPlayer = (float) amount / count;

        player.sendMessage(mm.deserialize(
                "<green>Distribuyendo <white>" + amount + "</white> exp entre <white>" + count + "</white> miembros.</green>"
        ));

        for (Player m : recipients) {
            int toGive = Math.round(perPlayer);
            m.giveExp(toGive);
            m.sendMessage(mm.deserialize(
                    "<aqua>Has recibido <white>" + toGive + "</white> exp compartida.</aqua>"
            ));
        }
    }
}