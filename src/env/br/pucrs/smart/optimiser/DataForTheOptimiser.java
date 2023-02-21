package br.pucrs.smart.optimiser;

import java.io.BufferedReader;
//glpsol
import java.io.FileWriter;
//exceptions
import java.io.IOException;
import java.io.InputStreamReader;
//java
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import br.pucrs.smart.postgresql.models.LaudoInternacaoSql;
import br.pucrs.smart.postgresql.models.LeitoSql;

public class DataForTheOptimiser {
	//informação pro glpsol
	Map<String, Map> regras; //regras de cada tipo de quarto, e.g. regras de quartos da medicina interna
	Map<String, OpQuarto> quartos; //nome do quarto -> objeto do quarto(leitos e regras) 
	Map<String, LeitoSql> leitos; //nome do leito -> objeto do leito
	Map<String, OpPaciente> pacientesMap; //cpf -> objeto1 do paciente 
	List<OpPaciente> pacientes; // Lista de pacientes 
	List<OpCaract> caracts; //lista de todas caracteristicas possiveis (e.g genero=[masculino,feminino])
	Map<String, OpCaract> caractMap; //nome da caracteristica -> objeto da caracteristica; 
	Map<String, OpPaciente> leitoAloc; //leito -> paciente; 
	
	//	private final String GLPSol = "a:\\Programs\\glpk-4.65\\w64\\glpsol.exe"; //localizacao do glpsol
	private final String GLPSol = "/opt/bin/glpsol";
	private Process glpsol; //processo do glpsol
	BufferedReader out; //output do glpsol
	private int maxMinT = 1; //tipo de criterio de maximizacao
	private float pCuidado = (float)0.7; //peso do criterio de cuidado na maximizacao tipo 1
	
	//pacientes nao alocados
	List<OpPaciente> nAloc; 
	
