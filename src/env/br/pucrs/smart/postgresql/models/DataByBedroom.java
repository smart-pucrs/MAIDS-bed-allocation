package br.pucrs.smart.postgresql.models;

import java.util.ArrayList;
import java.util.List;

public class DataByBedroom {

    private String quarto;
    private List<DataByBed> leitos;
    private String genero;
    private String especialidade;
    private String tipo_especialidade;
    private String faixa_etaria;
	private List<String> cds_categoria_isolamento;
    private String status_quarto;
	private Boolean preferencialmente_isolamento;
	private String especialidade_preferencial;
    private String convenio;
    private String acomodacao;

	public DataByBedroom(String quarto, List<DataByBed> leitos) {
        this.quarto = quarto;
        this.leitos = leitos;
    }

    
    public void addleito(DataByBed leito) {
		if (this.leitos != null) {
			this.leitos.add(leito);
		} else {
			this.leitos = new ArrayList<DataByBed>();
			this.leitos.add(leito);
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

	public String getQuarto() {
		return quarto;
	}
	public void setQuarto(String quarto) {
		this.quarto = quarto;
	}
	public List<DataByBed> getLeitos() {
		return leitos;
	}
	public void setLeitos(List<DataByBed> leitos) {
		this.leitos = leitos;
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
	public List<String> getCds_categoria_isolamento() {
		return cds_categoria_isolamento;
	}
	public void setCds_categoria_isolamento(List<String> cds_categoria_isolamento) {
		this.cds_categoria_isolamento = cds_categoria_isolamento;
	}
    public String getStatus_quarto() {
		return status_quarto;
	}
	public void setStatus_quarto(String status_quarto) {
		this.status_quarto = status_quarto;
	}
	public Boolean getPreferencialmente_isolamento() {
		return preferencialmente_isolamento;
	}
	public void setPreferencialmente_isolamento(Boolean preferencialmente_isolamento) {
		this.preferencialmente_isolamento = preferencialmente_isolamento;
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
	public String getAcomodacao() {
		return acomodacao;
	}
	public void setAcomodacao(String acomodacao) {
		this.acomodacao = acomodacao;
	}

    @Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
		if (quarto != null) {
			sb.append(" id: ");
			sb.append(quarto);
		}
        if (leitos != null) {
            sb.append(" leitos:  [");
	        for (DataByBed l : leitos) {
		        	sb.append(l.toString());
		        	sb.append(", ");
		        }
	        sb.append("], ");
        }
        if (genero != null) {
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
        if (cds_categoria_isolamento != null) {
            sb.append(" cds_categoria_isolamento:  [");
            for (String c : cds_categoria_isolamento) {
                	sb.append(c);
                	sb.append(", ");
                }
            sb.append("], ");
        }
        if (preferencialmente_isolamento != null) {
            sb.append(" preferencialmente_isolamento: ");
            sb.append(preferencialmente_isolamento);
        }
        if (especialidade_preferencial != null) {
            sb.append(" especialidade_preferencial: ");
            sb.append(especialidade_preferencial);
        }
        if (convenio != null) {
            sb.append(" convenio: ");
            sb.append(convenio);
        }
		if (acomodacao != null) {
			sb.append(" acomodacao: ");
			sb.append(acomodacao);
		}
        sb.append("}").append("\n");
        return sb.toString();
    }

}
