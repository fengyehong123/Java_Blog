package blog.web.admin;

import blog.pojo.Type;
import blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(
            // 默认一页显示1条数据,根据id来倒序排序
            // 前端并没有传入,Pageable相关数据,我们用的是Pageable对象的默认值
            @PageableDefault(size = 10,direction = Sort.Direction.DESC) Pageable pageable,
            // 前端的页面展示模型
            Model model
    ){
        // springBoot 会把前端传入的值封装成 pageable 分页对象
        Page<Type> typePage = typeService.listType(pageable);

        // 把从后端获取的分页参数传递给前端
        model.addAttribute("page",typePage);

        return "admin/types";
    }

    // 保存数据入库
    @PostMapping("/types")  // @Valid 后端对前端提交的数据进行校验(在实体类中注解校验) BindingResult 接收校验的结果传输给前端
    // @Valid Type type, BindingResult result 两者中间不能有其他参数,否则校验无效
    public String postTypes(@Valid Type type, BindingResult result, RedirectAttributes attributes){

        // 如果前端要增加的分类已经存在
        Type typeByName = typeService.getTypeByName(type.getName());
        if (typeByName != null){
            // 添加自定义的验证错误
            /*
            *  参数1: 校验不通过的字段名: 要和实体类保持一致
            *  参数2: 错误提示信息名称
            *  参数3: 错误提示信息值
            * */
            result.rejectValue("name", "nameError","分类名称不能重复添加");
        }

        // 如果校验失败产生错误的话,就跳转到分类界面
        if (result.hasErrors()){
            // 后端验证不同过的话,重新跳转到分类输入页面
            return "admin/type-input";
        }

        // 把分类信息保存入库
        Type type1 = typeService.saveType(type);

        if (type1 == null){
            // 把错误信息重定向到跳转的页面中
            attributes.addFlashAttribute("message", "新增失败");
        } else {

            attributes.addFlashAttribute("message", "新增成功");
        }

        // 数据保存完成之后,重定向跳转页面显示,重定向,定向的是网址
        return "redirect:/admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model){

        // 当进入分类界面的时候,返回一个空的Type 对象到前端,防止前端报错(前端添加了后端对分类的校验结果)
        model.addAttribute("type",new Type());
        return "admin/type-input";
    }

    // 编辑已经有的分类信息
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        Type type = typeService.getType(id);
        model.addAttribute("type",type);

        // 把要修改的数据返回到页面上去
        return "admin/type-input";
    }

    // 处理重新编辑的值
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result,@PathVariable Long id, RedirectAttributes attributes){

        Type typeByName = typeService.getTypeByName(type.getName());
        if (typeByName != null){
            result.rejectValue("name", "nameError","分类名称不能重复添加");
        }

        if (result.hasErrors()){
            return "admin/type-input";
        }

        Type t = typeService.updateType(id, type);
        if (t == null){
            // 把错误信息重定向到跳转的页面中
            attributes.addFlashAttribute("message", "更新失败");
        } {
            attributes.addFlashAttribute("message", "更新成功");
        }

        return "redirect:/admin/types";
    }

    // 根据id删除分类(如果分类下面还有文章的话,禁止删除分类,提示先更改文章的分类)
    @GetMapping("/types/{id}/delete")
    public String deleteTypeById(@PathVariable Long id,RedirectAttributes attributes){

        typeService.deleteType(id,attributes);

        // 重定向到列表中
        return "redirect:/admin/types";
    }

}
