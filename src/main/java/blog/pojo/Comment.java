package blog.pojo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_comment")
public class Comment {

    @Id
    @GeneratedValue
    private Long id;
    private String nickname;
    private String email;
    private String content;
    private String avatar;
    @Temporal(TemporalType.TIMESTAMP)  // 指定数据库存放数据的类型
    private Date createTime;

    // 构建表和表之间的关系
    @ManyToOne
    private Blog blog;
    // 评论和评论的自关联一对多的关系
    @OneToMany(mappedBy = "parentComment")  // 父评论,一个父评论下面有好几个子评论
    private List<Comment> replyCommentList = new ArrayList<>();
    @ManyToOne  // 子评论,一个子评论下面只能归属于一个父评论
    private Comment parentComment;

    // 添加一个字段,标记是否为管理员的评论信息
    private boolean adminComment;

    public Comment() {
    }

    public boolean isAdminComment() {
        return adminComment;
    }

    public void setAdminComment(boolean adminComment) {
        this.adminComment = adminComment;
    }

    public List<Comment> getReplyCommentList() {
        return replyCommentList;
    }

    public void setReplyCommentList(List<Comment> replyCommentList) {
        this.replyCommentList = replyCommentList;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", content='" + content + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createTime=" + createTime +
                ", blog=" + blog +
                ", replyCommentList=" + replyCommentList +
                ", parentComment=" + parentComment +
                ", adminComment=" + adminComment +
                '}';
    }
}
