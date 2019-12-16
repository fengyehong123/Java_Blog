package blog.web.admin;

import blog.pojo.CommentAndBlog;
import blog.service.CommentService;
import blog.vo.CommentSearch;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class CommentAdminController {

    private static final String commentPage = "admin/comment";
    @Autowired
    private CommentService commentService;

    // 分页显示所有的评论信息
    @GetMapping("/commentList")
    public String returnAllComment(
            @PageableDefault(size = 3,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
            Model model
            ){

        Page<CommentAndBlog> commentList =  commentService.findAllComment(pageable);

        model.addAttribute("page", commentList);

        return commentPage;
    }

    // 根据条件搜索评论信息
    @PostMapping("/searchCommentList")
    public String returnSearchObj(
            @PageableDefault(size = 3,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            CommentSearch commentSearch
    ){

        // 数据进行局部查询刷新
        Page<CommentAndBlog> commentList = commentService.findCommentByCondition(pageable,commentSearch);

        model.addAttribute("page", commentList);

        // 把数据加载到局部刷新的地方
        return "admin/comment :: CommentList";

    }

    // 根据id删除博客的评论
    @GetMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable Long id,RedirectAttributes attributes){

        // 调用方法进行删除
        commentService.deleteComment(id);

        attributes.addFlashAttribute("message","评论删除成功" );

        // 删除之后,重定向到评论信息的显示页面
        return "redirect:/admin/commentList";
    }

}
