package br.pucrs.smart.postgresql.models;

public class CaracteristicaSql {
	
	private Integer id;
	private String caracteristica;
	private String descricao;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCaracteristica() {
		return caracteristica;
	}
	public void setCaracteristica(String caracteristica) {
		this.caracteristica = caracteristica;
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
		if (caracteristica != null) {
			sb.append(", ").append("\n");
			sb.append(" caracteristica: ");
			sb.append(caracteristica);
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
