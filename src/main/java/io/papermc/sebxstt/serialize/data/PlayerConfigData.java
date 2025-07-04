package io.papermc.sebxstt.serialize.data;

import io.papermc.sebxstt.enums.PlayerTypeGroup;
import io.papermc.sebxstt.instances.CheckPoint;
import io.papermc.sebxstt.instances.PlayerConfig;
import io.papermc.sebxstt.instances.RequestGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerConfigData {
    public UUID id;
    public UUID player;
    public String post;
    public UUID group;
    public List<RequestGroupData> requestGroup = new ArrayList<>();

    public ArrayList<CheckPointData> checkpoints = new ArrayList<>();
    public CheckPointData lastDeath;

    public PlayerConfigData() {}

    private PlayerConfigData(PlayerConfig playerConfig) {
        if (playerConfig == null || playerConfig.player == null) return;

        this.id = playerConfig.id;
        this.player = playerConfig.player;

        if (playerConfig.getPlayerType() != null) {
            this.post = playerConfig.getPlayerType().toString().toLowerCase();
        }

        if (playerConfig.getCurrentGroup() != null) {
            this.group = playerConfig.getCurrentGroup();
        }

        if (playerConfig.checkPoints != null) {
            for (CheckPoint cp : playerConfig.checkPoints) {
                CheckPointData cpd = CheckPointData.create(cp);
                if (cpd != null) this.checkpoints.add(cpd);
            }
        }

        if (playerConfig.requestGroup != null) {
            for (RequestGroup rg : playerConfig.requestGroup) {
                RequestGroupData rgd = RequestGroupData.create(rg);
                if (rgd != null) this.requestGroup.add(rgd);
            }
        }

        if (playerConfig.getLastDeath() != null) {
            this.lastDeath = CheckPointData.create(playerConfig.lastDeath);
        }
    }

    public static PlayerConfigData create(PlayerConfig playerConfig) {
        return new PlayerConfigData(playerConfig);
    }

    public PlayerConfig resolve() {
        if (player == null) return null;

        PlayerConfig pc = new PlayerConfig(player);
        pc.setId(this.id);

        try {
            if (this.post != null) {
                PlayerTypeGroup type = PlayerTypeGroup.valueOf(this.post.toUpperCase());
                pc.setPlayerType(type);
            }
        } catch (IllegalArgumentException ignored) {}

        if (group != null) {
            pc.setCurrentGroup(group);
        }

        if (checkpoints != null) {
            for (CheckPointData cp : checkpoints) {
                CheckPoint resolved = cp.resolve();
                if (resolved != null) pc.checkPoints.add(resolved);
            }
        }

        if (requestGroup != null) {
            for (RequestGroupData rg : requestGroup) {
                RequestGroup resolved = rg.resolve();
                if (resolved != null) pc.requestGroup.add(resolved);
            }
        }

        if (lastDeath != null) {
            CheckPoint resolved = lastDeath.resolve();
            if (resolved != null) pc.lastDeath = resolved;
        }

        return pc;
    }
}