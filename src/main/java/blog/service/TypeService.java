package blog.service;

import blog.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

public interface TypeService {

    // 新增分类
    Type saveType(Type type);

    // 查询分类
    Type getType(Long id);

    // 分页查询
    Page<Type> listType(Pageable pageable);

    // 查询全部,不需要分页
    List<Type> listAllType();

    // 修改分类
    Type updateType(Long id,Type type);

    // 删除分类
    void deleteType(Long id,RedirectAttributes attributes);

    // 通过名称查询方法
    Type getTypeByName(String name);

    // 获取所有的分类
    // List<Type> findAllTye();

    // 根据传输的值在首页显示分类的数量
    List<Type> listTypeTop(Integer size);


}
