package chris.dicom.bean;

public class ResponseBean {
	int isError=0;
	String message="Success";
	
	
	Object obj=null;

	
	
	public int getIsError() {
		return isError;
	}

	public void setIsError(int isError) {
		this.isError = isError;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
