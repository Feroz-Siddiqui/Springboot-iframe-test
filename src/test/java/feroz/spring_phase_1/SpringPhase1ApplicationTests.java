package feroz.spring_phase_1;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.javafaker.Faker;

import feroz.spring_phase_1.dbmodel.Product;
import feroz.spring_phase_1.dbrepo.ProductRepo;

@SpringBootTest
class SpringPhase1ApplicationTests {

	@Autowired
	ProductRepo productRepo;
	@Test
	void contextLoads() {
//		List<String> publicApis = List.of("/cdn.jsdelivr.net/**", "/getbootstrap.com/**", "/index/**", "index/**");
//
//		System.out.println(String.join(",", publicApis));
		Faker faker = Faker.instance();
		for(int i =0 ; i< 10 ;i++) {
			Product product = new Product(faker.book().title());
			productRepo.save(product);
		
		}
		
		
		
	}

}