	//simbolo de caracteristica que precisa ser igual
	String EQUALS;
	/**************************************************
	*	Inicializa os valores dos quartos para o GLPSol
	***************************************************/
	public void initQuartos(List<LeitoSql> dbLeitos) throws ExecutionException, InterruptedException{}	
	/**************************************************
	*	Inicializa os valores dos pacientes para o GLPSol
	***************************************************/
	public void initPacientes(List<LaudoInternacaoSql> laudos) throws ExecutionException, InterruptedException{}
	
	
	/**************************************************
	*	Print dos quartos
	***************************************************/
	public void printQuartos() {
		for(Map.Entry<String, OpQuarto> quarto : quartos.entrySet()){
			System.out.printf("Quarto %s | Regra %s\n", quarto.getKey(), quarto.getValue().regras);
			for(String leito : quarto.getValue().numLeitos){
				 System.out.printf("\tLeito %s\n", leito);
			}
		}
	}
	
	
	/**************************************************
	*	Print dos pacientes
	***************************************************/
	public void printPacientes() {
		for(OpCaract c : caracts){ 
			System.out.printf("Caract %s | %s\n", c.caract, c.opts);
		}
		System.out.println();
		for(OpPaciente p : pacientes){
			System.out.printf("Paciente %s | id %s", p.nome, p.id);
			int count = 0;
			for(OpCaract c : caracts){ 
				System.out.printf(" | " + c.caract + " " + c.opts.get(p.caracts[count]));
				count++;
			}
			System.out.println();
		}
	}
	
	
	/**************************************************
	*	Print numerico dos pacientes
	***************************************************/
	public void printPacientesDebug() {
		for(OpCaract c : caracts){ 
			System.out.printf("Caract %s | %s\n", c.caract, c.opts);
		}
		System.out.println();
		for(OpPaciente p : pacientes){
			System.out.printf("Paciente %s | id %s | gen %d | %d tipo\n", p.nome, p.id, p.caracts[0], p.caracts[1]);
		}
	}
	
	
	/**************************************************
	*	Whitelist/blacklist de pacientes/quartos e leitos
	**************************************************/
	//FIX
	public void pacienteWL(String[] cpfs){
		for(String p : pacientesMap.keySet().toArray(new String[0])){
			boolean found = false;
			for(String s : cpfs){
				if(s.equals(p)){
					found = true;
					break;
				}
			}
			if(!found){
				OpPaciente pRem = pacientesMap.remove(p);
				pacientes.remove(pRem);
				leitoAloc.values().remove(pRem);
			}			
		}
	}
	public void pacienteBL(String[] cpfs){
		for(String c : cpfs){
			if(pacientesMap.containsKey(c)){
				OpPaciente pRem = pacientesMap.remove(c);
				pacientes.remove(pRem);
				leitoAloc.values().remove(pRem);
			}			
		}
	}
	//remove quartos e leitos não especificados
	public void leitoWL(String[] quartosW, String[] leitos){
		String[] qKeys = quartos.keySet().toArray(new String[0]);
		for(String qNam : qKeys){
			boolean found = false;
			for(String s : quartosW){
				if(qNam.equals(s)){
					System.out.printf("%s = %s\n", qNam, s);
					found = true;
					break;
				}
			}
			if(!found){
				System.out.printf("%s\n", qNam);
				OpQuarto q = quartos.get(qNam);
				boolean keep = false;
				for(String l : q.numLeitos.toArray(new String[0])){
					found = false;
					for(String s : leitos){
						if(s.equals(l)){
							found = true;
							break;
						}
					}
					if(found){
						keep = true;
						continue;
					}
					q.numLeitos.remove(l);
				}
				if(!keep){
				System.out.printf("rem %s\n", qNam);
					quartos.remove(qNam);
					leitoAloc.remove(qNam);					
				}
			}
		}
	}
	//remove quartos e leitos especificados
	public void leitoBL(String[] quartosB, String[] leitos){
		String[] quartoK = quartos.keySet().toArray(new String[0]);
		for(String qR : quartosB){
			OpQuarto q = quartos.get(qR);
			if(q == null) continue;
			quartos.remove(qR);
			leitoAloc.remove(qR);
		}
		for(String lR : leitos){
			for(OpQuarto q : quartos.values()){
			boolean rem = false;
				for(int i = 0; i < q.numLeitos.size(); i++){
					String l = q.numLeitos.get(i);
					if(l.equals(lR)){
						q.numLeitos.remove(i);
						rem = true;
					}
					if(rem) break;
				}
			}
		}
	}
	public void testC(){
		System.out.println("TEST");
		for(String p : pacientesMap.keySet()) System.out.printf("%s %f %s\n",p,pacientesMap.get(p).valorCuidado,pacientesMap.get(p).nome);
		System.out.println("");
		for(String q : quartos.keySet()) System.out.printf("%s %f\n",q,quartos.get(q).valorCuidado);
		System.out.println("TEST");
	}
	
	
	/**************************************************
	*	Modelo dos quartos
	**************************************************/
	public void quartoOut(int movLim) throws IOException{
		FileWriter file = new FileWriter("modelo.dom");
		
        //set paciente
        file.write("set PACIENTE;\n\n");
		
        //parametro de caracteristicas		
        for(OpCaract c : caracts){
            file.write("param " + c.caract + "{p in PACIENTE} integer;\n");
		}
				
		String pAlocLim = ""; //pacientes ja alocados
		String pAlocMov = ""; //pacientes ja alocados
		int curPAloc = 0; //numero de pacientes ja alocados
		
        //quartos
        String nomeQuartos = ""; //garante que o paciente so esta em um quarto
        for(String quartoNam : quartos.keySet()){
            nomeQuartos += "Q" + quartoNam + "[p]" + " + ";
			OpQuarto quartoObj = quartos.get(quartoNam);
            String leitoNams = "";
            String tam = String.valueOf(quartoObj.numLeitos.size());
            
            //comentario leitos
            for(String leito : quartoObj.numLeitos) leitoNams += leito + "\t";
            file.write("\n\n/***************************************************\n* Quarto " + quartoNam + "\n* Leitos:\t" + leitoNams + "\n***************************************************/\n");
            
            //variavel quarto
            file.write("var Q" + quartoNam + "{p in PACIENTE} binary;\n");
            
            //constraint quantidade de leitos
            file.write("/* max " + tam + " leito" + (tam.equals("1")?"":"s") +" */\ns.t. Q" + quartoNam + "_max: sum {p in PACIENTE} Q" + quartoNam + "[p] <= " + tam + ";\n\n");
			
			//constraint de pacientes que ja estao alocados
			for(String leito : quartoObj.numLeitos){
				OpPaciente p = leitoAloc.get(leito);
				if(p == null) continue;
				curPAloc++;
				pAlocLim += "+ Q" + quartoNam + "['P" + p.id +"'] ";
				pAlocMov += "+ reserva['P" + p.id + "'] ";
			}
			
            //caracteristicas do quarto
			for(Map carM : quartoObj.regras){
				for(Map.Entry<String, String> c : ((Map<String,String>)carM).entrySet()){	
					String caractN = c.getKey();
					Object value = c.getValue();
					//caracteristica singular
					if(value instanceof String){
						String caractP = (String)value;
						//not
						if(caractN.charAt(0) == '!'){ 
							caractN = caractN.substring(1);
							file.write("/* " + caractN + " nao " + caractP + " */\n" + "s.t. Q" + quartoNam + "_" + caractN + "{p in PACIENTE}:\n\t(Q" + quartoNam + "[p]) - abs(" + caractN + "[p]-" + String.valueOf(caractMap.get(caractN).opts.indexOf(caractP)) + ")" + " <= 0;\n\n");
						}else{
							String div = String.valueOf(caractMap.get(caractN).opts.size());
							//caracteristicas precisam ser iguais
							 if(caractP.equals(EQUALS)){
								file.write("/* " + caractN + " igual */\n" + "s.t. Q" + quartoNam + "_" + caractN + "{p1 in PACIENTE, p2 in PACIENTE}:\n\t(Q" + quartoNam + "[p1]+Q" + quartoNam + "[p2])+" + (div.equals("1")?"":"(") + "abs(" + caractN + "[p1]-" + caractN + "[p2])" + (div.equals("1")?"":"/"+div+")") + " <= 2;\n\n");
							//caracteristica especifica
							}else{
								file.write("/* " + caractN + " " + caractP + " */\n" + "s.t. Q" + quartoNam + "_" + caractN + "{p in PACIENTE}:\n\t(2*Q" + quartoNam + "[p])+" + (div.equals("1")?"":"(") + "abs(" + caractN + "[p]-" + String.valueOf(caractMap.get(caractN).opts.indexOf(caractP)) + ")" + (div.equals("1")?"":"/"+div+")") + " <= 2;\n\n");
							}
						}
					}
				}
			}
		}
		
        //leitos reserva
        file.write("\n\n/***************************************************\n* Pacientes sem leito\n***************************************************/\n");
        file.write("var reserva{p in PACIENTE} binary;\n");
        
        //garante que o paciente so esta em um quarto
        file.write("\n/* garante que o paciente so esta em um quarto */\ns.t. pacienteQuarto{p in PACIENTE}: " + nomeQuartos + "reserva[p] == 1;\n");

        //garante que o paciente so esta em um quarto
        file.write("\n/* garante que pacientes alocados nao sao removidos */\ns.t. pac_keep: " + pAlocMov.substring(1) + " == 0;\n");
        
        //criterios de movimento de pacientes
        file.write("\n/* move no maximo " + String.valueOf(movLim) + " pacientes ja alocados */\ns.t. lim_mov:" + pAlocLim.substring(1) + ">= " + String.valueOf(curPAloc - movLim) + ";\n");
        
        file.write("\n\n/***************************************************\n* Criterios\n***************************************************/\n");
		switch(maxMinT){
			case 0:
				//maximiza o numero de pacientes alocados
				file.write("/* maximiza o numero de pacientes em leitos */\nminimize max_leitos: sum{p in PACIENTE} reserva[p];\n");
				break;
			case 1:
				//maximiza o numero de pacientes alocados
				file.write("/* maximiza o numero de pacientes em leitos com peso " + String.valueOf(pCuidado) + "*/\nmaximize max_leitos: (" + String.valueOf(pCuidado) + "*(");
				boolean first = true;
				for(OpPaciente p : pacientes){
					String aux = String.valueOf(p.valorCuidado);
					if(first){
						file.write(aux + "*(");
						first = false;
					}else{
						file.write(" + " + aux + "*(");						
					}
					aux = p.id;
					boolean qFirst = true;
					for(String q : quartos.keySet()){
						String qVal = String.valueOf(quartos.get(q).valorCuidado);
						if(!qFirst){
							file.write(" + ");
						}else{
							qFirst = false;
						}
						file.write(qVal + "*Q" + q + "['P" + aux + "']");
					}
					file.write(")");
				}
				file.write(")) - (" + String.valueOf(1-pCuidado) + "*(sum{p in PACIENTE} reserva[p]));\n");
				break;
		}
			
        //output
        file.write("\n\n\n/***************************************************\n* Output\n***************************************************/\nsolve;\n");
        for(String quarto : quartos.keySet()) file.write("\ndisplay Q" + quarto + ";");
        file.write("\ndisplay reserva;");
        file.write("\n\nend;");
		
		file.close();
	}
	
	
	/**************************************************
	*	Arquivo de dados
	**************************************************/
	public void pacienteOut() throws IOException{
		FileWriter file = new FileWriter("data.dom");
        
        //lista de pacientes
        file.write("set PACIENTE :=");
        for(OpPaciente paciente : pacientes) file.write(" P" + paciente.id);
            
        //parametros 
        file.write(";\n\nparam:");
        for(OpCaract c : caracts)	file.write("\t" + c.caract);
        file.write(" :=");
        for(OpPaciente paciente : pacientes){
            file.write("\n  P" + paciente.id);
            for(int i = 0; i < paciente.caracts.length; i++)
                file.write("\t" + String.valueOf(paciente.caracts[i]));
		}
        file.write(";\n\nend;\n ");
		
		file.close();
	}
	
	
	/**************************************************
	*	glpsol
	**************************************************/
	public void runAloc(int timeout) throws IOException, InterruptedException{		
		if(glpsol != null) abort();
		//roda o processo
		glpsol = Runtime.getRuntime().exec(GLPSol + " -m modelo.dom -d data.dom --tmlim " + String.valueOf(timeout));
		out = new BufferedReader(new InputStreamReader(glpsol.getInputStream()));
	}
	
		
	/**************************************************
	*	aborta o glpsol
	**************************************************/
	public void abort() throws IOException, InterruptedException{	
		if(glpsol != null){
			glpsol.destroyForcibly();
			glpsol = null;
		}
	}
	
	
	
