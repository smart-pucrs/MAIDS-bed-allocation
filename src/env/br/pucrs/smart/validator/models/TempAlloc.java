package br.pucrs.smart.validator.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TempAlloc {
	
	private boolean validated;
	private List<SimpleAllocation> allocation;
	private LocalDateTime saveAt;
	private int id;
	
	public boolean isValidated() {
		return validated;
	}
	public void setValidated(boolean validated) {
		this.validated = validated;
	}
	public List<SimpleAllocation> getAllocation() {
		return allocation;
	}
	public void setAllocation(List<SimpleAllocation> allocation) {
		this.allocation = allocation;
	}
	public void addAllocation(SimpleAllocation allocation) {
		if (this.allocation != null) {
			this.allocation.add(allocation);
		} else {
			this.allocation = new ArrayList<SimpleAllocation>();
			this.allocation.add(allocation);
		}
	}
	public LocalDateTime getSaveAt() {
		return saveAt;
	}
	public void setSaveAt(LocalDateTime saveAt) {
		this.saveAt = saveAt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("{ ");
        	sb.append(" id : ");
        	sb.append(id);
        	sb.append(", ");
	    sb.append(" validated : ");
        sb.append(validated);
        sb.append(", ");
        if (allocation != null) {
	        sb.append(" allocation : [");
	        for (SimpleAllocation a : allocation) {
	        	sb.append(" { ");
		        if (a.getLaudoInternacaoId() != null) {
		        	sb.append(" laudoInternacaoId : ");
		        	sb.append(a.getLaudoInternacaoId());
		        	sb.append(", ");
		        }
		        if (a.getLeitoNum() != null) {
		        	sb.append(" leitoNum : ");
		        	sb.append(a.getLeitoNum());
		        }
		        sb.append(" }, ");
	        }
	        sb.append("] ");
	    }
        	sb.append(", ");
        	sb.append(" saveAt : ");
        	sb.append(saveAt);
	    sb.append("} ");
		return sb.toString();
	}
	
	
}
