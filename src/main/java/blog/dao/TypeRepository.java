package blog.dao;

import blog.pojo.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type,Long>,JpaSpecificationExecutor<Type> {

    // 根据名称查询Type对象
    Type findByName(String name);

    // 在首页展示要显示的分类标签的数量,运用自定义查询
    @Query("select t from Type t")
    List<Type> findTop(Pageable pageable);

}