	/**************************************************
	*	processa o resultado do glpsol
	**************************************************/
	public boolean procAloc() throws IOException{					
		//procura o output dos quartos
		String line = out.readLine();
		boolean optimal = true;
		while(!line.startsWith("Display statement at line")){
			if(line.contains("already defined")){
				throw new RuntimeException("CPF " + line.substring(line.indexOf('[')+2,line.indexOf(']')) + " duplicado.");
			}
			if(line.contains("duplicate tuple")){
				throw new RuntimeException("CPF " + line.substring(30,line.length()-8) + " duplicado.");
			}
			if(line.contains("PROBLEM HAS NO PRIMAL FEASIBLE SOLUTION")){
				throw new RuntimeException("PROBLEM HAS NO PRIMAL FEASIBLE SOLUTION");
			}
			if(line == null) return false;			
			if(line.contains("TIME LIMIT EXCEEDED")){
				optimal = false;
			}
			line = out.readLine();
		}
//		System.out.println(optimal? "Optimal result" : "Non optimal result");
		//aloca o paciente para leito dentro do quarto
		line = out.readLine();
		while(!line.startsWith("reserva")){
			String quarto = line.substring(1,line.indexOf('['));
			while(!line.startsWith("Display statement at line")){
				line = line.trim();
				if(line.charAt(line.length()-1)=='1'){
					OpPaciente paciente = pacientesMap.get(line.substring(line.indexOf('[')+2,line.indexOf(']')));
					//paciente nao alocado
					if(!leitoAloc.containsValue(paciente)){
						for(String leito : quartos.get(quarto).numLeitos){
							//leito disponivel
							if(!leitoAloc.containsKey(leito)){
								leitoAloc.put(leito, paciente);
								break;
							}
						}
					}
				}
				line = out.readLine();
			}
			line = out.readLine();
		}
		
		//pacientes nao alocados
		nAloc = new ArrayList<>();
		while(line.startsWith("reserva")){
			line = line.trim();
			if(line.charAt(line.length()-1)=='1') nAloc.add(pacientesMap.get(line.substring(line.indexOf('[')+2,line.indexOf(']'))));
			line = out.readLine();
		}	
		return true;
	}
	
	
	
