// CArtAgO artifact code for bed allocation validator

package br.pucrs.smart.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import br.pucrs.smart.postgresql.PostgresDb;
import br.pucrs.smart.postgresql.models.LaudoInternacaoSql;
import br.pucrs.smart.postgresql.models.LeitoSql;
import br.pucrs.smart.postgresql.models.PlanoValidacaoSql;
import br.pucrs.smart.postgresql.models.ValidacaoSql;
import br.pucrs.smart.validator.models.ErrorVal;
import br.pucrs.smart.validator.models.PddlStrings;
import br.pucrs.smart.validator.models.ResultVal;
import br.pucrs.smart.validator.models.TempAlloc;
import cartago.Artifact;
import cartago.OPERATION;
import cartago.OpFeedbackParam;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class ValidatorArtifact extends Artifact {// implements IValidator {
	private static PDDL pddl = null;
	boolean domain = false;
	boolean problem = false;
	boolean plan = false;
	private List<LaudoInternacaoSql> laudos;
	private List<LeitoSql> leitos;

	void init() {
	}

	@OPERATION
	void getValidationResult(OpFeedbackParam<Literal> result) {
		TempAlloc tempAlloc = PostgresDb.getTempAllocation();
		if (tempAlloc != null) {
			ValidacaoSql val = PostgresDb.prepareToValidate(tempAlloc);
			// result.set(validate(val, tempAlloc));
		} else {
			Literal l = ASSyntax.createLiteral("result");
			l.addTerm(ASSyntax.createString("NULL"));
			result.set(l);
		}
	}

	ListTerm createMotiveBelief(List<ResultVal> finalResult) {
		Collection<Term> terms = new LinkedList<Term>();
		for (ResultVal r : finalResult) {
			Literal l = ASSyntax.createLiteral("err", ASSyntax.createString(r.getNomePaciente()));
			l.addTerm(ASSyntax.createString(r.getNumeroLeito()));
			if (r.getErrors() != null) {
				ListTerm errorsList = createErrorBelief(r.getErrors());
				l.addTerm(errorsList);
			}
			terms.add(l);
		}

		return ASSyntax.createList(terms);
	}

	ListTerm createErrorBelief(List<ErrorVal> errorsVal) {
		Collection<Term> terms = new LinkedList<Term>();
		for (ErrorVal e : errorsVal) {
			Literal l = ASSyntax.createLiteral("mot", ASSyntax.createString(e.getType()));
			l.addTerm(ASSyntax.createString(e.getPredicado()));
			l.addTerm(ASSyntax.createString(e.getPredType()));
			terms.add(l);
		}

		return ASSyntax.createList(terms);
	}

	String getPacienteName(String id) {
		for (LaudoInternacaoSql l : laudos) {
			if (l.getId().toString().equals(id))
				return l.getNomePaciente();
		}
		return null;
	}

	String getLeitoNumber(String id) {
		for (LeitoSql l : leitos) {
			if (l.getId().toString().equals(id))
				return l.getNumero();
		}
		return null;
	}

// 	public Literal validate(ValidacaoSql val, TempAlloc tempAlloc) {
// 		this.laudos = val.getLaudos();
// 		this.leitos = val.getLeitos();

// 		/*********************************************
// 		 * Transforming database data into pddl files
// 		 *******************************************/

// 		PddlBuilder a = new PddlBuilder(val.getLaudos(), val.getLeitos());
// 		PddlStrings pddlStrings = a.buildPddl();
// 		val.setProblema(pddlStrings.getProblem());
// 		val.setPlano(pddlStrings.getPlan());
// 		/********************************************/

// 		/***************************************
// 		 * Calling PDDL validator
// 		 *************************************/

// 		pddl = Parser.parseDomain("src/resources/domain.pddl");
// 		Parser.parseProblem(pddl, "problem", pddlStrings.getProblem());
// 		Parser.parsePlan(pddl, "plan", pddlStrings.getPlan());
// 		List<Object[]> out = pddl.tryPlanForce(false);

// 		/***************************************
// 		 * Building result
// 		 *************************************/

// 		List<ResultVal> finalResult = new ArrayList<>();
// 		for (Object[] o : out) {
// 			ResultVal resultVal = new ResultVal();
// //			System.out.print("Error in action \"( " );
// 			resultVal.setValid(false);
// 			int i = 0;
// 			for (String s : (String[]) o[0]) {
// //				System.out.print(s + " ");
// 				if (i == 1)
// 					resultVal.setIdPaciente(takeOutFirstChart(s));
// 				if (i == 2)
// 					resultVal.setIdLeito(takeOutFirstChart(s));
// 				i++;
// 			}
// //			System.out.println(")\"");
// 			if (((Object[]) (o[1])).length == 0) {
// 				ErrorVal errorVal = new ErrorVal();
// //				System.out.println("Invalid parameters");
// 				errorVal.setType("invalidParameters");
// 				resultVal.addErrors(errorVal);
// 			} else {
// 				List<String[]> l = (List<String[]>) ((Object[]) (o[1]))[0];
// 				if (l.size() > 0) {
// //					System.out.println("Missing positive predicates");
// 					for (String[] str : l) {
// 						ErrorVal errorVal = new ErrorVal();
// 						errorVal.setType("missingPositive");
// //						System.out.println(" ");
// 						int j = 0;
// 						for (String s : str) {
// //							System.out.println("s["+j+"]: "+s);
// 							if (j == 0)
// 								errorVal.setPredicado(changePredType(s));
// 							if (j == 1)
// 								errorVal.setId(takeOutFirstChart(s));
// 							if (j == 2)
// 								errorVal.setPredType(changePredType(s));
// 							j++;
// 						}
// 						resultVal.addErrors(errorVal);
// //						System.out.println("");
// 					}
// 				}
// 				l = (List<String[]>) ((Object[]) (o[1]))[1];
// 				if (l.size() > 0) {
// //					System.out.println("Present negative predicates");
// 					for (String[] str : l) {
// 						ErrorVal errorVal = new ErrorVal();
// 						errorVal.setType("presentNegative");
// //						System.out.println(" ");
// 						int j = 0;
// 						for (String s : str) {
// //							System.out.println("s["+j+"]: "+s);
// 							if (j == 0)
// 								errorVal.setPredicado(changePredType(s));
// 							if (j == 1)
// 								errorVal.setId(takeOutFirstChart(s));
// 							if (j == 2)
// 								errorVal.setPredType(changePredType(s));
// 							j++;
// 						}
// 						resultVal.addErrors(errorVal);
// //						System.out.println("");
// 					}
// 				}

// 			}
// 			resultVal.setNomePaciente(getPacienteName(resultVal.getIdPaciente()));
// 			resultVal.setNumeroLeito(getLeitoNumber(resultVal.getIdLeito()));
// 			finalResult.add(resultVal);
// 		}
// 		UUID guid = java.util.UUID.randomUUID();

// 		PostgresDb.addValidationResult(this.laudos, pddl.goalAchieved(), guid.toString(), pddlStrings.getProblem(),
// 				pddlStrings.getPlan(), finalResult);
// 		PostgresDb.setTempAlocValidated(tempAlloc.getId());

// 		pddl.valOut("src/resources/output.tex");

// 		Literal l = ASSyntax.createLiteral("result");
// 		l.addTerm(ASSyntax.createString(guid.toString()));
// 		l.addTerm(ASSyntax.createString(pddl.goalAchieved()));
// 		l.addTerm(createMotiveBelief(finalResult));
// //		defineObsProperty("result", guid.toString(), pddl.goalAchieved(), createMotiveBelief(finalResult));
// 		// result(Id, IsValid, Errors)
// 		return l;
// 	}

	String takeOutFirstChart(String str) {
		StringBuilder newStr = new StringBuilder();
		newStr.append(str);
		return (newStr.deleteCharAt(0)).toString();
	}

	String changePredType(String str) {
		switch (str) {
		case "bedstay":
			return "Tipo de Estadia ";
		case "bedroomtype":
			return "Tipo ";
		case "bedorigin":
			return "Tipo de Encaminhamento ";
		case "bedgender":
			return "Gênero ";
		case "bedage":
			return "Faixa Etária ";
		case "bedbirthtype":
			return "Tipo de puerpério ";
		case "bedcare":
			return "Cuidados ";
		case "bedspecialty":
			return "Especialidade ";
		case "bedisolation":
			return "Isolamento ";
		case "minimos":
			return "Mínimos";
		case "intensivos":
			return "Intensivos";
		case "semiintensivos":
			return "Semi-Intensivos";
		case "geral":
			return "Geral";
		case "cardiologia":
			return "Cardiologia";
		case "cirurgiabariatrica":
			return "Cirurgia Bariatrica";
		case "cirurgiacardiaca":
			return "Cirurgia Cardíaca";
		case "uclunidadedecuidadosespeciais":
			return "UCL";
		case "cirurgiadigestiva":
			return "Cirurgia Digestiva";
		case "cirurgiavascular":
			return "Cirurgia Vascular";
		case "endovascular":
			return "Endovascular";
		case "gastro":
			return "Gastro";
		case "ginecologia":
			return "Ginecologia";
		case "infecto":
			return "Infecto";
		case "medicinainterna":
			return "Medicina Interna";
		case "neurologia":
			return "Neurologia";
		case "obstetricia":
			return "Obstetrícia";
		case "oncologia":
			return "Oncologia";
		case "pneumo":
			return "Pneumo";
		case "psiquiatria":
			return "Psiquiatria";
		case "uti":
			return "UTI";
		case "aborto":
			return "Aborto";
		case "nascimento":
			return "Nascimento";
		case "crianca":
			return "Infantil";
		case "adulto":
			return "Adulto";
		case "adolescente":
			return "Adolescente";
		case "masculino":
			return "Masculino";
		case "feminino":
			return "Feminino";
		case "eletivo":
			return "Eletivo";
		case "agudo":
			return "Agudo";
		case "clinico":
			return "Clínico";
		case "cirurgico":
			return "Cirúrgico";
		case "longapermanencia":
			return "Longa Permanência";
		case "girorapido":
			return "Giro Rápido";
		default:
			return " ";
		}

	}
}