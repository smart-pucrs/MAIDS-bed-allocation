package br.pucrs.smart.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class PostgresConnection {
	private Connection connection;
	private String url = "jdbc:postgresql://localhost:5432/hsl-real-data";
	private String user = "postgres";
	private String password = "hslsecret";

	public PostgresConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public int runSQL(String sql) {
		try {
			Statement stm = connection.createStatement();
			int res = stm.executeUpdate(sql);
			connection.close();
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return 0;
		}
	}

	public int runSqlGettingKey(String sql) {
		try {

			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.execute();

			ResultSet rs = ps.getGeneratedKeys();
			int generatedKey = 0;
			if (rs.next()) {
				generatedKey = rs.getInt(1);
			}
			connection.close();
			return generatedKey;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return 0;
		}
	}

	public ResultSet runSearch(String sql) {
		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			connection.close();
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}

}
