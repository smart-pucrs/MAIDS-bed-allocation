package br.pucrs.smart.postgresql.Crud;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.pucrs.smart.postgresql.PostgresConnection;
import br.pucrs.smart.postgresql.models.AlocacaoOtimizadaSql;
import br.pucrs.smart.postgresql.models.AlocacaoSugeridaSql;

public class CrudAlocacoesOtimizadas {

	public static String addAlocacaoOtimizada(AlocacaoOtimizadaSql a) {
		String response = "";
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Insert into alocacoesOtimizadas (id,allAllocated,alocar,alreadySuggested,concluido,saveAt)"
				+ "values (default," + a.isAllAllocated() + "," + a.isAlocar() + "," + a.isAlreadySuggested() + ","
				+ a.isConcluido() + ",NOW())";
		int res = dbConnection.runSqlGettingKey(sql);

		if (res > 0) {
			for (AlocacaoSugeridaSql as : a.getAlocacoesSugeridas()) {
				PostgresConnection dbConnection2 = new PostgresConnection();
				String sql2 = "Insert into alocacoesSugeridas (id,alocacaoOtimizadaId,laudoInternacaoId,leitoNum)"
						+ "values (default," + res + "," + as.getLaudoInternacaoId() + ",'" + as.getLeitoNum() + "')";
				int res2 = dbConnection2.runSQL(sql2);
				if (res2 <= 0) {
					System.out.println("[CrudAlocacoesOtimizadas] Error when registering suggested allocation");
					response = "Erro ao cadastrar alocação sugerida";
				}
			}
			for (Integer i : a.getNotAllocated()) {
				PostgresConnection dbConnection3 = new PostgresConnection();
				String sql3 = "Insert into alocacoesSugeridas (id,alocacaoOtimizadaId,laudoInternacaoId)"
						+ "values (default," + res + "," + i + ")";
				int res3 = dbConnection3.runSQL(sql3);
				if (res3 <= 0) {
					System.out.println("[CrudAlocacoesOtimizadas] Error when registering suggested allocation");
					response = "Erro ao cadastrar alocação sugerida";
				}
			}
			if (!response.contains("Erro")) {
				response = "Alocação otimizada cadastrada com sucesso";
			}
		} else {
			System.out.println("[CrudAlocacoesOtimizadas] Error when registering optimized allocation");
			response = "Erro ao cadastrar alocação otimizada";
		}
		return response;
	}

	public static String setLastOptimizationConcluded(boolean alocar) {
		AlocacaoOtimizadaSql alloc = getLastOptimizerResult();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Update alocacoesOtimizadas set concluido = true, alocar = " + alocar + " where id = "
				+ alloc.getId() + ";";

		int res = dbConnection.runSQL(sql);

		if (res > 0) {
			return "Success";
		} else {
			System.out.println("[CrudAlocacaoTemporaria] Error when setting concluded in id " + alloc.getId());
			return "Error";
		}
	}

	public static AlocacaoOtimizadaSql getLastOptimizerResult() {
		AlocacaoOtimizadaSql alloc = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from alocacoesOtimizadas where saveAt = (Select MAX(saveAt) from alocacoesOtimizadas where concluido=false)";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				alloc = rsToAlocacaoOtimizada(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (alloc != null) {
			alloc.setAlocacoesSugeridas(getAlocacoesSugeridasByTempAllocId(alloc.getId()));
		}
		return alloc;
	}

	public static AlocacaoOtimizadaSql getLastOptimizerResultWithPatientName() {
		AlocacaoOtimizadaSql alloc = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from alocacoesOtimizadas where saveAt = (Select MAX(saveAt) from alocacoesOtimizadas where concluido=false)";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				alloc = rsToAlocacaoOtimizada(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (alloc != null) {
			alloc.setAlocacoesSugeridas(getAlocacoesSugeridasByTempAllocIdWithPatientName(alloc.getId()));
		}
		return alloc;
	}

	private static List<AlocacaoSugeridaSql> getAlocacoesSugeridasByTempAllocIdWithPatientName(int id) {
		List<AlocacaoSugeridaSql> allocation = new ArrayList<AlocacaoSugeridaSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select a.id, a.alocacaoOtimizadaId, a.laudointernacaoid, a.leitonum, l.nomepaciente from alocacoesSugeridas a\n"
				+ "INNER JOIN laudointernacao l ON a.laudointernacaoid = l.id\n" + "where alocacaoOtimizadaId = " + id;
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				allocation.add(rsToAlocacaoSugeridaWithName(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allocation;
	}

	private static List<AlocacaoSugeridaSql> getAlocacoesSugeridasByTempAllocId(int id) {
		List<AlocacaoSugeridaSql> allocation = new ArrayList<AlocacaoSugeridaSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from alocacoesSugeridas where alocacaoOtimizadaId = " + id;
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				allocation.add(rsToAlocacaoSugerida(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allocation;
	}

	private static AlocacaoOtimizadaSql rsToAlocacaoOtimizada(ResultSet rs) {
		AlocacaoOtimizadaSql alloc = new AlocacaoOtimizadaSql();
		try {
			alloc.setId(rs.getInt("id"));
			alloc.setAllAllocated(rs.getBoolean("allAllocated"));
			alloc.setAlocar(rs.getBoolean("alocar"));
			alloc.setAlreadySuggested(rs.getBoolean("alreadySuggested"));
			alloc.setConcluido(rs.getBoolean("concluido"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return alloc;

	}

	private static AlocacaoSugeridaSql rsToAlocacaoSugerida(ResultSet rs) {
		AlocacaoSugeridaSql alloc = new AlocacaoSugeridaSql();
		try {
			alloc.setId(rs.getInt("id"));
			alloc.setAlocacaoOtimizadaId(rs.getInt("alocacaoOtimizadaId"));
			alloc.setLaudoInternacaoId(rs.getInt("laudoInternacaoId"));
			alloc.setLeitoNum(rs.getString("leitoNum"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return alloc;
	}

	private static AlocacaoSugeridaSql rsToAlocacaoSugeridaWithName(ResultSet rs) {
		AlocacaoSugeridaSql alloc = new AlocacaoSugeridaSql();
		try {
			alloc.setId(rs.getInt("id"));
			alloc.setAlocacaoOtimizadaId(rs.getInt("alocacaoOtimizadaId"));
			alloc.setLaudoInternacaoId(rs.getInt("laudoInternacaoId"));
			alloc.setLeitoNum(rs.getString("leitoNum"));
			alloc.setNomePaciente(rs.getString("nomePaciente"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return alloc;
	}
}
