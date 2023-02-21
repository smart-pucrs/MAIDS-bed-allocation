package br.pucrs.smart.postgresql.Crud;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.pucrs.smart.postgresql.PostgresConnection;
import br.pucrs.smart.postgresql.models.Infeccao;
import br.pucrs.smart.postgresql.models.InfeccaoPorPaciente;

public class CrudInfeccao {

    public static List<Infeccao> getInfeccoes() {
		List<Infeccao> infeccoes = new ArrayList<Infeccao>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from infeccao";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				infeccoes.add(rsToInfeccao(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return infeccoes;
	}

    public static List<InfeccaoPorPaciente> getInfeccoesPorPaciente() { //ver se funciona
		List<Infeccao> infeccoes = new ArrayList<Infeccao>();
		List<InfeccaoPorPaciente> infeccoesPorPaciente = new ArrayList<InfeccaoPorPaciente>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select distinct cod_paciente, cd_categoria from infeccao order by cod_paciente,cd_categoria";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				infeccoes.add(rsToInfec(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        List<String> cods_paciente = infeccoes.stream().map(x->x.getCod_paciente()).distinct().collect(Collectors.toList());
        for (String cod_paciente : cods_paciente) {
            List<Infeccao> infeccoes_paciente = infeccoes.stream().filter(x->x.getCod_paciente().equals(cod_paciente)).collect(Collectors.toList());
            InfeccaoPorPaciente infeccaoPorPaciente = new InfeccaoPorPaciente();
            infeccaoPorPaciente.setCod_paciente(cod_paciente);
            for (Infeccao in : infeccoes_paciente) {
                infeccaoPorPaciente.addCd_categoria(in.getCd_categoria());
            }
            infeccoesPorPaciente.add(infeccaoPorPaciente);
        }

		return infeccoesPorPaciente;
	}

    public static InfeccaoPorPaciente getInfeccoesByCodPaciente(String cod) { // ver se funciona
        List<Infeccao> infeccoes = new ArrayList<Infeccao>();
        PostgresConnection dbConnection = new PostgresConnection();
        String sql = "Select distinct cod_paciente, cd_categoria from infeccao where cod_paciente = '" + cod + "' order by cd_categoria";
        ResultSet rs = dbConnection.runSearch(sql);

        try {
            while (rs.next()) {
                infeccoes.add(rsToInfec(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        InfeccaoPorPaciente infeccaoPorPaciente = new InfeccaoPorPaciente();
        infeccaoPorPaciente.setCod_paciente(cod);
        for (Infeccao in : infeccoes) {
            infeccaoPorPaciente.addCd_categoria(in.getCd_categoria());
        }
        return infeccaoPorPaciente;
    }


    private static Infeccao rsToInfec(ResultSet rs) {
		Infeccao infeccao = new Infeccao();
		try {
			infeccao.setCod_paciente(rs.getString("cod_paciente"));
			infeccao.setCd_categoria(rs.getString("cd_categoria"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infeccao;
	}

    private static Infeccao rsToInfeccao(ResultSet rs) {
		Infeccao infeccao = new Infeccao();
		try {
			infeccao.setId(rs.getInt("id"));
			infeccao.setCod_paciente(rs.getString("cod_paciente"));
			infeccao.setDt_registro(rs.getTimestamp("dt_registro"));
			infeccao.setCd_categoria(rs.getString("cd_categoria"));
			infeccao.setDs_categoria(rs.getString("ds_categoria"));
			infeccao.setOrdem_decresc(rs.getInt("ordem_decresc"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infeccao;
	}



}
