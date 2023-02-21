package br.pucrs.smart.postgresql.models;

public class AlocacaoSugeridaSql {
	
	private Integer id;
	private Integer alocacaoOtimizadaId;
	private Integer laudoInternacaoId;
	private String nomePaciente;
	private String leitoNum;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAlocacaoOtimizadaId() {
		return alocacaoOtimizadaId;
	}
	public void setAlocacaoOtimizadaId(Integer alocacaoOtimizadaId) {
		this.alocacaoOtimizadaId = alocacaoOtimizadaId;
	}
	public Integer getLaudoInternacaoId() {
		return laudoInternacaoId;
	}
	public void setLaudoInternacaoId(Integer laudoInternacaoId) {
		this.laudoInternacaoId = laudoInternacaoId;
	}
	public String getNomePaciente() {
		return nomePaciente;
	}
	public void setNomePaciente(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}
	public String getLeitoNum() {
		return leitoNum;
	}
	public void setLeitoNum(String leitoNum) {
		this.leitoNum = leitoNum;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
		if (id != null) {
			sb.append(" id: ");
			sb.append(id);
		}
		if (alocacaoOtimizadaId != null) {
			sb.append(", ").append("\n");
			sb.append(" alocacaoOtimizadaId: ");
			sb.append(alocacaoOtimizadaId);
		}
		if (laudoInternacaoId != null) {
			sb.append(", ").append("\n");
			sb.append(" laudoInternacaoId: ");
			sb.append(laudoInternacaoId);
		}
		if (leitoNum != null) {
			sb.append(", ").append("\n");
			sb.append(" leitoNum: ");
			sb.append(leitoNum);
		}
		sb.append("\n").append(" }");
		return sb.toString();
	}
}
