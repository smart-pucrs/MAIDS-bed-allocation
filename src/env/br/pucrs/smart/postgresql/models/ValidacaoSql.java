package br.pucrs.smart.postgresql.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ValidacaoSql {

	private String id;
	private List<InternadoSql> laudos;
	private List<PlanoValidacaoSql> planoAlocacao;
	private List<LeitoSql> leitos;
	private LocalDateTime saveAt;
	private String problema;
	private String plano;
	private String retorno;
	private boolean valido;
	private boolean concluido;
	private boolean alocar;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<InternadoSql> getLaudos() {
		return laudos;
	}
	public void setLaudos(List<InternadoSql> laudos) {
		this.laudos = laudos;
	}
	public List<PlanoValidacaoSql> getPlanoAlocacao() {
		return planoAlocacao;
	}
	public void setPlanoAlocacao(List<PlanoValidacaoSql> planoAlocacao) {
		this.planoAlocacao = planoAlocacao;
	}
	public List<LeitoSql> getLeitos() {
		return leitos;
	}
	public void setLeitos(List<LeitoSql> leitos) {
		this.leitos = leitos;
	}
	public LocalDateTime getSaveAt() {
		return saveAt;
	}
	public void setSaveAt(LocalDateTime localDateTime) {
		this.saveAt = localDateTime;
	}
	public String getProblema() {
		return problema;
	}
	public void setProblema(String problema) {
		this.problema = problema;
	}
	public String getPlano() {
		return plano;
	}
	public void setPlano(String plano) {
		this.plano = plano;
	}
	public String getRetorno() {
		return retorno;
	}
	public void setRetorno(String retorno) {
		this.retorno = retorno;
	}
	public boolean isValido() {
		return valido;
	}
	public void setValido(boolean valido) {
		this.valido = valido;
	}
	public boolean isConcluido() {
		return concluido;
	}
	public void setConcluido(boolean concluido) {
		this.concluido = concluido;
	}
	public boolean isAlocar() {
		return alocar;
	}
	public void setAlocar(boolean alocar) {
		this.alocar = alocar;
	}
	public void addinLaudos(InternadoSql laudo) {
		if (this.laudos != null) {
			this.laudos.add(laudo);
		} else {
			this.laudos = new ArrayList<InternadoSql>();
			this.laudos.add(laudo);
		}
	}
	public void addLeitos(LeitoSql leito) {
		if (this.leitos != null) {
			this.leitos.add(leito);
		} else {
			this.leitos = new ArrayList<LeitoSql>();
			this.leitos.add(leito);
		}
	}
	public void addPlanoAlocacao(PlanoValidacaoSql plano) {
		if (this.planoAlocacao != null) {
			this.planoAlocacao.add(plano);
		} else {
			this.planoAlocacao = new ArrayList<PlanoValidacaoSql>();
			this.planoAlocacao.add(plano);
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
	    if (saveAt != null) {
	        sb.append(", ").append("\n");
	        sb.append(" saveAt: ");
	        sb.append(saveAt.toString());
	    }
	    if (problema != null) {
	        sb.append(", ").append("\n");
	        sb.append(" problema: ");
	        sb.append(problema);
	    }
	    if (plano != null) {
	        sb.append(", ").append("\n");
	        sb.append(" plano: ");
	        sb.append(plano);
	    }
	    if (retorno != null) {
	        sb.append(", ").append("\n");
	        sb.append(" retorno: ");
	        sb.append(retorno);
	    }
	    sb.append(", ").append("\n");
	    sb.append(" valido: ");
	    sb.append(valido);
	    sb.append(", ").append("\n");
	    sb.append(" concluido: ");
	    sb.append(concluido);
	    sb.append(", ").append("\n");
	    sb.append(" alocar: ");
	    sb.append(alocar);
	    if (laudos != null) {
	        sb.append(", ").append("\n");
	        sb.append(" laudos: ");
	        sb.append(laudos.toString());
	    }
	    if (planoAlocacao != null) {
	        sb.append(", ").append("\n");
	        sb.append(" planoAlocacao: ");
	        sb.append(planoAlocacao.toString());
	    }
	    if (leitos != null) {
	        sb.append(", ").append("\n");
	        sb.append(" leitos: ");
	        sb.append(leitos.toString());
	    }
	    sb.append("\n").append("}");
		return sb.toString();
	}
}
