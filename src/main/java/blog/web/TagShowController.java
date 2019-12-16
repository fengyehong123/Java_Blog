package blog.web;

import blog.pojo.Tag;
import blog.service.BlogService;
import blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String Tags(
            @PathVariable Long id,
            @PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ){
        // 获取所有标签
        List<Tag> TagList = tagService.listTagTop(10000);

        // 如果前端传来的标签id为-1的话,说明是从导航栏里面点击标签进入标签页面
        if (id == -1){
            // 默认显示第一个的标签
            id = TagList.get(0).getId();
        }

        // 把所有的标签信息在标签界面上展示
        model.addAttribute("tags", TagList);
        // 根据标签的id查询该id下面的标签博客,把博客对象传递到前端
        model.addAttribute("page",blogService.listBlog(id,pageable));
        // 把当前选中的id传回前端,被选中的id高亮显示
        model.addAttribute("activeTagId",id);

        return "tags";
    }
}
