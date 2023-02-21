package br.pucrs.smart.postgresql.Crud;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.base.Functions;

import br.pucrs.smart.postgresql.PostgresConnection;
import br.pucrs.smart.postgresql.models.PedidoLeitoSql;

public class CrudPedidoLeito {
    

	public static List<PedidoLeitoSql> getPedidosLeito() {
		List<PedidoLeitoSql> pedidos = new ArrayList<PedidoLeitoSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from pedido_leito";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				pedidos.add(rsToPedido(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pedidos;
	}

	public static List<PedidoLeitoSql> getTiposPedidos() {
		List<PedidoLeitoSql> pedidos = new ArrayList<PedidoLeitoSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select tipo_solicitacao, cod_paciente from pedido_leito";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				pedidos.add(rsToTipoPedido(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pedidos;
	}

	public static PedidoLeitoSql getPedidoLeitoById(int id) {
		PedidoLeitoSql pedido_leito = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from pedido_leito where id = " + id;
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				pedido_leito = rsToPedido(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pedido_leito;
	}

	public static PedidoLeitoSql getPedidoLeitoByPaciente(String cod) {
		PedidoLeitoSql pedido_leito = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from pedido_leito where cod_paciente = '" + cod + "'";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				pedido_leito = rsToPedido(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pedido_leito;
	}

	public static PedidoLeitoSql getSimplePedidoLeitoByPaciente(String cod) {
		PedidoLeitoSql pedido_leito = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select tipo_solicitacao, cod_paciente, convenio, cobertura from pedido_leito where cod_paciente = '" + cod + "'";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				pedido_leito = rsToSimplePedido(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pedido_leito;
	}

	public static PedidoLeitoSql getPedidoLeitoByAtendimento(String atendimento) {
		PedidoLeitoSql pedido_leito = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from pedido_leito where atendimento = '" + atendimento + "'";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				pedido_leito = rsToPedido(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pedido_leito;
	}

	public static PedidoLeitoSql getPedidoLeitoByLeitoOrigem(String numLeito) {
		PedidoLeitoSql pedido_leito = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from pedido_leito where leito_origem = '" + numLeito + "'";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				pedido_leito = rsToPedido(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pedido_leito;
	}

    public static PedidoLeitoSql getPedidoLeitoByLeitoSugerido(String numLeito) {
		PedidoLeitoSql pedido_leito = null;
		PostgresConnection dbConnection = new PostgresConnection();
		String sql = "Select * from pedido_leito where leito_sugerido = '" + numLeito + "'";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				pedido_leito = rsToPedido(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pedido_leito;
	}

	public static List<PedidoLeitoSql> getPedidosLeitoByIdList(List<Integer> pedidosId) {
		List<PedidoLeitoSql> pedidos = new ArrayList<PedidoLeitoSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String ids = String.join(",", Lists.transform(pedidosId, Functions.toStringFunction()));
		String sql = "Select * from pedido_leito where id IN (" + ids + ");";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				pedidos.add(rsToPedido(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pedidos;
	}


	public static List<PedidoLeitoSql> getPedidosLeitoByPacientes(List<String> cods) {
		List<PedidoLeitoSql> pedidos = new ArrayList<PedidoLeitoSql>();
		PostgresConnection dbConnection = new PostgresConnection();
		String strCods = String.join("','", Lists.transform(cods, Functions.toStringFunction()));
		String sql = "Select * from pedido_leito where cod_paciente IN ('" + strCods + "');";
		ResultSet rs = dbConnection.runSearch(sql);

		try {
			while (rs.next()) {
				pedidos.add(rsToPedido(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pedidos;
	}

	private static PedidoLeitoSql rsToPedido(ResultSet rs) {
		PedidoLeitoSql pedido_leito = new PedidoLeitoSql();
		try {
			pedido_leito.setId(rs.getInt("id"));
			pedido_leito.setTipo_solicitacao(rs.getString("tipo_solicitacao"));
			pedido_leito.setAtendimento(rs.getString("atendimento"));
			pedido_leito.setDthr_atendimento(rs.getTimestamp("dthr_atendimento"));
			pedido_leito.setCod_paciente(rs.getString("cod_paciente"));
			pedido_leito.setConvenio(rs.getString("convenio"));
			pedido_leito.setCobertura(rs.getString("cobertura"));
			pedido_leito.setDthr_solic_transferencia(rs.getTimestamp("dthr_solic_transferencia"));
			pedido_leito.setDthr_pre_internacao(rs.getTimestamp("dthr_pre_internacao"));
			pedido_leito.setLeito_origem(rs.getString("leito_origem"));
			pedido_leito.setLeito_sugerido(rs.getString("leito_sugerido"));
			pedido_leito.setCd_leito_reserva(rs.getString("cd_leito_reserva"));
			pedido_leito.setSn_prioridade(rs.getString("sn_prioridade"));
			pedido_leito.setFaixaetaria(rs.getString("faixaetaria"));
			pedido_leito.setGenero(rs.getString("genero"));
			pedido_leito.setEspecialidade(rs.getString("especialidade"));
			pedido_leito.setTipo_especialidade(rs.getString("tipo_especialidade"));
			pedido_leito.setCod_medico(rs.getString("cod_medico"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pedido_leito;
	}

	private static PedidoLeitoSql rsToSimplePedido(ResultSet rs) {
		PedidoLeitoSql pedido_leito = new PedidoLeitoSql();
		try {
			pedido_leito.setTipo_solicitacao(rs.getString("tipo_solicitacao"));
			pedido_leito.setCod_paciente(rs.getString("cod_paciente"));
			pedido_leito.setConvenio(rs.getString("convenio"));
			pedido_leito.setCobertura(rs.getString("cobertura"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pedido_leito;
	}
	
	private static PedidoLeitoSql rsToTipoPedido(ResultSet rs) {
		PedidoLeitoSql pedido_leito = new PedidoLeitoSql();
		try {
			pedido_leito.setTipo_solicitacao(rs.getString("tipo_solicitacao"));
			pedido_leito.setCod_paciente(rs.getString("cod_paciente"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pedido_leito;
	}

}
