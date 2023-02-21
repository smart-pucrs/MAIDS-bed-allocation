package br.pucrs.smart.postgresql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.pucrs.smart.postgresql.Crud.CrudAlocacaoTemporaria;
import br.pucrs.smart.postgresql.Crud.CrudAlocacoesOtimizadas;
import br.pucrs.smart.postgresql.Crud.CrudCaracteristicas;
import br.pucrs.smart.postgresql.Crud.CrudDataByBedroom;
import br.pucrs.smart.postgresql.Crud.CrudInfeccao;
import br.pucrs.smart.postgresql.Crud.CrudInternado;
import br.pucrs.smart.postgresql.Crud.CrudLeito;
import br.pucrs.smart.postgresql.Crud.CrudNurseException;
import br.pucrs.smart.postgresql.Crud.CrudPedidoLeito;
import br.pucrs.smart.postgresql.Crud.CrudValidacoes;
import br.pucrs.smart.postgresql.models.AlocacaoOtimizadaSql;
import br.pucrs.smart.postgresql.models.AlocacaoSugeridaSql;
import br.pucrs.smart.postgresql.models.DataByBed;
import br.pucrs.smart.postgresql.models.DataByBedroom;
import br.pucrs.smart.postgresql.models.DataByPatient;
import br.pucrs.smart.postgresql.models.InfeccaoPorPaciente;
import br.pucrs.smart.postgresql.models.InternadoSql;
import br.pucrs.smart.postgresql.models.LeitoSql;
import br.pucrs.smart.postgresql.models.NurseExceptionSql;
import br.pucrs.smart.postgresql.models.PedidoLeitoSql;
import br.pucrs.smart.postgresql.models.PlanoValidacaoSql;
import br.pucrs.smart.postgresql.models.ValidacaoSql;
import br.pucrs.smart.validator.models.ResultVal;
import br.pucrs.smart.validator.models.SimpleAllocation;
import br.pucrs.smart.validator.models.TempAlloc;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;

public class PostgresDb {
	

	public static List<Object> getBedsData() {
		List<Object> terms = new ArrayList<Object>();

		List<LeitoSql> leitos = CrudLeito.getLeitos();
		List<String> numsLeitos = new ArrayList<String>();

		leitos.forEach(leito -> {

			numsLeitos.add(leito.getNumero());
			terms.addAll(DataTranslator.translateBedData(leito));
		});

		// terms.addAll(DataTranslator.getDifferentIndividuals(numsLeitos));
		return terms;

	}

	public static List<Object> getBedsDataByRoom() {
		List<Object> terms = new ArrayList<Object>();
		List<DataByBedroom> quartos = CrudDataByBedroom.getDataByBedrooms();

		quartos.forEach(quarto -> {
			terms.addAll(DataTranslator.translateBedroomData(quarto));
		});
		
		return terms;
	}

	

	
	public static List<Object> getInPatientsData() {
		List<Object> terms = new ArrayList<Object>();

		List<InternadoSql> internados = CrudInternado.getInternados();
		// List<String> patientsCods = new ArrayList<String>();
		internados.forEach(internado -> {
			// patientsCods.add(internado.getCod_paciente());
			terms.addAll(DataTranslator.translateInPatientData(internado));
		});

		// terms.addAll(DataTranslator.getDifferentIndividuals(patientsCods));
		return terms;
	}

	public static List<Object> getPedidosLeitoData() {
		List<Object> terms = new ArrayList<Object>();

		List<PedidoLeitoSql> pedidos = CrudPedidoLeito.getPedidosLeito();
		// List<String> patientsCods = new ArrayList<String>();

		pedidos.forEach(pedido -> {
			// patientsCods.add(pedido.getCod_paciente());
			terms.addAll(DataTranslator.translateBedRequestsData(pedido));
		});

		// terms.addAll(DataTranslator.getDifferentIndividuals(patientsCods));
		return terms;
	}