	/**************************************************
	*	glpsol print
	**************************************************/
	public void printAloc() throws IOException{	
		for(Map.Entry<String, OpQuarto> quarto : quartos.entrySet()){
			System.out.println(quarto.getKey());
			for(String leito : quarto.getValue().numLeitos){
				System.out.printf("\tLeito %s", leito);
				OpPaciente p = leitoAloc.get(leito);
				//leito nao alocado
				if(p == null){
					System.out.println(" : ---");
				}else{
					System.out.printf(" : %s\n", p.nome);
				}
			}
		}
	}	
	//nao alocados
	public void printNAloc() throws IOException{	
		System.out.println("Pacientes nao alocados------------------------------");
		for(OpPaciente p : nAloc) System.out.println("\t"+p.nome);
	}
	
	
	
	/**************************************************
	*	pddl out
	**************************************************/
	public void pddl() throws IOException{	
		/*
		*domain
		*/
		FileWriter file = new FileWriter("domain.pddl");
        file.write("(define (domain hospital)\n");
		
        file.write("\t(:requirements :strips :equality :typing)\n");
		//types
		file.write("\n\t(:types\n");
		file.write("\t\tpaciente\n");
		file.write("\t\tleito\n");
		for(OpCaract c : caracts) file.write("\t\t"+c.caract.replace(' ', '-')+"\n");
		file.write("\t)\n");
		//predicates
        file.write("\n\t(:predicates \n");
        file.write("\t\t(in ?paciente - paciente ?leito - leito)\n");
        file.write("\t\t(ocupado ?leito - leito)\n");
        file.write("\t\t(alocado ?paciente - paciente)");
		for(OpCaract c : caracts){
			for(String opt : c.opts) file.write("\n\t\t(" + c.caract.replace(' ', '-') + "-" + opt.replace(' ', '-') +" ?paciente - paciente)");
		}
		file.write("\n\t)\n");
		
		//actions
		for(Map.Entry<String, Map> regra : regras.entrySet()){
			file.write("\n\t(:action aloc-"+regra.getKey().replace(' ', '-')+ " \n");
			//parameters
			file.write("\t\t:parameters (?paciente - paciente ?leito - leito");
			for(Map.Entry<String, String> r : ((Map<String,String>)regra.getValue()).entrySet()){
				if(r.getValue().equals(EQUALS)){
					file.write(" ?" + r.getKey().replace(' ', '-') + "-paciente - " + r.getKey().replace(' ', '-') + " ?" + r.getKey().replace(' ', '-') + "-leito" + " - " + r.getKey().replace(' ', '-'));
				}
			}
			file.write(")\n");
			
			//preconditions
			file.write("\t\t:precondition (and (not (alocado ?paciente))\n\t\t\t(not (ocupado ?leito))");			
			for(Map.Entry<String, String> r : ((Map<String,String>)regra.getValue()).entrySet()){
				if(r.getValue().equals(EQUALS)){
					file.write("\n\t\t\t(= ?" + r.getKey().replace(' ', '-') + "-paciente ?" + r.getKey().replace(' ', '-') + "-leito)");
				}else if(r.getKey().charAt(0) == '!'){ 
					file.write("\n\t\t\t(not (" + r.getKey().substring(1).replace(' ', '-') + "-" + r.getValue().replace(' ', '-') + " ?paciente))");
				}else{ 					
					file.write("\n\t\t\t(" + r.getKey().replace(' ', '-') + "-" + r.getValue().replace(' ', '-') + " ?paciente)");
				}
			}
			file.write(")\n");
			
			file.write("\t\t:effect (and (in ?paciente ?leito)\n\t\t\t(ocupado ?leito)\n\t\t\t(alocado ?paciente))\n\t)\n");
		}
		file.write(")");
		file.close();
		
		
		/*
		*problem
		*/
		file = new FileWriter("problem.pddl");
        file.write("(define (problem aloc)\n");
        file.write("\t(:domain hospital)\n");
		
        file.write("\n\t(:objects \n\t\t;pacientes\n\t\t");
		for(String paciente : pacientesMap.keySet()) file.write(paciente + " - paciente  ");
		file.write("\n\t\t;leitos\n\t\t");
		for(OpQuarto quarto : quartos.values()) for(String leito : quarto.numLeitos) file.write(leito + " - leito  ");
		file.write("\n\t\t;Caracteristicas");
		for(OpCaract c : caracts){
			file.write("\n\t\t");
			for(String opt : c.opts) file.write(c.caract.replace(' ', '-') + "--" + opt.replace(' ', '-') + " - " + c.caract.replace(' ', '-') + "  ");
		}
		file.write("\n\t)\n");
		
		//init
        file.write("\n\t(:init ");
		for(OpPaciente p : pacientes){
			for(int i = 0; i < p.caracts.length; i++) file.write("\n\t\t(" + caracts.get(i).caract.replace(' ', '-') + "-" + (caracts.get(i).opts.get(p.caracts[i])).replace(' ', '-') + " " + p.id + ")");
		}
		file.write("\n\t)\n");
		
		file.write("\n\t(:goal (and");
		for(Map.Entry<String, OpPaciente> aloc : leitoAloc.entrySet()) file.write("\n\t\t\t(in " + aloc.getValue().id + " " + aloc.getKey() + ")");
		for(OpQuarto quarto : quartos.values()) for(String leito : quarto.numLeitos) if(!leitoAloc.containsKey(leito)) file.write("\n\t\t\t(not(ocupado " + leito + "))");
		file.write("\n\t\t)\n\t)\n\n)");
		file.close();
		
		
		/*
		*plan
		*/
		file = new FileWriter("plan.pddl");
		for(OpQuarto quarto : quartos.values()){ 
			String[] eq = null;
			int i = 0;
			for(Map.Entry<String, String> r : ((Map<String,String>)regras.get(quarto.regrasID)).entrySet()){
				if(r.getValue().equals(EQUALS)){
					i++;
				}
			}
			eq = new String[i];
			eq[0] = null;
			for(String leito : quarto.numLeitos){
				
				OpPaciente p = leitoAloc.get(leito);
				if(p == null) continue;
				
				file.write("(aloc-"+quarto.regrasID.replace(' ', '-') + " " + p.id + " " + leito.replace(' ', '-'));
				if(eq[0] == null){
					i = 0;
					for(Map.Entry<String, String> r : ((Map<String,String>)regras.get(quarto.regrasID)).entrySet()){
						if(r.getValue().equals(EQUALS)){
							OpCaract c = caractMap.get(r.getKey());
							file.write(" " + c.caract+"--"+c.opts.get(p.caracts[c.id]).replace(' ', '-') + " " + c.caract+"--"+c.opts.get(p.caracts[c.id]).replace(' ', '-'));
							eq[i] = c.opts.get(p.caracts[c.id]).replace(' ', '-');
							i++;
						}
					}
				}else{			
					i = 0;
					for(Map.Entry<String, String> r : ((Map<String,String>)regras.get(quarto.regrasID)).entrySet()){
						if(r.getValue().equals(EQUALS)){
							OpCaract c = caractMap.get(r.getKey());
							file.write(" " + c.caract+"--"+c.opts.get(p.caracts[c.id]).replace(' ', '-'));
								file.write(" " + c.caract+"--"+eq[i]);
							i++;
						}
					}
				}
				
				file.write(")\n");
				
			}			
		}
		file.close();
	}	
	
	
	/**************************************************
	*	Create optimizer
	**************************************************/
	public OptimiserResult optInit(){		
		List<Allocation> pAloc = new ArrayList<Allocation>(); //pAloc, todos os pacientes alocados em um quarto
		List<LaudoInternacaoSql> laudosData = new ArrayList<LaudoInternacaoSql>();
		if(leitoAloc != null){ //Se existe algum paciente alocado
			for(Map.Entry<String, OpPaciente> mE : leitoAloc.entrySet()){
				//sem paciente
				if(mE.getValue() == null) continue; 
				 
				laudosData.add(((OpPaciente)mE.getValue()).laudo);
				//paciente nao movido
				if(mE.getKey().equals(((OpPaciente)mE.getValue()).leitoP)) continue;

				Allocation aux = new Allocation();
				aux.setIdPaciente(((OpPaciente)mE.getValue()).id);
				aux.setLeito((String)mE.getKey());
				//%PLACEHOLDER%
				aux.setLeitoData(null);
				aux.setLaudo(null);
				pAloc.add(aux);
				LeitoSql lt = leitos.get((String)mE.getKey());
				((OpPaciente)mE.getValue()).laudo.setLeitoNum(lt.getNumero());
			}
		}
		
		List<String> pNAloc = new ArrayList<String>();
		if(nAloc != null){
			for(OpPaciente p : nAloc){
				laudosData.add(p.laudo);
				pNAloc.add(p.id);
			}
		}
		
		OptimiserResult out = new OptimiserResult();
		out.setSugestedAllocation(pAloc);
		out.setLaudosData(laudosData);
		out.setNotAllocated(pNAloc);
		out.setAllAllocated(pNAloc.size() == 0);
		out.setAlocar(false);
		out.setConcluido(false);	
		return out;
	}
}



