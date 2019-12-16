package blog.dao;

import blog.pojo.Comment;
import blog.pojo.CommentAndBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long>,JpaSpecificationExecutor<Comment> {

    // 自定义文章评论列表的查询,可以根据创建时间进行排序
    // 查询出父评论为空的评论,也就是说查询出所有的父评论
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);

    // 根据博客的id删除跟博客相关的所有的评论
    @Query(value = "delete from t_comment where blog_id= ?1", nativeQuery = true)
    @Modifying
    void deleteByBlogId(Long id);

    // 根据父评论的id查询是否有这个对象
    @Query(value = "select id from t_comment where parent_comment_id =?1",nativeQuery = true)
    Long findObjByParentCommentId(Long parent_comment_id);

}
