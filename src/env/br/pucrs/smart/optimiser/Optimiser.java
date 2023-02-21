package br.pucrs.smart.optimiser;

//exceptions
import java.io.IOException;
//java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import br.pucrs.smart.postgresql.Crud.CrudCaracteristicas;
import br.pucrs.smart.postgresql.Crud.CrudExcecoes;
import br.pucrs.smart.postgresql.Crud.CrudInternado;
import br.pucrs.smart.postgresql.Crud.CrudLeito;
import br.pucrs.smart.postgresql.Crud.CrudNurseException;
import br.pucrs.smart.postgresql.Crud.CrudRegras;
import br.pucrs.smart.postgresql.models.ExcecaoSql;
import br.pucrs.smart.postgresql.models.LaudoInternacaoSql;
import br.pucrs.smart.postgresql.models.LeitoSql;
import br.pucrs.smart.postgresql.models.NurseExceptionSql;

public class Optimiser extends DataForTheOptimiser {

	// simbolo de caracteristica nao definida
	private final String NONE = "_NONE";

	// Quartos com excecoes
	Map<String, OpPaciente> leitoAlocEx; // leito -> paciente;
	Map<String, OpQuarto> quartosEx; // nome do quarto -> objeto do quarto(leitos e regras)
	List<String> keysRegras;

	// %PLACEHOLDER%
	public Optimiser() throws IOException {
		EQUALS = "%igual%";

		try {
			initRules();
			initCharacteristics();
		} catch (ExecutionException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error downloading rules and/or characteristics");
		}
	}

	public void initRules(List<NurseExceptionSql> exception, String specialty)
			throws ExecutionException, InterruptedException {
		// inicializa as regras
		regras = new HashMap<>();
		keysRegras = new ArrayList<String>();

		Map<String, Map<String, String>> regrasInMaps = CrudRegras.getRegrasInMaps();
		regrasInMaps.forEach((key, value) -> {
			Map<String, String> m = new HashMap<>();
			m.putAll(value);
			if (key.equals(specialty)) {
				value.forEach((k, v) -> {
					for (NurseExceptionSql e : exception) {
						if (k.equals(e.getTipo())) {
							m.remove(k, v);
						}
					}
					
				});
			}
			if (!m.isEmpty()) regras.put(key, m);
		});
		keysRegras.add("especialidade");
	}

	public void initRules() throws ExecutionException, InterruptedException {
		// inicializa as regras
		regras = new HashMap<>();
		keysRegras = new ArrayList<String>();

		Map<String, Map<String, String>> regrasInMaps = CrudRegras.getRegrasInMaps();

		regrasInMaps.forEach((key, value) -> {
			regras.put(key, value);
		});

		keysRegras.add("especialidade");
	}

	public void initCharacteristics() throws InterruptedException, ExecutionException {
		// inicializa as caracteristicas possiveis
		caracts = new ArrayList<>();
		caractMap = new HashMap<>();

		Map<String, List<String>> caracteristicas = CrudCaracteristicas.getCaracteristicasInArrays();

		caracteristicas.forEach((key, values) -> {
			OpCaract c = new OpCaract(key);
			c.opts = values;
			caracts.add(c);
			caractMap.put(key, c);
		});
		for (int i = 0; i < caracts.size(); i++) {
			caracts.get(i).id = i;
		}
	}

	/**************************************************
	 * Inicializa os valores dos quartos para o GLPSol
	 ***************************************************/
	@Override
	public void initQuartos(List<LeitoSql> dbLeitos) throws ExecutionException, InterruptedException {

		// inicializa os quartos e leitos
		leitos = new HashMap<>();
		quartos = new HashMap<>();
		for (LeitoSql leito : dbLeitos) {
			String quartoNum = leito.getQuarto();
			String leitoNum = leito.getNumero();
			leitos.put(leitoNum, leito);

			float valorCuidado = 1000;//leito.getDistanciaEnfermaria() != null
//					? 1000 - Float.parseFloat(String.valueOf(leito.getDistanciaEnfermaria()))
//					: 1000;

			OpQuarto quartoObj;
			// adiciona leito ao quarto existente
			if (quartos.containsKey(quartoNum)) {
				quartoObj = quartos.get(quartoNum);
				// cria um quarto novo a partir do leito
			} else {
				quartoObj = new OpQuarto();
				quartoObj.regras = new ArrayList<>();
				quartoObj.valorCuidado = valorCuidado;
				for (String parameter : keysRegras) {
					String key = leito.get(parameter);
					Map m = (Map) regras.get(key);
					if (m != null) {
						quartoObj.regras.add(m);
						quartoObj.regrasID = key;
						break;
					}
				}
				quartos.put(quartoNum, quartoObj); // <numero do quarto, objeto quarto com leitos dentro>
			}
			quartoObj.numLeitos.add(leitoNum);
		}
	}

