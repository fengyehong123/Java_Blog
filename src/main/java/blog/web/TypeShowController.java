package blog.web;

import blog.pojo.Type;
import blog.service.BlogService;
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
import java.util.List;

@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(
            @PathVariable Long id,
            @PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ){
        // 获取所有分类
        List<Type> typeList = typeService.listTypeTop(10000);

        // 如果前端传来的分类id为-1的话,说明是从导航栏里面点击分类进入分类页面
        if (id == -1){
            // 默认显示第一个的分类
            id = typeList.get(0).getId();
        }

        // 构件查询条件
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);

        // 把所有的分类信息在分类界面上展示
        model.addAttribute("types", typeList);
        // 根据分类的id查询该id下面的分类博客,把博客对象传递到前端
        model.addAttribute("page",blogService.listBlog(pageable, blogQuery));
        // 把当前选中的id传回前端,被选中的id高亮显示
        model.addAttribute("activeTypeId",id);

        return "types";
    }
}
