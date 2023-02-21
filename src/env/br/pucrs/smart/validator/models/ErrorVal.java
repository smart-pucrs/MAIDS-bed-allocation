package br.pucrs.smart.validator.models;

public class ErrorVal {
	private String type;
	private String predicado;
	private String id;
	private String predType;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPredicado() {
		return predicado;
	}
	public void setPredicado(String predicado) {
		this.predicado = predicado;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPredType() {
		return predType;
	}
	public void setPredType(String predType) {
		this.predType = predType;
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("{ ");
        if (type != null) {
	        sb.append(" type : ");
            sb.append(type);
        }
        if (predicado != null) {
            sb.append(", ");
	        sb.append(" predicado : ");
            sb.append(predicado);
        }
        if (id != null) {
            sb.append(", ");
	        sb.append(" id : ");
            sb.append(id);
        }
        if (predType != null) {
            sb.append(", ");
	        sb.append(" predType : ");
            sb.append(predType);
        }
	    sb.append("} ");
		return sb.toString();
	}

}
