package br.pucrs.smart.postgresql.Crud;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.pucrs.smart.postgresql.PostgresConnection;

import br.pucrs.smart.postgresql.models.DataByBed;
import br.pucrs.smart.postgresql.models.DataByBedroom;
import br.pucrs.smart.postgresql.models.DataByPatient;
import br.pucrs.smart.postgresql.models.InternadoSql;
import br.pucrs.smart.postgresql.models.LeitoSql;
import br.pucrs.smart.postgresql.models.PedidoLeitoSql;

public class CrudDataByBedroom {
	
	public static List<DataByBedroom> getDataByBedrooms() {
        List<DataByBedroom> quartos = new ArrayList<DataByBedroom>();
        List<DataByBed> leitos = getDataByBeds();

        quartos = leitos.stream().collect(Collectors.groupingBy(DataByBed::getQuarto)).entrySet().stream()
        .map(x -> new DataByBedroom(x.getKey(), x.getValue())).collect(Collectors.toList());

        quartos.forEach(quarto -> {

			// get genero grouping by leito.getGenero()
			List<String> generos = quarto.getLeitos().stream().map(x -> x.getGenero()).distinct()
					.collect(Collectors.toList());
			if (generos.size() == 1) {
				quarto.setGenero(generos.get(0) != null ? generos.get(0) : "NONE");
			} else {
				quarto.setGenero("NONE");
			}

			// get especialidades grouping by leito.getEspecialidade()
			List<String> especialidades = quarto.getLeitos().stream().map(x -> x.getEspecialidade()).distinct()
					.collect(Collectors.toList());
			if (especialidades.size() == 1) {
				quarto.setEspecialidade(especialidades.get(0) != null ? especialidades.get(0) : "NONE");
			} else {
				quarto.setEspecialidade("NONE");
			}

			// get tipo_especialidade grouping by leito.getTipo_especialidade()
			List<String> tipo_especialidades = quarto.getLeitos().stream().map(x -> x.getTipo_especialidade())
					.distinct().collect(Collectors.toList());
			if (tipo_especialidades.size() == 1) {
				quarto.setTipo_especialidade(tipo_especialidades.get(0) != null ? tipo_especialidades.get(0) : "NONE");
			} else {
				quarto.setTipo_especialidade("NONE");
			}

			// get faixa_etaria grouping by leito.getFaixa_etaria()
			List<String> faixa_etarias = quarto.getLeitos().stream().map(x -> x.getFaixa_etaria()).distinct()
					.collect(Collectors.toList());
			if (faixa_etarias.size() == 1) {
				quarto.setFaixa_etaria(faixa_etarias.get(0) != null ? faixa_etarias.get(0) : "NONE");
			} else {
				quarto.setFaixa_etaria("NONE");
			}

			// get cds_categoria_isolamento grouping by leito.cds_categoria_isolamento() where leito.cds_categoria_isolamento() != null
			List<List<String>> cds_categoria_isolamentos = quarto.getLeitos().stream().filter(x -> x.getCds_categoria_isolamento() != null).map(x -> x.getCds_categoria_isolamento()).distinct()
					.collect(Collectors.toList());
			if (cds_categoria_isolamentos.size() == 1) {
				quarto.setCds_categoria_isolamento(cds_categoria_isolamentos.get(0));
			} else if (cds_categoria_isolamentos.size() > 1) {
				Set<String> set = new LinkedHashSet<>();
				for (List<String> cds : cds_categoria_isolamentos) {
					if (cds.size()>0) {
						set.addAll(cds);
					}
				}
				//Convert Set to ArrayList ordered
				quarto.setCds_categoria_isolamento(new ArrayList<String>(set));
			}
			// count how many beds are occupied
			int ocupados = (int) quarto.getLeitos().stream().filter(x -> !x.getStatus_leito().equals("Vago")).count();
			if (quarto.getLeitos().size() == ocupados) {
				quarto.setStatus_quarto("Totalmente Ocupado");
			} else if (ocupados == 0) {
				quarto.setStatus_quarto("Vazio");
			} else {
				quarto.setStatus_quarto("Parcialmente Ocupado");
			}

			quarto.setPreferencialmente_isolamento(quarto.getLeitos().get(0).getPreferencialmente_isolamento() != null ? quarto.getLeitos().get(0).getPreferencialmente_isolamento() : false);
			quarto.setEspecialidade_preferencial(quarto.getLeitos().get(0).getEspecialidade_preferencial() != null ? quarto.getLeitos().get(0).getEspecialidade_preferencial() : "NONE");
			quarto.setConvenio(quarto.getLeitos().get(0).getConvenio());
		});

        return quartos;
    }

