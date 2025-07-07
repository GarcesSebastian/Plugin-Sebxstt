package io.papermc.sebxstt.instances;

import io.papermc.sebxstt.helpers.GroupPermissions;
import io.papermc.sebxstt.instances.enums.PlayerTypeGroup;
import io.papermc.sebxstt.functions.utils.InPlayer;
import io.papermc.sebxstt.providers.ConfigurationProvider;
import io.papermc.sebxstt.providers.PluginProvider;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerConfig {
    public UUID id;
    public UUID player;
    public UUID currentGroup;
    public ArrayList<RequestGroup> requestGroup = new ArrayList<>();
    public PlayerTypeGroup playerType = PlayerTypeGroup.NONE;
    public boolean ChatEnabledGroup = false;

    public ArrayList<CheckPoint> checkPoints = new ArrayList<>();
    public CheckPoint lastDeath;
    private BukkitTask taskLastDeath;

    public PlayerConfig(UUID uuid) {
        this.id = UUID.randomUUID();
        this.player = uuid;
    }

    public void showInfo() {
        String playerName = InPlayer.name(player);
        PlayersGroup gp = InPlayer.group(this.currentGroup);

        String invitedStatus = getInvited()
                ? "<green>Invitado a un grupo.</green>"
                : "<red>Sin invitación activa.</red>";

        String groupInfo = (gp != null)
                ? "<aqua>" + gp.getName() + "</aqua>"
                : "<gray>— Ninguno —</gray>";

        String groupColor = (gp != null)
                ? "<" + gp.getColor().name().toLowerCase() + ">" + gp.getColor().name().toLowerCase() + "</" + gp.getColor().name().toLowerCase() + ">"
                : "<gray>—</gray>";

        String roleText;
        switch (this.playerType) {
            case LEADER -> roleText = "<gold>Lider</gold>";
            case OFFICER -> roleText = "<blue>Oficial</blue>";
            case MEMBER -> roleText = "<gray>Miembro</gray>";
            case GUEST -> roleText = "<dark_red>Casual</dark_red>";
            case NONE -> roleText = "<dark_red>Ninguno</dark_red>";
            default -> roleText = "<dark_gray>Desconocido</dark_gray>";
        }

        InPlayer.message(this.player,
                "<gradient:#00ff99:#0099ff><bold>===== ESTADÍSTICAS DEL JUGADOR =====</bold></gradient>\n" +
                        "<gold>Jugador:</gold> <white>" + playerName + "</white>\n" +
                        "<gold>Estado de invitación:</gold> " + invitedStatus + "\n" +
                        "<gold>Grupo asignado:</gold> " + groupInfo + "\n" +
                        "<gold>Cargo en el grupo:</gold> " + roleText + "\n" +
                        "<gold>Color del grupo:</gold> " + groupColor + "\n" +
                        "<gradient:#00ff99:#0099ff><bold>====================================</bold></gradient>"
                );
    }

    public void invitate(RequestGroup requestGroup) {
        this.requestGroup.add(requestGroup);
    }

    public void saveLastDeath(CheckPoint lastDeathCP) {
        if (getLastDeath() != null) {
            InPlayer.message(this.player,
                    "<yellow><bold>¡Aviso!</bold></yellow> Se ha reemplazado tu <gray>último punto de muerte</gray>."
            );
        }

        setLastDeath(lastDeathCP);

        int cooldownMinutes = ConfigurationProvider.CooldownLastDeathCheckPoint;

        InPlayer.message(this.player,
                "<green><bold>Nuevo punto de muerte guardado correctamente.</bold></green>\n" +
                        "<gray>Este punto expirará en <yellow>" + cooldownMinutes + " minuto(s)</yellow>.</gray>"
        );

        if (taskLastDeath != null) {
            taskLastDeath.cancel();
            taskLastDeath = null;
        }

        this.taskLastDeath = new BukkitRunnable() {
            public void run() {
                if (getLastDeath() == null) return;

                setLastDeath(null);
                InPlayer.message(player,
                        "<gray>Tu punto de muerte ha expirado. Ya no puedes regresar.</gray>"
                );

                taskLastDeath = null;
            }
        }.runTaskLater(PluginProvider.get(), cooldownMinutes * 60L * 20L);
    }

    public void useLastDeath() {
        if (getLastDeath() == null) {
            InPlayer.message(player,"<red><bold>No se encontró un punto de retorno.</bold></red>");
            return;
        }

        lastDeath.teleport();
        setLastDeath(null);
        InPlayer.message(player,"<gradient:#00ffd5:#007cf0><bold>Teletransportado a tu último punto de muerte.</bold></gradient>");
    }

    public void saveCheckPoint(String name) {
        if (!(InPlayer.instance(player) instanceof Player p)) return;
        if (this.checkPoints.size() >= ConfigurationProvider.MaxCheckPoints) {
            InPlayer.message(player,
                    "<red><bold>No se pudo guardar el checkpoint.</bold></red>\n" +
                            "<gray>Has alcanzado el máximo de checkpoints permitidos (" + ConfigurationProvider.MaxCheckPoints + ").</gray>\n" +
                            "<yellow>Elimina uno con:</yellow> <white>/checkpoint delete <nombre></white>"
            );
            return;
        }

        if (this.checkPoints.stream().anyMatch(checkPoint -> checkPoint.getName().equalsIgnoreCase(name))) {
            InPlayer.message(player,
                    "<red><bold>No se pudo guardar el checkpoint.</bold></red>\n" +
                            "<gray>Ya existe un checkpoint con ese nombre.</gray>"
            );
            return;
        }

        Location defaultLocation = p.getLocation();
        CheckPoint newCheckPoint = new CheckPoint(p, defaultLocation, name);
        this.checkPoints.add(newCheckPoint);

        InPlayer.message(player,
                "<green>Checkpoint guardado exitosamente.</green>\n" +
                        "<yellow>Nombre:</yellow> <white><bold>" + name + "</bold></white>\n" +
                        "<gray>Para volver a este punto, usa:</gray> <gold><italic>/checkpoint teleport " + name + "</italic></gold>"
        );
    }

    public void deleteCheckPoint(String name) {
        CheckPoint cp = this.checkPoints.stream().filter(checkPoint -> checkPoint.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        if (cp == null) {
            InPlayer.message(player,
                    "<red>El checkpoint <white><bold>" + name + "</bold></white> no existe o ya fue eliminado.</red>\n" +
                            "<gray>Verifica el nombre con <gold><italic>/checkpoint delete <tab></italic></gold></gray>"
            );
            return;
        }

        this.checkPoints.remove(cp);
        InPlayer.message(player,
                "<green>El checkpoint <bold>" + name + "</bold> ha sido eliminado correctamente.</green>"
        );
    }

    public void checkPoint(String name) {
        CheckPoint cp = this.checkPoints.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);

        if (cp == null) {
            InPlayer.message(player,
                    "<red>No se encontró un checkpoint con el nombre <white><bold>" + name + "</bold></white>.</red>\n" +
                            "<gray>Verifica el nombre con <gold><italic>/checkpoint teleport <tab></italic></gold> para ver sugerencias.</gray>"
            );
            return;
        }

        cp.teleport();
        InPlayer.message(player,
                "<green>Te has teleportado correctamente al checkpoint <white><bold>" + name + "</bold></white>.</green>"
        );
    }

    public void setCurrentGroup(UUID currentGroup) {
        this.currentGroup = currentGroup;
    }

    public void setRequestGroup(ArrayList<RequestGroup> requestGroup) {
        this.requestGroup = requestGroup;
    }

    public void setPlayerType(PlayerTypeGroup playerType) {
        if (!(InPlayer.instance(player) instanceof Player p)) return;
        this.playerType = playerType;

        if (!(InPlayer.group(currentGroup) instanceof PlayersGroup gp)) return;

        if (this.currentGroup != null) {
            gp.getStorage().setupContents();
        }

        if (!GroupPermissions.canOpenStorage(playerType)) {
            StorageTeam storage = gp.getStorage();
            Inventory openInventory = p.getOpenInventory().getTopInventory();

            if (openInventory.equals(storage.instance)) {
                p.closeInventory();
            }
        }
    }

    public void setLastDeath(CheckPoint lastDeath) {
        this.lastDeath = lastDeath;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setChatEnabledGroup(boolean chatEnabledGroup) {
        ChatEnabledGroup = chatEnabledGroup;
    }

    public boolean getInvited() {
        return !this.requestGroup.isEmpty();
    }

    public UUID getCurrentGroup() {
        return currentGroup;
    }

    public ArrayList<RequestGroup> getRequestGroup() {
        return requestGroup;
    }

    public PlayerTypeGroup getPlayerType() {
        return playerType;
    }

    public ArrayList<CheckPoint> getCheckPoints() {
        return checkPoints;
    }

    public CheckPoint getLastDeath() {
        return lastDeath;
    }

    public UUID getId() {
        return id;
    }

    public boolean getChatEnabledGroup() {
        return this.ChatEnabledGroup;
    }
}
