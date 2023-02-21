package br.pucrs.smart.postgresql.models;

public class RegraSql {
	private Integer id;
	private String especialidade;
	private String restricao;
	private String valorRestricao;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEspecialidade() {
		return especialidade;
	}
	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}
	public String getRestricao() {
		return restricao;
	}
	public void setRestricao(String restricao) {
		this.restricao = restricao;
	}
	public String getValorRestricao() {
		return valorRestricao;
	}
	public void setValorRestricao(String valorRestricao) {
		this.valorRestricao = valorRestricao;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
		if (id != null) {
			sb.append(" id: ");
			sb.append(id);
		}
		if (especialidade != null) {
			sb.append(", ").append("\n");
			sb.append(" especialidade: ");
			sb.append(especialidade);
		}
		if (restricao != null) {
			sb.append(", ").append("\n");
			sb.append(" restricao: ");
			sb.append(restricao);
		}
		if (valorRestricao != null) {
			sb.append(", ").append("\n");
			sb.append(" valorRestricao: ");
			sb.append(valorRestricao);
		}
		sb.append("\n").append("}");
		return sb.toString();
	}
}
