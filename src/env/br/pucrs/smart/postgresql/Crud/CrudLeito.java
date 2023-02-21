package br.pucrs.smart.postgresql.Crud;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.pucrs.smart.postgresql.PostgresConnection;
import br.pucrs.smart.postgresql.models.LeitoSql;

public class CrudLeito {

	public static List<LeitoSql> getLeitos() {
		List<LeitoSql> leitos = new ArrayList<LeitoSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from leito";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				leitos.add(rsToLeito(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return leitos;
	}
	
	public static List<LeitoSql> getLeitosVagos() {
		List<LeitoSql> leitos = new ArrayList<LeitoSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from leito where status_leito = 'Vago'";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				leitos.add(rsToLeito(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return leitos;
	}
	
	public static List<LeitoSql> getLeitosOcupados() {
		List<LeitoSql> leitos = new ArrayList<LeitoSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from leito where status_leito LIKE '%Ocup%'";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				leitos.add(rsToLeito(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return leitos;
	}
	
	public static LeitoSql getLeitoById(int id) {
		LeitoSql leito = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from leito where id = " + id;
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				leito = rsToLeito(rs);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}

		return leito;
	}
	
	public static LeitoSql getLeitoByNumero(String numero) {
		LeitoSql leito = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from leito where numero = '"+numero+"'";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				leito = rsToLeito(rs);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}

		return leito;
	}
	
	public static List<LeitoSql> getLeitosByNumList(List<String> leitosNum) {
		List<LeitoSql> leitos = new ArrayList<LeitoSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String nums = String.join("','", leitosNum);
		String sql = "Select * from leito where numero IN ('" + nums + "');";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				leitos.add(rsToLeito(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return leitos;
	}
	
	public static String registerAlloc(String numero) {
		String response = "";
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Update leito set status_leito = 'Ocup. por paciente'"
				+" where numero = '" + numero + "';";
		int res = dbConnection.runSQL(sql);

		if (res > 0) {
			response = "Alocação registrada com sucesso";
		} else {
			System.out.println("[CrudLeito] Error when registering allocation");
			response = "Erro ao registrar alocação";
		}

		return response;
	}
	
	public static String releaseBed(String numero) {
		String response = "";
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Update leito set status_leito = 'Vago'"
				+" where numero = '" + numero + "';";
		int res = dbConnection.runSQL(sql);

		if (res > 0) {
			response = "Leito "+numero+" liberado com sucesso";
		} else {
			System.out.println("[CrudLeito] Error when realising bed "+numero);
			response = "Erro ao liberar leito " + numero;
		}

		return response;
	}


	private static LeitoSql rsToLeito(ResultSet rs) {
		LeitoSql leito = new LeitoSql();
		try {
			leito.setId(rs.getInt("id"));
			leito.setAcomodacao(rs.getString("acomodacao"));
			leito.setGenero(rs.getString("genero"));
			leito.setNumero(rs.getString("numero"));
			leito.setQuarto(rs.getString("quarto"));
			leito.setStatus_leito(rs.getString("status_leito"));
			leito.setPreferencialmente_isolamento(rs.getBoolean("preferencialmente_isolamento"));
			leito.setDistancia_enfermaria(rs.getFloat("distancia_enfermaria"));
			leito.setEspecialidade_preferencial(rs.getString("especialidade_preferencial"));
			leito.setConvenio(rs.getString("leito_sus") == "SIM" ? "SUS" : "CONVENIOS");
			leito.setTipo_leito(rs.getString("tipo_leito"));
			leito.setUnidade(rs.getString("unidade"));
			leito.setUnidade_clasee(rs.getString("unidade_clasee"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return leito;
	}

	

}
