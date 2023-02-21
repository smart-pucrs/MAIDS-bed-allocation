package br.pucrs.smart.postgresql.models;


import java.sql.Timestamp;

public class InternadoSql {
	private Integer id;
	private String atendimento;
	private String cod_paciente;
	private String faixaetaria;
	private Timestamp data_internacao;
	private Integer dias_internado;
	private String genero;
	private String cod_medico;
	private String especialidade;
	private String tipo_especialidade;
	private String grau_dependencia;
	private String numero_leito_atual;
	private String unidade_internacao_atual;
	private String unidade_grupo_atual;
	private String cobertura_leito_atual;
	private String cobertura_paciente;
	private String cobertura_atendimento;
	
	
	public String get(String param) {
		switch (param) {
		case "id":
			return id.toString();
		case "atendimento":
			return atendimento;
		case "cod_paciente":
			return cod_paciente;
		case "faixaetaria":
			return faixaetaria;
		case "data_internacao":
			return data_internacao.toString();
		case "dias_internado":
			return dias_internado.toString();
		case "genero":
			return genero;
		case "cod_medico":
			return cod_medico;
		case "especialidade":
			return especialidade;
		case "tipo_especialidade":
			return tipo_especialidade;
		case "grau_dependencia":
			return grau_dependencia;
		case "numero_leito_atual":
			return numero_leito_atual;
		case "unidade_internacao_atual":
			return unidade_internacao_atual;
		case "unidade_grupo_atual":
			return unidade_grupo_atual;
		case "cobertura_leito_atual":
			return cobertura_leito_atual;
		case "cobertura_paciente":
			return cobertura_paciente;
		case "cobertura_atendimento":
			return cobertura_atendimento;
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
	public String getAtendimento() {
		return atendimento;
	}
	public void setAtendimento(String atendimento) {
		this.atendimento = atendimento;
	}
	public String getCod_paciente() {
		return cod_paciente;
	}
	public void setCod_paciente(String cod_paciente) {
		this.cod_paciente = cod_paciente;
	}
	public String getFaixaetaria() {
		return faixaetaria;
	}
	public void setFaixaetaria(String faixaetaria) {
		this.faixaetaria = faixaetaria;
	}
	public Timestamp getData_internacao() {
		return data_internacao;
	}
	public void setData_internacao(Timestamp data_internacao) {
		this.data_internacao = data_internacao;
	}
	public Integer getDias_internado() {
		return dias_internado;
	}
	public void setDias_internado(Integer dias_internado) {
		this.dias_internado = dias_internado;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getCod_medico() {
		return cod_medico;
	}
	public void setCod_medico(String cod_medico) {
		this.cod_medico = cod_medico;
	}
	public String getEspecialidade() {
		return especialidade;
	}
	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}
	public String getTipo_especialidade() {
		return tipo_especialidade;
	}
	public void setTipo_especialidade(String tipo_especialidade) {
		this.tipo_especialidade = tipo_especialidade;
	}
	public String getGrau_dependencia() {
		return grau_dependencia;
	}
	public void setGrau_dependencia(String grau_dependencia) {
		this.grau_dependencia = grau_dependencia;
	}
	public String getNumero_leito_atual() {
		return numero_leito_atual;
	}
	public void setNumero_leito_atual(String numero_leito_atual) {
		this.numero_leito_atual = numero_leito_atual;
	}
	public String getUnidade_internacao_atual() {
		return unidade_internacao_atual;
	}
	public void setUnidade_internacao_atual(String unidade_internacao_atual) {
		this.unidade_internacao_atual = unidade_internacao_atual;
	}
	public String getUnidade_grupo_atual() {
		return unidade_grupo_atual;
	}
	public void setUnidade_grupo_atual(String unidade_grupo_atual) {
		this.unidade_grupo_atual = unidade_grupo_atual;
	}
	public String getCobertura_leito_atual() {
		return cobertura_leito_atual;
	}
	public void setCobertura_leito_atual(String cobertura_leito_atual) {
		this.cobertura_leito_atual = cobertura_leito_atual;
	}
	public String getCobertura_paciente() {
		return cobertura_paciente;
	}
	public void setCobertura_paciente(String cobertura_paciente) {
		this.cobertura_paciente = cobertura_paciente;
	}
	public String getCobertura_atendimento() {
		return cobertura_atendimento;
	}
	public void setCobertura_atendimento(String cobertura_atendimento) {
		this.cobertura_atendimento = cobertura_atendimento;
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
		if (id != null) {
			sb.append(" id: ");
			sb.append(id);
		}
		if (atendimento != null) {
			sb.append(", ").append("\n");
			sb.append(" atendimento: ");
			sb.append(atendimento);
		}
		if (cod_paciente != null) {
			sb.append(", ").append("\n");
			sb.append(" cod_paciente: ");
			sb.append(cod_paciente);
		}
		if (faixaetaria != null) {
			sb.append(", ").append("\n");
			sb.append(" faixaetaria: ");
			sb.append(faixaetaria);
		}
		if (data_internacao != null) {
			sb.append(", ").append("\n");
			sb.append(" data_internacao: ");
			sb.append(data_internacao);
		}
		if (dias_internado != null) {
			sb.append(", ").append("\n");
			sb.append(" dias_internado: ");
			sb.append(dias_internado);
		}
		if (genero != null) {
			sb.append(", ").append("\n");
			sb.append(" genero: ");
			sb.append(genero);
		}
		if (cod_medico != null) {
			sb.append(", ").append("\n");
			sb.append(" cod_medico: ");
			sb.append(cod_medico);
		}
		if (especialidade != null) {
			sb.append(", ").append("\n");
			sb.append(" especialidade: ");
			sb.append(especialidade);
		}
		if (tipo_especialidade != null) {
			sb.append(", ").append("\n");
			sb.append(" tipo_especialidade: ");
			sb.append(tipo_especialidade);
		}
		if (grau_dependencia != null) {
			sb.append(", ").append("\n");
			sb.append(" grau_dependencia: ");
			sb.append(grau_dependencia);
		}
		if (numero_leito_atual != null) {
			sb.append(", ").append("\n");
			sb.append(" numero_leito_atual: ");
			sb.append(numero_leito_atual);
		}
		if (unidade_internacao_atual != null) {
			sb.append(", ").append("\n");
			sb.append(" unidade_internacao_atual: ");
			sb.append(unidade_internacao_atual);
		}
		if (unidade_grupo_atual != null) {
			sb.append(", ").append("\n");
			sb.append(" unidade_grupo_atual: ");
			sb.append(unidade_grupo_atual);
		}
		if (cobertura_leito_atual != null) {
			sb.append(", ").append("\n");
			sb.append(" cobertura_leito_atual: ");
			sb.append(cobertura_leito_atual);
		}
		if (cobertura_paciente != null) {
			sb.append(", ").append("\n");
			sb.append(" cobertura_paciente: ");
			sb.append(cobertura_paciente);
		}
		if (cobertura_atendimento != null) {
			sb.append(", ").append("\n");
			sb.append(" cobertura_atendimento: ");
			sb.append(cobertura_atendimento);
		}
		sb.append("\n").append("}");
		return sb.toString();
	}
}
