package br.pucrs.smart.postgresql.models;

public class LeitoSql {

	private Integer id;
	private String acomodacao;
	private String genero;
	private String numero;
	private String quarto;
	private String status_leito;
	private Boolean preferencialmente_isolamento;
	private Float distancia_enfermaria;
	private String especialidade_preferencial;
	private String convenio;
	private String tipo_leito; //FIXO, EXTRA
	private String unidade;
	private String unidade_clasee;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAcomodacao() {
		return acomodacao;
	}
	public void setAcomodacao(String acomodacao) {
		this.acomodacao = acomodacao;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getQuarto() {
		return quarto;
	}
	public void setQuarto(String quarto) {
		this.quarto = quarto;
	}
	public String getStatus_leito() {
		return status_leito;
	}
	public void setStatus_leito(String status_leito) {
		this.status_leito = status_leito;
	}
	public Boolean getPreferencialmente_isolamento() {
		return preferencialmente_isolamento;
	}
	public void setPreferencialmente_isolamento(Boolean preferencialmente_isolamento) {
		this.preferencialmente_isolamento = preferencialmente_isolamento;
	}
	public float getDistancia_enfermaria() {
		return distancia_enfermaria;
	}
	public void setDistancia_enfermaria(float distancia_enfermaria) {
		this.distancia_enfermaria = distancia_enfermaria;
	}
	public String getEspecialidade_preferencial() {
		return especialidade_preferencial;
	}
	public void setEspecialidade_preferencial(String especialidade_preferencial) {
		this.especialidade_preferencial = especialidade_preferencial;
	}
	public String getConvenio() {
		return convenio;
	}
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	public String getTipo_leito() {
		return tipo_leito;
	}
	public void setTipo_leito(String tipo_leito) {
		this.tipo_leito = tipo_leito;
	}
	public String getUnidade() {
		return unidade;
	}
	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}
	public String getUnidade_clasee() {
		return unidade_clasee;
	}
	public void setUnidade_clasee(String unidade_clasee) {
		this.unidade_clasee = unidade_clasee;
	}
	
	public String get(String param) {
		switch (param) {
		case "id":
			return id.toString();
		case "acomodacao":
			return acomodacao;
		case "genero":
			return genero;
		case "numero":
			return numero;
		case "quarto":
			return quarto;
		case "status_leito":
			return status_leito;
		case "preferencialmente_isolamento":
			return preferencialmente_isolamento.toString();
		case "distancia_enfermaria":
			return distancia_enfermaria.toString();
		case "especialidade_preferencial":
			return especialidade_preferencial;
		case "convenio":
			return convenio;
		case "tipo_leito":
			return tipo_leito;
		case "unidade":
			return unidade;
		case "unidade_clasee":
			return unidade_clasee;
		default:
			return null;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
		if (id != null) {
			sb.append(" id: ");
			sb.append(id);
		}
		if (acomodacao != null) {
			sb.append(", ").append("\n");
			sb.append(" acomodacao: ");
			sb.append(acomodacao);
		}
		if (genero != null) {
			sb.append(", ").append("\n");
			sb.append(" genero: ");
			sb.append(genero);
		}
		if (numero != null) {
			sb.append(", ").append("\n");
			sb.append(" numero: ");
			sb.append(numero);
		}
		if (quarto != null) {
			sb.append(", ").append("\n");
			sb.append(" quarto: ");
			sb.append(quarto);
		}
		if (status_leito != null) {
			sb.append(", ").append("\n");
			sb.append(" status_leito: ");
			sb.append(status_leito);
		}
		if (preferencialmente_isolamento != null) {
			sb.append(", ").append("\n");
			sb.append(" preferencialmente_isolamento: ");
			sb.append(preferencialmente_isolamento);
		}
		if (distancia_enfermaria != null) {
			sb.append(", ").append("\n");
			sb.append(" distancia_enfermaria: ");
			sb.append(distancia_enfermaria);
		}
		if (especialidade_preferencial != null) {
			sb.append(", ").append("\n");
			sb.append(" especialidade_preferencial: ");
			sb.append(especialidade_preferencial);
		}
		if (convenio != null) {
			sb.append(", ").append("\n");
			sb.append(" convenio: ");
			sb.append(convenio);
		}
		if (tipo_leito != null) {
			sb.append(", ").append("\n");
			sb.append(" tipo_leito: ");
			sb.append(tipo_leito);
		}
		if (unidade != null) {
			sb.append(", ").append("\n");
			sb.append(" unidade: ");
			sb.append(unidade);
		}
		if (unidade_clasee != null) {
			sb.append(", ").append("\n");
			sb.append(" unidade_clasee: ");
			sb.append(unidade_clasee);
		}				
		sb.append("\n").append("}");
		return sb.toString();
	}
	
}
