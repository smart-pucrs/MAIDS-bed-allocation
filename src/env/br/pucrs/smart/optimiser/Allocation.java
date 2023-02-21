package br.pucrs.smart.optimiser;

import br.pucrs.smart.postgresql.models.LaudoInternacaoSql;
import br.pucrs.smart.postgresql.models.LeitoSql;

public class Allocation {

	private String idPaciente;
	private String leito;
	private LeitoSql leitoData;
	private LaudoInternacaoSql laudo;

	public String getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(String idPaciente) {
		this.idPaciente = idPaciente;
	}

	public String getLeito() {
		return leito;
	}

	public void setLeito(String leito) {
		this.leito = leito;
	}

	public LeitoSql getLeitoData() {
		return leitoData;
	}

	public void setLeitoData(LeitoSql leitoData) {
		this.leitoData = leitoData;
	}

	public LaudoInternacaoSql getLaudo() {
		return laudo;
	}

	public void setLaudo(LaudoInternacaoSql laudo) {
		this.laudo = laudo;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		sb.append(" idPaciente : ");
		sb.append(idPaciente);
		sb.append(", ");
		if (leito != null) {
			sb.append(" leito : ");
			sb.append(leito);
		}
		if (leitoData != null) {
			sb.append(", ");
			sb.append(" leitoData : ");
			sb.append(leitoData.toString());
		}
		if (laudo != null) {
			sb.append(", ");
			sb.append(" laudo:  ");
			sb.append(laudo.toString());
		}
		sb.append("} ");
		return sb.toString();
	}

}
