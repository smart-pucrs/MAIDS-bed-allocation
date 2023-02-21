package br.pucrs.smart.postgresql.Crud;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.pucrs.smart.postgresql.PostgresConnection;
import br.pucrs.smart.postgresql.models.NurseExceptionSql;

public class CrudNurseException {
	public static List<NurseExceptionSql> getActiveNurseExceptions() {
		List<NurseExceptionSql> exceptions = new ArrayList<NurseExceptionSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from nurseException where active=true";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				exceptions.add(rsToNurseException(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return exceptions;
	}

	public static List<NurseExceptionSql> getActiveNurseExceptionsWithNames() {
		List<NurseExceptionSql> exceptions = new ArrayList<NurseExceptionSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select e.id, e.active, e.laudoInternacaoId, e.tipo, l.nomepaciente " + "from nurseException e "
				+ "INNER JOIN laudointernacao l " + "ON e.laudointernacaoid = l.id " + "where active=true;";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				exceptions.add(rsToNurseExceptionWithName(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return exceptions;
	}

	public static List<NurseExceptionSql> getNurseExceptionsByLaudoInternacaoId(int laudoInternacaoId) {
		List<NurseExceptionSql> exceptions = new ArrayList<NurseExceptionSql>();

		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from nurseException where active=true AND laudoInternacaoId=" + laudoInternacaoId + ";";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				exceptions.add(rsToNurseException(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exceptions;

	}

	public static String addNurseException(NurseExceptionSql exception) {
		String response = "";
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Insert into nurseException (id,laudoInternacaoId,tipo,active) " + "values (default,"
				+ exception.getLaudoInternacaoId() + ",'" + exception.getTipo() + "'," + exception.isActive() + ");";
		int res = dbConnection.runSQL(sql);
		if (res > 0) {
			response = "Exceção cadastrado com sucesso";
		} else {
			System.out.println("[CrudValidacoes] Error when registering exception");
			response = "Erro ao cadastrar exceção";
		}
		return response;
	}

	private static NurseExceptionSql rsToNurseException(ResultSet rs) {
		NurseExceptionSql exception = new NurseExceptionSql();
		try {
			exception.setId(rs.getInt("id"));
			exception.setLaudoInternacaoId(rs.getInt("laudoInternacaoId"));
			exception.setTipo(rs.getString("tipo"));
			exception.setActive(rs.getBoolean("active"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return exception;
	}

	private static NurseExceptionSql rsToNurseExceptionWithName(ResultSet rs) {
		NurseExceptionSql exception = new NurseExceptionSql();
		try {
			exception.setId(rs.getInt("id"));
			exception.setLaudoInternacaoId(rs.getInt("laudoInternacaoId"));
			exception.setTipo(rs.getString("tipo"));
			exception.setActive(rs.getBoolean("active"));
			exception.setNomePaciente(rs.getString("nomePaciente"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return exception;
	}
}
