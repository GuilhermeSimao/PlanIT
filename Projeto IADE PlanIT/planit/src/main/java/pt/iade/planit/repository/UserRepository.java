package pt.iade.planit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.iade.planit.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
