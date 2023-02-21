// CArtAgO artifact code for project smart

package br.pucrs.smart.optimiser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.pucrs.smart.postgresql.Crud.CrudInternado;
import br.pucrs.smart.postgresql.Crud.CrudLeito;
import br.pucrs.smart.postgresql.models.LaudoInternacaoSql;
import br.pucrs.smart.postgresql.models.LeitoSql;
import cartago.Artifact;
import cartago.OPERATION;
import cartago.OpFeedbackParam;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class OptimiserArtifact extends Artifact {
	private Optimiser optimiser;
	private OptimiserResult optimiserResult;

	void init() {
		try {
			optimiser = new Optimiser();
		} catch (Exception e) {
		}
	}

	@OPERATION
	void getOptimiserResult(OpFeedbackParam<OptimiserResult> response) {
		response.set(this.optimiserResult);
	}
	
	@OPERATION
	void getSuggestionByPatient(Object[] names, OpFeedbackParam<Literal> response) {
		List<String> arrNames = new ArrayList<String>();
		for (int i = 0; i < names.length; i++) {
			arrNames.add(names[i].toString());
		}
		if (optimiser == null)
			init();
		try {
			this.optimiserResult = optimiser.getSuggestionByPatient(arrNames);
			Literal optimiserBelief = createOptimiserBelief(this.optimiserResult);
//			Literal optimiserBelief = createBeliefToTest(arrNames);

			response.set(optimiserBelief);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	Literal createBeliefToTest(List<String> arrNames) {
		Literal l = ASSyntax.createLiteral("optimiserResult");
		l.addTerm(ASSyntax.createString("false"));
		Literal lna = ASSyntax.createLiteral("notAlloc");
		Collection<Term> terms = new LinkedList<Term>();
		for (String name : arrNames) {
			terms.add(ASSyntax.createString(name));
		}
		lna.addTerm(ASSyntax.createList(terms));
		l.addTerm(lna);
		Literal lsa = ASSyntax.createLiteral("sugestedAllocation");
		lsa.addTerm(ASSyntax.createList(new LinkedList<Term>()));
		l.addTerm(lsa);
		return l;
	}
	
	@OPERATION
	void getSuggestionByPatient(Object[] names, Object[] bedsToIgnore, OpFeedbackParam<Literal> response) {
		List<String> arrNames = new ArrayList<String>();
		for (int i = 0; i < names.length; i++) {
			arrNames.add(names[i].toString());
		}
		List<String> arrBeds = new ArrayList<String>();
		for (int i = 0; i < bedsToIgnore.length; i++) {
			arrBeds.add(bedsToIgnore[i].toString());
		}
		if (optimiser == null)
			init();
		try {
			this.optimiserResult = optimiser.getSuggestionByPatient(arrNames,arrBeds);
			Literal optimiserBelief = createOptimiserBelief(this.optimiserResult);
			response.set(optimiserBelief);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OPERATION
	// suggests allocation for all beds
	void suggestOptimisedAllocation(OpFeedbackParam<Literal> response) {
		if (optimiser == null)
			init();
		try {
			this.optimiserResult = optimiser.getOptimisationResult(0);
//			System.out.println("this.optimiserResult");
//			System.out.println(this.optimiserResult);
			Literal optimiserBelief = createOptimiserBelief(this.optimiserResult);
			response.set(optimiserBelief);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@OPERATION
	// suggests allocation for all beds
	void suggestOptimisedAllocationMoving(Byte numPatients, OpFeedbackParam<Literal> response) {
		if (optimiser == null)
			init();
		try {
			this.optimiserResult = optimiser.getOptimisationResult(numPatients.intValue());
//			System.out.println("this.optimiserResult");
//			System.out.println(this.optimiserResult);
			Literal optimiserBelief = createOptimiserBelief(this.optimiserResult);
			response.set(optimiserBelief);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// return a Literal like
	// optimiserResult(IsAllAllocated,notAlloc([PacienteName]), sugestedAllocation([alloc(PacienteName, NumLeito)])) 
	// where IsAllAllocated is boolean
	Literal createOptimiserBelief(OptimiserResult op) {

		Literal l = ASSyntax.createLiteral("optimiserResult");
		l.addTerm(ASSyntax.createString(op.isAllAllocated()));
		l.addTerm(createNotAllocBelief(op));
		l.addTerm(createSugestedAllocationBelief(op));
		return l;
	}

	// return a Literal like notAlloc([PacienteName])
	Literal createNotAllocBelief(OptimiserResult op) {

		Literal l = ASSyntax.createLiteral("notAlloc");
		Collection<Term> terms = new LinkedList<Term>();
		for (String id : op.getNotAllocated()) {
			for (LaudoInternacaoSql p : op.getLaudosData()) {
				if (p.getId().toString().equals(id)) {
					terms.add(ASSyntax.createString(p.getNomePaciente()));
				}
			}
		}
		l.addTerm(ASSyntax.createList(terms));
		return l;
	}

	// return a Literal like sugestedAllocation([alloc(PacienteName, NumLeito)])
	Literal createSugestedAllocationBelief(OptimiserResult op) {

		Literal l = ASSyntax.createLiteral("sugestedAllocation");
		Collection<Term> terms = new LinkedList<Term>();
		for (Allocation a : op.getSugestedAllocation()) {
			for (LaudoInternacaoSql p : op.getLaudosData()) {
				if (p.getId().toString().equals(a.getIdPaciente())) {
					Literal allocLiteral = ASSyntax.createLiteral("alloc");
					allocLiteral.addTerm(ASSyntax.createString(p.getNomePaciente()));
					allocLiteral.addTerm(ASSyntax.createString(p.getLeitoNum()));
					terms.add(allocLiteral);
				}
			}
		}
		l.addTerm(ASSyntax.createList(terms));
		return l;
	}

//	@OPERATION
//	// allocates all beds
//	void pddl() {
//		try {
//			optimiser.pddl();
////			optimiser.tests();
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//	}

// 	OptimiserResult getData(OptimiserResult op) throws InterruptedException, ExecutionException {
// 		OptimiserResult optimiserResult = new OptimiserResult();
// 		optimiserResult.setAllAllocated(op.isAllAllocated());
// 		optimiserResult.setAlreadySuggested(false);
// 		optimiserResult.setSugestedAllocation(op.getSugestedAllocation());
// 		optimiserResult.setNotAllocated(op.getNotAllocated());
// 		if (op.getSugestedAllocation() != null) {
// 			for (Allocation optimiser : op.getSugestedAllocation()) {
// 				LaudoInternacaoSql laudo = CrudInternado.getInternadoById(Integer.getInteger(optimiser.getIdPaciente()));
// 				if (laudo != null) {
// //				System.out.println(laudo.toString());

// 					String bedNumber = optimiser.getLeito();
// //						System.out.println("Leito: " + bedNumber);
// 					LeitoSql bed = CrudLeito.getLeitoByNumero(bedNumber);
// 					if (bed != null) {
// //						System.out.println(bed.toString());
// 						laudo.setLeitoNum(bed.getNumero());
// 					}

// 					optimiserResult.addLaudosData(laudo);
// 				}
// 			}
// 		}
// 		if (op.getNotAllocated() != null && !op.getNotAllocated().isEmpty()) {
// //			System.out.println("entreou no segundo for");
// 			for (String id : op.getNotAllocated()) {
// 				LaudoInternacaoSql laudo = CrudInternado.getInternadoById(Integer.getInteger(id));
// 				optimiserResult.addLaudosData(laudo);
// 			}
// 		}
// 		return optimiserResult;
// 	}
}