    public static List<DataByBed> getDataByFreeBeds() {
        List<DataByBed> leitos = new ArrayList<DataByBed>();
		List<LeitoSql> leitosSql = CrudLeito.getLeitosVagos();

		leitosSql.forEach(leito -> {
			leitos.add(leitoToDataByBed(leito, new ArrayList<DataByPatient>()));
		});

        return leitos;
    }

    public static List<DataByBed> getDataByBeds() {
        List<DataByBed> leitos = new ArrayList<DataByBed>();
		List<DataByPatient> pacientes = getDataByPatients();
		List<LeitoSql> leitosSql = CrudLeito.getLeitos();

		leitosSql.forEach(leito -> {
			List<DataByPatient> paciente = pacientes.stream()
					.filter(x -> x.getNumero_leito_atual().equals(leito.getNumero())).collect(Collectors.toList());
			leitos.add(leitoToDataByBed(leito, paciente));
		});

        return leitos;
    }

    public static DataByBed leitoToDataByBed(LeitoSql leito, List<DataByPatient> pacientes) {
		DataByBed leitoData = new DataByBed();

		leitoData.setId(leito.getId());
		leitoData.setAcomodacao(leito.getAcomodacao());
		leitoData.setNumero(leito.getNumero());
		leitoData.setQuarto(leito.getQuarto());
		leitoData.setStatus_leito(leito.getStatus_leito());
		leitoData.setPreferencialmente_isolamento(leito.getPreferencialmente_isolamento());
		leitoData.setEspecialidade_preferencial(leito.getEspecialidade_preferencial());
		leitoData.setConvenio(leito.getConvenio());

		if (pacientes.size() > 0 ) {
			leitoData.setGenero(pacientes.get(0).getGenero());
			leitoData.setEspecialidade(pacientes.get(0).getEspecialidade());
			leitoData.setTipo_especialidade(pacientes.get(0).getTipo_especialidade());
			leitoData.setFaixa_etaria(pacientes.get(0).getFaixa_etaria());
			leitoData.setCds_categoria_isolamento(pacientes.get(0).getCds_categoria_isolamento());
			leitoData.setPaciente(pacientes.get(0));
		}

		return leitoData;
	}

    public static List<DataByPatient> getDataByPatients() {
		List<DataByPatient> pacientes = new ArrayList<DataByPatient>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select i.id, i.cod_paciente, i.faixaetaria as faixa_etaria, i.genero, i.especialidade, i.tipo_especialidade, i.numero_leito_atual, pl.tipo_solicitacao,  pl.convenio,  pl.cobertura  from  internado as i  left join  pedido_leito as pl on i.cod_paciente = pl.cod_paciente;";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				pacientes.add(rsToDataByPatient(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pacientes;
	}


	private static DataByPatient rsToDataByPatient(ResultSet rs) {
		DataByPatient paciente = new DataByPatient();
		try {
			paciente.setId(rs.getInt("id"));
			paciente.setCod_paciente(rs.getString("cod_paciente"));
			paciente.setFaixa_etaria(rs.getString("faixa_etaria"));
			paciente.setGenero(rs.getString("genero"));
			paciente.setEspecialidade(rs.getString("especialidade"));
			paciente.setTipo_especialidade(rs.getString("tipo_especialidade"));
			paciente.setNumero_leito_atual(rs.getString("numero_leito_atual"));
			paciente.setTipo_solicitacao(rs.getString("tipo_solicitacao"));
			paciente.setConvenio(rs.getString("convenio"));
			paciente.setCobertura(rs.getString("cobertura"));
			paciente.setCds_categoria_isolamento(
				CrudInfeccao.getInfeccoesByCodPaciente(paciente.getCod_paciente()).getCds_categoria());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paciente;
	}

}
