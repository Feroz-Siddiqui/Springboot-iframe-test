package feroz.spring_phase_1.dbmodel;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class Product {
	@Id
	private Long id;
	private String name;
	private Date createdAt = new Date();
	private Date updatedAt= new Date();
	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Product(String name) {
		super();
		this.name = name;
	}


	public Product( String name, Date createdAt, Date updatedAt) {
		super();
		this.name = name;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	
	
	
	
}
