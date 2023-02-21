package jia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;


public class unifyRule extends DefaultInternalAction {
	
	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

		// Get Parameters
		List<Literal> rules = (List<Literal>) args[0]; // receives defeasible rules without unification
		List<Term> preds = (List<Term>) args[1]; // receives the unified terms
		Unifier u = new Unifier();
		List<Term> unifiedRules = new ArrayList<Term>();
		
		List<Term> predsByRules =  new ArrayList<Term>();
		List<Term> duplicatePreds =  new ArrayList<Term>();
		List<String> uniquePredsStr = new ArrayList<String>();
		List<Term> uniquePreds = new ArrayList<Term>();
		
		// Separate single predicates to be unified first
		preds.forEach(p->{
			Literal pLit = Literal.parseLiteral(p.toString());
			if (uniquePredsStr.contains(pLit.getAsListOfTerms().get(1).toString())) {
				int index = uniquePredsStr.indexOf(pLit.getAsListOfTerms().get(1).toString());
				duplicatePreds.add(p);
				duplicatePreds.add(uniquePreds.get(index));
				uniquePredsStr.remove(index);
				uniquePreds.remove(index);
			} else {
				uniquePredsStr.add(pLit.getAsListOfTerms().get(1).toString());
				uniquePreds.add(p);
			}
		});
		
		
		UnifiedReturn urUnique = unifyByPred(uniquePreds, rules, u);

		 u = urUnique.u;
		 predsByRules.addAll(urUnique.predsByRules);
		 unifiedRules.addAll(urUnique.unifiedRules);
			
		UnifiedReturn urDuplicate = unifyByPred(duplicatePreds, rules, u);
		
		u = urDuplicate.u;
		urDuplicate.predsByRules.forEach(pbr -> {
			if (!predsByRules.contains(pbr)) {
				predsByRules.add(pbr);
			}
		});
		urDuplicate.unifiedRules.forEach(ufr -> {
			if (!unifiedRules.contains(ufr)) {
				unifiedRules.add(ufr);
			}
		});
		
		 if (rules.size() > unifiedRules.size()) {
				UnifiedReturn urByRules = unifyByPred(predsByRules, rules, u);	

				u = urByRules.u;
				urByRules.predsByRules.forEach(pbr -> {
					if (!predsByRules.contains(pbr) & !duplicatePreds.contains(pbr) & !uniquePreds.contains(pbr)) {
						predsByRules.add(pbr);
					}
				});
				urByRules.unifiedRules.forEach(ufr -> {
					if (!unifiedRules.contains(ufr)) {
						unifiedRules.add(ufr);
					}
				});
		 }

		return un.unifies(args[2], ASSyntax.parseTerm(unifiedRules.toString()));

	}
	
	UnifiedReturn unifyByPred(List<Term> preds, List<Literal> rules, Unifier u) {
		List<Term> unifiedRules = new ArrayList<Term>();
		List<Term> predsByRules =  new ArrayList<Term>();
		
		 for (Literal arg : rules) {

	    	  Literal argLit = Literal.parseLiteral(arg.toString());
		      List<Term> listTerms = argLit.getTerms(); 
		      
		      // for each element of the list, check if it is possible to carry out the unification 
		      // with each unified term passed as a parameter.
		      for (Term element : listTerms) {  
		    	    if(element.isList()) {
		    	    	List<Term> list2 = (List<Term>) element;
		    	    	for (Term term : list2) {
		    	    		for (Term pred : preds) {
		    	    			Literal predLit = Literal.parseLiteral(pred.toString());
			    	    		if(u.unifies(term, predLit)) {
//			    	    			System.out.print(term);
//			    	    			System.out.print(" -> ");
//			    	    			System.out.println(predLit);
//			    	    			System.out.print("Unifier function -> ");
//			    	    			System.out.println(u.toString());
			    	    		}
		    	    		}
		    	    	}
		    	    } else {
		    	    	for (Term pred : preds) {
		    	    		Literal predLit = Literal.parseLiteral(pred.toString());
			    	    	if(u.unifies(element, predLit)) {
//			    	    		System.out.print(element);
//		    	    			System.out.print(" -> ");
//		    	    			System.out.println(predLit);
//		    	    			System.out.print("Unifier function -> ");
//		    	    			System.out.println(u.toString());
				    		}
		    	    	}
		    	    }
		      }
		      
		      Term unifiedRule = getUnifiedRule(argLit, u);
				if (unifiedRule.isGround()) {
					if (!unifiedRules.contains(unifiedRules)) {
						unifiedRules.add(unifiedRule);
					}
					Literal unifiedRuleLit = Literal.parseLiteral(unifiedRule.toString());
					Term t = unifiedRuleLit.getTerm(0); // add only rule conclusion to predsByRules
						if(t.isList()) {
							List<Term> tList = (List<Term>) t;
							tList.forEach(elementTList -> {
								if (!predsByRules.contains(elementTList)) predsByRules.add(elementTList);
							});
						} else {
							if (!predsByRules.contains(t)) predsByRules.add(t);
						}
				}
		 }
		
		 UnifiedReturn ur = new UnifiedReturn();
		 ur.u = u;
		 ur.predsByRules = predsByRules;
		 ur.unifiedRules = unifiedRules;
		return ur;
	}
	

	Term getUnifiedRule(Literal rule, Unifier u) {
		Term ruleLit = Literal.parseLiteral(rule.toString());
		return Literal.parseLiteral(ruleLit.capply(u).toString());
	}
}

class UnifiedReturn {
	Unifier u;
	List<Term> unifiedRules;
	List<Term> predsByRules;
}
