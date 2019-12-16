package blog.vo;

// 封装查询评论的条件对象
public class CommentSearch {

    private String nickname;
    private String content;

    public CommentSearch() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommentSearch{" +
                "nickname='" + nickname + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
