package br.pucrs.smart.postgresql.models;

import java.util.ArrayList;
import java.util.List;

public class InfeccaoPorPaciente {
	
	private String cod_paciente;
	private List<String> cds_categoria;
	
	public String getCod_paciente() {
		return cod_paciente;
	}
	public void setCod_paciente(String cod_paciente) {
		this.cod_paciente = cod_paciente;
	}
	public List<String> getCds_categoria() {
		return cds_categoria;
	}
	public void setCds_categoria(List<String> cds_categoria) {
		this.cds_categoria = cds_categoria;
	}

	public void addCd_categoria(String cd) {
		if (this.cds_categoria != null) {
			this.cds_categoria.add(cd);
		} else {
			this.cds_categoria = new ArrayList<String>();
			this.cds_categoria.add(cd);
		}
	}

	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("{ ");
        	sb.append(" cod_paciente : ");
        	sb.append(cod_paciente);
        	sb.append(", ");
        if (cds_categoria != null) {
	        sb.append(" cds_categoria : [");
	        for (String c : cds_categoria) {
		        	sb.append(c);
		        	sb.append(", ");
		        }
		        sb.append(" }, ");
	        }
	        sb.append("] ");
	    sb.append("} ");
		return sb.toString();
	}

}
