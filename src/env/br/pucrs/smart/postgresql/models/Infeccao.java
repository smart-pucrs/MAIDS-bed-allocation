package br.pucrs.smart.postgresql.models;

import java.sql.Timestamp;

public class Infeccao {
	
	private Integer id;
	private String cod_paciente;
	private Timestamp dt_registro;
	private String cd_categoria;
	private String ds_categoria;
	private Integer ordem_decresc;
	
	public String get(String param) {
		switch (param) {
		case "id":
			return id.toString();
		case "cod_paciente":
			return cod_paciente;
		case "dt_registro":
			return dt_registro.toString();
		case "cd_categoria":
			return cd_categoria;
		case "ds_categoria":
			return ds_categoria;
		case "ordem_decresc":
			return ordem_decresc.toString();
		default:
			return null;
		}
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCod_paciente() {
		return cod_paciente;
	}
	public void setCod_paciente(String cod_paciente) {
		this.cod_paciente = cod_paciente;
	}
	public Timestamp getDt_registro() {
		return dt_registro;
	}
	public void setDt_registro(Timestamp dt_registro) {
		this.dt_registro = dt_registro;
	}
	public String getCd_categoria() {
		return cd_categoria;
	}
	public void setCd_categoria(String cd_categoria) {
		this.cd_categoria = cd_categoria;
	}
	public String getDs_categoria() {
		return ds_categoria;
	}
	public void setDs_categoria(String ds_categoria) {
		this.ds_categoria = ds_categoria;
	}
	public Integer getOrdem_decresc() {
		return ordem_decresc;
	}
	public void setOrdem_decresc(Integer ordem_decresc) {
		this.ordem_decresc = ordem_decresc;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
		if (id != null) {
			sb.append(" id: ");
			sb.append(id);
		}
		if (cod_paciente != null) {
			sb.append(", ").append("\n");
			sb.append(" cod_paciente: ");
			sb.append(cod_paciente);
		}
		if (dt_registro != null) {
			sb.append(", ").append("\n");
			sb.append(" dt_registro: ");
			sb.append(dt_registro);
		}
		if (cd_categoria != null) {
			sb.append(", ").append("\n");
			sb.append(" cd_categoria: ");
			sb.append(cd_categoria);
		}
		if (ds_categoria != null) {
			sb.append(", ").append("\n");
			sb.append(" ds_categoria: ");
			sb.append(ds_categoria);
		}
		if (ordem_decresc != null) {
			sb.append(", ").append("\n");
			sb.append(" ordem_decresc: ");
			sb.append(ordem_decresc);
		}	
		sb.append("\n").append("}");
		return sb.toString();
	}

}
