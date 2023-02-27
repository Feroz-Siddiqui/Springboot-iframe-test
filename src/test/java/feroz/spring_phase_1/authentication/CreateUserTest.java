package feroz.spring_phase_1.authentication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.javafaker.Faker;

import feroz.spring_phase_1.dbmodel.AppUser;
import feroz.spring_phase_1.dbmodel.Gender;
import feroz.spring_phase_1.dbmodel.ProfileDBFile;
import feroz.spring_phase_1.dbmodel.UserProfile;
import feroz.spring_phase_1.dbrepo.AppUserRepo;
import feroz.spring_phase_1.dbrepo.ProfileDBFileRepo;
@SpringBootTest
public class CreateUserTest {

	@Autowired
	AppUserRepo appUserRepo;
	
	@Autowired
	ProfileDBFileRepo profileDBFileRepo;
	
	@Test
	void contextLoads() {
		System.out.println("hshshhshshshsh....");
		
		try {
			createSingleUser();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void createSingleUser() throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		Faker faker = Faker.instance();
		for(int i=0 ;i<20;i++) {
		File file = new File("/Users/itadminsalesken/Downloads/test.jpeg");
		String name =faker.name().firstName();
		Calendar c = Calendar.getInstance();
		c.set(1989, 10, 6);
		
		UserProfile userProfile = new UserProfile(StringEscapeUtils.escapeSql(name), "siddiqui", c.getTime(), Gender.MALE, null, true);
		
//		Optional<AppUser> appOptional =appUserRepo.findById(1l);
//		if(appOptional.isPresent()) {
//			AppUser appUser = appOptional.get();
//			appUser.setUserProfile(userProfile);
//			appUserRepo.save(appUser);
//			
//		}
		
		AppUser appUser = new AppUser(faker.name().username(), "123456",userProfile);
		
		AppUser k=appUserRepo.save(appUser);
		
		ProfileDBFile profileDBFile = new ProfileDBFile(name+".jpeg", "jpeg", IOUtils.toByteArray(new FileInputStream(file)),1070l);
		profileDBFile.setUserId(k.getId());
		profileDBFileRepo.save(profileDBFile);
		}
		
		
	}
}
