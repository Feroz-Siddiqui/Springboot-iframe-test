package feroz.spring_phase_1.dbrepo;

import org.springframework.data.repository.CrudRepository;

import feroz.spring_phase_1.dbmodel.UserProfile;

public interface UserProfileRepo extends CrudRepository<UserProfile,Long>{

}
