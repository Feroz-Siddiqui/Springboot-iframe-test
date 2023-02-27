package feroz.spring_phase_1.model;

public class AppResponse {

	private Boolean success;
	private Integer code;
	
	
	
	public AppResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AppResponse(Boolean success, Integer code) {
		super();
		this.success = success;
		this.code = code;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	
	
	
}
