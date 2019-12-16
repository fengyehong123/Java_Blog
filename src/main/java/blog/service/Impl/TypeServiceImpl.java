package blog.service.Impl;

import blog.NotFoundException;
import blog.dao.BlogRepository;
import blog.dao.TypeRepository;
import blog.pojo.Type;
import blog.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// 管理分类
@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private BlogRepository blogRepository;

    @Override
    @Transactional
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Override
    public Type getType(Long id) {
        // 延时查询
        Type type = typeRepository.getOne(id);
        return type;
    }

    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    // 查询全部的分类信息,不需要分页
    @Override
    public List<Type> listAllType() {
        return typeRepository.findAll();
    }

    @Override
    @Transactional
    public Type updateType(Long id, Type type) {

        Type type1 = typeRepository.getOne(id);
        if (type1 == null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type, type1);
        // 因为 type1 中含有id,所以会完成更新的操作
        return typeRepository.save(type1);
    }

    // 删除分类,删除分类的同时,先校验分类下面有没有文章,如果有文章的话,禁止删除
    @Override
    @Transactional
    public void deleteType(Long id,RedirectAttributes attributes) {


        // 先查询该分类下面是否有文章,如果有的话,禁止分类的删除
        Long blogId = blogRepository.findBlogByTypeId(id);

        if (blogId == 0){
            // 如果分类下面没有博客存在的话,可以删除
            typeRepository.deleteById(id);
            attributes.addFlashAttribute("message", "删除分类成功");
        } else {
            // 并且提示要先修改文章的分类
            attributes.addFlashAttribute("message", "该分类下面有文章,请先修改文章的分类");
        }

    }

    // 通过名称查询Type
    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    // 在首页显示分类的数量
    @Override
    public List<Type> listTypeTop(Integer size) {

        // 构建一个排序的对象 倒序排序,按照数量排序
        Sort sort = new Sort(Sort.Direction.DESC,"blogList.size");

        // 构造一个分页对象
        Pageable pageAble = PageRequest.of(0, size,sort);

        List<Type> typeList = typeRepository.findTop(pageAble);

        return typeList;
    }
}
