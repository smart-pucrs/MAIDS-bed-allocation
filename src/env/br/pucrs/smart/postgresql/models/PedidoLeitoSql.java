package br.pucrs.smart.postgresql.models;

import java.sql.Timestamp;

public class PedidoLeitoSql {

	private Integer id;
	private String tipo_solicitacao;
	private String atendimento;
	private Timestamp dthr_atendimento;
	private String cod_paciente;
	private String convenio;
	private String cobertura;
	private Timestamp dthr_solic_transferencia;
	private Timestamp dthr_pre_internacao;
	private String leito_origem;
	private String leito_sugerido;
	private String cd_leito_reserva;
	private String sn_prioridade;
	private String faixaetaria;
	private String genero;
	private String especialidade;
	private String tipo_especialidade;
	private String cod_medico;
	
	public String get(String param) {
		switch (param) {
		case "id":
			return id.toString();
		case "tipo_solicitacao":
			return tipo_solicitacao;
		case "atendimento":
			return atendimento;
		case "dthr_atendimento":
			return dthr_atendimento.toString();
		case "cod_paciente":
			return cod_paciente;
		case "convenio":
			return convenio;
		case "cobertura":
			return cobertura;
		case "dthr_solic_transferencia":
			return dthr_solic_transferencia.toString();
		case "dthr_pre_internacao":
			return dthr_pre_internacao.toString();
		case "leito_origem":
			return leito_origem;
		case "leito_sugerido":
			return leito_sugerido;
		case "cd_leito_reserva":
			return cd_leito_reserva;
		case "sn_prioridade":
			return sn_prioridade;
		case "faixaetaria":
			return faixaetaria;
		case "genero":
			return genero;
		case "especialidade":
			return especialidade;
		case "tipo_especialidade":
			return tipo_especialidade;
		case "cod_medico":
			return cod_medico;
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

	public String getTipo_solicitacao() {
		return tipo_solicitacao;
	}

	public void setTipo_solicitacao(String tipo_solicitacao) {
		this.tipo_solicitacao = tipo_solicitacao;
	}

	public String getAtendimento() {
		return atendimento;
	}

	public void setAtendimento(String atendimento) {
		this.atendimento = atendimento;
	}

	public Timestamp getDthr_atendimento() {
		return dthr_atendimento;
	}

	public void setDthr_atendimento(Timestamp dthr_atendimento) {
		this.dthr_atendimento = dthr_atendimento;
	}

	public String getCod_paciente() {
		return cod_paciente;
	}

	public void setCod_paciente(String cod_paciente) {
		this.cod_paciente = cod_paciente;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	public String getCobertura() {
		return cobertura;
	}

	public void setCobertura(String cobertura) {
		this.cobertura = cobertura;
	}

	public Timestamp getDthr_solic_transferencia() {
		return dthr_solic_transferencia;
	}

	public void setDthr_solic_transferencia(Timestamp dthr_solic_transferencia) {
		this.dthr_solic_transferencia = dthr_solic_transferencia;
	}

	public Timestamp getDthr_pre_internacao() {
		return dthr_pre_internacao;
	}

	public void setDthr_pre_internacao(Timestamp dthr_pre_internacao) {
		this.dthr_pre_internacao = dthr_pre_internacao;
	}

	public String getLeito_origem() {
		return leito_origem;
	}

	public void setLeito_origem(String leito_origem) {
		this.leito_origem = leito_origem;
	}

	public String getLeito_sugerido() {
		return leito_sugerido;
	}

	public void setLeito_sugerido(String leito_sugerido) {
		this.leito_sugerido = leito_sugerido;
	}

	public String getCd_leito_reserva() {
		return cd_leito_reserva;
	}

	public void setCd_leito_reserva(String cd_leito_reserva) {
		this.cd_leito_reserva = cd_leito_reserva;
	}

	public String getSn_prioridade() {
		return sn_prioridade;
	}

	public void setSn_prioridade(String sn_prioridade) {
		this.sn_prioridade = sn_prioridade;
	}

	public String getFaixaetaria() {
		return faixaetaria;
	}

	public void setFaixaetaria(String faixaetaria) {
		this.faixaetaria = faixaetaria;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
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

	public String getCod_medico() {
		return cod_medico;
	}

	public void setCod_medico(String cod_medico) {
		this.cod_medico = cod_medico;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
		if (id != null) {
			sb.append(" id: ");
			sb.append(id);
		}
		if (tipo_solicitacao != null) {
			sb.append(", ").append("\n");
			sb.append(" tipo_solicitacao: ");
			sb.append(tipo_solicitacao);
		}
		if (atendimento != null) {
			sb.append(", ").append("\n");
			sb.append(" atendimento: ");
			sb.append(atendimento);
		}
		if (dthr_atendimento != null) {
			sb.append(", ").append("\n");
			sb.append(" dthr_atendimento: ");
			sb.append(dthr_atendimento);
		}
		if (cod_paciente != null) {
			sb.append(", ").append("\n");
			sb.append(" cod_paciente: ");
			sb.append(cod_paciente);
		}
		if (convenio != null) {
			sb.append(", ").append("\n");
			sb.append(" convenio: ");
			sb.append(convenio);
		}
		if (cobertura != null) {
			sb.append(", ").append("\n");
			sb.append(" cobertura: ");
			sb.append(cobertura);
		}
		if (dthr_solic_transferencia != null) {
			sb.append(", ").append("\n");
			sb.append(" dthr_solic_transferencia: ");
			sb.append(dthr_solic_transferencia);
		}
		if (dthr_pre_internacao != null) {
			sb.append(", ").append("\n");
			sb.append(" dthr_pre_internacao: ");
			sb.append(dthr_pre_internacao);
		}
		if (leito_origem != null) {
			sb.append(", ").append("\n");
			sb.append(" leito_origem: ");
			sb.append(leito_origem);
		}
		if (leito_sugerido != null) {
			sb.append(", ").append("\n");
			sb.append(" leito_sugerido: ");
			sb.append(leito_sugerido);
		}
		if (cd_leito_reserva != null) {
			sb.append(", ").append("\n");
			sb.append(" cd_leito_reserva: ");
			sb.append(cd_leito_reserva);
		}
		if (sn_prioridade != null) {
			sb.append(", ").append("\n");
			sb.append(" sn_prioridade: ");
			sb.append(sn_prioridade);
		}
		if (faixaetaria != null) {
			sb.append(", ").append("\n");
			sb.append(" faixaetaria: ");
			sb.append(faixaetaria);
		}
		if (genero != null) {
			sb.append(", ").append("\n");
			sb.append(" genero: ");
			sb.append(genero);
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
		if (cod_medico != null) {
			sb.append(", ").append("\n");
			sb.append(" cod_medico: ");
			sb.append(cod_medico);
		}		
		sb.append("\n").append("}");
		return sb.toString();
	}

}
