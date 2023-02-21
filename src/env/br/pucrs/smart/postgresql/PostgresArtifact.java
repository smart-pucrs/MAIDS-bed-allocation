// CArtAgO artifact code for project explainable_agents

package br.pucrs.smart.postgresql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import br.pucrs.smart.optimiser.OptimiserResult;
import br.pucrs.smart.postgresql.Crud.CrudDataByBedroom;
import br.pucrs.smart.postgresql.Crud.CrudLeito;
import br.pucrs.smart.postgresql.models.AlocacaoOtimizadaSql;
import br.pucrs.smart.postgresql.models.AlocacaoSugeridaSql;
import br.pucrs.smart.postgresql.models.DataByBed;
import br.pucrs.smart.postgresql.models.DataByBedroom;
import br.pucrs.smart.postgresql.models.LeitoSql;
import cartago.Artifact;
import cartago.INTERNAL_OPERATION;
import cartago.OPERATION;
import cartago.OpFeedbackParam;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class PostgresArtifact extends Artifact {
	List<DataByBed> leitos;
	void init() {
		System.out.println("[PostgresArtifact] started");
		this.leitos = CrudDataByBedroom.getDataByBeds();
	}

	/**
	 * @return A list of ({@link InternadoSql}) translated to Literal.
	 */
	@OPERATION
	void getPatientsData(OpFeedbackParam<Literal[]> data) {
		List<Object> patientsData;
		patientsData = PostgresDb.getInPatientsData();
		data.set(patientsData.toArray(new Literal[patientsData.size()]));
	}

	/**
	 * @return A list of ({@link PedidoLeitoSql}) translated to Literal.
	 */
	@OPERATION
	void getPedidosLeitoData(OpFeedbackParam<Literal[]> data) {
		List<Object> pedidosLeitoData;
		pedidosLeitoData = PostgresDb.getPedidosLeitoData();
		data.set(pedidosLeitoData.toArray(new Literal[pedidosLeitoData.size()]));

	}

	/**
	 * @return A list of ({@link DataByBedroom}) translated to Literal.
	 */
	@OPERATION
	void getBedsDataByRoom(OpFeedbackParam<Literal[]> data) {
		List<Object> bedsData;
		bedsData = PostgresDb.getBedsDataByRoom();
		data.set(bedsData.toArray(new Literal[bedsData.size()]));
	}

	/**
	 * @return A list of ({@link InfeccaoPorPaciente}) translated to Literal.
	 */
	@OPERATION
	void getInfeccoesData(OpFeedbackParam<Literal[]> data) {
		List<Object> infeccoesData;
		infeccoesData = PostgresDb.getInfeccoesPorPaciente();
		data.set(infeccoesData.toArray(new Literal[infeccoesData.size()]));

	}

	/**
	 * @return A list of ({@link LeitoSql}) translated to Literal.
	 */
	@OPERATION
	void getBedsData(OpFeedbackParam<Literal[]> data) {
		List<Object> bedsData;
		bedsData = PostgresDb.getBedsData();
		data.set(bedsData.toArray(new Literal[bedsData.size()]));
	}

	/**
	 * @return A list of ({@link DifferentIndividuals}) translated to Literal.
	 */
	@OPERATION
	void getCaracteristicas(OpFeedbackParam<Literal[]> data) {
		List<Object> caracteristicas;
		caracteristicas = PostgresDb.getCaracteristicas();
		data.set(caracteristicas.toArray(new Literal[caracteristicas.size()]));

	}

	@OPERATION
	void updateValidationResult(String id, String result,OpFeedbackParam<String> response) {
		response.set(PostgresDb.updateValidationResult(id, result));
	}

	@OPERATION
	void allocBylastValidationResult(OpFeedbackParam<String> response) {
		response.set(PostgresDb.allocByValidationResult());
	}
	
	@OPERATION
	void allocValidValPatients(OpFeedbackParam<String> response) {
		response.set(PostgresDb.allocValidValPatients());
	}

	@OPERATION
	void concludLastValidation(OpFeedbackParam<String> response) {
		response.set(PostgresDb.concludLastValidationWithoutAlloc());
	}
	
	@OPERATION
	void allocByValidationWithExceptions(Object[] names, OpFeedbackParam<String> response) {
		List<String> arrNames = new ArrayList<String>();
		for (int i = 0; i < names.length; i++) {
			arrNames.add(names[i].toString());
		}
		response.set(PostgresDb.allocByValidationWithExceptions(arrNames));
	}

	@OPERATION
	void setOptimiserResult(OptimiserResult result, OpFeedbackParam<String> response) {
		response.set(PostgresDb.addOptimiserResult(opResultToAlocOtSql(result)));
	}

	@OPERATION
	void cancelOpAllocation(OpFeedbackParam<String> response) {
		response.set(PostgresDb.concludLastOptimizationWithoutAlloc());
	}
	
	@OPERATION
	void allocByOptimizerWithExceptions(Object[] names, OpFeedbackParam<String> response) {
		List<String> arrNames = new ArrayList<String>();
		for (int i = 0; i < names.length; i++) {
			arrNames.add(names[i].toString());
		}
		response.set(PostgresDb.allocByOptimizerWithExceptions(arrNames));
	}
	
	@OPERATION
	void allocByOptimizerResult(OpFeedbackParam<String> response) {
		response.set(PostgresDb.allocByOptimizerResult());
	}
	
	@OPERATION
	void confirmAllocation(String patient, String bed, OpFeedbackParam<String> response) {
		response.set(PostgresDb.confirmAllocation(patient, bed));
	}

	@OPERATION
	void getBed(String genero, String faixaEtaria, String tipEsp, String acomodacao, String status, OpFeedbackParam<String> response) {
		//filter this.leitos by the parameters
		List<DataByBed> filteredBeds = this.leitos;
		System.out.println("Size: "+ filteredBeds.size());
		System.out.println("genero: "+ genero);
		System.out.println("faixaEtaria: "+ faixaEtaria);
		System.out.println("tipEsp: "+ tipEsp);
		System.out.println("tipEsp.equals(): "+ tipEsp.equals(""));
		System.out.println("acomodacao: "+ acomodacao);
		System.out.println("status: "+ status);
		
		if (!status.equals("")) {
			filteredBeds = filteredBeds.stream()
			.filter(b -> b.getStatus_leito() != null & b.getStatus_leito().contains(status)).collect(Collectors.toList());
		}
		// if (filteredBeds.size() > 0) {
		// 	if (!genero.equals("")) {
		// 		filteredBeds = this.leitos.stream()
		// 		.filter(b -> b.getGenero() != null & b.getGenero().contains(genero)).collect(Collectors.toList());
		// 	}
		// 	if (filteredBeds.size() > 0) {
		// 		if (!faixaEtaria.equals("")) {
		// 			filteredBeds = filteredBeds.stream()
		// 			.filter(b -> b.getFaixa_etaria() != null & b.getFaixa_etaria().contains(faixaEtaria)).collect(Collectors.toList());
		// 		}
		// 		if (filteredBeds.size() > 0) {
		// 			if (!tipEsp.equals("")) {
		// 				filteredBeds = filteredBeds.stream()
		// 				.filter(b -> b.getTipo_especialidade() != null & b.getEspecialidade().contains(tipEsp)).collect(Collectors.toList());
		// 				if (filteredBeds.size() > 0) {
		// 					if (!acomodacao.equals("")) {
		// 						filteredBeds = filteredBeds.stream()
		// 						.filter(b -> b.getAcomodacao() != null & b.getAcomodacao().contains(acomodacao)).collect(Collectors.toList());
		// 					}
		// 				}
		// 			}
		// 		}
		// 	}
			
		// }
		if (filteredBeds.size() == 0) {
			response.set("NONE");
			return;
		}
		response.set(filteredBeds.get(0).getNumero());
	}
	
	@OPERATION
	void allocByOptimizerMoving(OpFeedbackParam<String> response) {
		response.set(PostgresDb.allocByOptimizerMoving(null));
	}
	
	@OPERATION
	void allocByOptimizerMoving(Object[] names, OpFeedbackParam<String> response) {
		List<String> arrNames = new ArrayList<String>();
		for (int i = 0; i < names.length; i++) {
			arrNames.add(names[i].toString());
		}
		response.set(PostgresDb.allocByOptimizerMoving(arrNames));
	}
	
	
	
	@OPERATION
	void getNurseExceptions(String patient, OpFeedbackParam<Literal[]> response) {
		List<Object> exceptions;
		exceptions = PostgresDb.getNurseExceptionsByPatient(patient);
		response.set(exceptions.toArray(new Literal[exceptions.size()]));
	}

//	@Override // REVIEW
	public void updateBB(String type, Collection<Term> terms) {
		System.out.println("************ [PostgresArtifact] updateBB");
		execInternalOp("createUpdateBelief", type, terms);

	}

	@INTERNAL_OPERATION
	void createUpdateBelief(String type, Collection<Term> terms) {
		System.out.println("[************* PostgresArtifact] createUpdateBelief");
		defineObsProperty(type, ASSyntax.createList(terms));
	}
	
	AlocacaoOtimizadaSql opResultToAlocOtSql(OptimiserResult result) {
		AlocacaoOtimizadaSql alloc = new AlocacaoOtimizadaSql();
		alloc.setAllAllocated(result.isAllAllocated());
		alloc.setAlocar(null);
		alloc.setConcluido(false);
		alloc.setAlreadySuggested(false);

		List<AlocacaoSugeridaSql> suggested = new ArrayList<AlocacaoSugeridaSql>();
		result.getSugestedAllocation().forEach(a -> {
			AlocacaoSugeridaSql as = new AlocacaoSugeridaSql();
			as.setLaudoInternacaoId(Integer.parseInt(a.getIdPaciente()));
			as.setLeitoNum(a.getLeito());
			suggested.add(as);
		});
		alloc.setAlocacoesSugeridas(suggested);
		List<Integer> notAlloc = new ArrayList<Integer>();
		result.getNotAllocated().forEach(a -> {
			notAlloc.add(Integer.parseInt(a));
		});
		alloc.setNotAllocated(notAlloc);
		return alloc;
	}
}