	/**************************************************
	 * Inicializa os valores dos pacientes para o GLPSol
	 ***************************************************/
	@Override
	public void initPacientes(List<LaudoInternacaoSql> laudos) throws ExecutionException, InterruptedException {
		// inicializa os pacientes
		pacientes = new ArrayList<>();
		pacientesMap = new HashMap<>();
		leitoAloc = new HashMap<String, OpPaciente>();
		for (LaudoInternacaoSql laudo : laudos) {
			OpPaciente p = new OpPaciente();
			p.nome = laudo.getNomePaciente();
			p.id = laudo.getId().toString();
			p.valorCuidado = 0;
			p.leitoP = "";
			p.laudo = laudo;

			if (laudo.isInternado()) {
				p.leitoP = laudo.getLeitoNum();
				leitoAloc.put(laudo.getLeitoNum(), p);
			}

			/*
			 * valor positivo = tenta colocar mais perto dos quartos de enfermeira 0 =
			 * ignora valor negativo = coloca mais longe Vermelho - Emergente +3 Laranja -
			 * Muito Urgente +2 Amarelo - Urgente +1 Verde - Pouco Urgente 0 Azul - Não
			 * Urgente 0
			 */
			if (laudo.getClassificacaoDeRisco() != null) {
				switch (laudo.getClassificacaoDeRisco()) {
				case "Emergente":
					p.valorCuidado = Float.parseFloat(String.valueOf("3"));
					break;
				case "Muito Urgente":
					p.valorCuidado = Float.parseFloat(String.valueOf("2"));
					break;
				case "Urgente":
					p.valorCuidado = Float.parseFloat(String.valueOf("1"));
					break;
				case "Pouco Urgente":
					p.valorCuidado = Float.parseFloat(String.valueOf("0"));
					break;
				case "Não Urgente":
					p.valorCuidado = Float.parseFloat(String.valueOf("0"));
					break;
				default:
					break;
				}
			} else {
				p.valorCuidado = 0;
			}

			// caracteristicas
			p.caracts = new int[caracts.size()];
			for (int i = 0; i < caracts.size(); i++) {
				OpCaract curCar = caracts.get(i);
				p.caracts[i] = curCar.opts.indexOf(laudo.get(curCar.caract));
				// caracteristica nao existe no paciente
				if (p.caracts[i] == -1) {
					p.caracts[i] = curCar.opts.indexOf(NONE);
				}
			}
			// adiciona ao array e mapa
			pacientes.add(p);
			pacientesMap.put(p.id, p);
		}

	}

	/**************************************************
	 * Print das alocacoes
	 ***************************************************/
	@Override
	public void printAloc() throws IOException {
		// quartos alocados
		super.printAloc();

		// quartos com excecoes
		for (Map.Entry<String, OpQuarto> quarto : quartosEx.entrySet()) {
			System.out.println(quarto.getKey() + " (Exc)");
			for (String leito : quarto.getValue().numLeitos) {
				System.out.printf("\tLeito %s", leito);
				OpPaciente p = leitoAlocEx.get(leito);
				// leito nao alocado
				if (p == null) {
					System.out.println(" : ---");
				} else {
					System.out.printf(" : %s\n", p.nome);
				}
			}
		}

		// pacientes nao alocados
		if (nAloc.size() > 0)
			printNAloc();
	}

	/**************************************************
	 * Remove quartos com execoes
	 ***************************************************/
	public void initExcecoes() throws ExecutionException, InterruptedException {
		quartosEx = new HashMap();
		leitoAlocEx = new HashMap();

		List<ExcecaoSql> excecoes = CrudExcecoes.getExcecoes();

		for (ExcecaoSql excecao : excecoes) {

			if (quartos.get(excecao.getQuarto()) == null)
				continue;
			for (String leito : quartos.get(excecao.getQuarto()).numLeitos) {
				OpPaciente p = leitoAloc.remove(leito);
				if (p != null) {
					pacientes.remove(p);
					leitoAlocEx.put(leito, pacientesMap.remove(p.id));
				}
			}
			quartosEx.put(excecao.getQuarto(), quartos.remove(excecao.getQuarto()));
		}

	}

