package br.pucrs.smart.validator.models;

public class SimpleAllocation {

	private Integer id;
	private Integer alocacaoTemporariaId;
	private Integer laudoInternacaoId;
	private String leitoNum;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAlocacaoTemporariaId() {
		return alocacaoTemporariaId;
	}
	public void setAlocacaoTemporariaId(Integer alocacaoTemporariaId) {
		this.alocacaoTemporariaId = alocacaoTemporariaId;
	}
	public Integer getLaudoInternacaoId() {
		return laudoInternacaoId;
	}
	public void setLaudoInternacaoId(Integer laudoInternacaoId) {
		this.laudoInternacaoId = laudoInternacaoId;
	}
	public String getLeitoNum() {
		return leitoNum;
	}
	public void setLeitoNum(String leitoNum) {
		this.leitoNum = leitoNum;
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
        if (alocacaoTemporariaId != null) {
        	sb.append(" alocacaoTemporariaId : ");
        	sb.append(alocacaoTemporariaId);
        }
        if (laudoInternacaoId != null) {
        	sb.append(" laudoInternacaoId : ");
        	sb.append(laudoInternacaoId);
        	sb.append(", ");
        }
        if (leitoNum != null) {
        	sb.append(" leitoNum : ");
        	sb.append(leitoNum);
        }
        sb.append("} ");
		return sb.toString();
	}
	
}
