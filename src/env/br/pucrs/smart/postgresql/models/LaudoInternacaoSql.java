package br.pucrs.smart.postgresql.models;

import java.sql.Date;

public class LaudoInternacaoSql {

	private Integer id;
	private String faixaEtaria;
	private boolean ativo;
	private String crmMedico;
	private Date dataAlta;
	private Date dataInternacao;
	private String especialidade;
	private String genero;
	private boolean internado;
	private String leitoNum;
	private String medicoResponsavel;
	private String nomePaciente;
	private String tipoDeCuidado;
	private String tipoDeEncaminhamento;
	private String tipoDeEstadia;
	private String tipoDeLeito;
	private String tipoDeIsolamento;
	private String tipoDePuerperio;
	private String classificacaoDeRisco;

	
	public String get(String param) {
		switch (param) {
		case "faixaEtaria":
			return faixaEtaria;
		case "especialidade":
			return especialidade;
		case "genero":
			return genero;
		case "id":
			return id.toString();
		case "nomePaciente":
			return nomePaciente;
		case "tipoDeCuidado":
			return tipoDeCuidado;
		case "tipoDeEncaminhamento":
			return tipoDeEncaminhamento;
		case "tipoDeEstadia":
			return tipoDeEstadia;
		case "tipoDeLeito":
			return tipoDeLeito;
		case "tipoDeIsolamento":
			return tipoDeIsolamento;
		case "tipoDePuerperio":
			return tipoDePuerperio;
		case "classificacaoDeRisco":
			return classificacaoDeRisco;
		case "leitoNum":
			return leitoNum;
		default:
			return null;
		}
	}
	
	public String getFaixaEtaria() {
		return faixaEtaria;
	}

	public void setFaixaEtaria(String faixaEtaria) {
		this.faixaEtaria = faixaEtaria;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public String getCrmMedico() {
		return crmMedico;
	}

	public void setCrmMedico(String crmMedico) {
		this.crmMedico = crmMedico;
	}

	public Date getDataAlta() {
		return dataAlta;
	}

	public void setDataAlta(Date dataAlta) {
		this.dataAlta = dataAlta;
	}

	public Date getDataInternacao() {
		return dataInternacao;
	}

	public void setDataInternacao(Date dataInternacao) {
		this.dataInternacao = dataInternacao;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isInternado() {
		return internado;
	}

	public void setInternado(boolean internado) {
		this.internado = internado;
	}

	public String getLeitoNum() {
		return leitoNum;
	}

	public void setLeitoNum(String leitoNum) {
		this.leitoNum = leitoNum;
	}

	public String getMedicoResponsavel() {
		return medicoResponsavel;
	}

	public void setMedicoResponsavel(String medicoResponsavel) {
		this.medicoResponsavel = medicoResponsavel;
	}

	public String getNomePaciente() {
		return nomePaciente;
	}

	public void setNomePaciente(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}

	public String getTipoDeCuidado() {
		return tipoDeCuidado;
	}

	public void setTipoDeCuidado(String tipoDeCuidado) {
		this.tipoDeCuidado = tipoDeCuidado;
	}

	public String getTipoDeEncaminhamento() {
		return tipoDeEncaminhamento;
	}

	public void setTipoDeEncaminhamento(String tipoDeEncaminhamento) {
		this.tipoDeEncaminhamento = tipoDeEncaminhamento;
	}

	public String getTipoDeEstadia() {
		return tipoDeEstadia;
	}

	public void setTipoDeEstadia(String tipoDeEstadia) {
		this.tipoDeEstadia = tipoDeEstadia;
	}

	public String getTipoDeLeito() {
		return tipoDeLeito;
	}

	public void setTipoDeLeito(String tipoDeLeito) {
		this.tipoDeLeito = tipoDeLeito;
	}

	public String getTipoDeIsolamento() {
		return tipoDeIsolamento;
	}

	public void setTipoDeIsolamento(String tipoDeIsolamento) {
		this.tipoDeIsolamento = tipoDeIsolamento;
	}

	public String getTipoDePuerperio() {
		return tipoDePuerperio;
	}

	public void setTipoDePuerperio(String tipoDePuerperio) {
		this.tipoDePuerperio = tipoDePuerperio;
	}

	public String getClassificacaoDeRisco() {
		return classificacaoDeRisco;
	}

	public void setClassificacaoDeRisco(String classificacaoDeRisco) {
		this.classificacaoDeRisco = classificacaoDeRisco;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
		if (id != null) {
			sb.append(" id: ");
			sb.append(id);
		}
		if (nomePaciente != null) {
			sb.append(", ").append("\n");
			sb.append(" nomePaciente: ");
			sb.append(nomePaciente);
		}
		if (faixaEtaria != null) {
			sb.append(", ").append("\n");
			sb.append(" faixaEtaria: ");
			sb.append(faixaEtaria);
		}
		sb.append(", ").append("\n");
		sb.append(" ativo: ");
		sb.append(ativo);
		if (crmMedico != null) {
			sb.append(", ").append("\n");
			sb.append(" crmMedico: ");
			sb.append(crmMedico);
		}
		if (dataAlta != null) {
			sb.append(", ").append("\n");
			sb.append(" dataAlta: ");
			sb.append(dataAlta);
		}
		if (dataInternacao != null) {
			sb.append(", ").append("\n");
			sb.append(" dataInternacao: ");
			sb.append(dataInternacao);
		}
		if (especialidade != null) {
			sb.append(", ").append("\n");
			sb.append(" especialidade: ");
			sb.append(especialidade);
		}
		if (genero != null) {
			sb.append(", ").append("\n");
			sb.append(" genero: ");
			sb.append(genero);
		}
		sb.append(", ").append("\n");
		sb.append(" internado: ");
		sb.append(internado);

		if (leitoNum != null) {
			sb.append(", ").append("\n");
			sb.append(" idLeito: ");
			sb.append(leitoNum);
		}
		if (medicoResponsavel != null) {
			sb.append(", ").append("\n");
			sb.append(" medicoResponsavel: ");
			sb.append(medicoResponsavel);
		}
		if (tipoDeCuidado != null) {
			sb.append(", ").append("\n");
			sb.append(" tipoDeCuidado: ");
			sb.append(tipoDeCuidado);
		}
		if (tipoDeEncaminhamento != null) {
			sb.append(", ").append("\n");
			sb.append(" tipoDeEncaminhamento: ");
			sb.append(tipoDeEncaminhamento);
		}
		if (tipoDeEstadia != null) {
			sb.append(", ").append("\n");
			sb.append(" tipoDeEstadia: ");
			sb.append(tipoDeEstadia);
		}
		if (tipoDeLeito != null) {
			sb.append(", ").append("\n");
			sb.append(" tipoDeLeito: ");
			sb.append(tipoDeLeito);
		}
		if (tipoDeIsolamento != null) {
			sb.append(", ").append("\n");
			sb.append(" tipoDeIsolamento: ");
			sb.append(tipoDeIsolamento);
		}
		if (tipoDePuerperio != null) {
			sb.append(", ").append("\n");
			sb.append(" tipoDePuerperio: ");
			sb.append(tipoDePuerperio);
		}
		if (classificacaoDeRisco != null) {
			sb.append(", ").append("\n");
			sb.append(" classificacaoDeRisco: ");
			sb.append(classificacaoDeRisco);
		}
		sb.append("\n").append("}");
		return sb.toString();
	}
}
