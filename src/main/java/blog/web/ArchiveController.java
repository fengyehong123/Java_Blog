package blog.web;

import blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model){

        // 年份和年份对应的博客
        model.addAttribute("archiveMap", blogService.archiveBlog());
        // 博客的总条数
        model.addAttribute("blogCount", blogService.countBlog());

        return "archives";
    }
}
