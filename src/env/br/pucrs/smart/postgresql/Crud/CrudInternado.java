package br.pucrs.smart.postgresql.Crud;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.base.Functions;

import br.pucrs.smart.postgresql.PostgresConnection;
import br.pucrs.smart.postgresql.models.InternadoSql;

public class CrudInternado {

	public static List<InternadoSql> getInternados() {
		List<InternadoSql> internados = new ArrayList<InternadoSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from internado";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				internados.add(rsToInternado(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return internados;
	}

	public static InternadoSql getInternadoById(int id) {
		InternadoSql internado = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from internado where id = " + id;
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				internado = rsToInternado(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return internado;
	}

	public static InternadoSql getInternadoByPaciente(String cod) {
		InternadoSql internado = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from internado where cod_paciente = '" + cod + "'";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				internado = rsToInternado(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return internado;
	}

	public static InternadoSql getInternadoByAtendimento(String atendimento) {
		InternadoSql internado = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from internado where atendimento = '" + atendimento + "'";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				internado = rsToInternado(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return internado;
	}

	public static InternadoSql getInternadoByLeito(String numLeito) {
		InternadoSql internado = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from internado where numero_leito_atual = '" + numLeito + "'";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				internado = rsToInternado(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return internado;
	}

	public static String registerAlloc(int id, String leitoNum) { //REVER
		String response = "";
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Update internado set data_internacao =  NOW(), numero_leito_atual = '" + leitoNum + "' where id = " + id+ ";";

		int res = dbConnection.runSQL(sql);

		if (res > 0) {
			response = "Alocação registrada com sucesso";
		} else {
			System.out.println("[CrudLaudoInternacao] Error when registering allocation");
			response = "Erro ao registrar alocação";
		}

		return response;
	}

	public static List<InternadoSql> getInternadosByIdList(List<Integer> internadosId) {
		List<InternadoSql> internados = new ArrayList<InternadoSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String ids = String.join(",", Lists.transform(internadosId, Functions.toStringFunction()));
		String sql = "Select * from internado where id IN (" + ids + ");";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				internados.add(rsToInternado(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return internados;
	}

	private static InternadoSql rsToInternado(ResultSet rs) {
		InternadoSql internado = new InternadoSql();
		try {
			internado.setId(rs.getInt("id"));
			internado.setAtendimento(rs.getString("atendimento"));
			internado.setCod_paciente(rs.getString("cod_paciente"));
			internado.setFaixaetaria(rs.getString("faixaetaria"));
			internado.setData_internacao(rs.getTimestamp("data_internacao"));
			internado.setDias_internado(rs.getInt("dias_internado"));
			internado.setGenero(rs.getString("genero"));
			internado.setCod_medico(rs.getString("cod_medico"));
			internado.setEspecialidade(rs.getString("especialidade"));
			internado.setTipo_especialidade(rs.getString("tipo_especialidade"));
			internado.setGrau_dependencia(rs.getString("grau_dependencia"));
			internado.setNumero_leito_atual(rs.getString("numero_leito_atual"));
			internado.setUnidade_internacao_atual(rs.getString("unidade_internacao_atual"));
			internado.setUnidade_grupo_atual(rs.getString("unidade_grupo_atual"));
			internado.setCobertura_leito_atual(rs.getString("cobertura_leito_atual"));
			internado.setCobertura_paciente(rs.getString("cobertura_paciente"));
			internado.setCobertura_atendimento(rs.getString("cobertura_atendimento"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return internado;
	}

	public static List<InternadoSql> getInternadosByPacientes(List<String> cods) {
		List<InternadoSql> internados = new ArrayList<InternadoSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String strCods = String.join("','", Lists.transform(cods, Functions.toStringFunction()));
		String sql = "Select * from internado where cod_paciente IN ('" + strCods + "');";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				internados.add(rsToInternado(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return internados;
	}

}
