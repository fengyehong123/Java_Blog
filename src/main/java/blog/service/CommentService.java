package blog.service;

import blog.pojo.Comment;
import blog.pojo.CommentAndBlog;
import blog.vo.CommentSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CommentService {

    // 获取评论的列表
    List<Comment> listCommentByBlogId(Long id);

    // 保存一条评论
    Comment saveComment(Comment comment);

    // 查询出所有的评论(附带文章的标题名)
    Page<CommentAndBlog> findAllComment(Pageable pageable);

    // 根据条件进行局部查询
    Page<CommentAndBlog> findCommentByCondition(Pageable pageable, CommentSearch commentSearch);

    // 根据id来删除评论
    void deleteComment(Long id);
}
