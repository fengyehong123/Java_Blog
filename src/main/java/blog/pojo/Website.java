package blog.pojo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_website")
public class Website {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer cateId;
    private String name;
    private String website;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @ManyToOne
    private WebCategory webCategory;

    public Website() {
    }

    public WebCategory getWebCategory() {
        return webCategory;
    }

    public void setWebCategory(WebCategory webCategory) {
        this.webCategory = webCategory;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Website{" +
                "id=" + id +
                ", cateId=" + cateId +
                ", name='" + name + '\'' +
                ", website='" + website + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
