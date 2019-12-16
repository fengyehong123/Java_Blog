package blog.pojo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_webCategory")
public class WebCategory {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private Integer Parent_id;
    private Integer ParentValid;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    // 网站分类和网站是一对多的关系
    // 一的这一方放弃对主键的维护
    @OneToMany(mappedBy = "webCategory")
    private List<Website> websiteList = new ArrayList<>();

    public WebCategory() {
    }

    public List<Website> getWebsiteList() {
        return websiteList;
    }

    public void setWebsiteList(List<Website> websiteList) {
        this.websiteList = websiteList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParent_id() {
        return Parent_id;
    }

    public void setParent_id(Integer parent_id) {
        Parent_id = parent_id;
    }

    public Integer getParentValid() {
        return ParentValid;
    }

    public void setParentValid(Integer parentValid) {
        ParentValid = parentValid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "WebCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Parent_id=" + Parent_id +
                ", ParentValid=" + ParentValid +
                ", updateTime=" + updateTime +
                '}';
    }
}
