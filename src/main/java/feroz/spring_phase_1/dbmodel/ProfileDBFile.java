package feroz.spring_phase_1.dbmodel;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class ProfileDBFile {
	@Id
	private Long id;
	private String fileName;
	private String fileType;
	private byte[] file_content;
	private Date createdAt = new Date();
	private Date updatedAt = new Date();
	private Long fileSize;
	private Long userId;

	public ProfileDBFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProfileDBFile(String fileName, String fileType, byte[] file_content, Long fileSize) {
		super();
		this.fileName = fileName;
		this.fileType = fileType;
		this.file_content = file_content;
		this.fileSize = fileSize;
	}
	
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getFile_content() {
		return file_content;
	}

	public void setFile_content(byte[] file_content) {
		this.file_content = file_content;
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

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "ProfileDBFile [id=" + id + ", fileName=" + fileName + ", fileType=" + fileType + ", file_content="
				+ Arrays.toString(file_content) + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", fileSize=" + fileSize + ", userId=" + userId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(file_content);
		result = prime * result + Objects.hash(createdAt, fileName, fileSize, fileType, id, updatedAt, userId);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProfileDBFile other = (ProfileDBFile) obj;
		return Objects.equals(createdAt, other.createdAt) && Objects.equals(fileName, other.fileName)
				&& Objects.equals(fileSize, other.fileSize) && Objects.equals(fileType, other.fileType)
				&& Arrays.equals(file_content, other.file_content) && Objects.equals(id, other.id)
				&& Objects.equals(updatedAt, other.updatedAt) && Objects.equals(userId, other.userId);
	}

	
	
	


}
