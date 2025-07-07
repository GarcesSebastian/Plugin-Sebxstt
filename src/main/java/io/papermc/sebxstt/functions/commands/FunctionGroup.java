package io.papermc.sebxstt.functions.commands;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.sebxstt.instances.enums.PlayerTypeGroup;
import io.papermc.sebxstt.functions.utils.InPlayer;
import io.papermc.sebxstt.functions.utils.Lib;
import io.papermc.sebxstt.instances.PlayerConfig;
import io.papermc.sebxstt.instances.PlayersGroup;
import io.papermc.sebxstt.instances.StorageTeam;
import io.papermc.sebxstt.providers.PlayerProvider;
import io.papermc.sebxstt.serialize.data.PlayerConfigData;
import io.papermc.sebxstt.serialize.data.PlayerGroupData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

import static io.papermc.sebxstt.index.mainData;
import static io.papermc.sebxstt.index.mm;
import static io.papermc.sebxstt.providers.DataStoreProvider.DS;

public class FunctionGroup {
    public static void ChangePostGroup(CommandContext<CommandSourceStack> ctx, String target, String cargo) {
        PlayerTypeGroup type;

        try {
            type = PlayerTypeGroup.valueOf(cargo.toUpperCase());
        } catch (IllegalArgumentException e) {
            ctx.getSource().getSender().sendMessage(Component.text("Cargo inválido: " + cargo));
            return;
        }

        var senderRaw = ctx.getSource().getSender();
        if (!(senderRaw instanceof Player p)) return;

        PlayerConfig senderConfig = Lib.getPlayerConfig(p);
        PlayerTypeGroup senderType = senderConfig.getPlayerType();

        var targetPlayer = Bukkit.getPlayerExact(target);
        if (targetPlayer == null) {
            p.sendMessage(mm.deserialize("<red><bold>Jugador no encontrado:</bold> " + target + "</red>"));
            return;
        }

        PlayersGroup grp = Lib.FindPlayerInGroup(p.getName());
        if (grp == null) {
            p.sendMessage(mm.deserialize("<red><bold>No tienes grupo activo.</bold> Usa <yellow>/group create <color> <nombre></yellow>"));
            return;
        }

        if (InPlayer.name(grp.getOwner()).equals(targetPlayer.getName())) {
            p.sendMessage(mm.deserialize("<red><bold>Sin permiso:</bold> No puedes cambiar el cargo del dueño.</red>"));
            return;
        }

        if (targetPlayer.getName().equalsIgnoreCase(p.getName())) {
            p.sendMessage(mm.deserialize("<red><bold>Sin permiso:</bold> No puedes cambiarte el rango.</red>"));
            return;
        }

        if (senderType != PlayerTypeGroup.CONTROLLER && senderType != PlayerTypeGroup.MANAGER) {
            p.sendMessage(mm.deserialize("<red><bold>Sin permiso:</bold> No tienes el cargo suficiente para hacer esta acción.</red>"));
            return;
        }

        PlayerConfig targetConfig = Lib.getPlayerConfig(targetPlayer);
        PlayerTypeGroup previousType = targetConfig.getPlayerType();

        targetConfig.setPlayerType(type);

        p.sendMessage(mm.deserialize(
                "<green><bold>Cargo actualizado:</bold></green> <white>" + target + "</white> ahora es <yellow>" + type.name() + "</yellow>"
        ));

        targetPlayer.sendMessage(mm.deserialize(
                "<gold><bold>Tu cargo ha sido actualizado:</bold></gold> Ahora eres <yellow>" + type.name() + "</yellow> en el grupo <white>" + grp.getName() + "</white>"
        ));

        grp.getPlayers().forEach(player -> {
            player.sendMessage(mm.deserialize(
                    "<gray>[</gray><blue>Grupo</blue><gray>]</gray> <white>" + p.getName() +
                            "</white> <green>cambió el cargo de</green> <yellow>" + target +
                            "</yellow> <green>de</green> <red>" + previousType.name() +
                            "</red> <green>a</green> <aqua>" + type.name() + "</aqua>"
            ));
        });
    }

