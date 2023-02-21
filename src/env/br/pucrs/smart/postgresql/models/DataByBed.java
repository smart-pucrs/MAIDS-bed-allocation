package br.pucrs.smart.postgresql.models;

import java.util.ArrayList;
import java.util.List;

public class DataByBed {

	private Integer id;
	private String acomodacao;
	private String genero;
    private String especialidade;
    private String tipo_especialidade;
    private String faixa_etaria;
	private String numero;
	private String quarto;
	private String status_leito;
	private Boolean preferencialmente_isolamento;
	private List<String> cds_categoria_isolamento;
	private String especialidade_preferencial;
	private String convenio;
    private DataByPatient paciente;
	
	public String get(String param) {
		switch (param) {
		case "id":
			return id.toString();
		case "acomodacao":
			return acomodacao;
		case "genero":
			return genero;
        case "especialidade":
            return especialidade;
        case "tipo_especialidade":
            return tipo_especialidade;
        case "faixa_etaria":
            return faixa_etaria;
		case "numero":
			return numero;
		case "quarto":
			return quarto;
		case "status_leito":
			return status_leito;
		case "preferencialmente_isolamento":
			return preferencialmente_isolamento.toString();
        case "cds_categoria_isolamento":
            return cds_categoria_isolamento.toString();
		case "especialidade_preferencial":
			return especialidade_preferencial;
		case "convenio":
			return convenio;
        case "paciente":
            return paciente.getCod_paciente();
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

	public String getFaixa_etaria() {
		return faixa_etaria;
	}

	public void setFaixa_etaria(String faixa_etaria) {
		this.faixa_etaria = faixa_etaria;
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

	public List<String> getCds_categoria_isolamento() {
		return cds_categoria_isolamento;
	}

	public void setCds_categoria_isolamento(List<String> cds_categoria_isolamento) {
		this.cds_categoria_isolamento = cds_categoria_isolamento;
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

	public DataByPatient getPaciente() {
		return paciente;
	}

	public void setPaciente(DataByPatient paciente) {
		this.paciente = paciente;
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
        if (faixa_etaria != null) {
            sb.append(", ").append("\n");
            sb.append(" faixa_etaria: ");
            sb.append(faixa_etaria);
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
        if (cds_categoria_isolamento != null) {
            sb.append(" cds_categoria_isolamento:  [");
            for (String c : cds_categoria_isolamento) {
                	sb.append(c);
                	sb.append(", ");
                }
            sb.append("], ");
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
        if (paciente != null) {
            sb.append(", ").append("\n");
            sb.append(" paciente: ");
            sb.append(paciente.toString());
        }
		sb.append("\n").append("}");
		return sb.toString();
	}
	
}
