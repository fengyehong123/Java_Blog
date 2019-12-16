package blog.web;

import blog.pojo.Blog;
import blog.service.BlogService;
import blog.service.TagService;
import blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(
            // 指定默认的分页方式
            @PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ){
        Page<Blog> listBlog = blogService.listBlog(pageable);

        toIndex(model,listBlog);

        return "index";
    }

    // 访问文章的详情页面
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model){

        model.addAttribute("blog",blogService.getMarkDownToHtml(id));
        return "blog";
    }

    // 点击搜索按钮进行搜索
    @PostMapping("/search")
    public String search(
            @PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            @RequestParam String query
    ){
        // 因为要要进行模糊匹配查询,因此用%连接要模糊查询的关键词
        model.addAttribute("page", blogService.listBlog("%"+query+"%", pageable));

        // 把查询的字符串返回到页面上去
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/search")
    public String search(
            @PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable
            ,Model model
    ){
        Page<Blog> listBlog = blogService.listBlog(pageable);

        toIndex(model,listBlog);

        return "index";
    }

    @GetMapping("/footer/newblog")
    public String newBlogList(Model model){

        // 获取3条最新的博客放置在页面的底部
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newBlogList";
    }

    // 抽取出的访问首页的方法
    public void toIndex(Model model,Page<Blog> listBlog) {

        // 博客文章的数据,显示在首页
        model.addAttribute("page",listBlog);
        // 博客的分类列表显示在首页  暂时定义显示6条数据
        model.addAttribute("types",typeService.listTypeTop(6));
        // 博客的标签内容(显示数量最大的前10条标签)显示在首页
        model.addAttribute("tags", tagService.listTagTop(10));
        // 显示所有的标签(标签云使用)
        model.addAttribute("tagList", tagService.getAllTag());
        // 显示博客的推荐列表
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
    }

}
