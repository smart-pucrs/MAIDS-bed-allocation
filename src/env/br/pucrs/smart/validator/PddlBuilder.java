package br.pucrs.smart.validator;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import br.pucrs.smart.postgresql.models.LaudoInternacaoSql;
import br.pucrs.smart.postgresql.models.LeitoSql;
import br.pucrs.smart.validator.models.PddlStrings;

public class PddlBuilder {

	private ArrayList<LaudoInternacaoSql> pacientes;
	private ArrayList<LeitoSql> leitos;
	private LeitoSql leito;

	public PddlBuilder(List<LaudoInternacaoSql> laudos, List<LeitoSql> leitos) {
		this.pacientes = (ArrayList<LaudoInternacaoSql>) laudos;
		this.leitos = (ArrayList<LeitoSql>) leitos;

//		System.out.println("## PddlBuilder created ##");		
	}

	public PddlStrings buildPddl() {
		PddlStrings builtPddl = new PddlStrings();
//		int countPlano = 0;
		StringBuilder problem = new StringBuilder();
		StringBuilder objects = new StringBuilder();
		StringBuilder initPatient = new StringBuilder();
		StringBuilder initLeito = new StringBuilder();
		StringBuilder goal = new StringBuilder();
		StringBuilder plan = new StringBuilder();

		for (LaudoInternacaoSql paciente : pacientes) {
			// #### Problem
			objects.append('\n').append("  " + concatP(paciente.getId().toString()) + " - patient ");

			initPatient.append("  (patient" + myTrim(paciente.getEspecialidade()) + " "
					+ concatP(paciente.getId().toString()) + ")").append("\n");
			initPatient.append("  (patientspecialty " + concatP(paciente.getId().toString()) + " "
					+ myTrim(paciente.getEspecialidade()) + ")").append("\n");
			initPatient.append("  (patientage " + concatP(paciente.getId().toString()) + " "
					+ myTrim(paciente.getFaixaEtaria()) + ")").append("\n");
			initPatient.append("  (patientgender " + concatP(paciente.getId().toString()) + " "
					+ myTrim(paciente.getGenero()) + ")").append("\n");
			initPatient.append("  (patientcare " + concatP(paciente.getId().toString()) + " "
					+ myTrim(paciente.getTipoDeCuidado()) + ")").append("\n");
			initPatient.append("  (patientorigin " + concatP(paciente.getId().toString()) + " "
					+ myTrim(paciente.getTipoDeEncaminhamento()) + ")").append("\n");
			initPatient.append("  (patientroomtype " + concatP(paciente.getId().toString()) + " "
					+ myTrim(paciente.getTipoDeLeito()) + ")").append("\n");
			initPatient.append("  (patientstay " + concatP(paciente.getId().toString()) + " "
					+ myTrim(paciente.getTipoDeEstadia()) + ")").append("\n");
			if (paciente.getEspecialidade().equals("Isolamento")) {
				initPatient.append("\n").append("  (isolation " + concatP(paciente.getId().toString()) + ")");
			}
			if (paciente.getEspecialidade().equals("Obstetricia")) {
				initPatient.append("\n").append("  (patientbirthtype " + concatP(paciente.getId().toString()) + " "
						+ myTrim(paciente.getTipoDePuerperio()) + ")");
			}

			goal.append(" (allocated " + concatP(paciente.getId().toString()) + ")");

			// ### bed
			if (paciente.getLeitoNum() != null) {

				this.leito = this.leitos.stream().filter(a -> a.getNumero().equals(paciente.getLeitoNum()))
						.collect(Collectors.toList()).get(0);
				objects.append("\n").append("  " + concatB(this.leito.getId().toString()) + " - bed ");

				initLeito.append("\n").append("  (bedfree " + concatB(this.leito.getId().toString()) + ")")
						.append("\n");
//				initLeito.append("  (bed" + myTrim(this.leito.getEspecialidade()) + " "
//						+ concatB(this.leito.getId().toString()) + ")").append("\n");
//				initLeito.append("  (bedspecialty " + concatB(this.leito.getId().toString()) + " "
//						+ myTrim(this.leito.getEspecialidade()) + ")").append("\n");
//				initLeito.append("  (bedage " + concatB(this.leito.getId().toString()) + " "
//						+ myTrim(this.leito.getFaixaEtaria()) + ")").append("\n");
//				initLeito.append("  (bedgender " + concatB(this.leito.getId().toString()) + " "
//						+ myTrim(this.leito.getGenero()) + ")").append("\n");
//				initLeito.append("  (bedcare " + concatB(this.leito.getId().toString()) + " "
//						+ myTrim(this.leito.getTipoDeCuidado()) + ")").append("\n");
//				initLeito.append("  (bedorigin " + concatB(this.leito.getId().toString()) + " "
//						+ myTrim(this.leito.getTipoDeEncaminhamento()) + ")").append("\n");
//				initLeito.append("  (bedroomtype " + concatB(this.leito.getId().toString()) + " "
//						+ myTrim(this.leito.getTipoDeLeito()) + ")").append("\n");
//				initLeito.append("  (bedstay " + concatB(this.leito.getId().toString()) + " "
//						+ myTrim(paciente.getTipoDeEstadia()) + ")").append("\n");
//
//				if (this.leito.getEspecialidade().equals("Isolamento")) {
//					initLeito.append("  (bedisolation " + concatB(this.leito.getId().toString()) + ")");
//				}
//				if (this.leito.getEspecialidade().equals("Obstetricia")) {
//					initLeito.append("  (bedbirthtype " + concatB(this.leito.getId().toString()) + " "
//							+ myTrim(this.leito.getTipoDePuerperio()) + ")");
//				}

			}

			// #### Plan
			switch (paciente.getEspecialidade()) {
			case "UTI":
				plan.append("( alocateuti " + concatP(paciente.getId().toString()) + ")").append("\n");
				break;

			case "Isolamento":
				plan.append("( allocateisolation " + concatP(paciente.getId().toString()) + " "
						+ concatB(this.leito.getId().toString()) + ")").append("\n");
				break;

			case "Obstetrícia":
				plan.append("( allocateobstetricia " + concatP(paciente.getId().toString()) + " "
						+ concatB(this.leito.getId().toString()) + " " + myTrim(paciente.getTipoDePuerperio()) + ")")
						.append("\n");
				break;

			case "UCL – Unidade de Cuidados Especiais":
				plan.append("( allocateucl " + concatP(paciente.getId().toString()) + " "
						+ concatB(this.leito.getId().toString()) + " " + myTrim(paciente.getFaixaEtaria()) + ")")
						.append("\n");
				break;

			case "AVC":
				plan.append("( allocateavc " + concatP(paciente.getId().toString()) + " "
						+ concatB(this.leito.getId().toString()) + " " + myTrim(paciente.getGenero()) + ")")
						.append("\n");
				break;

			case "Psiquiatria":
				plan.append("( allocatepsiquiatria " + concatP(paciente.getId().toString()) + " "
						+ concatB(this.leito.getId().toString()) + " " + myTrim(paciente.getGenero()) + ")")
						.append("\n");
				break;

			case "Cirurgia bariátrica":
				plan.append("( allocatecirurgiabariatrica " + concatP(paciente.getId().toString()) + " "
						+ concatB(this.leito.getId().toString()) + " " + myTrim(paciente.getGenero()) + ")")
						.append("\n");
				break;

			case "Ginecologia":
				plan.append("( allocateginecologia " + concatP(paciente.getId().toString()) + " "
						+ concatB(this.leito.getId().toString()) + " " + myTrim(paciente.getTipoDeLeito()) + ")")
						.append("\n");
				break;

			default:
				plan.append("( allocate " + concatP(paciente.getId().toString()) + " "
						+ concatB(this.leito.getId().toString().toString()) + " " + myTrim(paciente.getEspecialidade())
						+ " " + myTrim(paciente.getTipoDeEstadia()) + " " + myTrim(paciente.getTipoDeLeito()) + " "
						+ myTrim(paciente.getTipoDeEncaminhamento()) + " " + myTrim(paciente.getGenero()) + " "
						+ myTrim(paciente.getFaixaEtaria()) + " " + myTrim(paciente.getTipoDeCuidado()) + ")")
						.append("\n");
				break;
			}

//	        countPlano = countPlano + 1;
		}

		problem.append("(define (problem hospital-problem)").append("\n");
		problem.append("(:domain hospitaldomain)").append("\n");
		problem.append("(:objects").append("\n");
		problem.append("\n");
		problem.append("  ;variacoes possiveis").append("\n");
		problem.append("  minimos - care").append("\n");
		problem.append("  intensivos - care").append("\n");
		problem.append("  semiintensivos - care").append("\n");
		problem.append("  geral - specialty").append("\n");
		problem.append("  cardiologia - specialty").append("\n");
		problem.append("  cirurgiabariatrica - specialty").append("\n");
		problem.append("  cirurgiacardiaca - specialty").append("\n");
		problem.append("  uclunidadedecuidadosespeciais - specialty").append("\n");
		problem.append("  cirurgiadigestiva - specialty").append("\n");
		problem.append("  cirurgiavascular - specialty").append("\n");
		problem.append("  endovascular - specialty").append("\n");
		problem.append("  gastro - specialty").append("\n");
		problem.append("  ginecologia - specialty").append("\n");
		problem.append("  infecto - specialty").append("\n");
		problem.append("  medicinainterna - specialty").append("\n");
		problem.append("  neurologia - specialty").append("\n");
		problem.append("  obstetricia - specialty").append("\n");
		problem.append("  oncologia - specialty").append("\n");
		problem.append("  pneumo - specialty").append("\n");
		problem.append("  psiquiatria - specialty").append("\n");
		problem.append("  uti - specialty").append("\n");
		problem.append("  aborto - birthtype").append("\n");
		problem.append("  nascimento - birthtype").append("\n");
		problem.append("  crianca - age").append("\n");
		problem.append("  adulto - age").append("\n");
		problem.append("  adolescente - age").append("\n");
		problem.append("  indefinido - age").append("\n");
		problem.append("  masculino - gender").append("\n");
		problem.append("  feminino - gender").append("\n");
		problem.append("  eletivo - origin").append("\n");
		problem.append("  agudo - origin").append("\n");
		problem.append("  clinico - roomtype").append("\n");
		problem.append("  cirurgico - roomtype").append("\n");
		problem.append("  longapermanencia - stay").append("\n");
		problem.append("  girorapido - stay").append("\n");
		problem.append("\n");
		problem.append("  ").append(objects).append("\n");
		problem.append("  )").append("\n");
		problem.append("  (:init ").append("\n");
		problem.append(" ").append(initPatient).append("\n");
		problem.append("  ").append(initLeito).append("\n");
		problem.append("  )").append("\n");
		problem.append("  (:goal (and ").append(goal).append("\n");
		problem.append("  )").append("\n");
		problem.append(" )").append("\n");
		problem.append(")");

		builtPddl.setPlan(plan.toString());

		builtPddl.setProblem(problem.toString());

		return builtPddl;
	}

	String myTrim(String str) {
		str = str.replaceAll("-", "");
		return Normalizer.normalize(StringUtils.deleteWhitespace(str.toLowerCase()), Normalizer.Form.NFD)
				.replaceAll("[^\\p{ASCII}]", "");
	}

	String concatP(String uid) {
		return 'p' + uid;
	}

	String concatB(String uid) {
		return 'b' + uid;
	}

}