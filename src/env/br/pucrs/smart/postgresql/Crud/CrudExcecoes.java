package br.pucrs.smart.postgresql.Crud;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.pucrs.smart.postgresql.PostgresConnection;
import br.pucrs.smart.postgresql.models.ExcecaoSql;

public class CrudExcecoes {
	
	public static List<ExcecaoSql> getExcecoes() {
		List<ExcecaoSql> excecoes = new ArrayList<ExcecaoSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from excecoes";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				excecoes.add(rsToExcecao(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return excecoes;
	}

	private static ExcecaoSql rsToExcecao(ResultSet rs) {
		ExcecaoSql excecao = new ExcecaoSql();
		try {
			excecao.setId(rs.getInt("id"));
			excecao.setQuarto(rs.getString("quarto"));
			excecao.setDescricao(rs.getString("descricao"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return excecao;
	}


}
