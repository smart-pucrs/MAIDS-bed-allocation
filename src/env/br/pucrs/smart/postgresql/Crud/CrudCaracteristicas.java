package br.pucrs.smart.postgresql.Crud;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.pucrs.smart.postgresql.PostgresConnection;
import br.pucrs.smart.postgresql.models.CaracteristicaSql;

public class CrudCaracteristicas {

	public static List<CaracteristicaSql> getCaracteristicas() {
		List<CaracteristicaSql> caracteristicas = new ArrayList<CaracteristicaSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from caracteristicas";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				caracteristicas.add(rsToCaracteristica(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return caracteristicas;
	}

	public static Map<String, List<String>> getCaracteristicasInArrays() {
		Map<String, List<String>> caracteristicas = new HashMap<String, List<String>>();
		PostgresConnection dbConnection = new PostgresConnection();
		ResultSet rsDistinct = dbConnection.runSearch("SELECT DISTINCT caracteristica FROM caracteristicas");
		List<String> tipos = new ArrayList<String>();
		try {
			while (rsDistinct.next()) {
				tipos.add(rsDistinct.getString("caracteristica"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		tipos.forEach(tipo -> {
			PostgresConnection dbConnection2 = new PostgresConnection();
			String sql = "SELECT descricao FROM caracteristicas WHERE caracteristica = '" + tipo + "'";
			ResultSet rs = dbConnection2.runSearch(sql);
			try {
				List<String> descricoes = new ArrayList<String>();
				while (rs.next()) {
					descricoes.add(rs.getString("descricao"));
				}
				caracteristicas.put(tipo, descricoes);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		return caracteristicas;
	}

	private static CaracteristicaSql rsToCaracteristica(ResultSet rs) {
		CaracteristicaSql caracteristica = new CaracteristicaSql();
		try {
			caracteristica.setId(rs.getInt("id"));
			caracteristica.setCaracteristica(rs.getString("caracteristica"));
			caracteristica.setDescricao(rs.getString("descricao"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return caracteristica;
	}

}
