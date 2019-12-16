package blog.web;

import blog.pojo.Comment;
import blog.pojo.User;
import blog.service.BlogService;
import blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;
    // 从配置文件中获取评论用户的头像的路径
    @Value("${comment.avatar}")
    private String avatar;

    // 查询某一篇博客下面的评论列表
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){

        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));

        // 返回到评论页面下的片段
        return "blog :: commentList";
    }

    // 提交对博客的评论
    @PostMapping("/comments")
    public String commentBlog(Comment comment, HttpSession session){

        // 根据评论对象获取被评论的博客的id
        Long blogId = comment.getBlog().getId();
        // 把blog对象保存到comment对象中
        comment.setBlog(blogService.getBlog(blogId));

        // 通过session获取user的信息
        User user = (User) session.getAttribute("user");

        // 如果登录状态的管理员的话
        if (user != null){
            // 评论信息设置为管理员的头像
            comment.setAvatar(user.getAvatar());
            // 明确这条评论由管理员发出的
            comment.setAdminComment(true);
            // 管理员名称可以在前端来获取
            // comment.setNickname(user.getNickname());
        } else {
            // 从配置文件中获取评论头像的地址
            comment.setAvatar(avatar);
        }

        // 保存评论对象进入数据库
        commentService.saveComment(comment);

        return "redirect:/comments/" + blogId;
    }
}