    public static void CreateGroup(CommandContext<CommandSourceStack> ctx, String name, String colorInput) {
        var senderRaw = ctx.getSource().getSender();
        if (!(senderRaw instanceof Player p)) return;

        if (Lib.FindPlayerInGroup(p.getName()) != null) {
            p.sendMessage(mm.deserialize("<gold><bold>Ya tienes un grupo activo.</bold></gold>"));
            return;
        }
        if (name == null || name.isBlank()) {
            p.sendMessage(mm.deserialize("<red><bold>Nombre inválido.</bold></red>"));
            return;
        }

        ChatColor color;
        try {
            color = ChatColor.valueOf(colorInput.toUpperCase());
            if (!color.isColor()) throw new IllegalArgumentException();
        } catch (Exception e) {
            p.sendMessage(mm.deserialize("<red>Color inválido. Usa: /group create <color> <nombre></red>"));
            Lib.ChooseGroupColor(p);
            return;
        }

        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name) != null) {
            p.sendMessage(mm.deserialize("<red>Ya existe un team con ese nombre. Usa otro nombre.</red>"));
            return;
        }

        PlayersGroup grp = new PlayersGroup(name, p.getUniqueId(), color);
        mainData.playersGroups.add(grp);

        PlayerConfig pc = Lib.getPlayerConfig(p);
        if (pc == null) {
            p.sendMessage(mm.deserialize("<red>No se encontro PlayerConfig para: </red> " + p.getName()));
            return;
        }

        pc.setCurrentGroup(grp.getId());
        pc.setPlayerType(PlayerTypeGroup.MANAGER);

        PlayerProvider.setup(p.getUniqueId());
        DS.edit("id", pc.id.toString(), PlayerConfigData.create(pc), PlayerConfigData.class);
        DS.create(PlayerGroupData.create(grp), PlayerGroupData.class);
        p.sendMessage(mm.deserialize(
                "<green><bold>Grupo creado:</bold> <white>" + name + "</white>\n" +
                        "<gold>Color:</gold> <" + color.name().toLowerCase() + "><bold>" + color.name().toLowerCase() + "</bold></" + color.name().toLowerCase() + ">"
        ));
    }

    public static void PreviewGroup(CommandContext<CommandSourceStack> ctx) {
        var senderRaw = ctx.getSource().getSender();
        if (!(senderRaw instanceof Player p)) return;

        PlayersGroup grp = Lib.FindPlayerInGroup(p.getName());
        if (grp == null) {
            p.sendMessage(mm.deserialize(
                    "<red><bold>No perteneces a ningún grupo.</bold> Usa <yellow>/group create <color> <nombre></yellow>"
            ));
            return;
        }

        grp.showInfo(p);
    }

    public static void LeaveGroup(CommandContext<CommandSourceStack> ctx) {
        var senderRaw = ctx.getSource().getSender();
        if (!(senderRaw instanceof Player p)) return;
        PlayersGroup grp = Lib.FindPlayerInGroup(p.getName());
        if (grp == null) {
            p.sendMessage(mm.deserialize(
                    "<red><bold>No tienes un grupo activo.</bold> Usa <yellow>/group create <color> <nombre></yellow>"
            ));
            return;
        }
        if (p.getName().equals(InPlayer.name(grp.getOwner()))) {
            p.sendMessage(mm.deserialize(
                    "<red><bold>No puedes salir</bold> Eres dueño del grupo</red>\n"
                            + "<gold>Usa el comando <bold>/group disband</bold> para disolver el grupo</gold>"
            ));
            return;
        }
        UUID playerRemovedUUID = grp.getMembers().stream().filter(plr -> InPlayer.name(plr).equals(p.getName())).findFirst().orElse(null);
        if(!(InPlayer.instance(playerRemovedUUID) instanceof Player playerRemoved)) return;
        boolean removed = grp.kickMember(playerRemoved);
        if (removed) {
            p.sendMessage(mm.deserialize("<green>Has salido de <gold><bold>" + grp.getName() + "</bold></gold></green>"));
            var owner = grp.getOwner();
            InPlayer.message(owner,
                    "<yellow><italic>" + p.getName() + "</italic> ha salido de tu grupo</yellow>"
            );
        } else {
            p.sendMessage(mm.deserialize("<red><bold>Error al salir del grupo</bold></red>"));
        }
    }

    public static void InviteGroup(CommandContext<CommandSourceStack> ctx, String target, String cargo) {
        PlayerTypeGroup type;

        try {
            type = PlayerTypeGroup.valueOf(cargo.toUpperCase());
        } catch (IllegalArgumentException e) {
            ctx.getSource().getSender().sendMessage(Component.text("Cargo inválido: " + cargo));
            return;
        }

        var senderRaw = ctx.getSource().getSender();
        if (!(senderRaw instanceof Player p)) return;
        var invited = Bukkit.getPlayerExact(target);
        if (invited == null) {
            p.sendMessage(mm.deserialize("<red><bold>Jugador no encontrado:</bold></red> <white>" + target + "</white>"));
            return;
        }
        PlayersGroup grp = Lib.FindPlayerInGroup(p.getName());
        if (grp == null) {
            p.sendMessage(mm.deserialize(
                    "<red><bold>No tienes grupo activo.</bold> Usa <yellow>/group create <color> <nombre></yellow>"
            ));
            return;
        }
        if (!p.getName().equals(InPlayer.name(grp.getOwner()))) {
            p.sendMessage(mm.deserialize("<red><bold>Sin permiso</bold> Solo el dueño puede invitar</red>"));
            return;
        }
        if (grp.getMembers().stream().anyMatch(m -> InPlayer.name(m).equals(target)) || InPlayer.name(grp.getOwner()).equals(target)) {
            p.sendMessage(mm.deserialize("<yellow>" + target + " ya es miembro</yellow>"));
            return;
        }
        grp.sendInvitation(invited, p, type);
        p.sendMessage(mm.deserialize(
                "<green>Invitaste a <white><bold>" + target + "</bold></white> al grupo <gold><bold>" + grp.getName() + "</bold></gold></green>"
        ));

        invited.sendMessage(mm.deserialize(
                "<gradient:#00ff99:#0099ff><bold>¡Has recibido una invitación!</bold></gradient>\n" +
                        "<gold>Grupo:</gold> <white><bold>" + grp.getName() + "</bold></white>\n" +
                        "<gold>Invitado por:</gold> <white>" + p.getName() + "</white>\n" +
                        "<gray>Puedes ver y gestionar tus invitaciones usando:</gray> <aqua>/invitations aceptar|rechazar <tab></aqua>\n" +
                        "<gray>Ejemplo:</gray> <aqua>/invitations aceptar " + grp.getName() + "</aqua>"
        ));
    }

    public static void KickGroup(CommandContext<CommandSourceStack> ctx, String target) {
        var senderRaw = ctx.getSource().getSender();
        if (!(senderRaw instanceof Player p)) return;
        var kicked = Bukkit.getPlayerExact(target);
        if (kicked == null) {
            p.sendMessage(mm.deserialize("<red><bold>Jugador no encontrado:</bold> " + target + "</red>"));
            return;
        }
        PlayersGroup grp = Lib.FindPlayerInGroup(p.getName());
        if (grp == null) {
            p.sendMessage(mm.deserialize(
                    "<red><bold>No tienes un grupo activo.</bold> Usa <yellow>/group create <color> <nombre></yellow>"
            ));
            return;
        }
        if (!p.getName().equals(InPlayer.name(grp.getOwner()))) {
            p.sendMessage(mm.deserialize("<red><bold>Sin permiso:</bold> Solo el dueño puede sacar miembros.</red>"));
            return;
        }
        if (InPlayer.name(grp.getOwner()).equals(target)) {
            p.sendMessage(mm.deserialize("<yellow><bold>No puedes expulsar al dueño del grupo.</bold></yellow>"));
            return;
        }
        var members = grp.getMembers() != null ? grp.getMembers() : new ArrayList<UUID>();
        if (members.stream().noneMatch(m -> InPlayer.name(m).equals(target))) {
            p.sendMessage(mm.deserialize("<yellow>" + target + " no es miembro del grupo.</yellow>"));
            return;
        }
        grp.kickMember(kicked);
        p.sendMessage(mm.deserialize(
                "<green>Expulsaste a <white><bold>" + target + "</bold></white> de <gold><bold>" + grp.getName() + "</bold></gold>.</green>"
        ));
        kicked.sendMessage(mm.deserialize(
                "<red><bold>Has sido expulsado del grupo:</bold> <white>" + grp.getName() + "</white>\n" +
                        "<gold>Por:</gold> <white>" + p.getName() + "</white>"
        ));
    }

    public static void DisbandGroup(CommandContext<CommandSourceStack> ctx) {
        var sender = ctx.getSource().getSender();
        if (!(sender instanceof Player p)) return;
        PlayersGroup grp = Lib.FindPlayerInGroup(p.getName());
        if (grp == null) {
            p.sendMessage(mm.deserialize("<red>No perteneces a ningún grupo.</red>"));
            return;
        }
        if (!p.getName().equals(InPlayer.name(grp.getOwner()))) {
            p.sendMessage(mm.deserialize("<red>Solo el dueño del grupo puede disolverlo.</red>"));
            return;
        }
        grp.disolve();
        p.sendMessage(mm.deserialize("<green>Grupo <white><bold>" + grp.getName() + "</bold></white> disuelto correctamente.</green>"));
    }

    public static void ShowStorage(CommandContext<CommandSourceStack> ctx) {
        var senderRaw = ctx.getSource().getSender();
        if (!(senderRaw instanceof Player p)) return;

        PlayersGroup grp = Lib.FindPlayerInGroup(p.getName());
        if (grp == null) {
            p.sendMessage(mm.deserialize(
                    "<red><bold>No perteneces a ningún grupo.</bold> Usa <yellow>/group create <color> <nombre></yellow>"
            ));
            return;
        }

        StorageTeam storage = grp.getStorage();
        storage.open(p);
    }
}
