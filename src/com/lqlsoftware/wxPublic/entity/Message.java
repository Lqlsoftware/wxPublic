package com.lqlsoftware.wxPublic.entity;

/**
 * Created by robinlu on 2017/6/6.
 */
public class Message {

    private String ToUserName;

    private String FromUserName;

    private Long CreateTime;

    private String  MsgType;

    private String Content;

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.FromUserName = fromUserName;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        this.ToUserName = toUserName;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public Long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Long createTime) {
        this.CreateTime = createTime;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }
}
