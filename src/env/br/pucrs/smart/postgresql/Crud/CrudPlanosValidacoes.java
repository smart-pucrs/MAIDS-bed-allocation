package br.pucrs.smart.postgresql.Crud;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.pucrs.smart.postgresql.PostgresConnection;
import br.pucrs.smart.postgresql.models.CaracteristicaSql;
import br.pucrs.smart.postgresql.models.PlanoValidacaoSql;

public class CrudPlanosValidacoes {
	
	public static String addPlanoValidacao(String validacaoId, int laudoInternacaoId, String leitoNum, boolean isValid) {
		String response = "";
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Insert into planosValidacoes (id,validacaoId,laudoInternacaoId,leitoNum,isValid) "
				+ "values (default,'"+validacaoId+"',"+laudoInternacaoId+",'"+leitoNum+"',"+isValid+")";
		int res = dbConnection.runSQL(sql);
		if (res > 0) {
			response = "Plano de alocação cadastrado com sucesso";
		} else {
			System.out.println("[CrudValidacoes] Error when registering planned allocation");
			response = "Erro ao cadastrar plano de alocação";
		}
		
		return response;
	}
	
	public static List<PlanoValidacaoSql> getPlanoValidacaoByValidacaoId(String validacaoId) {
		List<PlanoValidacaoSql>  plano = new ArrayList<PlanoValidacaoSql>();
		
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from planosValidacoes where validacaoId = '"+validacaoId+ "';";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				plano.add(rsToPlanoValidacao(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return plano;
		
	}
	
	private static PlanoValidacaoSql rsToPlanoValidacao(ResultSet rs) {
		PlanoValidacaoSql plano = new PlanoValidacaoSql();
		try {
			plano.setId(rs.getInt("id"));
			plano.setValidacaoId(rs.getString("validacaoId"));
			plano.setLaudoInternacaoId(rs.getInt("laudoInternacaoId"));
			plano.setLeitoNum(rs.getString("leitoNum"));
			plano.setIsvalid(rs.getBoolean("isValid"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return plano;
	}

}
