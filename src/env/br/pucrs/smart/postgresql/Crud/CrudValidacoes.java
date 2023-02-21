package br.pucrs.smart.postgresql.Crud;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.pucrs.smart.postgresql.PostgresConnection;
import br.pucrs.smart.postgresql.models.LaudoInternacaoSql;
import br.pucrs.smart.postgresql.models.PlanoValidacaoSql;
import br.pucrs.smart.postgresql.models.ValidacaoSql;
import br.pucrs.smart.validator.models.SimpleAllocation;
import br.pucrs.smart.validator.models.TempAlloc;

public class CrudValidacoes {

	public static String addValidacao(ValidacaoSql v) {
		String response = "";
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Insert into validacoes (id,saveAt,problema,plano,retorno,valido,concluido,alocar) " + "values ('"
				+ v.getId() + "'," + "NOW(),'" + v.getProblema() + "','" + v.getPlano() + "','" + v.getRetorno() + "',"
				+ v.isValido() + "," + v.isConcluido() + "," + v.isAlocar() + ")";

		int res = dbConnection.runSQL(sql);

		if (res > 0) {
			String resPlano = "";
			for (PlanoValidacaoSql alloc : v.getPlanoAlocacao()) {
				
				String r = CrudPlanosValidacoes.addPlanoValidacao(v.getId(), alloc.getLaudoInternacaoId(), alloc.getLeitoNum(), alloc.isIsvalid());
				if (r.contains("Erro")) {
					System.out.println("[CrudValidacoes] Error when registering planned allocation");
					resPlano = "Erro ao cadastrar plano de alocação";
				}
			}
			if (!resPlano.contains("Erro")) {
				response = "Validação cadastrada com sucesso";
			}
		} else {
			System.out.println("[CrudValidacoes] Error when registering validation");
			response = "Erro ao cadastrar validação";
		}
		return response;
	}

	public static ValidacaoSql getLastValidation() {
		ValidacaoSql val = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from validacoes where saveAt = (Select MAX(saveAt) from validacoes where concluido=false)";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				val = rsToValidacao(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (val != null) {
			val.setPlanoAlocacao(CrudPlanosValidacoes.getPlanoValidacaoByValidacaoId(val.getId()));
		}
		return val;
	}

	private static ValidacaoSql rsToValidacao(ResultSet rs) {
		ValidacaoSql val = new ValidacaoSql();
		try {
			val.setId(rs.getString("id"));
			val.setAlocar(rs.getBoolean("alocar"));
			val.setConcluido(rs.getBoolean("concluido"));
			val.setValido(rs.getBoolean("valido"));
			val.setSaveAt(rs.getObject("saveAt", LocalDateTime.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public static String updateResultValidacao(String id, String retorno) {
		String response = "";
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Update validacoes set retorno = '" + retorno + "' where id = '" + id + "';";

		int res = dbConnection.runSQL(sql);

		if (res > 0) {
			response = "Validação alterada com sucesso";
		} else {
			System.out.println("[CrudAlocacoesOtimizadas] Error when updating validation");
			response = "Erro ao atualizar validação";
		}
		return response;

	}

	public static String setConcluido(String id, boolean concluido, boolean alocar) {
		String response = "";
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Update validacoes set concluido = " + concluido + ", alocar = " + alocar + " where id = '" + id
				+ "';";

		int res = dbConnection.runSQL(sql);

		if (res > 0) {
			response = "Validação concluída";
		} else {
			System.out.println("[CrudValidacoes] Error when concluding validation");
			response = "Erro ao concluir validação";
		}

		return response;
	}

}
