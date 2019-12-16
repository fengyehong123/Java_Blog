package blog.pojo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity  // 标记为实体类
@Table(name = "t_blog")  // 指定映射的数据库的名称
public class Blog {

    @Id
    @GeneratedValue  // 主键自动生成策略
    private Long id;
    private String title;  // 文章的标题
    // 保存文章需要的是longtext类型,不能是String类型
    @Basic(fetch = FetchType.LAZY)  // 懒加载的方式,只要用到的时候才会获取
    @Lob
    private String content;  // 文章的内容
    private String firstPicture;  // 文章的首图
    private String flag;  // 原创转载翻译
    private Integer views;  // 评论
    private boolean appreciation;  // 赞赏
    private boolean shareStatement;  // 转载数声明
    private boolean commentAble;  // 是否允许评论
    private boolean published;  // 是发布还是保存草稿
    private boolean recommend;  // 是否推荐
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;  // 创建时间,时间戳类型的数据,保存到数据库中,时间和日期都存在
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;  // 更新时间
    @Transient
    private String tagIds;

    // 博客的描述(首页展示的时候回用到)
    private String description;

    // 构建表和表之间的关系
    @ManyToOne  // 分类和博客的关系是 多对一
    private Type type;  // blog主动来维护和type之间的关系,type是用来被维护的
    @ManyToMany(cascade = {CascadeType.PERSIST})  // 级联新增,当保存一个博客的时候,与博客关联的tag也会被保存到数据库
    private List<Tag> tagList = new ArrayList<>();
    @ManyToOne  // 博客和用户是多对一的关系
    private User user;
    @OneToMany(mappedBy = "blog")  // 博客和评论是一对多的关系
    private List<Comment> commentList = new ArrayList<>();

    public Blog() {
    }

    // 处理数组,转换为字符串
    public void init(){
        this.tagIds = tagsToIds(this.getTagList());
    }

    // 将数组转化为字符串  1,2,3
    private String tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public boolean isAppreciation() {
        return appreciation;
    }

    public void setAppreciation(boolean appreciation) {
        this.appreciation = appreciation;
    }

    public boolean isShareStatement() {
        return shareStatement;
    }

    public void setShareStatement(boolean shareStatement) {
        this.shareStatement = shareStatement;
    }

    public boolean isCommentAble() {
        return commentAble;
    }

    public void setCommentAble(boolean commentAble) {
        this.commentAble = commentAble;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", firstPicture='" + firstPicture + '\'' +
                ", flag='" + flag + '\'' +
                ", views=" + views +
                ", appreciation=" + appreciation +
                ", shareStatement=" + shareStatement +
                ", commentAble=" + commentAble +
                ", published=" + published +
                ", recommend=" + recommend +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", tagIds='" + tagIds + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", tagList=" + tagList +
                ", user=" + user +
                ", commentList=" + commentList +
                '}';
    }
}