	public static List<Object> getCaracteristicas() {

		List<Object> terms = new ArrayList<Object>();
		Map<String, List<String>> characteristicsFromDB = CrudCaracteristicas.getCaracteristicasInArrays();

		// characteristicsFromDB.forEach((key, values) -> {
		// terms.addAll(DataTranslator.getDifferentIndividuals(values));
		// });

		return terms;

	}

	public static List<Object> getInfeccoesPorPaciente() {

		List<Object> terms = new ArrayList<Object>();
		List<InfeccaoPorPaciente> infeccoes = CrudInfeccao.getInfeccoesPorPaciente();

		infeccoes.forEach(in -> {
			terms.addAll(DataTranslator.translateIsolationData(in));
		});

		return terms;

	}

	public static String addOptimiserResult(AlocacaoOtimizadaSql ao) {
		String res = CrudAlocacoesOtimizadas.addAlocacaoOtimizada(ao);
		if (res.contains("Erro")) {
			return "Erro";
		} else {
			return "Success";
		}
	}

	public static String addValidationResult(List<InternadoSql> laudos, boolean valido, String guid,
			String problem, String plan, List<ResultVal> finalResult) {

		ValidacaoSql val = new ValidacaoSql();
		val.setConcluido(false);
		val.setSaveAt(LocalDateTime.now());
		val.setValido(valido);
		val.setId(guid);
		val.setAlocar(false);
		val.setProblema(problem);
		val.setPlano(plan);

		laudos.forEach(laudo -> {
			PlanoValidacaoSql plv = new PlanoValidacaoSql();
			plv.setLaudoInternacaoId(laudo.getId());
			plv.setLeitoNum(laudo.getNumero_leito_atual());
			plv.setIsvalid(true);
			for (ResultVal resultVal : finalResult) {
				if (Integer.parseInt(resultVal.getIdPaciente()) == laudo.getId()) {
					plv.setIsvalid(false);
				}
			}
			val.addPlanoAlocacao(plv);

		});
		return CrudValidacoes.addValidacao(val);
	}

	public static String updateValidationResult(String id, String response) {
		String res = CrudValidacoes.updateResultValidacao(id, response);
		if (res.contains("Erro")) {
			return "Erro";
		} else {
			return "Success";
		}
	}

	public static String allocByValidationResult() {
		ValidacaoSql val = CrudValidacoes.getLastValidation();
		String res = "";
		for (PlanoValidacaoSql alocacao : val.getPlanoAlocacao()) {
			String resAlloc = allocate(alocacao.getLaudoInternacaoId(), alocacao.getLeitoNum());
			if (resAlloc.contains("Erro")) {
				res = resAlloc;
			}
		}
		if (res.contains("Erro")) {
			return "Erro";
		} else {
			String resVal = CrudValidacoes.setConcluido(val.getId(), true, true);
			res = res + " e " + resVal;
			return "Success";
		}
	}

	public static String concludLastValidationWithoutAlloc() {
		ValidacaoSql val = CrudValidacoes.getLastValidation();
		String res = CrudValidacoes.setConcluido(val.getId(), true, false);
		if (res.contains("Erro")) {
			return "Erro";
		} else {
			return "Success";
		}
	}

	public static String allocate(int laudoInternacaoId, String leitoNum) {
		String resLaudo = CrudInternado.registerAlloc(laudoInternacaoId, leitoNum);
		if (!resLaudo.contains("Erro")) {
			String resLeito = CrudLeito.registerAlloc(leitoNum);
			return resLeito;
		} else {
			return resLaudo;
		}
	}

	public static TempAlloc getTempAllocation() {
		return CrudAlocacaoTemporaria.getAlocacaoTemporaria();
	}

	public static ValidacaoSql prepareToValidate(TempAlloc t) {
		ValidacaoSql val = new ValidacaoSql();
		List<Integer> laudosId = new ArrayList<Integer>();
		List<String> leitosNum = new ArrayList<String>();
		t.getAllocation().forEach(a -> {
			laudosId.add(a.getLaudoInternacaoId());
			leitosNum.add(a.getLeitoNum());
		});

		val.setLaudos(CrudInternado.getInternadosByIdList(laudosId));

		for (InternadoSql laudo : val.getLaudos()) {
			for (SimpleAllocation plano : t.getAllocation()) {
				if (laudo.getId().equals(plano.getLaudoInternacaoId())) {
					laudo.setNumero_leito_atual(plano.getLeitoNum());
					continue;
				}
			}
		}

		val.setLeitos(CrudLeito.getLeitosByNumList(leitosNum));

		return val;
	}

