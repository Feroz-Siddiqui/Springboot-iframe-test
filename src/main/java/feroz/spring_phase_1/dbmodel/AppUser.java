package feroz.spring_phase_1.dbmodel;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Table
public class AppUser {
	@Id
	private Long id;
	@NotNull(message = "You can't leave this empty.")
	@Size(min = 5, max = 255, message = "Please enter between {min} and {max} characters.")
	private String username;
	@NotNull(message = "You can't leave this empty.")
	@Size(min = 5, max = 255, message = "Please enter between {min} and {max} characters.")
	private String password;
	private Date createdAt = new Date();
	private Date updatedAt = new Date();
	@MappedCollection(idColumn = "user_id")
	private UserProfile userProfile;

	public AppUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AppUser(
			@NotNull(message = "You can't leave this empty.") @Size(min = 5, max = 255, message = "Please enter between {min} and {max} characters.") String username,
			@NotNull(message = "You can't leave this empty.") @Size(min = 5, max = 255, message = "Please enter between {min} and {max} characters.") String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public AppUser(String username, String password,UserProfile userProfile) {
		this.username = username;
		this.password = password;
		this.userProfile = userProfile;

	}

	public AppUser(String username, String password, Date createdAt, Date updatedAt) {
		super();
		this.username = username;
		this.password = password;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

}
