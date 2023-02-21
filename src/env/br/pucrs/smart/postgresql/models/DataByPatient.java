package br.pucrs.smart.postgresql.models;

import java.util.ArrayList;
import java.util.List;

public class DataByPatient {

	private Integer id;
	private String cod_paciente;
	private String faixa_etaria;
	private String genero;
	private String especialidade;
	private String tipo_especialidade;
	private String numero_leito_atual;
	private List<String> cds_categoria_isolamento;
	private String tipo_solicitacao;
	private String convenio;
	private String cobertura;

	public String get(String param) {
		switch (param) {
		case "id":
			return id.toString();
		case "cod_paciente":
			return cod_paciente;
		case "faixa_etaria":
			return faixa_etaria;
		case "genero":
			return genero;
		case "especialidade":
			return especialidade;
		case "tipo_especialidade":
			return tipo_especialidade;
		case "numero_leito_atual":
			return numero_leito_atual;
		case "tipo_solicitacao":
			return tipo_solicitacao;
		case "convenio":
			return convenio;
		case "cobertura":
			return cobertura;
		default:
			return null;
		}
	}
    
    public void addCd_categoria_isolamento(String cd_categoria_isolamento) {
		if (this.cds_categoria_isolamento != null) {
			this.cds_categoria_isolamento.add(cd_categoria_isolamento);
		} else {
			this.cds_categoria_isolamento = new ArrayList<String>();
			this.cds_categoria_isolamento.add(cd_categoria_isolamento);
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
	public String getFaixa_etaria() {
		return faixa_etaria;
	}
	public void setFaixa_etaria(String faixa_etaria) {
		this.faixa_etaria = faixa_etaria;
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
	public String getNumero_leito_atual() {
		return numero_leito_atual;
	}
	public void setNumero_leito_atual(String numero_leito_atual) {
		this.numero_leito_atual = numero_leito_atual;
	}
	public List<String> getCds_categoria_isolamento() {
		return cds_categoria_isolamento;
	}
	public void setCds_categoria_isolamento(List<String> cds_categoria_isolamento) {
		this.cds_categoria_isolamento = cds_categoria_isolamento;
	}
	public String getTipo_solicitacao() {
		return tipo_solicitacao;
	}
	public void setTipo_solicitacao(String tipo_solicitacao) {
		this.tipo_solicitacao = tipo_solicitacao;
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
		if (faixa_etaria != null) {
			sb.append(", ").append("\n");
			sb.append(" faixa_etaria: ");
			sb.append(faixa_etaria);
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
		if (numero_leito_atual != null) {
			sb.append(", ").append("\n");
			sb.append(" numero_leito_atual: ");
			sb.append(numero_leito_atual);
		}
		if (cds_categoria_isolamento != null) {
            sb.append(" cds_categoria_isolamento:  [");
            for (String c : cds_categoria_isolamento) {
                	sb.append(c);
                	sb.append(", ");
                }
            sb.append("], ");
        }
		if (tipo_solicitacao != null) {
			sb.append(", ").append("\n");
			sb.append(" tipo_solicitacao: ");
			sb.append(tipo_solicitacao);
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
		sb.append("\n").append("}");
		return sb.toString();
	}

}
