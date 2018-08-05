package chen.dto;


import java.util.Date;

public class MessageDto {
    private Integer messageId;

    private Integer userId;

    private Integer messageType;

    private Integer pid;

    private String content;

    private Date pubDate;

    public MessageDto() {
    }

    public MessageDto(Integer messageId, Integer userId, Integer messageType, Integer pid, String content, Date pubDate) {
        this.messageId = messageId;
        this.userId = userId;
        this.messageType = messageType;
        this.pid = pid;
        this.content = content;
        this.pubDate = pubDate;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }
}