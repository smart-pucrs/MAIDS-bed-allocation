package br.pucrs.smart.Dial4JaCa;

public class ResponseFront {
	
	private Integer status;
	private String response;
	private String error;
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

}