	/**************************************************
	 * Inicializa todos os valores do banco de dados
	 ***************************************************/
	// public void init(List<String> names) throws ExecutionException, InterruptedException {

	// 	List<LaudoInternacaoSql> laudos = CrudInternado.getLaudosAtivos();
	// 	List<LaudoInternacaoSql> laudosToUse = new ArrayList<LaudoInternacaoSql>();
	// 	for (LaudoInternacaoSql laudo : laudos) {
	// 		if (laudo.isInternado()) {
	// 			laudosToUse.add(laudo);
	// 		} else {
	// 			for (String name : names) {
	// 				if (laudo.getNomePaciente().equals(name)) {
	// 					laudosToUse.add(laudo);
	// 				}
	// 			}
	// 		}
	// 	}
	// 	if (names.size() == 1) {
	// 		for (LaudoInternacaoSql l : laudosToUse) {
	// 			if (!l.isInternado()) {
	// 				List<NurseExceptionSql> exception = CrudNurseException
	// 						.getNurseExceptionsByLaudoInternacaoId(l.getId());
	// 				if (!exception.isEmpty()) {
	// 					initRules(exception, l.getEspecialidade());
	// 				}

	// 			}
	// 		}
	// 	}

	// 	initQuartos(CrudLeito.getLeitos());
	// 	initPacientes(laudosToUse);
	// 	initExcecoes();
	// }

	// public void init(List<String> names, List<String> beds) throws ExecutionException, InterruptedException {

	// 	List<LaudoInternacaoSql> laudos = CrudInternado.getLaudosAtivos();
	// 	List<LaudoInternacaoSql> laudosToUse = new ArrayList<LaudoInternacaoSql>();
	// 	for (LaudoInternacaoSql laudo : laudos) {
	// 		if (laudo.isInternado()) {
	// 			laudosToUse.add(laudo);
	// 		} else {
	// 			for (String name : names) {
	// 				if (laudo.getNomePaciente().equals(name)) {
	// 					laudosToUse.add(laudo);
	// 				}
	// 			}
	// 		}
	// 	}

	// 	List<LeitoSql> leitos = CrudLeito.getLeitos();
	// 	List<LeitoSql> leitosToUse = new ArrayList<LeitoSql>();
	// 	leitosToUse.addAll(leitos);
	// 	for (LeitoSql leito : leitos) {
	// 		for (String num : beds) {
	// 			if (removeSpaces(num).equals(leito.getNumero())) {
	// 				leitosToUse.remove(leito);
	// 			}
	// 		}
	// 	}
		
	// 	if (names.size() == 1) {
	// 		for (LaudoInternacaoSql l : laudosToUse) {
	// 			if (!l.isInternado()) {
	// 				List<NurseExceptionSql> exception = CrudNurseException
	// 						.getNurseExceptionsByLaudoInternacaoId(l.getId());
	// 				if (!exception.isEmpty()) {
	// 					initRules(exception, l.getEspecialidade());
	// 				}

	// 			}
	// 		}
	// 	}

	// 	initQuartos(leitosToUse);
	// 	initPacientes(laudosToUse);
	// 	initExcecoes();
	// }

	// public void init() throws ExecutionException, InterruptedException {
	// 	initQuartos(CrudLeito.getLeitos());
	// 	initPacientes(CrudInternado.getLaudosAtivos());
	// 	initExcecoes();
	// }

	public OptimiserResult getOptimisationResult(Integer patientsToMove) throws ExecutionException, InterruptedException, IOException {
		// init();
		quartoOut(patientsToMove);
		pacienteOut();
		runAloc(10); // 10 segundos max
		procAloc();
		OptimiserResult result = optInit();
		return result;
	}

	public OptimiserResult getSuggestionByPatient(List<String> names)
			throws ExecutionException, InterruptedException, IOException {
		// init(names);
		quartoOut(0);
		pacienteOut();
		runAloc(10); // 10 segundos max
		procAloc();
		OptimiserResult result = optInit();
		return result;
	}

	public OptimiserResult getSuggestionByPatient(List<String> names, List<String> beds)
			throws ExecutionException, InterruptedException, IOException {
		// init(names, beds);
		quartoOut(0);
		pacienteOut();
		runAloc(10); // 10 segundos max
		procAloc();
		OptimiserResult result = optInit();
		System.out.println("*********** Result");
		System.out.println(result);
		return result;
	}

	String removeSpaces(String phrase) {
		return phrase.replaceAll(" ", "");
	}

}