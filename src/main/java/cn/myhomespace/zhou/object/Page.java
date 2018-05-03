package cn.myhomespace.zhou.object;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouw on 2018/5/3.
 */
public class Page implements Serializable {
    private int id;
    private String title;
    private String content;
    private String url;
    private String siteName;
    private Date createTime;
    private String pic;
    private String sources;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSources() {
        return sources;
    }

    public void setSource(String sources) {
        this.sources = sources;
    }
}
