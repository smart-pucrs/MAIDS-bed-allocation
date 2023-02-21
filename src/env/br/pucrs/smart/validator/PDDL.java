package br.pucrs.smart.validator;

//java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.File;

public class PDDL{	
	
	/****************************************************
	* 			DOMAIN
	****************************************************/
	public String domain;			//domain name
	private List<String> reqs;		//pddl requirements 
	private boolean[] rFlags;		//pddl requirement flags
	public List<String> types;		//types
	public List<Integer> subTypes;	//subtypes -> types 
	private List<Pred> preds;		//predicate name -> predicate object map
	private Map<String, Act> acts;	//action name -> action object map
	
	//initializes domain with name 
	public PDDL(String domain){
		this.domain = domain;
		reqs = new ArrayList<String>();
		types = new ArrayList<String>();
		subTypes = new ArrayList<Integer>();
		acts = new HashMap<String, Act>();
		rFlags = new boolean[2];	//equality, typed
	}	
	public void addAction(Act act){
		acts.put(act.name, act);
	}	
	public void addPredicates(List<Pred> preds){
		this.preds = preds;
	}	
	public void addReq(String req){
		reqs.add(req);
		if(req.equals(":equality")) rFlags[0] = true;
		if(req.equals(":typing")) rFlags[1] = true;
	}	
	public void addType(String type){
		types.add(type);
	}
	public void addType(String[] sTypes, String type){
		if(!types.contains(type)){
			types.add(type);
			subTypes.add(-1);
		}
		for(String t : sTypes){
			types.add(t);
			subTypes.add(types.indexOf(type));
		}
	}
	
	public int getTypeId(String type){
		return types.indexOf(type);
	}
	
		
	/****************************************************
	* 			PROBLEM
	****************************************************/
	public String problem;			//problem name
	private List<String[]> iState; 	//initial state
	public List<String[]> state; 	//current state
	private List<String[]> posGoals;//positive goals
	private List<String[]> negGoals;//negative goals
	private List<String> objects;	//objects
	private int[] objectTs;			//object types
	
	public void problem(String problem){
		this.problem = problem;
		objects = new ArrayList<String>();
		posGoals = new ArrayList<String[]>();
		negGoals = new ArrayList<String[]>();
		plan = new ArrayList<String[]>();
		state = new ArrayList<String[]>();
		iState = new ArrayList<String[]>();
	}
	public void iniState(List<String[]> state){
		this.state = state;
		
		this.iState = new ArrayList<String[]>();
		for(String[] pred : state){
			String[] aux = new String[pred.length];
			for(int i = 0; i < pred.length; i++) aux[i] = pred[i];
			iState.add(aux);
		}
	}
	public void addObjs(List<String> objs){
		objects = objs;
	}
	public void addObjs(List<String> objs, List<String> type){
		objects = objs;
		if(rFlags[1]){
			objectTs = new int[type.size()];
			for(int i = 0; i < type.size(); i++){
				objectTs[i] = types.indexOf(type.get(i));
			}
		}
		
	}
	public void pDomain(String domain){
		return;
	}
	public void addPosGoal(String[] goal){
		posGoals.add(goal);
	}
	public void addNegGoal(String[] goal){
		negGoals.add(goal);
	}
		
	public void resetState(){
		state = new ArrayList<String[]>();
		for(String[] pred : iState) state.add(pred);
	}		
	
	public boolean goalAchieved(){		
		boolean found = false;
		for(String[] goal : posGoals){
			found = false;
			for(String[] p : state){
				if(goal.length == p.length){
					for(int i = 0; i < goal.length; i++){
						found = true;
						if(!goal[i].equals(p[i])){
							found = false;
							break;
						}
					}
				}
				if(found) break;
			}
			if(!found) return false; 
		}
		for(String[] goal : negGoals){
			for(String[] p : state){
				if(goal.length == p.length){
					for(int i = 0; i < goal.length; i++){
						found = true;
						if(!goal[i].equals(p[i])){
							found = false;
							break;
						}
					}
					if(found) return false;
				}
			}
		}
		return true;
	}
	
	
	/****************************************************
	* 			PLAN
	****************************************************/
	public static String template = "src/resources/template"; //template file
	public static String domainFile;
	public static String problemFile;
	public static String planFile;
	
	List<String[]> plan;	//plan stack
	public void addPAct(String[] act){
		plan.add(act);
	}
	
	public boolean hasPlan(){
		return (plan.size()>0);
	}
	
