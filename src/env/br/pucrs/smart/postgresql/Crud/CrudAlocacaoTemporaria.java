package br.pucrs.smart.postgresql.Crud;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import br.pucrs.smart.postgresql.PostgresConnection;
import br.pucrs.smart.postgresql.models.AlocacaoOtimizadaSql;
import br.pucrs.smart.postgresql.models.AlocacaoSugeridaSql;
import br.pucrs.smart.postgresql.models.LaudoInternacaoSql;
import br.pucrs.smart.postgresql.models.ValidacaoSql;
import br.pucrs.smart.validator.models.SimpleAllocation;
import br.pucrs.smart.validator.models.TempAlloc;

public class CrudAlocacaoTemporaria {

	public static TempAlloc getAlocacaoTemporaria() {
		TempAlloc tempAloc = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from alocacaoTemporaria where saveAt = (Select MAX(saveAt) from alocacaoTemporaria where validated=false)";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				tempAloc = rsToTempAloc(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (tempAloc != null) {
			tempAloc.setAllocation(getsimpleAllocationByTempAllocId(tempAloc.getId()));	
		}
		return tempAloc;
	}

	private static List<SimpleAllocation> getsimpleAllocationByTempAllocId(int id) {
		List<SimpleAllocation> allocation = new ArrayList<SimpleAllocation>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from simpleAllocation where alocacaoTemporariaId = " + id;
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				allocation.add(rsToSimpleAllocation(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allocation;
	}

	public static void setTempAllocValidated(int id) {
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Update alocacaoTemporaria set validated = true" + " where id = " + id + ";";

		int res = dbConnection.runSQL(sql);

		if (res <= 0)
			System.out.println("[CrudAlocacaoTemporaria] Error when setting validated");

	}
	
	public static String addTempAlloc(TempAlloc a) {
		String response = "";
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Insert into alocacaoTemporaria (id,validated,saveAt)"
				+ "values (default,false,NOW())";
		int res = dbConnection.runSqlGettingKey(sql);

		if (res > 0) {
			for (SimpleAllocation as: a.getAllocation()) {
				PostgresConnection dbConnection2 = new PostgresConnection();
				String sql2 = "Insert into simpleAllocation (id,alocacaoTemporariaId,laudoInternacaoId,leitoNum)"
						+ "values (default,"+res+","+as.getLaudoInternacaoId()+",'"+as.getLeitoNum()+"')";
				int res2 = dbConnection2.runSQL(sql2);
				if (res2 <= 0) {
					System.out.println("[CrudAlocacaoTemporaria] Error when registering simple allocation");
					response = "Erro ao cadastrar alocação sugerida pelo operador";
				}
			}
			if (!response.contains("Erro")) {
				response = "Alocação temporária cadastrada com sucesso";
			}
		} else {
			System.out.println("[CrudAlocacaoTemporaria] Error when registering temporary allocation");
			response = "Erro ao cadastrar alocação temporária";
		}
		return response;
	}

	private static TempAlloc rsToTempAloc(ResultSet rs) {
		TempAlloc tempAloc = new TempAlloc();
		try {
			tempAloc.setId(rs.getInt("id"));
			tempAloc.setSaveAt(rs.getObject("saveAt", LocalDateTime.class));
			tempAloc.setValidated(rs.getBoolean("validated"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempAloc;

	}

	private static SimpleAllocation rsToSimpleAllocation(ResultSet rs) {
		SimpleAllocation simpleAllocation = new SimpleAllocation();
		try {
			simpleAllocation.setId(rs.getInt("id"));
			simpleAllocation.setAlocacaoTemporariaId(rs.getInt("alocacaoTemporariaId"));
			simpleAllocation.setLaudoInternacaoId(rs.getInt("laudoInternacaoId"));
			simpleAllocation.setLeitoNum(rs.getString("leitoNum"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return simpleAllocation;
	}

}