	public static void setTempAlocValidated(int id) {
		CrudAlocacaoTemporaria.setTempAllocValidated(id);

	}

	public static String allocByOptimizerWithExceptions(List<String> names) {
		AlocacaoOtimizadaSql alloc = CrudAlocacoesOtimizadas.getLastOptimizerResult();
		List<InternadoSql> patients = CrudInternado.getInternadosByPacientes(names);
		List<AlocacaoSugeridaSql> newAlloc = new ArrayList<AlocacaoSugeridaSql>();
		newAlloc.addAll(alloc.getAlocacoesSugeridas());
		for (AlocacaoSugeridaSql a : newAlloc) {
			for (InternadoSql p : patients) {
				if (p.getId() == a.getLaudoInternacaoId()) {
					alloc.getAlocacoesSugeridas().remove(a);
				}
			}
		}
		if (alloc.getAlocacoesSugeridas().isEmpty()) {
			return "None";
		} else {
			String response = "";
			for (AlocacaoSugeridaSql a : alloc.getAlocacoesSugeridas()) {
				String res = allocate(a.getLaudoInternacaoId(), a.getLeitoNum());
				if (res.contains("Erro")) {
					response = res;
				}
			}
			if (response.contains("Erro")) {
				return "Erro";
			} else {
				return "Success";
			}
		}
	}

	public static String allocByOptimizerMoving(List<String> names) {
		AlocacaoOtimizadaSql alloc = CrudAlocacoesOtimizadas.getLastOptimizerResult();
		List<InternadoSql> exceptPatients = null;
		if (names != null) {
			exceptPatients = CrudInternado.getInternadosByPacientes(names);
		}
		List<Integer> idsLaudos = new ArrayList<Integer>();

		List<AlocacaoSugeridaSql> newAlloc = new ArrayList<AlocacaoSugeridaSql>();
		newAlloc.addAll(alloc.getAlocacoesSugeridas());

		for (AlocacaoSugeridaSql a : newAlloc) {
			idsLaudos.add(a.getLaudoInternacaoId());
			if (names != null) {
				for (InternadoSql p : exceptPatients) {
					if (p.getId() == a.getLaudoInternacaoId()) {
						alloc.getAlocacoesSugeridas().remove(a);
						idsLaudos.remove(a.getLaudoInternacaoId());
					}
				}
			}
		}

		if (alloc.getAlocacoesSugeridas().isEmpty()) {
			return "None";
		} else {
			String response = "";
			List<InternadoSql> laudos = CrudInternado.getInternadosByIdList(idsLaudos);
			for (InternadoSql laudo : laudos) {
				String res = CrudLeito.releaseBed(laudo.getNumero_leito_atual());
				if (res.contains("Erro")) {
					response = res;
				}

			}

			for (AlocacaoSugeridaSql a : alloc.getAlocacoesSugeridas()) {
				String res = allocate(a.getLaudoInternacaoId(), a.getLeitoNum());
				if (res.contains("Erro")) {
					response = res;
				}
			}
			if (response.contains("Erro")) {
				return "Erro";
			} else {
				return "Success";
			}
		}
	}

	public static String concludLastOptimizationWithoutAlloc() {
		return CrudAlocacoesOtimizadas.setLastOptimizationConcluded(false);
	}

	public static String allocByOptimizerResult() {
		AlocacaoOtimizadaSql alloc = CrudAlocacoesOtimizadas.getLastOptimizerResult();
		String response = "";
		for (AlocacaoSugeridaSql a : alloc.getAlocacoesSugeridas()) {
			String res = allocate(a.getLaudoInternacaoId(), a.getLeitoNum());
			if (res.contains("Erro")) {
				response = res;
			}
		}
		if (response.contains("Erro")) {
			return "Erro";
		} else {
			return "Success";
		}
	}

