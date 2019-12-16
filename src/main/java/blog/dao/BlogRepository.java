package blog.dao;

import blog.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long>,JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommend = true ")
    List<Blog> findTop(Pageable pageable);

    // 根据条件进行博客查询  String query 是要查询的参数
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query,Pageable pageable);

    // 更新博客文章的浏览次数
    @Transactional
    @Modifying  // 标记要修改,而不是查询
    @Query("update Blog b set b.views = b.views + 1 where b.id = ?1")
    int updateBlogViews(Long id);

    // 对所有博客按照年份来进行归档
    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y') order by year desc ")
    List<String> findGroupYear();

    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1")
    List<Blog> findByYear(String year);

    @Query(value = "select count(id) from t_blog where type_id = ?1" ,nativeQuery = true)
    Long findBlogByTypeId(Long typeId);

    @Query(value = "select count(blog_list_id) from t_blog_tag_list where tag_list_id = ?1" ,nativeQuery = true)
    Long findBlogByTagId(Long tag_list_id);

}
