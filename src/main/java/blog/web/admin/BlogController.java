package blog.web.admin;

import blog.pojo.Blog;
import blog.pojo.User;
import blog.service.BlogService;
import blog.service.TagService;
import blog.service.TypeService;
import blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(
            // 指定默认的分页方式
            @PageableDefault(size = 10,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
            BlogQuery blog,  // 自定义的前端的封装类
            Model model
    ){
        // 分类下拉列表信息
        model.addAttribute("types",typeService.listAllType());

        // 文章信息
        model.addAttribute("page",blogService.listBlog(pageable, blog));

        return LIST;
    }

    // 局部查询
    @PostMapping("/blogs/search")
    public String search(
            // 指定默认的分页方式
            @PageableDefault(size = 10,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
            BlogQuery blog,
            Model model
    ){

        model.addAttribute("page",blogService.listBlog(pageable, blog));
        // 定义一个片段,实现局部的渲染
        return "admin/blogs :: blogList";
    }

    // 跳转到博客发布的表单页面
    @GetMapping("/blogs/input")
    public String blogInput(Model model){

        // 设置博客的标签和分类
        setTypeAndTag(model);

        model.addAttribute("blog",new Blog());
        return INPUT;
    }

    public void setTypeAndTag(Model model){
        // 初始化标签
        model.addAttribute("tags", tagService.getAllTag());
        // 初始化分类
        model.addAttribute("types",typeService.listAllType());
    }

    // 新增加文章和修改文章公用一个方法
    @PostMapping("/blogs")
    public String addAndUpdate(Blog blog, HttpSession session, RedirectAttributes attributes){

        // 通过session获取当前登录的用户(明确这个文章是哪个用户发表的)
        User user = (User)session.getAttribute("user");
        blog.setUser(user);

        // 前端提交的数据中,有文章的分类(一个)和文章的标签(多个),保存到blog对象中
        blog.setType(typeService.getType(blog.getType().getId()));

        // 封装blog的标签(数组)
        blog.setTagList(tagService.listTag(blog.getTagIds()));

        Blog b;
        if (blog.getId() == null) {
            // 我们可以在Blog的实体类中添加响应的校验注解来判断前端提交的数据是否为空
            // 因为就只有我们自己在管理,所以不校验也是可以的
            b =  blogService.saveBlog(blog); // 封装文章的分类
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }


        if (b == null ) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }

        // 返回到列表页面
        return REDIRECT_LIST;
    }

    // 博客的编辑修改页面
    @GetMapping("/blogs/{id}/input")
    public String blogEdit(@PathVariable Long id, Model model){

        setTypeAndTag(model);

        // 根据id查询获取要修改的博客对象
        Blog blog = blogService.getBlog(id);
        blog.init();  // 将TagList处理成一个字符串

        // 把要修改的博客对象传入到前端
        model.addAttribute("blog",blog);
        return INPUT;
    }

    // 删除博客(删除博客的同时,把博客下面的评论同步删除)
    @GetMapping("/blogs/{id}/delete")
    public String deleteBlog(@PathVariable Long id,RedirectAttributes attributes){

        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "博客删除成功");

        return REDIRECT_LIST;
    }
}
