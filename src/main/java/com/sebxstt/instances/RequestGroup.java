package com.sebxstt.instances;

import com.sebxstt.instances.enums.PlayerTypeGroup;

import java.util.UUID;

public class RequestGroup {
    public UUID instance;
    public UUID group;
    public PlayerTypeGroup post;
    public UUID invitator;

    public RequestGroup(UUID instance, UUID invitator, PlayersGroup group, PlayerTypeGroup post) {
        this.instance = instance;
        this.invitator = invitator;
        this.group = group.id;
        this.post = post;
    }

    public UUID getGroup() {
        return group;
    }

    public UUID getInstance() {
        return instance;
    }

    public PlayerTypeGroup getPost() {
        return post;
    }

    public void setGroup(UUID group) {
        this.group = group;
    }

    public void setInstance(UUID instance) {
        this.instance = instance;
    }

    public void setPost(PlayerTypeGroup post) {
        this.post = post;
    }
}
