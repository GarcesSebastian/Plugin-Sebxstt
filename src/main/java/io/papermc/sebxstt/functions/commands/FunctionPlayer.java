package io.papermc.sebxstt.functions.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.sebxstt.functions.utils.InPlayer;
import io.papermc.sebxstt.functions.utils.Lib;
import io.papermc.sebxstt.instances.PlayerConfig;
import io.papermc.sebxstt.instances.PlayersGroup;
import io.papermc.sebxstt.instances.RequestGroup;
import io.papermc.sebxstt.instances.http.FetchProfile;
import io.papermc.sebxstt.instances.http.FetchTextures;
import io.papermc.sebxstt.instances.ingame.inventory.NextInventory;
import io.papermc.sebxstt.instances.ingame.inventory.NextInventoryProvider;
import io.papermc.sebxstt.managers.HttpManager;
import io.papermc.sebxstt.providers.PluginProvider;
import io.papermc.sebxstt.serialize.data.PlayerConfigData;
import io.papermc.sebxstt.serialize.data.PlayerGroupData;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static io.papermc.sebxstt.index.*;
import static io.papermc.sebxstt.providers.DataStoreProvider.DS;

public class FunctionPlayer {
    public static void test(CommandContext<CommandSourceStack> ctx, String target) {
        CommandSender senderRaw = ctx.getSource().getSender();
        if(!(senderRaw instanceof Player p)) return;
        OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(target);
        NextGUI.open(offPlayer.getUniqueId());
    }

    public static void test2(CommandContext<CommandSourceStack> ctx, String target) {
        Player plr = Bukkit.getPlayerExact(target);
        assert plr != null;

        NextInventory nextInventory = NextInventoryProvider.nextInventoryList.stream()
                .filter(iv -> iv.getPlayers().contains(plr.getUniqueId()))
                .findFirst().orElse(null);

        if (nextInventory == null) throw new IllegalStateException("Not found nextInventory by id player " + plr.getUniqueId());

        nextInventory.getItems().forEach(item -> {
            item.setName(UUID.randomUUID().toString().substring(0, 8));
            item.draggable(true);
        });
    }

    public static void npc(CommandContext<CommandSourceStack> ctx, String name) throws Exception {
        if (!(ctx.getSource().getSender() instanceof Player viewer)) return;
        CraftPlayer craftViewer = (CraftPlayer) viewer;
        ServerPlayer viewerHandle = craftViewer.getHandle();

        // Skin
        FetchProfile prof = HttpManager.getProfile(name);
        FetchTextures tex = HttpManager.getTextures(prof.id);
        GameProfile gp = new GameProfile(UUID.randomUUID(), name);
        gp.getProperties().put("textures", new Property("textures", tex.texture, tex.signature));

//        MinecraftServer server = viewerHandle.getServer();
//        ServerLevel level = viewerHandle.serverLevel();
//
//        // Crear NPC
//        ServerPlayer npc = new ServerPlayer(server, level, gp, ClientInformation.createDefault());
//        server.getPlayerList().placeNewPlayer(dummyConn, npc, cookie);
//        world.addNewPlayer(npc);
//
//        npc.setPos(viewer.getX(), viewer.getY(), viewer.getZ());
//
//        // Registrar NPC en la lista interna
//        Connection dummyConn = new Connection(null);
//        CommonListenerCookie cookie = CommonListenerCookie.createInitial(gp, true);
//        server.getPlayerList().placeNewPlayer(dummyConn, npc, cookie);
//
//        // Crear wrappers
//        ServerEntity wrapper = new ServerEntity(level, npc, 0, false, buf -> {}, Set.of());
//
//        // Envío únicamente al jugador que ejecuta el comando
//        ServerGamePacketListenerImpl conn = viewerHandle.connection;
//        conn.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
//        conn.send(new ClientboundAddEntityPacket(npc, wrapper));
//        conn.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.REMOVE_PLAYER, npc));
    }

    public static void ClearTeams(CommandContext<CommandSourceStack> ctx) {
        var senderRaw = ctx.getSource().getSender();
        if (!(senderRaw instanceof Player p)) return;

        ArrayList<PlayersGroup> copy = new ArrayList<>(mainData.playersGroups);
        for (PlayersGroup group : copy) {
            group.dissolve();
        }

        Lib.clearOrphanNameTags();

        Scoreboard main = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Team team : new ArrayList<>(main.getTeams())) {
            team.unregister();
        }

