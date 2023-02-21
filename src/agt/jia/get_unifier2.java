// Internal action code for project data_access_control

package jia;

import java.util.ArrayList;
import java.util.List;

import jason.asSemantics.*;
import jason.asSyntax.*;

public class get_unifier2 extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
    	
       // Get Parameters        
       List<Literal> rules = (List<Literal>) args[0]; //receives defeasible rules without unification
       List<Term> preds = (List<Term>) args[1]; // receives the unified terms
       Unifier u = new Unifier();

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
		    	    			System.out.print("Unifier function -> ");
		    	    			System.out.println(u.toString());
		    	    		}
	    	    		}
	    	    	}
	    	    } else {
	    	    	for (Term pred : preds) {
	    	    		Literal predLit = Literal.parseLiteral(pred.toString());
		    	    	if(u.unifies(element, predLit)) {
			    		}
	    	    	}
	    	    }
	      }
      }
      
      return un.unifies(args[2], new StringTermImpl(u.toString()));
    
    }
}
