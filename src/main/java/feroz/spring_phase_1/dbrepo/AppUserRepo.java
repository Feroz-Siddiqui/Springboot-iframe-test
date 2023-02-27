package feroz.spring_phase_1.dbrepo;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import feroz.spring_phase_1.dbmodel.AppUser;

public interface AppUserRepo extends CrudRepository<AppUser, Long> {

	@Query("select * from app_user where username = :custom ")
	AppUser findUser(@Param("custom") String username);

	AppUser findByUsername(String username);

	Optional<AppUser> findById(Long id);
}
