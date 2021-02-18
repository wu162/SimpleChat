package com.clientsrc.message;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ChatMessage {
    public String content;
    public String from;
    public String to;
    public int type;
    public String fp;
    @JsonIgnore
    public int retryCount;

    public ChatMessage(String content, String from, String to, int type, String fp) {
        this.content = content;
        this.from = from;
        this.to = to;
        this.type = type;
        this.fp = fp;
    }

    public ChatMessage(String content, String from, String to, int type) {
        this.content = content;
        this.from = from;
        this.to = to;
        this.type = type;
        this.fp="";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFp() {
        return fp;
    }

    public void setFp(String fp) {
        this.fp = fp;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", type=" + type +
                ", fp='" + fp + '\'' +
                '}';
    }
}
