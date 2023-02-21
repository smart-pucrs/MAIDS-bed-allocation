package br.pucrs.smart.postgresql.models;

public class PlanoValidacaoSql {
	private int id;
	private String validacaoId;
	private int laudoInternacaoId;
	private String leitoNum;
	private boolean isvalid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValidacaoId() {
		return validacaoId;
	}

	public void setValidacaoId(String validacaoId) {
		this.validacaoId = validacaoId;
	}

	public int getLaudoInternacaoId() {
		return laudoInternacaoId;
	}

	public void setLaudoInternacaoId(int laudoInternacaoId) {
		this.laudoInternacaoId = laudoInternacaoId;
	}

	public String getLeitoNum() {
		return leitoNum;
	}

	public void setLeitoNum(String leitoNum) {
		this.leitoNum = leitoNum;
	}

	public boolean isIsvalid() {
		return isvalid;
	}

	public void setIsvalid(boolean isvalid) {
		this.isvalid = isvalid;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
		sb.append(" id: ");
		sb.append(id);
		if (validacaoId != null) {
			sb.append(", ").append("\n");
			sb.append(" validacaoId: ");
			sb.append(validacaoId);
		}
		sb.append(", ").append("\n");
		sb.append(" laudoInternacaoId: ");
		sb.append(laudoInternacaoId);
		if (leitoNum != null) {
			sb.append(", ").append("\n");
			sb.append(" leitoNum: ");
			sb.append(leitoNum);
		}
		sb.append("\n").append("}");
		return sb.toString();
	}

}
