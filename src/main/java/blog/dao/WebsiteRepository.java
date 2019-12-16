package blog.dao;

import blog.pojo.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WebsiteRepository extends JpaRepository<Website,Long>,JpaSpecificationExecutor<Website> {


}
