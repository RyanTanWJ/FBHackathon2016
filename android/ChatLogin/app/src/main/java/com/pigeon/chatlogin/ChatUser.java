package com.pigeon.chatlogin;

import java.io.Serializable;

/**
 * Created by dumi on 3/9/2016.
 */
public class ChatUser implements Serializable {
    private String id;
    /*
    type: fb - facebook, gg - google
     */
    private String type;

    private String name;

    private String profilePictureUrl;

    public ChatUser(String id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePictureUrl() {
        return this.profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