	//fixes case
	public void fixPlanCase(){
		for(int i = 0; i < plan.size(); i++){
			if(acts.get(plan.get(i)[0]) == null){
				for(Act act : acts.values()){
					if(act.name.equalsIgnoreCase(plan.get(i)[0])){
						plan.get(i)[0] = act.name;
						break;
					}
				}
			}
			for(int j = 1; j < plan.get(i).length; j++){
				for(String obj : objects){
					if(plan.get(i)[j].equals(obj)) continue;
					if(plan.get(i)[j].equalsIgnoreCase(obj)){
						plan.get(i)[j] = obj;
					}
				}
			}
		}
	}
	
	
	//true = return null
	//invalid parameters = return Object[0]
	//false = return missing req/ present neg req 
	public Object[] tryAct(int action, boolean print){
		String[] next = plan.get(action);
		Act act = acts.get(next[0]);
		if(print){
			System.out.print("Action ( ");
			for(String s : next) System.out.print(s + " ");
			System.out.println(" )");
		}
		String[] pars =  new String[next.length-1];
		int[] parTs =  new int[next.length-1];		
		for(int i = 0; i < pars.length; i++) pars[i] = next[i+1];
		if(rFlags[1]){
			for(int i = 0; i < pars.length; i++) parTs[i] = objectTs[objects.indexOf(pars[i])];
		}
		if(!act.applicable(state, pars, parTs, subTypes, rFlags)){
			Object[] error = new Object[2];
			error[0] = next;
			error[1] = act.reason(state, pars, parTs, subTypes, rFlags);
			return error;					
		}else{
			state = act.apply(state, pars);
		}
		return null;
	}
	
	public Object[] tryPlan(boolean print){
		Object[] resp = null;
		//TEMP
		for(int pLen = 0; pLen < plan.size(); pLen++){
			resp = tryAct(pLen, print);
			if(resp != null) return resp;
		}
		return resp;
	}
	
	public List<Object[]> tryPlanForce(boolean print){
		List<Object[]> resp = new ArrayList<Object[]>();
		for(int pLen = 0; pLen < plan.size(); pLen++){
			Object[] res = tryAct(pLen, print);
			if(res != null){
				resp.add(res);
			}
		}
		return resp;
	}	
	
