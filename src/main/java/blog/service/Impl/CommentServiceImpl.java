package blog.service.Impl;

import blog.NotFoundException;
import blog.dao.CommentRepository;
import blog.pojo.Comment;
import blog.pojo.CommentAndBlog;
import blog.service.BlogService;
import blog.service.CommentService;
import blog.vo.CommentSearch;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BlogService blogService;

    // 根据blog的id来查询文章的评论
    @Override
    public List<Comment> listCommentByBlogId(Long id) {

        Sort sort = new Sort(Sort.Direction.ASC,"createTime");  // 根据创建时间来排序

        // 查询出所有的第一级别的评论
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(id, sort);
        // 调用对评论加工的方法
        return eachComment(comments);
    }

    // 保存文章的评论
    @Override
    @Transactional
    public Comment saveComment(Comment comment) {

        // 获取前端提交过来父评论的id (我们在隐藏域中已经添加了默认父评论的id为 -1)
        Long parentCommentId = comment.getParentComment().getId();

        // 如果这条评论有父评论的话
        if (parentCommentId != -1){

            // 如果父评论不是-1,先检查父评论所对应的评论id是否存在,如果存在的话,说明父评论确实存在,评论保存
            // 查询父亲评论的id是否存在,如果父评论的id不存在的话,不能把所谓的子评论保存到数据库中,否则后台管理系统会因为关联不到父评论报错
            Long commentId = commentRepository.findObjByParentCommentId(parentCommentId);
            if (commentId != null){
                // 父评论的内容设置到子评论的对象中
                comment.setParentComment(commentRepository.getOne(parentCommentId));
            } else {
                // 这种情况意味着父评论被删除,但是子评论还是对父评论进行了评论
                comment.setParentComment(null);
            }

        } else if (parentCommentId == -1){
            // 如果幅父评论的id为 -1 的话,说明没有父评论,所谓的子评论就是父评论
            comment.setParentComment(null);
        }

        // 初始化评论的创建时间
        comment.setCreateTime(new Date());
        // 将评论保存到数据库
        return commentRepository.save(comment);
    }

    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        // 把评论对象的数据都放到这个集合里面
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        // 合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {
        // 复制之后的所有的评论对象集合
        for (Comment comment : comments) {
            // 一个评论的子评论集合
            List<Comment> replys1 = comment.getReplyCommentList();
            // 遍历子评论集合
            for(Comment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            // 修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyCommentList(tempReplys);
            // 清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    // 存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();
    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);//顶节点添加到临时存放集合

        // 如果子评论还有评论的话
        if (comment.getReplyCommentList().size()>0) {
            // 获取子评论的评论集合
            List<Comment> replys = comment.getReplyCommentList();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                // 如果子评论下面还有多条评论的话,递归,继续遍历
                if (reply.getReplyCommentList().size()>0) {
                    recursively(reply);
                }
            }
        }
    }

    // 查询出所有文章的评论(附带文章标题)
    @Override
    public Page<CommentAndBlog> findAllComment(Pageable pageable) {

        // 查询出所有的文章的评论
        // 文章评论没有文章标题,需要我们自己构造
        Page<Comment> commentList = commentRepository.findAll(pageable);

        return returnResult(commentList,pageable);
    }

    // 根据条件进行评论的查询
    @Override
    public Page<CommentAndBlog> findCommentByCondition(Pageable pageable, CommentSearch comment) {

        // 构造条件查询
        Specification<Comment> Specific = new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery cq, CriteriaBuilder cb) {
                // 构造插叙条件的数组
                ArrayList<Predicate> predicates = new ArrayList<>();
                if (!"".equals(comment.getNickname()) && comment.getNickname() != null ){
                    predicates.add( cb.like(root.<String>get("nickname"),"%"+ comment.getNickname() +"%" ) );
                }
                if (!"".equals(comment.getContent()) && comment.getContent()!= null ){
                    predicates.add( cb.like(root.<String>get("content"),"%"+ comment.getContent()+"%" ) );
                }

                // 将列表转换成数组
                Predicate[] array = predicates.toArray(new Predicate[predicates.size()]);

                cq.where(array);

                return null;
            }
        };

        Page<Comment> commentList = commentRepository.findAll(Specific, pageable);
        if (commentList == null){
            throw new NotFoundException("该查询条件下无博客");
        }

        return returnResult(commentList,pageable);
    }

    // 根据id来删除评论
    @Override
    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    // 抽出的方法
    private Page<CommentAndBlog> returnResult (Page<Comment> commentList,Pageable pageable){
        ArrayList<CommentAndBlog> arrayList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentAndBlog commentAndBlog = new CommentAndBlog();
            commentAndBlog.setNickname(comment.getNickname());
            commentAndBlog.setContent(comment.getContent());
            commentAndBlog.setCreateTime(comment.getCreateTime());
            // 封装评论的id(为删除或者编辑id的时候使用)
            commentAndBlog.setId(comment.getId());

            // 封装评论所对应的文章
            Long blogId = comment.getBlog().getId();
            // 根据文章id查找文章的标题
            String blogTitle = blogService.getBlog(blogId).getTitle();
            commentAndBlog.setTitle(blogTitle);

            // 把构造好的对象添加到集合中
            arrayList.add(commentAndBlog);
        }

        long count = commentRepository.count();

        // 通过PageImpl<> 构造分页对象
        /*
         * 参数一: 查询到的对象
         * 参数二: 分页对象
         * 参数三: 数据的总的数量
         * */
        Page<CommentAndBlog> commentAndBlogs = new PageImpl<>(arrayList, pageable, count);
        return commentAndBlogs;
    }

}
