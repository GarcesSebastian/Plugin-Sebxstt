package com.sebxstt.serialize.data;

import com.sebxstt.instances.enums.PlayerTypeGroup;
import com.sebxstt.functions.utils.InPlayer;
import com.sebxstt.instances.PlayersGroup;
import com.sebxstt.instances.RequestGroup;

import java.util.UUID;

public class RequestGroupData {
    public UUID instance;
    public UUID group;
    public String post;
    public UUID invitator;

    private RequestGroupData(RequestGroup rg) {
        if (rg == null || rg.instance == null || rg.invitator == null) return;

        this.instance = rg.instance;
        this.invitator = rg.invitator;

        if (rg.getGroup() != null) {
            this.group = rg.getGroup();
        }

        if (rg.getPost() != null) {
            this.post = rg.getPost().name().toLowerCase();
        }
    }

    public static RequestGroupData create(RequestGroup rg) {
        return rg != null ? new RequestGroupData(rg) : null;
    }

    public RequestGroup resolve() {
        if (instance == null || invitator == null || group == null || post == null) return null;

        PlayersGroup pg = InPlayer.group(group);
        if (pg == null) return null;

        PlayerTypeGroup type;
        try {
            type = PlayerTypeGroup.valueOf(post.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }

        return new RequestGroup(instance, invitator, pg, type);
    }
}