	public void valOut(String outN){
		try{
			Scanner in = new Scanner(new File(template));
			FileWriter out = new FileWriter(outN);
			while(in.hasNext()){
				String line = in.nextLine();
				if(line.contains("!%") && line.contains("%!")){
					line = lineRep(line);
					//plan 
					if(line.contains("%!plan!%")){
						int count = 1;
						for(String[] p : plan){
							String aux = "(\\textbf{" + p[0] + "}";
							for(int i = 1; i < p.length; i++) aux += " " + p[i];
							aux += ")";
							out.write(line.replace("%!planNum!%", String.valueOf(count)).replace("%!plan!%", aux) + "\n");
							count++;
						}
						continue;
					}
					//validation
					else if(line.contains("%!val!%")){
						int count = 1;
						//action line
						String[] val = line.split("%!val!%", 2);
						//prefix
						line = in.nextLine();
						if(line.contains("!%") && line.contains("%!")) line = lineRep(line);
						String pre = "";
						while(!line.contains("%!valP!%")){
							pre += line + "\n";
							line = in.nextLine();
							if(line.contains("!%") && line.contains("%!")) line = lineRep(line);
						}
						//effects line
						String[] valP = line.split("%!valP!%", 2);						
						//suffix
						line = in.nextLine();
						if(line.contains("!%") && line.contains("%!")) line = lineRep(line);
						String suf = "";
						while(!line.contains("%!endVal!%")){
							suf += line + "\n";
							line = in.nextLine();
							if(line.contains("!%") && line.contains("%!")) line = lineRep(line);
						}
						for(String[] p : plan){
							String aux = "(\\textbf{" + p[0] + "}";
							for(int i = 1; i < p.length; i++) aux += " " + p[i];
							aux += ")";
							out.write(val[0].replace("%!valNum!%", String.valueOf(count)) + aux + val[1].replace("%!valNum!%", String.valueOf(count)) + "\n");
							out.write(pre);
							Act act = acts.get(p[0]);
							String[] pars =  new String[p.length-1];
							int[] parTs =  new int[p.length-1];		
							for(int i = 0; i < pars.length; i++) pars[i] = p[i+1];
							if(rFlags[1]){
								for(int i = 0; i < pars.length; i++) parTs[i] = objectTs[objects.indexOf(pars[i])];
							}
							if(!act.applicable(state, pars, parTs, subTypes, rFlags)){
								Object[] error = act.reason(state, pars, parTs, subTypes, rFlags);		
								if(error.length == 0){
									out.write(valP[0] + "Invalid parameters" + valP[1] + "\n" + suf + "\n");
								}else{
									List<String[]> l = (List<String[]>)error[0];
									if(l.size() > 0){
										out.write(valP[0] + "Positive predicates missing" + valP[1] + "\n");
										for(String[] str : l){
											aux = "(\\textit{" + str[0] + "}";
											for(int i = 1; i < str.length; i++) aux += " " + str[i];
											aux += ")";
											out.write(valP[0] + "[+] " + aux + valP[1] + "\n");
										}
									}
									l = (List<String[]>)error[1];
									if(l.size() > 0){
										out.write(valP[0] + "Negative predicates present" + valP[1] + "\n");
										for(String[] str : l){
											aux = "(\\textit{" + str[0] + "}";
											for(int i = 1; i < str.length; i++) aux += " " + str[i];
											aux += ")";
											out.write(valP[0] + "[-] " +  aux + valP[1] + "\n");
										}
									}
								}
							}else{
								Object[] effects = act.getEffects(state, pars);
								for(String[] pE : (List<String[]>)effects[0]){
									state.add(pE);
									aux = "(\\textit{" + pE[0] + "}";
									for(int i = 1; i < pE.length; i++) aux += " " + pE[i];
									aux += ")";
									out.write(valP[0] + "+" + aux + valP[1] + "\n");
								}
								for(String[] nE : (List<String[]>)effects[1]){
									state.remove(nE);
									aux = "(" + nE[0];
									for(int i = 1; i < nE.length; i++) aux += " " + nE[i];
									aux += ")";
									out.write(valP[0] + "-" + aux + valP[1] + "\n");
								}
							}
							out.write(suf);
							count++;
						}
						continue;
					}
					out.write(line);
					out.write("\n");
				}else{
					out.write(line);
					out.write("\n");
				}
			}
			out.close();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	//replaces commands with results in line
	private String lineRep(String line){
		line = line.replace("%!name!%", "PDDL Plan Validator");
		line = line.replace("%!domain!%", domain);
		line = line.replace("%!domainFile!%", domainFile.replace("\\", "\\textbackslash "));
		line = line.replace("%!problem!%", problem);
		line = line.replace("%!problemFile!%", problemFile.replace("\\", "\\textbackslash "));
		line = line.replace("%!planFile!%", planFile.replace("\\", "\\textbackslash "));
		line = line.replace("%!goalAchieved!%", goalAchieved()? "Goal achieved" : "Goal not achieved");
		return line;
	}
		
	/****************************************************
	* 			//
	****************************************************/	
	public boolean checkA(){
		boolean err = false;
		//no domain
		if(domain == null){
			System.err.println("Error no domain file");
			return true;
		}
		boolean strips = false;
		for(String req : reqs){
			switch(req){	
				case ":strips":
					strips = true;
					break;
				case ":equality":
					break;
				case ":typing":
					break;
				default:
					err = true;
					System.err.println("Error requirement " + req + " not implemented");
					break;
			}
		}
		//no :strips
		if(!strips) {
			System.err.println("Error :strips is required");
		}
		//typing
		if(rFlags[1]){
			if(types.size() == 0) System.err.println("Warning no types declared");
			//for 
		}
		if(preds.size() == 0) System.err.println("Warning no predicates declared");
		if(acts.size() == 0) System.err.println("Warning no actions declared");
		if(err) return true;
		//no problem
		if(problem == null){
			System.err.println("Error no problem file");
			return true;
		}
		for(String[] str : state){
			Pred p = null;
			for(Pred aux : preds) if(aux.name.equals(str[0])){
				p = aux;
				break;
			}
			if(p == null){
				System.err.println("Error invalid predicate " + str[0]);
				err = true;
			}
			if(str.length-1 != p.type.length){
				System.err.print("Error invalid parameters for predicate " + str[0] + " in ( ");
				for(String s : str) System.err.print(s + " ");
				System.err.println(")");
				err = true;
			}
			//typed
			if(rFlags[1]){
				for(int i = 0; i < str.length-1; i++){
					int aux = objects.indexOf(str[i+1]);
					if(aux == -1){
						System.err.println("Error invalid object " + str[i+1]);
						err = true;
						continue;
					}
					int type = objectTs[aux];
					if(p.type[i] != type){
						if(subTypes.size() > 0) if(p.type[i] != subTypes.get(type)){
							System.err.println("Error action " + str[0] + " requires type " + types.get(p.type[i]) + ", found " + str[i+1] + " - " + types.get(type));
						}
					}
				}
			}
		}
		if(err) return true;
		//no plan
		if(planFile == null){
			System.err.println("Error no plan file");
			return true;
		}
		//typed
		if(rFlags[1]){
			for(String[] p : plan) {
				Act a = acts.get(p[0]);
				if(a == null){
					System.err.println("Error invalid action " + p[0]);
					err = true;
					continue;
				}
				if(p.length-1 != a.parTs.length){
					System.err.print("Error invalid parameters for actions " + p[0] + " in ( ");
					for(String s : p) System.err.print(s + " ");
					System.err.println(")");
					err = true;
				}else{
					for(int i = 1; i < p.length; i++){
						int aux = objects.indexOf(p[i]);
						if(aux == -1){
							System.err.println("Error invalid object " + p[i]);
							err = true;
							continue;
						}
						int type = objectTs[aux];
						if(a.parTs[i-1] != type){
							if(subTypes.size() > 0) if(a.parTs[i-1] != subTypes.get(type)){
								System.err.println("Error action " + p[0] + " requires type " + types.get(a.parTs[i-1]) + ", found " + p[i] + " - " + types.get(type));
							}
						}
					}
				}
			}
		}else{			
			for(String[] p : plan) {
				if(!acts.containsKey(p[0])){
					System.err.println("Error invalid action " + p[0]);
					err = true;
					continue;
				}
				for(int i = 1; i < p.length; i++) if(!objects.contains(p[i])){
					System.err.println("Error invalid object " + p[i]);
					err = true;
				}
			}
		}
		return err;
	}
	
	
	public void planTest(){
		Object[] out = tryPlan(true);
		if(out != null){
			System.out.print("Error in action \"( " );
			for(String s : (String[])out[0]) System.out.print(s + " ");
			System.out.println(")\"");
			if(((Object[])(out[1])).length == 0){
				System.out.println("Invalid parameters");
			}else{
				List<String[]> l = (List<String[]>)((Object[])(out[1]))[0];
				if(l.size() > 0){
					System.out.println("Missing positive predicates");
					for(String[] str : l){
						System.out.print(" ");
						for(String s : str) System.out.print(s + " ");
						System.out.println("");
					}
				}
				l = (List<String[]>)((Object[])(out[1]))[1];
				if(l.size() > 0){
					System.out.println("Present negative predicates");
					for(String[] str : l){
						System.out.print(" ");
						for(String s : str) System.out.print(s + " ");
						System.out.println("");
					}
				}
			}
		}
		System.out.println("Goal " + (goalAchieved()?"":"not ") + "achieved");
	}
		
	
	public void printDomain(){
		System.out.println("_______________________\nDOMAIN " + domain);
		System.out.println("requirements");
		for(String r : reqs)System.out.println("\t" + r);
		System.out.println("types");
		for(String t : types)System.out.println("\t" + t);
		if(subTypes.size() > 0){
			for(int i = 0; i < subTypes.size(); i++) if(subTypes.get(i) >= 0) System.out.println("\t"+types.get(i) + " = " + types.get(subTypes.get(i)));
		}
		System.out.println("Actions");
		for(Map.Entry<String,Act> a : acts.entrySet()) System.out.println("\t" + a.getValue() + "\n");
		System.out.println("predicates");
		if(preds != null) for(Pred p : preds)System.out.println("\t" + p);
	}
	
	public void printProbPl(){
		System.out.println("NAME---------------");
		System.out.println(problem);
		System.out.println("OBJS---------------");
		if(rFlags[1]){
			for(int i = 0; i < objects.size(); i++){
				System.out.print(objects.get(i) + "[" + types.get(objectTs[i]) + "] ");
			}
			System.out.println("");
		}else{
			System.out.println(objects);
		}
		System.out.println("STATE---------------");
		for(String[] pred : state){
			for(String p : pred) System.out.print(p + " ");
			System.out.println("");
		}
		System.out.println("POSGOALS---------------");
		for(String[] goal : posGoals){
			for(String g : goal) System.out.print(g + " ");
			System.out.println("");
		}
		System.out.println("NEGGOALS---------------");
		for(String[] goal : negGoals){
			for(String g : goal) System.out.print(g + " ");
			System.out.println("");
		}
		//plan
		System.out.println("PLAN---------------");
		for(String[] p : plan){
			for(String s : p) System.out.print(s + " ");
			System.out.println("");
		}
		System.out.println(goalAchieved());
		System.out.println("\n");
	}
	
	public void printState(){
		System.out.println("STATE---------------");
		for(String[] pred : state){
			for(String p : pred) System.out.print(p + " ");
			System.out.println("");
		}
	}
}

//Predicate object
class Pred{
	String name;
	String[] objs = null;
	int[] type = null;
	
	public Pred(String name){
		this.name = name;
	}
	public Pred(String name, List<String> objs){
		this.name = name;
		int size = objs.size();
		this.objs = new String[size];
		size--;
		for(int i = 0; i < objs.size(); i++){
			this.objs[i] = objs.get(size-i);
		}
	}
	public Pred(String name, List<String> objs, List<Integer> type){
		this.name = name;
		int size = objs.size();
		this.objs = new String[size];
		this.type = new int[size];
		size--;
		for(int i = 0; i <= size; i++){
			this.objs[i] = objs.get(size-i);
			this.type[i] = type.get(size-i).intValue();
		}
	}
	//print with type name
	@Override
	public String toString(){
		if(objs==null) return name;
		String out = name;
		if(type == null){
			for(int i = 0; i < objs.length; i++){
				out += " [" + objs[i] + "]";
			}
		}else{
			for(int i = 0; i < objs.length; i++){
				out += " [" + objs[i] + " - " + String.valueOf(type[i])+ "]";
			}			
		}
		return out;
	}
	public String toString(String[] types){
		String out = name;
		for(int i = 0; i < objs.length; i++){
			out += " [" + objs[i] + " - " + types[type[i]]+ "]";
		}		
		return out;
	}
}


//action
class Act{
	String name; 			//action name	
	List<String> pars;		//action parameters 
	String[] parTNs;		//name of the action parameters 
	int[] parTs;			//value of the action parameter types
	List<String[]> positivePreconditions; 
	List<String[]> negativePreconditions;
	List<String[]> positiveEffects; 
	List<String[]> negativeEffects;
	
	public Act(String name, List<String> pars, List<String[]> positivePreconditions, List<String[]> negativePreconditions, List<String[]> positiveEffects, List<String[]> negativeEffects){
		this.name = name;
		this.pars = pars;
		this.positivePreconditions = positivePreconditions;
		this.negativePreconditions = negativePreconditions;
		this.positiveEffects = positiveEffects;
		this.negativeEffects = negativeEffects;
	}
	
	public Act(String name, List<String> pars, List<String> parTs, List<String[]> positivePreconditions, List<String[]> negativePreconditions, List<String[]> positiveEffects, List<String[]> negativeEffects, List<String> types){
		this.name = name;
		this.pars = pars;
		this.positivePreconditions = positivePreconditions;
		this.negativePreconditions = negativePreconditions;
		this.positiveEffects = positiveEffects;
		this.negativeEffects = negativeEffects;
		this.parTs = new int[parTs.size()];
		this.parTNs = new String[parTs.size()];
		for(int i = 0; i < parTs.size(); i++){
			parTNs[i] = parTs.get(i);
			this.parTs[i] = types.indexOf(parTs.get(i));
		}
	}
	
	//returns false preconditions
	public Object[] reason(List<String[]> worldState, String[] parameters, int[] types, List<Integer> sTypes, boolean[] rFlags){		
		if(parameters.length != pars.size()) return new Object[0];
		if(rFlags[1]) for(int i = 0; i < types.length; i++) if(types[i] != parTs[i] && sTypes.get(i) != parTs[i]) return new Object[0];
		if(rFlags[0]){
			return reason1(worldState, parameters);
		}
		else{
			return reason0(worldState, parameters);
		}
	}
	private Object[] reason0(List<String[]> worldState, String[] parameters){		
		Object[] rList = new Object[2];
		List<String[]> positiveR = new ArrayList<String[]>();
		List<String[]> negativeR = new ArrayList<String[]>();
		for(String[] pP : positivePreconditions){
			if(pP.length == 1){
				boolean found = false;
				for(int i = 0; i < worldState.size();i++){
					if(worldState.get(i).length == 1){
						if(pP[0].equals(worldState.get(i)[0])){
							found = true;
							break;
						}
					}
				}
				if(!found) positiveR.add(pP);
				continue;
			}
			//orded predicate objects
			String[] pars = new String[pP.length-1]; 
			for(int i = 1; i < pP.length; i++) pars[i-1] = parameters[this.pars.indexOf(pP[i])];
			
			boolean found = false;
			for(int i = 0; i < worldState.size(); i++){
				if(worldState.get(i).length == pP.length){
					if(pP[0].equals(worldState.get(i)[0])){
						found = true;
						for(int j = 0; j < pP.length-1; j++){
							if(!worldState.get(i)[j+1].equals(pars[j])){
								found = false;
								break;
							}
						}
						if(found) break;
					}
				}
			}
			if(found) continue;
			String[] aux = new String[pP.length];
			aux[0] = pP[0];
			for(int i = 0; i < pars.length; i++) aux[i+1] = pars[i];
			positiveR.add(aux);
		}			
		for(String[] pP : negativePreconditions){
			if(pP.length == 1){
				for(int i = 0; i < worldState.size();i++){
					if(worldState.get(i).length == 1){
						if(pP[0].equals(worldState.get(i)[0])) negativeR.add(pP);
					}
				}
			}else{
				//orded predicate objects
				String[] pars = new String[pP.length-1]; 
				for(int i = 1; i < pP.length; i++) pars[i-1] = parameters[this.pars.indexOf(pP[i])];
				
				boolean found = false;
				for(int i = 0; i < worldState.size(); i++){
					if(worldState.get(i).length == pP.length){
						if(pP[0].equals(worldState.get(i)[0])){
							found = true;
							for(int j = 0; j < pP.length-1; j++){
								if(!worldState.get(i)[j+1].equals(pars[j])){
									found = false;
									break;
								}
							}
							if(found) break;
						}
					}
				}
				if(!found) continue;
				String[] aux = new String[pP.length];
				aux[0] = pP[0];
				for(int i = 0; i < pars.length; i++) aux[i+1] = pars[i];
				negativeR.add(aux);
			}
		}	
		rList[0] = positiveR;
		rList[1] = negativeR;
		return rList;
	}
	private Object[] reason1(List<String[]> worldState, String[] parameters){
		Object[] rList = new Object[2];
		List<String[]> positiveR = new ArrayList<String[]>();
		List<String[]> negativeR = new ArrayList<String[]>();
		for(String[] pP : positivePreconditions){
			if(pP.length == 1){
				boolean found = false;
				for(int i = 0; i < worldState.size();i++){
					if(worldState.get(i).length == 1){
						if(pP[0].equals(worldState.get(i)[0])){
							found = true;
							break;
						}
					}
				}
				if(!found) positiveR.add(pP);
				continue;
			}
			//orded predicate objects
			String[] pars = new String[pP.length-1]; 
			for(int i = 1; i < pP.length; i++) pars[i-1] = parameters[this.pars.indexOf(pP[i])];
			
			//equals 
			if(pP[0].equals("=")){
				for(int i = 0; i < pars.length-1; i++){
					if(!pars[i].equals(pars[i+1])){
						String[] aux = new String[pP.length];
						aux[0] = pP[0];
						for(int j = 0; j < pars.length; j++) aux[j+1] = pars[j];
						positiveR.add(aux);
						break;
					}
				}
				continue;
			}
			//predicate
			boolean found = false;
			for(int i = 0; i < worldState.size(); i++){
				if(worldState.get(i).length == pP.length){
					if(pP[0].equals(worldState.get(i)[0])){
						found = true;
						for(int j = 0; j < pP.length-1; j++){
							if(!worldState.get(i)[j+1].equals(pars[j])){
								found = false;
								break;
							}
						}
						if(found) break;
					}
				}
			}
			if(found) continue;
			String[] aux = new String[pP.length];
			aux[0] = pP[0];
			for(int i = 0; i < pars.length; i++) aux[i+1] = pars[i];
			positiveR.add(aux);
		}			
		for(String[] pP : negativePreconditions){
			if(pP.length == 1){
				for(int i = 0; i < worldState.size();i++){
					if(worldState.get(i).length == 1){
						if(pP[0].equals(worldState.get(i)[0])) negativeR.add(pP);
					}
				}
			}else{
				//orded predicate objects
				String[] pars = new String[pP.length-1]; 
				for(int i = 1; i < pP.length; i++) pars[i-1] = parameters[this.pars.indexOf(pP[i])];
				//equals 
				if(pP[0].equals("=")){
					for(int i = 0; i < pars.length-1; i++){
						if(pars[i].equals(pars[i+1])){
							String[] aux = new String[pP.length];
							aux[0] = pP[0];
							for(int j = 0; j < pars.length; j++) aux[j+1] = pars[j];
							negativeR.add(aux);
							break;
						}
					}
					continue;
				}
				boolean found = false;
				for(int i = 0; i < worldState.size(); i++){
					if(worldState.get(i).length == pP.length){
						if(pP[0].equals(worldState.get(i)[0])){
							found = true;
							for(int j = 0; j < pP.length-1; j++){
								if(!worldState.get(i)[j+1].equals(pars[j])){
									found = false;
									break;
								}
							}
							if(found) break;
						}
					}
				}
				if(!found) continue;
				String[] aux = new String[pP.length];
				aux[0] = pP[0];
				for(int i = 0; i < pars.length; i++) aux[i+1] = pars[i];
				negativeR.add(aux);
			}
		}	
		rList[0] = positiveR;
		rList[1] = negativeR;
		return rList;
	}
		
	
	public boolean applicable(List<String[]> worldState, String[] parameters, int[] types, List<Integer> sTypes, boolean[] rFlags){
		if(parameters.length != pars.size()) return false;
		if(rFlags[1]) for(int i = 0; i < types.length; i++) if(types[i] != parTs[i] && sTypes.get(i) != parTs[i]) return false;
		if(rFlags[0]){
			return applicable1(worldState, parameters, types);
		}
		else{
			return applicable0(worldState, parameters, types);
		}
	}		
	private boolean applicable0(List<String[]> worldState, String[] parameters, int[] types){
		for(String[] pP : positivePreconditions){
			if(pP.length == 1){
				boolean found = false;
				for(int i = 0; i < worldState.size();i++){
					if(worldState.get(i).length == 1){
						if(pP[0].equals(worldState.get(i)[0])){
							found = true;
							break;
						}
					}
				}
				if(found) continue;
				return false;
			}
			//orded predicate objects
			String[] pars = new String[pP.length-1]; 
			for(int i = 1; i < pP.length; i++) pars[i-1] = parameters[this.pars.indexOf(pP[i])];
			
			boolean found = false;
			for(int i = 0; i < worldState.size(); i++){
				if(worldState.get(i).length == pP.length){
					if(pP[0].equals(worldState.get(i)[0])){
						found = true;
						for(int j = 0; j < pP.length-1; j++){
							if(!worldState.get(i)[j+1].equals(pars[j])){
								found = false;
								break;
							}
						}
						if(found) break;
					}
				}
			}
			if(!found) return false;
		}
		for(String[] pP : negativePreconditions){
			if(pP.length == 1){
				for(int i = 0; i < worldState.size();i++){
					if(worldState.get(i).length == 1){
						if(pP[0].equals(worldState.get(i)[0])) return false;
					}
				}
			}
			//orded predicate objects
			String[] pars = new String[pP.length-1]; 
			for(int i = 1; i < pP.length; i++) pars[i-1] = parameters[this.pars.indexOf(pP[i])];
			
			for(int i = 0; i < worldState.size(); i++){
				if(worldState.get(i).length == pP.length){
					if(pP[0].equals(worldState.get(i)[0])){
						boolean found = true;
						for(int j = 0; j < pP.length-1; j++){
							if(!worldState.get(i)[j+1].equals(pars[j])){
								found = false;
								break;
							}
						}
						if(found) return false;
					}
				}
			}
		}
		return true;
	}
	private boolean applicable1(List<String[]> worldState, String[] parameters, int[] types){
		for(String[] pP : positivePreconditions){
			if(pP.length == 1){
				boolean found = false;
				for(int i = 0; i < worldState.size();i++){
					if(worldState.get(i).length == 1){
						if(pP[0].equals(worldState.get(i)[0])){
							found = true;
							break;
						}
					}
				}
				if(found) continue;
				return false;
			}
			//orded predicate objects
			String[] pars = new String[pP.length-1]; 
			for(int i = 1; i < pP.length; i++) pars[i-1] = parameters[this.pars.indexOf(pP[i])];
			
			//equals 
			if(pP[0].equals("=")){
				for(int i = 0; i < pars.length-1; i++){
					if(!pars[i].equals(pars[i+1])) return false;
				}
				continue;
			}
			//predicate
			boolean found = false;
			for(int i = 0; i < worldState.size(); i++){
				if(worldState.get(i).length == pP.length){
					if(pP[0].equals(worldState.get(i)[0])){
						found = true;
						for(int j = 0; j < pP.length-1; j++){
							if(!worldState.get(i)[j+1].equals(pars[j])){
								found = false;
								break;
							}
						}
						if(found) break;
					}
				}
			}
			if(!found) return false;
		}
		for(String[] pP : negativePreconditions){
			if(pP.length == 1){
				for(int i = 0; i < worldState.size();i++){
					if(worldState.get(i).length == 1){
						if(pP[0].equals(worldState.get(i)[0])) return false;
					}
				}
			}
			//orded predicate objects
			String[] pars = new String[pP.length-1]; 
			for(int i = 1; i < pP.length; i++) pars[i-1] = parameters[this.pars.indexOf(pP[i])];
			
			//equals 
			if(pP[0].equals("=")){
				boolean equals = true;
				for(int i = 0; i < pars.length-1; i++){
					if(!pars[i].equals(pars[i+1])){
						equals = false;
						break;
					}
				}
				if(equals) return false;
				continue;
			}
			//predicate
			for(int i = 0; i < worldState.size(); i++){
				if(worldState.get(i).length == pP.length){
					if(pP[0].equals(worldState.get(i)[0])){
						boolean found = true;
						for(int j = 0; j < pP.length-1; j++){
							if(!worldState.get(i)[j+1].equals(pars[j])){
								found = false;
								break;
							}
						}
						if(found) return false;
					}
				}
			}
		}
		return true;
	}
	
	public List<String[]> apply(List<String[]> worldState, String[] parameters){
		for(String[] pE : positiveEffects){
			if(pE.length == 1){
				boolean in = false;
				for(String[] s: worldState){
					if(s.length == 1){
						if(s[0].equals(pE[0])){
							in = true;
							break;
						}
					}
				}
				if(!in) worldState.add(pE);
				continue;
			}
			
			//orded predicate objects
			String[] aux = new String[pE.length]; 
			aux[0] = pE[0];
			for(int i = 1; i < pE.length; i++) aux[i] = parameters[this.pars.indexOf(pE[i])];
			boolean in = false;
			for(String[] s: worldState){
				if(s.length == aux.length){
					in = true;
					for(int i = 0; i < aux.length; i++) {
						if(!aux[i].equals(s[i])){
							in = false;
							break;
						}
					}
					if(in) break;
				}
			}
			if(!in) worldState.add(aux);
		}
		
		for(String[] nE : negativeEffects){
			if(nE.length == 1){
				for(int i = 0; i < worldState.size(); i++){
					if(worldState.get(i).length == 1){
						if(worldState.get(i)[0].equals(nE[0])){
							worldState.remove(i);
							break;
						}
					}
				}
			}
			//orded predicate objects
			String[] aux = new String[nE.length]; 
			aux[0] = nE[0];
			for(int i = 1; i < nE.length; i++) aux[i] = parameters[this.pars.indexOf(nE[i])];
			for(int i = 0; i < worldState.size(); i++){
				if(worldState.get(i).length == aux.length){
					boolean in = true;
					for(int j = 0; j < aux.length; j++) {
						if(!aux[j].equals(worldState.get(i)[j])){
							in = false;
							break;
						}
					}
					if(in) worldState.remove(i);
					break;
				}
			}
			worldState.remove(aux);
		}
		return worldState;
	}
	
	public Object[] getEffects(List<String[]> worldState, String[] parameters){
		Object[] out = new Object[2];
		out[0] = new ArrayList<String[]>();
		out[1] = new ArrayList<String[]>();
		for(String[] pE : positiveEffects){
			if(pE.length == 1){
				boolean in = false;
				for(String[] s: worldState){
					if(s.length == 1){
						if(s[0].equals(pE[0])){
							in = true;
							break;
						}
					}
				}
				if(!in) ((List)out[0]).add(pE);
				continue;
			}
			
			//orded predicate objects
			String[] aux = new String[pE.length]; 
			aux[0] = pE[0];
			for(int i = 1; i < pE.length; i++) aux[i] = parameters[this.pars.indexOf(pE[i])];
			boolean in = false;
			for(String[] s: worldState){
				if(s.length == aux.length){
					in = true;
					for(int i = 0; i < aux.length; i++) {
						if(!aux[i].equals(s[i])){
							in = false;
							break;
						}
					}
					if(in) break;
				}
			}
			if(!in) ((List)out[0]).add(aux);
		}
		
		for(String[] nE : negativeEffects){
			if(nE.length == 1){
				for(int i = 0; i < worldState.size(); i++){
					if(worldState.get(i).length == 1){
						if(worldState.get(i)[0].equals(nE[0])){
							((List)out[1]).remove(i);
							break;
						}
					}
				}
			}
			//orded predicate objects
			String[] aux = new String[nE.length]; 
			aux[0] = nE[0];
			for(int i = 1; i < nE.length; i++) aux[i] = parameters[this.pars.indexOf(nE[i])];
			for(int i = 0; i < worldState.size(); i++){
				if(worldState.get(i).length == aux.length){
					boolean in = true;
					for(int j = 0; j < aux.length; j++) {
						if(!aux[j].equals(worldState.get(i)[j])){
							in = false;
							break;
						}
					}
					if(in) ((List)out[1]).remove(i);
					break;
				}
			}
			((List)out[1]).remove(aux);
		}
		return out;
	}
	
	@Override
	public String toString(){
		String out = name;
		if(pars.size() > 0) out += "\n\tParams:  ";
		if(parTs == null){
			for(String par : pars){
				out += par + " ";
			}
		}else{
			for(int i = 0; i < pars.size(); i++){
				out += pars.get(i) + " [" + parTNs[i] + "] ";
			}			
		}
		if(positivePreconditions.size() > 0) out += "\n\tPosPrecd:  ";
		for(String[] pP : positivePreconditions){
			out += "[" + pP[0];
			for(int i = 1; i < pP.length; i++) out += " "+pP[i];
			out += "]";
		}
		if(negativePreconditions.size() > 0) out += "\n\tNegPrecd:  ";
		for(String[] nP : negativePreconditions){
			out += "[" + nP[0];
			for(int i = 1; i < nP.length; i++) out += " "+nP[i];
			out += "]";
		}
		if(positiveEffects.size() > 0) out += "\n\tPositive Effects:  ";
		for(String[] pE : positiveEffects){
			out += "[" + pE[0];
			for(int i = 1; i < pE.length; i++) out +=  " "+pE[i];
			out += "]";
		}
		if(negativeEffects.size() > 0) out += "\n\tNegative Effects:  ";
		for(String[] nE : negativeEffects){
			out += "[" + nE[0];
			for(int i = 1; i < nE.length; i++) out += " "+nE[i];
			out += "]";
		}
		return out;
	}
}