        p.sendMessage(mm.deserialize("<green><bold>Todos los equipos han sido eliminados correctamente.</bold></green>"));
    }


    public static void ReturnPlayer(CommandContext<CommandSourceStack> ctx) {
        var sender = ctx.getSource().getSender();
        if (!(sender instanceof Player p)) return;

        PlayerConfig pc = Lib.getPlayerConfig(p);
        if (pc == null) return;

        pc.useLastDeath();
        DS.edit("id", pc.id.toString(), PlayerConfigData.create(pc), PlayerConfigData.class);
    }

    public static void PreviewPlayer(CommandContext<CommandSourceStack> ctx) {
        var senderRaw = ctx.getSource().getSender();
        if (!(senderRaw instanceof Player p)) return;

        PlayerConfig pc = Lib.getPlayerConfig(p);

        if (pc == null) return;

        pc.showInfo();
    }

    public static void RequestPlayer(CommandContext<CommandSourceStack> ctx, String name, String option) {
        var senderRaw = ctx.getSource().getSender();
        if (!(senderRaw instanceof Player p)) return;

        boolean isMatch = Arrays.stream(PluginProvider.optionsInvitations).anyMatch(op -> op.equalsIgnoreCase(option));
        if (!isMatch) {
            p.sendMessage(mm.deserialize(
                    "<red>Opción inválida.</red>\n" +
                            "<gray>Usa: </gray><gold>/group request <nombre> aceptar|rechazar</gold>"
            ));
            return;
        }

        PlayersGroup grp = Lib.FindPlayerInGroup(p.getName());
        if (grp != null) {
            p.sendMessage(mm.deserialize(
                    "<red><bold>Ya perteneces a un grupo activo.</bold></red>\n" +
                            "<gray>Si deseas unirte a otro grupo, primero debes salir con:</gray> <gold>/group leave</gold>"
            ));
            return;
        }

        PlayerConfig pc = Lib.getPlayerConfig(p);
        if (pc == null) return;

        RequestGroup requestGroupMatch = pc.getRequestGroup().stream()
                .filter(req -> InPlayer.group(req.getGroup()).getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if (requestGroupMatch == null) {
            p.sendMessage(mm.deserialize(
                    "<red>No se encontró una invitación del grupo <white><bold>" + name + "</bold></white>.</red>\n" +
                            "<gray>Verifica que el nombre esté escrito correctamente.</gray>"
            ));
            return;
        }

        if (!pc.getInvited()) {
            p.sendMessage(mm.deserialize(
                    "<yellow>No tienes invitaciones activas.</yellow>\n" +
                            "<gray>Revisa con <gold>/group list</gold> para ver las disponibles.</gray>"
            ));
            return;
        }

        boolean confirm = option.equalsIgnoreCase("aceptar");
        if (!confirm) {
            pc.getRequestGroup().remove(requestGroupMatch);

            InPlayer.message(requestGroupMatch.invitator,
                    "<red><bold>Invitación rechazada.</bold></red>\n" +
                            "<gray>El jugador <aqua>" + p.getName() + "</aqua> ha rechazado la invitación al grupo <white>" + InPlayer.group(requestGroupMatch.getGroup()).getName() + "</white>.</gray>"
            );

            p.sendMessage(mm.deserialize(
                    "<red><bold>Invitación rechazada.</bold></red>\n" +
                            "<gray>Has rechazado la invitación al grupo <aqua>" + InPlayer.group(requestGroupMatch.getGroup()).getName() + "</aqua>.</gray>"
            ));
            return;
        }

        PlayersGroup groupInvited = InPlayer.group(requestGroupMatch.getGroup());
        groupInvited.addMember(p, requestGroupMatch.post);

        groupInvited.getPlayers().forEach(pl -> {
            pl.sendMessage(mm.deserialize(
                    "<gradient:#00ff99:#0099ff><bold>" + p.getName() + " se ha unido al grupo <white>" + groupInvited.getName() + "</white>!</bold></gradient>"
            ));
        });

        p.sendMessage(mm.deserialize(
                "<green><bold>Has aceptado la invitación al grupo <white>" + groupInvited.getName() + "</white>.</bold></green>\n" +
                        "<gray>¡Ya puedes colaborar con tus compañeros!</gray>"
        ));

        groupInvited.pending.removeIf(pre -> InPlayer.name(pre.player).equalsIgnoreCase(p.getName()));
        pc.getRequestGroup().remove(requestGroupMatch);

        DS.edit("id", pc.id.toString(), PlayerConfigData.create(pc), PlayerConfigData.class);
        DS.edit("id", groupInvited.id.toString(), PlayerGroupData.create(groupInvited), PlayerGroupData.class);
    }
}
