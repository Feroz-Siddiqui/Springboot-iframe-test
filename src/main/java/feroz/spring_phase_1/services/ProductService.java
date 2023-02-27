package feroz.spring_phase_1.services;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import feroz.spring_phase_1.dbmodel.Product;
import feroz.spring_phase_1.dbrepo.ProductRepo;

@Service
public class ProductService {

	@Autowired
	private ProductRepo productRepo;
	
	
	public Product createProduct(Product product) {
		return 	productRepo.save(product);
	}
	
	public List<Product> saveAllProducts(List<Product> list) {
		return IterableUtils.toList(productRepo.saveAll(list));
	}
	
	public List<Product> fetchAllProducts(){
		return  IterableUtils.toList(productRepo.findAll());
	}

	public Optional<Product> fetchProduct(Long id) {
		// TODO Auto-generated method stub
		return productRepo.findById(id);
	}
	
	
	
	
	
	
}
