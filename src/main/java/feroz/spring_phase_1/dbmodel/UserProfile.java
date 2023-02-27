package feroz.spring_phase_1.dbmodel;

import java.util.Date;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class UserProfile {
	@Id
	private Long id;
	private String firstName;
	private String lastName;
	private Date dob;
	private Gender gender;
	private String profileImage;
	private Long userId;
	private Date createdAt = new Date();
	private Date updatedAt = new Date();;
	private Boolean isEnabled;

	public UserProfile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserProfile(String firstName, String lastName, Date dob, Gender gender, String profileImage,
			Boolean isEnabled) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.gender = gender;
		this.profileImage = profileImage;
		this.isEnabled = isEnabled;
	}

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Override
	public String toString() {
		return "UserProfile [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", dob=" + dob
				+ ", gender=" + gender + ", profileImage=" + profileImage + ", userId=" + userId + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + ", isEnabled=" + isEnabled + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdAt, dob, firstName, gender, id, isEnabled, lastName, profileImage, updatedAt,
				userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserProfile other = (UserProfile) obj;
		return Objects.equals(createdAt, other.createdAt) && Objects.equals(dob, other.dob)
				&& Objects.equals(firstName, other.firstName) && gender == other.gender && Objects.equals(id, other.id)
				&& Objects.equals(isEnabled, other.isEnabled) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(profileImage, other.profileImage) && Objects.equals(updatedAt, other.updatedAt)
				&& Objects.equals(userId, other.userId);
	}
	
	
	

}
