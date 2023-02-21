package br.pucrs.smart.optimiser;

import java.util.ArrayList;
import java.util.List;

import br.pucrs.smart.postgresql.models.LaudoInternacaoSql;
import br.pucrs.smart.postgresql.models.LeitoSql;

public class OptimiserResult {

	private boolean allAllocated;
	private List<Allocation> sugestedAllocation; // use only idPaciente and leito
	private List<String> notAllocated; // List with idPaciente
	private List<LeitoSql> leitosData;
	private List<LaudoInternacaoSql> laudosData;
	private boolean alreadySuggested;
	private String id;
	private boolean alocar;
	private boolean concluido;

	public boolean isAllAllocated() {
		return allAllocated;
	}

	public void setAllAllocated(boolean allAllocated) {
		this.allAllocated = allAllocated;
	}

	public List<Allocation> getSugestedAllocation() {
		return sugestedAllocation;
	}

	public void setSugestedAllocation(List<Allocation> sugestedAllocation) {
		this.sugestedAllocation = sugestedAllocation;
	}

	public void addSugestedAllocation(Allocation allocation) {
		if (this.sugestedAllocation != null) {
			this.sugestedAllocation.add(allocation);
		} else {
			this.sugestedAllocation = new ArrayList<Allocation>();
			this.sugestedAllocation.add(allocation);
		}
	}

	public List<String> getNotAllocated() {
		return notAllocated;
	}

	public void setNotAllocated(List<String> notAllocated) {
		this.notAllocated = notAllocated;
	}

	public void addNotAllocated(String idPaciente) {
		if (this.notAllocated != null) {
			this.notAllocated.add(idPaciente);
		} else {
			this.notAllocated = new ArrayList<String>();
			this.notAllocated.add(idPaciente);
		}
	}

	public List<LeitoSql> getLeitosData() {
		return leitosData;
	}

	public void setLeitosData(List<LeitoSql> leitosData) {
		this.leitosData = leitosData;
	}

	public void addLeitosData(LeitoSql leito) {
		if (this.leitosData != null) {
			this.leitosData.add(leito);
		} else {
			this.leitosData = new ArrayList<LeitoSql>();
			this.leitosData.add(leito);
		}
	}

	public List<LaudoInternacaoSql> getLaudosData() {
		return laudosData;
	}

	public void setLaudosData(List<LaudoInternacaoSql> laudosData) {
		this.laudosData = laudosData;
	}

	public void addLaudosData(LaudoInternacaoSql laudo) {
		if (this.laudosData != null) {
			this.laudosData.add(laudo);
		} else {
			this.laudosData = new ArrayList<LaudoInternacaoSql>();
			this.laudosData.add(laudo);
		}
	}

	public boolean isAlreadySuggested() {
		return alreadySuggested;
	}

	public void setAlreadySuggested(boolean alreadySuggested) {
		this.alreadySuggested = alreadySuggested;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isAlocar() {
		return alocar;
	}

	public void setAlocar(boolean alocar) {
		this.alocar = alocar;
	}

	public boolean isConcluido() {
		return concluido;
	}

	public void setConcluido(boolean concluido) {
		this.concluido = concluido;
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		if (id != null) {
			sb.append(" id : ");
			sb.append(id);
			sb.append(", ");
		}
		sb.append(" allAllocated : ");
		sb.append(allAllocated);
		if (sugestedAllocation != null) {
			sb.append(", ");
			sb.append(" sugestedAllocation : [");
			for (Allocation a : sugestedAllocation) {
				sb.append(a.toString());
			}
			sb.append("] ");
		}
		if (notAllocated != null) {
			sb.append(", ");
			sb.append(" notAllocated : [");
			for (String s : notAllocated) {
				sb.append(s);
				sb.append(", ");
			}
			sb.append("] ");
		}
		if (leitosData != null) {
			sb.append(", ");
			sb.append(" leitosData : [");
			for (LeitoSql l : leitosData) {
				sb.append(l.toString());
				sb.append(", ");
			}
			sb.append("] ");
		}
		if (laudosData != null) {
			sb.append(", ");
			sb.append(" laudosData : [");
			for (LaudoInternacaoSql l : laudosData) {
				sb.append(l.toString());
				sb.append(", ");
			}
			sb.append("] ");
		}
		sb.append(", ");
		sb.append(" alreadySuggested : ");
		sb.append(alreadySuggested);
		sb.append(", ");
		sb.append(" alocar : ");
		sb.append(alocar);
		sb.append(", ");
		sb.append(" concluido : ");
		sb.append(concluido);
		sb.append("} ");
		return sb.toString();
	}

}
