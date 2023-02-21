package br.pucrs.smart.validator.models;

import java.util.ArrayList;
import java.util.List;

public class ResultVal {
	private String idPaciente;
	private String idLeito;
	private String nomePaciente;
	private String numeroLeito;
	private Boolean valid;
	private List<ErrorVal> errors;
	
	public String getIdPaciente() {
		return idPaciente;
	}
	public void setIdPaciente(String idPaciente) {
		this.idPaciente = idPaciente;
	}
	public String getIdLeito() {
		return idLeito;
	}
	public void setIdLeito(String idLeito) {
		this.idLeito = idLeito;
	}
	public String getNomePaciente() {
		return nomePaciente;
	}
	public void setNomePaciente(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}
	public String getNumeroLeito() {
		return numeroLeito;
	}
	public void setNumeroLeito(String numeroLeito) {
		this.numeroLeito = numeroLeito;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public List<ErrorVal> getErrors() {
		return errors;
	}
	public void setErrors(List<ErrorVal> errors) {
		this.errors = errors;
	}
	public void addErrors(ErrorVal errorVal) {
		if (this.errors != null) {
			this.errors.add(errorVal);
		} else {
			this.errors = new ArrayList<ErrorVal>();
			this.errors.add(errorVal);
		}
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("{ ");
        if (idPaciente != null) {
	        sb.append(" idPaciente : ");
            sb.append(idPaciente);
        }
        if (idLeito != null) {
            sb.append(", ");
	        sb.append(" idLeito : ");
            sb.append(idLeito);
        }
        if (nomePaciente != null) {
            sb.append(", ");
	        sb.append(" nomePaciente : ");
            sb.append(nomePaciente);
        }
        if (numeroLeito != null) {
            sb.append(", ");
	        sb.append(" numeroLeito : ");
            sb.append(numeroLeito);
        }
        sb.append(", ");
	    sb.append(" valid : ");
        sb.append(valid);
        
        if (errors != null) {
            sb.append(", ");
	        sb.append(" errors : [");
	        for (ErrorVal e : errors) {
		        sb.append(e.toString());
	        }
	        sb.append("] ");
	    }
	    sb.append("} ");
		return sb.toString();
	}
	
}
