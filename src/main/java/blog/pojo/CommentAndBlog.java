package blog.pojo;

public class CommentAndBlog extends Comment {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "CommentAndBlog{" +
                "title='" + title + '\'' +
                "} " + super.toString();
    }
}