	public static String allocByValidationWithExceptions(List<String> names) {
		ValidacaoSql val = CrudValidacoes.getLastValidation();
		List<InternadoSql> patients = CrudInternado.getInternadosByPacientes(names);
		String res = "";

		List<PlanoValidacaoSql> newAlloc = new ArrayList<PlanoValidacaoSql>();
		newAlloc.addAll(val.getPlanoAlocacao());
		for (PlanoValidacaoSql a : newAlloc) {
			for (InternadoSql p : patients) {
				if (p.getId() == a.getLaudoInternacaoId()) {
					val.getPlanoAlocacao().remove(a);
				}
			}
		}
		if (val.getPlanoAlocacao().isEmpty()) {
			return "None";
		} else {
			for (PlanoValidacaoSql alocacao : val.getPlanoAlocacao()) {
				String resAlloc = allocate(alocacao.getLaudoInternacaoId(), alocacao.getLeitoNum());
				if (resAlloc.contains("Erro")) {
					res = resAlloc;
				}
			}

			if (res.contains("Erro")) {
				return "Erro";
			} else {
				String resVal = CrudValidacoes.setConcluido(val.getId(), true, true);
				res = res + " e " + resVal;
				return "Success";
			}
		}
	}

	public static String allocValidValPatients() {
		ValidacaoSql val = CrudValidacoes.getLastValidation();
		String res = "";
		List<PlanoValidacaoSql> newAlloc = new ArrayList<PlanoValidacaoSql>();
		for (PlanoValidacaoSql a : val.getPlanoAlocacao()) {
			if (a.isIsvalid()) {
				newAlloc.add(a);
			}
		}
		if (newAlloc.isEmpty()) {
			return "None";
		} else {
			for (PlanoValidacaoSql alocacao : newAlloc) {
				String resAlloc = allocate(alocacao.getLaudoInternacaoId(), alocacao.getLeitoNum());
				if (resAlloc.contains("Erro")) {
					res = resAlloc;
				}
			}

			if (res.contains("Erro")) {
				return "Erro";
			} else {
				String resVal = CrudValidacoes.setConcluido(val.getId(), true, true);
				res = res + " e " + resVal;
				return "Success";
			}
		}
	}

	public static List<Object> getNurseExceptionsByPatient(String patient) {
		List<Object> terms = new ArrayList<Object>();
		InternadoSql laudo = CrudInternado.getInternadoByPaciente(patient);
		if (laudo != null) {
			List<NurseExceptionSql> exceptions = CrudNurseException
					.getNurseExceptionsByLaudoInternacaoId(laudo.getId());
			if (exceptions.isEmpty()) {
				Literal l = ASSyntax.createLiteral(DataTranslator.FunctorNurseException);
				l.addTerm(ASSyntax.createString(patient));
				l.addTerm(ASSyntax.createString("None"));
				terms.add(l);
			} else {
				terms.addAll(DataTranslator.translateExceptions(exceptions, patient));
			}
		} else {
			Literal l = ASSyntax.createLiteral(DataTranslator.FunctorNurseException);
			l.addTerm(ASSyntax.createString(patient));
			l.addTerm(ASSyntax.createString("None"));
			terms.add(l);

		}
		return terms;
	}

	public static String confirmAllocation(String patient, String bed) {
		String res = "";
		InternadoSql laudo = CrudInternado.getInternadoByPaciente(patient);
		if (laudo != null) {
			res = allocate(laudo.getId(), bed);
			if (res.contains("Erro")) {
				return "Erro";
			} else {
				return "Success";
			}
		} else {
			return "None";
		}
	}

	public static String allocateByTempAlloc(TempAlloc temp) {
		String response = "";
		for (SimpleAllocation a : temp.getAllocation()) {
			String res = allocate(a.getLaudoInternacaoId(), a.getLeitoNum());
			if (res.contains("Erro")) {
				response = res;
			}
		}
		if (response.contains("Erro")) {
			return "Erro";
		} else {
			return "Success";
		}
	}
}
