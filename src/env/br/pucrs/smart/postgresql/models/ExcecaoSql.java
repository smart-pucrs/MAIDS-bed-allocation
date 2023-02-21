package br.pucrs.smart.postgresql.models;

public class ExcecaoSql {
	
	private Integer id;
	private String quarto;
	private String descricao;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getQuarto() {
		return quarto;
	}
	public void setQuarto(String quarto) {
		this.quarto = quarto;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
		if (id != null) {
			sb.append(" id: ");
			sb.append(id);
		}
		if (quarto != null) {
			sb.append(", ").append("\n");
			sb.append(" quarto: ");
			sb.append(quarto);
		}
		if (descricao != null) {
			sb.append(", ").append("\n");
			sb.append(" descricao: ");
			sb.append(descricao);
		}
		sb.append("\n").append("}");
		return sb.toString();
	}
}
