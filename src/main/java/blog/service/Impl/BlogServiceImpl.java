package blog.service.Impl;

import blog.NotFoundException;
import blog.dao.BlogRepository;
import blog.dao.CommentRepository;
import blog.pojo.Blog;
import blog.pojo.Tag;
import blog.pojo.Type;
import blog.service.BlogService;
import blog.util.MarkdownUtils;
import blog.util.MyBeanUtils;
import blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository repository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Blog getBlog(Long id) {
        return repository.getOne(id);
    }

    // 根据条件分页查询博客
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {

        Specification<Blog> blogSpecification = new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {

                ArrayList<Predicate> predicateList = new ArrayList<>();
                // 判断是否有标题的条件传输过来(标题不等于空或者null的时候才会拼接条件)
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null ){
                    predicateList.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }

                // 根据Blog对象中的Type对象的id值进行查询
                // 因为id是Long类型的,所以不存在空格字符串的情况
                if (blog.getTypeId() != null){
                    predicateList.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                }

                // 判断是否是推荐的文章,是true的情况就进行查询
                if (blog.isRecommend()){
                    predicateList.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }

                // 把条件列表转换成数组
                Predicate[] predicates = predicateList.toArray(new Predicate[predicateList.size()]);
                // 通过条件数据进行查询
                cq.where(predicates);

                return null;
            }
        };

        Page<Blog> all = repository.findAll(blogSpecification, pageable);

        return all;
    }

    // 新增或者保存博客
    @Override
    @Transactional
    public Blog saveBlog(Blog blog) {

        // 新增博客没有id
        if (blog.getId() == null){
            // 初始化文章的创建时间和修改时间
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());

            // 初始文章的浏览次数
            blog.setViews(0);
        } else {
            // 修改博客有id
            blog.setUpdateTime(new Date());

        }

        return repository.save(blog);
    }

    @Override
    @Transactional
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = repository.getOne(id);
        if (b == null){
            throw new NotFoundException("该博客不存在");
        }

        // 从前端接收的值赋值给查询到的值
        // MyBeanUtils.getNullPropertyNames(blog) 过滤掉blog中值为null的属性
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        // 修改文章更新的时间
        b.setUpdateTime(new Date());
        return repository.save(b);
    }

    // 根据id来删除博客
    @Override
    @Transactional
    public void deleteBlog(Long id) {
        // 根据id来删除
        repository.deleteById(id);

        // 当博客被删除之后,博客的评论也被删除
        commentRepository.deleteByBlogId(id);
    }

    // 分页查询博客文章,不需要条件
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // 推荐博客的列表
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {

        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);

        return repository.findTop(pageable);
    }

    // 根据条件进行查询
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return repository.findByQuery(query,pageable );
    }

    // 把markdown语法转换为html语法
    @Override
    @Transactional
    public Blog getMarkDownToHtml(Long id) {

        // 根据id查询Blog对象
        Blog blog = repository.getOne(id);

        if (blog == null){
            throw new  NotFoundException("该篇博客不存在");
        }

        Blog b = new Blog();
        // 把查询到的blog拷贝给我们自定义的blog对象
        BeanUtils.copyProperties(blog,b);

        // 根据Blog对象获取出博客的内容
        String content = b.getContent();
        // 把markdown格式的数据转换为html格式的数据
        String strHtml = MarkdownUtils.markdownToHtmlExtensions(content);
        // 把转换完成的html内容放入blog对象返回给前端
        b.setContent(strHtml);

        // 更新博客的浏览次数
        repository.updateBlogViews(id);

        return b;
    }

    // 根据标签id来查询标签下面的博客
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {

        // 关联查询,博客和标签是多对多的关系
        Specification<Blog> spec = new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {
                // 关联查询,blog表和tag表关联
                // tagList 是 blog 实体类中的属性
                Join join = root.join("tagList");

                // join.get("id"): 获取到tag的id
                return cb.equal(join.get("id"), tagId);
            }
        };

        // 进行查询
        return repository.findAll(spec, pageable);
    }

    // 对所有的博客按照年份来进行归档
    @Override
    public Map<String, List<Blog>> archiveBlog() {

        // 获取所有的博客的年份
        List<String> yearList = repository.findGroupYear();

        // 存放年份和年份所对应的博客信息
        HashMap<String, List<Blog>> map = new HashMap<>();

        for (String year : yearList) {
            map.put(year, repository.findByYear(year));
        }

        // 把年份也年份对应的博客信息返回
        return map;
    }

    // 获取博客的总的条数
    @Override
    public Long countBlog() {
        return repository.count();
    }
}
