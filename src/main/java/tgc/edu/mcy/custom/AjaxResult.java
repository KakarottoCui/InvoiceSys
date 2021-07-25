package tgc.edu.mcy.custom;

public class AjaxResult {
	private Boolean success;
	private String message;
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public AjaxResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AjaxResult(Boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	
	public static AjaxResult build(Boolean success,String message) {
		return new AjaxResult(success, message);
	}
	public static AjaxResult build() {
		return new AjaxResult(true, "OK");
	}
	
	
}
