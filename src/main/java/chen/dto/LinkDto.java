package chen.dto;


public class LinkDto {
    private Integer linkId;

    private String linkName;

    private String url;


    public LinkDto(String linkName, String url) {
        this.linkName = linkName;
        this.url = url;
    }

    public LinkDto(Integer linkId, String linkName, String url) {
        this.linkId = linkId;
        this.linkName = linkName;
        this.url = url;
    }

    public LinkDto() {
        super();
    }

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName == null ? null : linkName.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }
}