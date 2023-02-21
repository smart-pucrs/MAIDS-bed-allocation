package br.pucrs.smart.postgresql.models;

public class NurseExceptionSql {
	private Integer id;
	private boolean active;
	private String nomePaciente;
	private Integer laudoInternacaoId;
	private String tipo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getNomePaciente() {
		return nomePaciente;
	}

	public void setNomePaciente(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}

	public Integer getLaudoInternacaoId() {
		return laudoInternacaoId;
	}

	public void setLaudoInternacaoId(Integer laudoInternacaoId) {
		this.laudoInternacaoId = laudoInternacaoId;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
		sb.append(" id: ");
		sb.append(id);
		sb.append(", ").append("\n");
		sb.append(" active: ");
		sb.append(active);
		sb.append(", ").append("\n");
		sb.append(" laudoInternacaoId: ");
		sb.append(laudoInternacaoId);
		if (tipo != null) {
			sb.append(", ").append("\n");
			sb.append(" tipo: ");
			sb.append(tipo);
		}
		sb.append("\n").append("}");
		return sb.toString();
	}

}
