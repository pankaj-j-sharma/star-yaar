package com.incampusit.staryaar.Notifications;

/*
 * Created by PANKAJ on 2/25/2019.
 */

public class Notification_Get_Set {

    String id, title, videoId, senderFbId, receiverFbId, actionType, message, icon, otherData, createdOn, readOn;
    Boolean isRead;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOtherData() {
        return otherData;
    }

    public void setOtherData(String otherData) {
        this.otherData = otherData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getSenderFbId() {
        return senderFbId;
    }

    public void setSenderFbId(String senderFbId) {
        this.senderFbId = senderFbId;
    }

    public String getReceiverFbId() {
        return receiverFbId;
    }

    public void setReceiverFbId(String receiverFbId) {
        this.receiverFbId = receiverFbId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getReadOn() {
        return readOn;
    }

    public void setReadOn(String readOn) {
        this.readOn = readOn;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }
}
