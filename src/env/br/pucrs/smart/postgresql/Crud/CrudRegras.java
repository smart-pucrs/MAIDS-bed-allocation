package br.pucrs.smart.postgresql.Crud;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.pucrs.smart.postgresql.PostgresConnection;
import br.pucrs.smart.postgresql.models.RegraSql;

public class CrudRegras {
	
	public static List<RegraSql> getCaracteristicas() {
		List<RegraSql> regras = new ArrayList<RegraSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from regras";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				regras.add(rsToRegra(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return regras;
	}

	public static Map<String, Map<String,String>> getRegrasInMaps() {
		Map<String, Map<String,String>> regras = new HashMap<String, Map<String,String>>();
		PostgresConnection dbConnection = new PostgresConnection();
		ResultSet rsDistinct = dbConnection.runSearch("SELECT DISTINCT especialidade FROM regras");
		List<String> tipos = new ArrayList<String>();
		try {
			while (rsDistinct.next()) {
				tipos.add(rsDistinct.getString("especialidade"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		tipos.forEach(tipo -> {
			Map<String,String> restricoes = getRestricoesByEspecialidade(tipo);
			regras.put(tipo, restricoes);
		});

		return regras;
	}
	
	public static Map<String,String> getRestricoesByEspecialidade(String especialidade) {
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "SELECT restricao, valorRestricao FROM regras WHERE especialidade = '" + especialidade + "'";
		ResultSet rs = dbConnection.runSearch(sql);
		Map<String,String> restricoes = new HashMap<String,String>();
		try {
			while (rs.next()) {
				restricoes.put(rs.getString("restricao"),rs.getString("valorRestricao"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return restricoes;
		
	}

	private static RegraSql rsToRegra(ResultSet rs) {
		RegraSql regra = new RegraSql();
		try {
			regra.setId(rs.getInt("id"));
			regra.setEspecialidade(rs.getString("especialidade"));
			regra.setRestricao(rs.getString("restricao"));
			regra.setValorRestricao(rs.getString("valorRestricao"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return regra;
	}

}
