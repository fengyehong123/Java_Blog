package blog.dao;

import blog.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

// jap操作的是user实体类,主键是long类型的
public interface UserRepository extends JpaRepository<User,Long>{

    // 根据命名规格
    User findByUsernameAndPassword(String username,String password);

    String findByUsername(String username);
}
