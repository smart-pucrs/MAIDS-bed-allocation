package br.pucrs.smart.postgresql.models;

import java.util.ArrayList;
import java.util.List;

public class AlocacaoOtimizadaSql {

	private Integer id;
	private Boolean allAllocated;
	private List<AlocacaoSugeridaSql> alocacoesSugeridas;
	private List<Integer> notAllocated; // List with idPaciente
	private Boolean alreadySuggested;
	private Boolean alocar;
	private Boolean concluido;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean isAllAllocated() {
		return allAllocated;
	}

	public void setAllAllocated(Boolean allAllocated) {
		this.allAllocated = allAllocated;
	}

	public List<AlocacaoSugeridaSql> getAlocacoesSugeridas() {
		return alocacoesSugeridas;
	}

	public void setAlocacoesSugeridas(List<AlocacaoSugeridaSql> alocacoesSugeridas) {
		this.alocacoesSugeridas = alocacoesSugeridas;
	}
	public List<Integer> getNotAllocated() {
		return notAllocated;
	}

	public void setNotAllocated(List<Integer> notAllocated) {
		this.notAllocated = notAllocated;
	}

	
	public void addAlocacaoSugerida(AlocacaoSugeridaSql alocacaoSugerida) {
		if (this.alocacoesSugeridas != null) {
			this.alocacoesSugeridas.add(alocacaoSugerida);
		} else {
			this.alocacoesSugeridas = new ArrayList<AlocacaoSugeridaSql>();
			this.alocacoesSugeridas.add(alocacaoSugerida);
		}
	}
	public void addNotAllocated(Integer idPaciente) {
		if (this.notAllocated != null) {
			this.notAllocated.add(idPaciente);
		} else {
			this.notAllocated = new ArrayList<Integer>();
			this.notAllocated.add(idPaciente);
		}
	}

	public Boolean isAlreadySuggested() {
		return alreadySuggested;
	}

	public void setAlreadySuggested(Boolean alreadySuggested) {
		this.alreadySuggested = alreadySuggested;
	}

	public Boolean isAlocar() {
		return alocar;
	}

	public void setAlocar(Boolean alocar) {
		this.alocar = alocar;
	}

	public Boolean isConcluido() {
		return concluido;
	}

	public void setConcluido(Boolean concluido) {
		this.concluido = concluido;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
	    if (id != null) {
	        sb.append(" id: ");
	        sb.append(id);
	    }
	    sb.append(", ").append("\n");
	    sb.append(" allAllocated: ");
	    sb.append(allAllocated);
	    if (alocacoesSugeridas != null) {
	        sb.append(", ").append("\n");
	        sb.append(" alocacoesSugeridas: ");
	        sb.append(alocacoesSugeridas);
	    }
	    if (notAllocated != null) {
	        sb.append(", ").append("\n");
	        sb.append(" notAllocated: [").append("\n");
	        notAllocated.forEach(n -> {
	            sb.append("  ").append(n).append(",\n");
	        });
	        sb.append(" ]");
	    }
	    sb.append(", ").append("\n");
	    sb.append(" alreadySuggested: ");
	    sb.append(alreadySuggested);
	    sb.append(", ").append("\n");
	    sb.append(" alocar: ");
	    sb.append(alocar);
	    sb.append(", ").append("\n");
	    sb.append(" concluido: ");
	    sb.append(concluido);
	    sb.append("\n").append("}");
		return sb.toString();
	}


}
