package br.pucrs.smart.postgresql;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import br.pucrs.smart.postgresql.models.DataByBed;
import br.pucrs.smart.postgresql.models.DataByBedroom;
import br.pucrs.smart.postgresql.models.DataByPatient;
// import br.pucrs.smart.postgresql.models.Infeccao;
import br.pucrs.smart.postgresql.models.InfeccaoPorPaciente;
import br.pucrs.smart.postgresql.models.InternadoSql;
// import br.pucrs.smart.postgresql.models.LaudoInternacaoSql;
import br.pucrs.smart.postgresql.models.LeitoSql;
import br.pucrs.smart.postgresql.models.NurseExceptionSql;
import br.pucrs.smart.postgresql.models.PedidoLeitoSql;
import jason.asSyntax.ASSyntax;
// import jason.asSyntax.ListTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class DataTranslator {

	private final static String FunctorPaciente = "paciente";
	private final static String FunctorLeito = "leito";
	private final static String FunctorQuarto = "quarto";
	
	private final static String FunctorDifferentIndividuals = "differentIndividuals";

	// private final static String FunctorQuartoELocalizadoNaUnidade = "e_localizado_na_unidade";
	private final static String FunctorQuartoPossuiStatus = "quarto_possui_status";
	
	private final static String FunctorLeitoPossuiStatus = "possui_status";
	private final static String FunctorLeitoEDaEspecialidade = "leito_e_da_especialidade";
	private final static String FunctorLeitoEPrefDaEspecialidade = "leito_e_pref_da_especialidade";
	private final static String FunctorLeitoEDaFaixaEtaria = "leito_e_da_faixa_etaria";
	private final static String FunctorLeitoEDoGenero = "leito_e_do_genero";
	// private final static String FunctorLeitoEPrefDoGenero = "leito_e_pref_do_genero";
	private final static String FunctorLeitoEDoTipoEspecialidade = "leito_e_do_tipo_especialidade";
	private final static String FunctorLeitoEPreferencialmenteIsolamento = "e_preferencialmente_isolamento";
	// private final static String FunctorLeitoEPreferencialmenteSus = "leito_e_preferencialmente_sus";
	private final static String FunctorLeitoSeLocalizaNoQuarto = "se_localiza_no_quarto";
	private final static String FunctorLeitoEDeAcomodacao = "e_de_acomodacao";
	// private final static String FunctorLeitoEDeCobertura = "e_de_cobertura";
	private final static String FunctorLeitoEDeConvenio = "e_de_convenio";
	private final static String FunctorLeitoEDeIsolamento = "e_de_isolamento";

	
	// private final static String FunctorDataInternacao = "data_internacao"; //data_internacao(CodPaciente,NumLeito,Data)
	// private final static String FunctorDataAlta = "data_alta"; //data_alta(CodPaciente,NumLeito,Data)

	private final static String FunctorEstaAlocadoNo = "esta_alocado_no";
	private final static String FunctorEDaEspecialidade = "e_da_especialidade";
	private final static String FunctorEDaFaixaEtaria = "e_da_faixa_etaria";
	private final static String FunctorEDoGenero = "e_do_genero";
	private final static String FunctorEDoTipoEspecialidade = "e_do_tipo_especialidade";
	// private final static String FunctorPossuiAcomodacao = "possui_acomodacao";
	private final static String FunctorPossuiCobertura = "possui_cobertura";
	private final static String FunctorPossuiConvenio = "possui_convenio";
	private final static String FunctorPossuiInfeccao = "possui_infeccao"; //possui_infeccao(CodPaciente,[Infeccoes])
	private final static String FunctorPossuiPedidoAtivo = "possui_pedido_ativo";

	// private final static String FunctorAtendimento = "atendimento"; //prontuario(CodPaciente, Atendimento)
	
	public final static String FunctorNurseException = "nurse_exception";
	
	static Collection<Term> translateInPatientData(InternadoSql paciente) {
		Collection<Term> terms = new LinkedList<Term>();
		
		terms.add(propertyToLiteralUniq(FunctorPaciente, paciente.getCod_paciente()));
		// terms.add(propertyToLiteralStr(FunctorAtendimento, paciente.getCod_paciente(), paciente.getAtendimento()));
		terms.add(propertyToLiteralStr(FunctorEDaFaixaEtaria, paciente.getCod_paciente(), paciente.getFaixaetaria()));
		terms.add(propertyToLiteralStr(FunctorEDoGenero, paciente.getCod_paciente(), paciente.getGenero()));
		terms.add(propertyToLiteralStr(FunctorEDaEspecialidade, paciente.getCod_paciente(), paciente.getEspecialidade()));
		terms.add(propertyToLiteralStr(FunctorEDoTipoEspecialidade, paciente.getCod_paciente(), paciente.getTipo_especialidade()));
		terms.add(propertyToLiteralStr(FunctorPossuiCobertura, paciente.getCod_paciente(), paciente.getCobertura_paciente()));
		terms.add(propertyToLiteralStr(FunctorEstaAlocadoNo, paciente.getCod_paciente(), paciente.getNumero_leito_atual()));

		terms.add(propertyToLiteralStr(FunctorLeitoEDaFaixaEtaria, paciente.getNumero_leito_atual(), paciente.getFaixaetaria()));
		terms.add(propertyToLiteralStr(FunctorLeitoEDoGenero, paciente.getNumero_leito_atual(), paciente.getGenero()));
		terms.add(propertyToLiteralStr(FunctorLeitoEDaEspecialidade, paciente.getNumero_leito_atual(), paciente.getEspecialidade()));
		terms.add(propertyToLiteralStr(FunctorLeitoEDoTipoEspecialidade, paciente.getNumero_leito_atual(), paciente.getTipo_especialidade()));

		return terms;
	}

	static Collection<Term> translateBedRequestsData(PedidoLeitoSql pedido) {
		Collection<Term> terms = new LinkedList<Term>();
		
		terms.add(propertyToLiteralStr(FunctorPossuiPedidoAtivo, pedido.getCod_paciente(), pedido.getTipo_solicitacao()));

		return terms;
	}

	static Collection<Term> translateIsolationData(InfeccaoPorPaciente infeccao) {
		Collection<Term> terms = new LinkedList<Term>();
		terms.add(propertyToLiteralStrList(FunctorPossuiInfeccao, infeccao.getCod_paciente(), infeccao.getCds_categoria()));
		return terms;
	}

	static Collection<Term> translateBedData(LeitoSql leito) {
		Collection<Term> terms = new LinkedList<Term>();
		terms.add(propertyToLiteralUniq(FunctorLeito, leito.getNumero()));
		terms.add(propertyToLiteralUniq(FunctorQuarto, leito.getQuarto()));
		terms.add(propertyToLiteralStr(FunctorLeitoSeLocalizaNoQuarto, leito.getNumero(), leito.getQuarto()));
		// terms.add(propertyToLiteralStr(FunctorQuartoELocalizadoNaUnidade, leito.getQuarto(), leito.getUnidade()));
		terms.add(propertyToLiteralStr(FunctorLeitoEPrefDaEspecialidade, leito.getNumero(), leito.getEspecialidade_preferencial()));
		terms.add(propertyToLiteralStr(FunctorLeitoEDoGenero, leito.getNumero(), leito.getGenero()));
		terms.add(propertyToLiteralStr(FunctorLeitoPossuiStatus, leito.getNumero(), leito.getStatus_leito()));
		terms.add(propertyToLiteralStr(FunctorLeitoEDeAcomodacao, leito.getNumero(), leito.getAcomodacao()));
		terms.add(propertyToLiteralStrBool(FunctorLeitoEPreferencialmenteIsolamento, leito.getNumero(), leito.getPreferencialmente_isolamento()));
		// terms.add(propertyToLiteralStrBool(FunctorLeitoEPreferencialmenteSus, leito.getNumero(), leito.getConvenio() == "SIM" ? true : false));
		// terms.add(propertyToLiteralStrBool(FunctorLeitoEDeIsolamento, leito.getNumero(), TipoIsolamento != null ? TipoIsolamento : "NONE"));
		
		return terms;
	}

	static Collection<Term> translateBedroomData(DataByBedroom quarto) {
		Collection<Term> terms = new LinkedList<Term>();
		terms.add(propertyToLiteralUniq(FunctorQuarto, quarto.getQuarto()));
		terms.add(propertyToLiteralStr(FunctorQuartoPossuiStatus, quarto.getQuarto(), quarto.getStatus_quarto()));
		

		quarto.getLeitos().forEach(leito -> {
			if (leito.getStatus_leito().equals("Vago")) {
				leito.setGenero(quarto.getGenero());
				leito.setEspecialidade(quarto.getEspecialidade());
				leito.setTipo_especialidade(quarto.getTipo_especialidade());
				leito.setFaixa_etaria(quarto.getFaixa_etaria());
				leito.setCds_categoria_isolamento(quarto.getCds_categoria_isolamento());
			}
			terms.addAll(translateBedData(leito));
		});
		


		return terms;
	}

	static Collection<Term> translateBedData(DataByBed leito) {
		Collection<Term> terms = new LinkedList<Term>();
		terms.add(propertyToLiteralUniq(FunctorLeito, leito.getNumero()));
		terms.add(propertyToLiteralStr(FunctorLeitoSeLocalizaNoQuarto, leito.getNumero(), leito.getQuarto()));
		terms.add(propertyToLiteralStr(FunctorLeitoEPrefDaEspecialidade, leito.getNumero(), leito.getEspecialidade_preferencial() != null ? leito.getEspecialidade_preferencial() : "NONE"));
		terms.add(propertyToLiteralStr(FunctorLeitoPossuiStatus, leito.getNumero(), leito.getStatus_leito()));
		terms.add(propertyToLiteralStr(FunctorLeitoEDeAcomodacao, leito.getNumero(), leito.getAcomodacao()));
		terms.add(propertyToLiteralStr(FunctorLeitoEDeConvenio, leito.getNumero(), leito.getConvenio()));
		terms.add(propertyToLiteralStrBool(FunctorLeitoEPreferencialmenteIsolamento, leito.getNumero(), leito.getPreferencialmente_isolamento() != null ? leito.getPreferencialmente_isolamento() : false));
		// terms.add(propertyToLiteralStrBool(FunctorLeitoEPreferencialmenteSus, leito.getNumero(), leito.getConvenio() == "SIM" ? true : false));
		
		if (leito.getCds_categoria_isolamento() != null) {
			terms.add(propertyToLiteralStrList(FunctorLeitoEDeIsolamento, leito.getNumero(), leito.getCds_categoria_isolamento()));
		} else {
			terms.add(propertyToLiteralStr(FunctorLeitoEDeIsolamento, leito.getNumero(), "NONE"));
		}

		if (leito.getPaciente() != null) {
			terms.add(propertyToLiteralStr(FunctorLeitoEDoGenero, leito.getNumero(), leito.getPaciente().getGenero() != null ? leito.getPaciente().getGenero() : "NONE"));
			terms.add(propertyToLiteralStr(FunctorLeitoEDaEspecialidade, leito.getNumero(), leito.getPaciente().getEspecialidade() != null ? leito.getPaciente().getEspecialidade() : "NONE"));
			terms.add(propertyToLiteralStr(FunctorLeitoEDaFaixaEtaria, leito.getNumero(), leito.getPaciente().getFaixa_etaria() != null ? leito.getPaciente().getFaixa_etaria() : "NONE"));
			terms.add(propertyToLiteralStr(FunctorLeitoEDoTipoEspecialidade, leito.getNumero(), leito.getPaciente().getTipo_especialidade() != null ? leito.getPaciente().getTipo_especialidade() : "NONE"));
			
			terms.addAll(translatePatientData(leito.getPaciente()));
		} else {
			terms.add(propertyToLiteralStr(FunctorLeitoEDoGenero, leito.getNumero(), leito.getGenero() != null ? leito.getGenero() : "NONE"));
			terms.add(propertyToLiteralStr(FunctorLeitoEDaEspecialidade, leito.getNumero(),leito.getEspecialidade() != null ? leito.getEspecialidade() : "NONE"));
			terms.add(propertyToLiteralStr(FunctorLeitoEDaFaixaEtaria, leito.getNumero(), leito.getFaixa_etaria() != null ? leito.getFaixa_etaria() : "NONE"));
			terms.add(propertyToLiteralStr(FunctorLeitoEDoTipoEspecialidade, leito.getNumero(), leito.getTipo_especialidade() != null ? leito.getTipo_especialidade() : "NONE"));
		}
		
		return terms;
	}

	static Collection<Term> translatePatientData(DataByPatient paciente) {
		Collection<Term> terms = new LinkedList<Term>();
		terms.add(propertyToLiteralUniq(FunctorPaciente, paciente.getCod_paciente()));
		terms.add(propertyToLiteralStr(FunctorEDaFaixaEtaria, paciente.getCod_paciente(), paciente.getFaixa_etaria() != null ? paciente.getFaixa_etaria() : "NONE"));
		terms.add(propertyToLiteralStr(FunctorEDoGenero, paciente.getCod_paciente(), paciente.getGenero() != null ? paciente.getGenero() : "NONE"));
		terms.add(propertyToLiteralStr(FunctorEDaEspecialidade, paciente.getCod_paciente(), paciente.getEspecialidade() != null ? paciente.getEspecialidade() : "NONE"));
		terms.add(propertyToLiteralStr(FunctorEDoTipoEspecialidade, paciente.getCod_paciente(), paciente.getTipo_especialidade() != null ? paciente.getTipo_especialidade() : "NONE"));
		terms.add(propertyToLiteralStr(FunctorEstaAlocadoNo, paciente.getCod_paciente(), paciente.getNumero_leito_atual() != null ? paciente.getNumero_leito_atual() : "NONE"));
		
		
		if (paciente.getCds_categoria_isolamento() != null) {
			terms.add(propertyToLiteralStrList(FunctorPossuiInfeccao, paciente.getCod_paciente(), paciente.getCds_categoria_isolamento()));
		} else {
			terms.add(propertyToLiteralStr(FunctorPossuiInfeccao, paciente.getCod_paciente(), "NONE"));
		}
		if (paciente.getTipo_solicitacao() != null) {
			terms.add(propertyToLiteralStr(FunctorPossuiPedidoAtivo, paciente.getCod_paciente(), paciente.getTipo_solicitacao()));
			terms.add(propertyToLiteralStr(FunctorPossuiConvenio, paciente.getCod_paciente(), paciente.getConvenio()));
			terms.add(propertyToLiteralStr(FunctorPossuiCobertura, paciente.getCod_paciente(), paciente.getCobertura()));
		}
		return terms;
	}


	
	static Collection<Term> getDifferentIndividuals(List<String> individuals) {
		Collection<Term> terms = new LinkedList<Term>();

		for (int i = 0; i < individuals.size()-1; i++) {
			String domain = individuals.get(i);
			for (int j = i+1; j < individuals.size(); j++) {
				String range = individuals.get(j);
				Literal l = ASSyntax.createLiteral(FunctorDifferentIndividuals);
				l.addTerm(ASSyntax.createString(domain));
				l.addTerm(ASSyntax.createString(range));
				terms.add(l);
			}
		}
		
		return terms;
	} 
	
	static Collection<Term> translateExceptions(List<NurseExceptionSql> exceptions, String patientName) {
		Collection<Term> terms = new LinkedList<Term>();
		for (NurseExceptionSql exception : exceptions) {
			Literal l = ASSyntax.createLiteral(FunctorNurseException);
			l.addTerm(ASSyntax.createString(patientName));
			l.addTerm(ASSyntax.createString(exception.getTipo()));
			terms.add(l);
		}
		return terms;
		
	}
	
	static Term propertyToLiteralStr(String functor, String str, String str2) {
		Literal l = ASSyntax.createLiteral(functor);
		l.addTerm(ASSyntax.createString(str));
		l.addTerm(ASSyntax.createString(str2));
		return l;
	}
	
	static Term propertyToLiteralDoubleStr(String functor, String str, String str2, String str3) {
		Literal l = ASSyntax.createLiteral(functor);
		l.addTerm(ASSyntax.createString(str));
		l.addTerm(ASSyntax.createString(str2));
		l.addTerm(ASSyntax.createString(str3));
		return l;
	}
	
	static Term propertyToLiteralStrBool(String functor, String str, boolean b) {
		Literal l = ASSyntax.createLiteral(functor);
		l.addTerm(ASSyntax.createString(str));
		l.addTerm(ASSyntax.createString(b));
		return l;
	}
	
	static Term propertyToLiteralStrInt(String functor, String str, int i) {
		Literal l = ASSyntax.createLiteral(functor);
		l.addTerm(ASSyntax.createString(str));
		l.addTerm(ASSyntax.createString(i));
		return l;
	}
	
	static Term propertyToLiteralAtom(String functor, String str, String str2) {
		Literal l = ASSyntax.createLiteral(functor);
		l.addTerm(ASSyntax.createString(str));
		l.addTerm(ASSyntax.createAtom(str2));
		return l;
	}

	static Term propertyToLiteralUniq(String functor, String str) {
		Literal l = ASSyntax.createLiteral(functor);
		l.addTerm(ASSyntax.createString(str));
		return l;
	}

	static Term propertyToLiteralStrList(String functor, String str, List<String> strs) {
		Literal l = ASSyntax.createLiteral(functor);
		l.addTerm(ASSyntax.createString(str));
		
		Collection<Term> valuesInTerms = new LinkedList<Term>();
		for (String element : strs) {
			valuesInTerms.add(ASSyntax.createString(element));
		}
		l.addTerm(ASSyntax.createList(valuesInTerms));
		return l;
	}
	

}
