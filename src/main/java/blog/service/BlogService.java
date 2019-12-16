package blog.service;

import blog.pojo.Blog;
import blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BlogService {

    // 根据id查询blog
    Blog getBlog(Long id);

    // 分页条件查询Blog 查询条件封装成为Blog对象
    Page<Blog> listBlog(Pageable pageable,BlogQuery blog);

    // 新增Blog
    Blog saveBlog(Blog blog);

    // 更新Blog
    Blog updateBlog(Long id,Blog blog);

    // 删除博客
    void deleteBlog(Long id);

    // 不需要条件,分页查询blog
    Page<Blog> listBlog(Pageable pageable);

    // 获取推荐博客的列表
    List<Blog> listRecommendBlogTop(Integer size);

    // 根据条件进行分页查询
    Page<Blog> listBlog(String query,Pageable pageable);

    // 把markdown格式的文章转换为html格式的文章
    Blog getMarkDownToHtml(Long id);

    // 根据标签id来查询id分类下面的博客
    Page<Blog> listBlog(Long tagId,Pageable pageable);

    // 对所有的博客按照年份来进行归档
    Map<String,List<Blog>> archiveBlog();

    // 获取博客的总的条数
    Long countBlog();


}
