package blog.dao;

import blog.pojo.WebCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WebCategoryRepository extends JpaRepository<WebCategory,Long>,JpaSpecificationExecutor<WebCategory> {


}
