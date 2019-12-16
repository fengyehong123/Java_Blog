package blog.service.Impl;

import blog.NotFoundException;
import blog.dao.BlogRepository;
import blog.dao.TagRepository;
import blog.pojo.Tag;
import blog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private BlogRepository blogRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {

        // 定义一个排序对象
        Sort sort = new Sort(Sort.Direction.DESC, "blogList.size");
        Pageable pageable = PageRequest.of(0, size,sort);
        return tagRepository.findTop(pageable);
    }

    @Override
    public List<Tag> listTag(String ids) { //1,2,3
        return tagRepository.findAllById(convertToList(ids));
    }

    // 将字符串转换为可迭代的数组
    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.getOne(id);
        if (t == null) {
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(tag,t);
        return tagRepository.save(t);
    }

    // 删除标签,如果标签下面还有文章的话,禁止删除,并提示修改文章分类
    @Transactional
    @Override
    public void deleteTag(Long id,RedirectAttributes attributes) {

        // 根据id来查询标签下是否有文章
        Long count = blogRepository.findBlogByTagId(id);

        if (count == 0){
            // 如果该标签下面没有博客的话,可以删除标签
            tagRepository.deleteById(id);  // 删除标签数据库
            // 删除博客和中间表中的数据

            attributes.addFlashAttribute("message","删除标签成功");
        } else {
            attributes.addFlashAttribute("message","该标签下还有博客,请先修改博客的标签" );
        }
    }

    // 获取到所有标签
    @Override
    public List<Tag> getAllTag() {
        return tagRepository.findAll();
    }

    // 根据ids来获取标签
    @Override
    public List<Tag> listTagByIds(String ids) {

        return tagRepository.findAllById(convertToList(ids));
    }
}