/**************************************************
*	Quarto
***************************************************/
class OpQuarto {
	public List<String> numLeitos;
	public List<LeitoSql> objLeitos;
	public List<Map> regras;
	public String regrasID;
	public float valorCuidado;
	
	public OpQuarto(){
		numLeitos = new ArrayList<String>();
		objLeitos = new ArrayList<LeitoSql>();
	}
}


/**************************************************
*	Caracteristicas
***************************************************/
class OpCaract {
	public String caract;
	public List<String> opts; 
	public int id;
	
	public OpCaract(String name){
		caract = name;
	}
}


/**************************************************
*	Paciente
***************************************************/
class OpPaciente {
	public String nome;
	public String id;
	public float valorCuidado;
	public String leitoP;
	int[] caracts;
	LaudoInternacaoSql laudo;
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\n");
		if (id != null) {
			sb.append(" id: ");
			sb.append(id);
		}
		if (nome != null) {
			sb.append(", ").append("\n");
			sb.append(" nome: ");
			sb.append(nome);
		}
		if (leitoP != null) {
			sb.append(", ").append("\n");
			sb.append(" leitoP: ");
			sb.append(leitoP);
		}
		sb.append(", ").append("\n");
		sb.append("caracts: [");
		for (int i : caracts) {
			sb.append(i).append(",");
		}
		sb.append("]");
		sb.append(", ").append("\n");
		sb.append(" valorCuidado: ");
		sb.append(valorCuidado);
		sb.append(", ").append("\n");
		sb.append("laudo: ");
		sb.append("  ").append(laudo.toString());
		sb.append("\n").append("}");
		return sb.toString();
	}
}