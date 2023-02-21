package br.pucrs.smart.Onto4JaCa.mas;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLRule;

import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class AxiomTranslator {
	
	static Literal translateAxioms(Set<OWLAxiom> explanation) {
		Collection<Term> rules =  new LinkedList<Term>();
		Collection<Term> assertions =  new LinkedList<Term>();
		Collection<Term> classInfo =  new LinkedList<Term>();
		for (OWLAxiom axiom : explanation) {
//			System.out.println(axiom);
			if(hasType(axiom, "Rule")){
				rules.add(translateRule(axiom));
			} else if(hasType(axiom, "ObjectPropertyDomain")){
				//ignore
			} else if(hasType(axiom, "ObjectPropertyRange")){
				//ignore
			} else if(hasType(axiom, "ObjectPropertyAssertion")){

//				OWLObjectPropertyAssertionAxiom oPAAxiom = (OWLObjectPropertyAssertionAxiom) axiom;
//				List<?> oPAAxiomComponents = oPAAxiom.components().collect(Collectors.toList());
//				
//				String domain = oPAAxiomComponents.get(0).toString();
//				String objectPropertie = oPAAxiomComponents.get(1).toString();
//				String range = oPAAxiomComponents.get(2).toString();
//				
//				Literal l = ASSyntax.createLiteral(OntoQueryLayerLiteral.getNameForJason(objectPropertie.substring((objectPropertie.indexOf("#")+1), objectPropertie.indexOf(">"))));
//				l.addTerm(ASSyntax.createString(domain.substring((domain.indexOf("#")+1), domain.indexOf(">"))));
//				l.addTerm(ASSyntax.createString(range.substring((range.indexOf("#")+1), range.indexOf(">"))));
				
				assertions.add(translateObjectPropertyAssertion(axiom));
			} else if(hasType(axiom, "DataPropertyDomain")){
//				//ignore
			} else if(hasType(axiom, "DifferentIndividuals")){
//				//ignore
			} else if(hasType(axiom, "InverseObjectProperties")){
//				//ignore
			} else if(hasType(axiom, "DataPropertyAssertion")){

//				OWLDataPropertyAssertionAxiom dPAAxiom = (OWLDataPropertyAssertionAxiom) axiom;
//				List<?> dPAAxiomComponents = dPAAxiom.components().collect(Collectors.toList());
//				
//				String domain = dPAAxiomComponents.get(0).toString();
//				String propertie = dPAAxiomComponents.get(1).toString();
//				String range = dPAAxiomComponents.get(2).toString();
//				
//				Literal l = ASSyntax.createLiteral(propertie.substring((propertie.indexOf("#")+1), propertie.indexOf(">")));
//				l.addTerm(ASSyntax.createString(domain.substring((domain.indexOf("#")+1), domain.indexOf(">"))));
//				if (range.contains("xsd:integer")) {
//					l.addTerm(ASSyntax.createString(range.substring(1, (range.indexOf("^^")-1))));
//				} else {
//					l.addTerm(ASSyntax.createString(range.substring((range.indexOf("#")+1), range.indexOf(">"))));
//				}
				
				assertions.add(translateDataPropertyAssertion(axiom));
			} else if(hasType(axiom, "SubClassOf")){

//				OWLSubClassOfAxiom sCAAxiom = (OWLSubClassOfAxiom) axiom;
//				List<?> sCAAxiomComponents = sCAAxiom.components().collect(Collectors.toList());
//				String domain = sCAAxiomComponents.get(0).toString();
//				String range = sCAAxiomComponents.get(1).toString();
//				
//				Literal l = ASSyntax.createLiteral("subClassOf");
//				l.addTerm(ASSyntax.createAtom(OntoQueryLayerLiteral.getNameForJason(domain.substring((domain.indexOf("#")+1), domain.indexOf(">")))));
//				l.addTerm(ASSyntax.createAtom(OntoQueryLayerLiteral.getNameForJason(range.substring((range.indexOf("#")+1), range.indexOf(">")))));
//				
				classInfo.add(translateSubClassOfAxiom(axiom));
			} else if(hasType(axiom, "ClassAssertion")){

//				OWLClassAssertionAxiom cAAxiom = (OWLClassAssertionAxiom) axiom;
//				List<?> cAAxiomComponents = cAAxiom.components().collect(Collectors.toList());
//
//				String range = cAAxiomComponents.get(0).toString();
//				String domain = cAAxiomComponents.get(1).toString();
//				Literal l = ASSyntax.createLiteral(OntoQueryLayerLiteral.getNameForJason(domain.substring((domain.indexOf("#")+1), domain.indexOf(">"))));
//				l.addTerm(ASSyntax.createString(range.substring((range.indexOf("#")+1), range.indexOf(">"))));
//				
				classInfo.add(translateClassAssertion(axiom));
			} else {
				System.out.println("[AxiomTranslator] Error: Type not registered: " + axiom.getAxiomType());
				System.out.println(axiom);
			}
		}
		
		Literal assertionsLiteral = ASSyntax.createLiteral("assertions", ASSyntax.createList(assertions));
		Literal classInfoLiteral = ASSyntax.createLiteral("classInfo", ASSyntax.createList(classInfo));
		Literal rulesLiteral = ASSyntax.createLiteral("rules", ASSyntax.createList(rules));
		
		Literal explanationTerms = ASSyntax.createLiteral("explanationTerms", rulesLiteral);
		explanationTerms.addTerm(assertionsLiteral);
		explanationTerms.addTerm(classInfoLiteral);
		
		return explanationTerms;
		
	}
	
	static Collection<Term> getTermsBySWRLAtoms(List<SWRLAtom> atoms) {
		Collection<Term> terms = new LinkedList<Term>();
		atoms.forEach(entity -> {
			String uriPred = entity.getPredicate().toString();
			String pred = uriPred.substring((uriPred.contains("owl:") ? uriPred.indexOf("owl:")+4 : uriPred.indexOf("#")+1), (uriPred.contains(">") ? uriPred.indexOf(">") : uriPred.length()));
			String jPred = OntoQueryLayerLiteral.getNameForJason(pred);
			Literal l = ASSyntax.createLiteral(jPred);
			
			List<?> var = entity.components().collect(Collectors.toList());
			var.forEach(v -> {
				String vs = v.toString();
				if (!vs.contains(pred)) {
					if (vs.contains("Variable")) {
						if(vs.contains("xsd:integer")) {
							// for example [Variable(<urn:swrl:var#A>), "17"^^xsd:integer]
							l.addTerm(ASSyntax.createVar(vs.substring((vs.indexOf("#")+1), vs.indexOf(">"))));
							l.addTerm(ASSyntax.createAtom(vs.substring((vs.indexOf(",")+3), (vs.indexOf("^^")-1))));
						} else {
							l.addTerm(ASSyntax.createVar(vs.substring((vs.indexOf("#")+1), vs.indexOf(">"))));
						}
					} else {
						l.addTerm(ASSyntax.createString(vs.substring((vs.indexOf("#")+1), vs.indexOf(">"))));
					}
				}
			});
			terms.add(l);
		});
		return terms;
	}
	
	static Term getTermsBySWRLAtomsHead(List<SWRLAtom> atoms) {
		Term term;

			SWRLAtom entity = atoms.get(0);
			String uriPred = entity.getPredicate().toString();
			String pred = uriPred.substring((uriPred.contains("owl:") ? uriPred.indexOf("owl:")+4 : uriPred.indexOf("#")+1), (uriPred.contains(">") ? uriPred.indexOf(">") : uriPred.length()));
			String jPred = OntoQueryLayerLiteral.getNameForJason(pred);
			Literal l = ASSyntax.createLiteral(jPred);
			
			List<?> var = entity.components().collect(Collectors.toList());
			var.forEach(v -> {
				String vs = v.toString();
				if (!vs.contains(pred)) {
					if (vs.contains("Variable")) {
						if(vs.contains("xsd:integer")) {
							// for example [Variable(<urn:swrl:var#A>), "17"^^xsd:integer]
							l.addTerm(ASSyntax.createVar(vs.substring((vs.indexOf("#")+1), vs.indexOf(">"))));
							l.addTerm(ASSyntax.createAtom(vs.substring((vs.indexOf(",")+3), (vs.indexOf("^^")-1))));
						} else {
							l.addTerm(ASSyntax.createVar(vs.substring((vs.indexOf("#")+1), vs.indexOf(">"))));
						}
					} else {
						l.addTerm(ASSyntax.createString(vs.substring((vs.indexOf("#")+1), vs.indexOf(">"))));
					}
				}
			});
			term =l;

		return term;
	}
	public static boolean hasType(OWLAxiom axiom, String type) {
		AxiomType<?> aType = axiom.getAxiomType();
		return aType.equals(AxiomType.getAxiomType(type));
	}
	
	static Literal translateRule(OWLAxiom axiom) {
//		 defeasible_rule([is_of_age_group(P,adult)],[person(P),age(P,A),greaterThan(A,17)])[as(<nome_do_esquema>)]
		SWRLRule rule = (SWRLRule) axiom;
		List<SWRLAtom> body = rule.bodyList();
		List<SWRLAtom> head = rule.headList();
		
		Collection<Term> bodyTerms = getTermsBySWRLAtoms(body);
		Term headTerms = getTermsBySWRLAtomsHead(head);
		
		Literal dF = ASSyntax.createLiteral("defeasible_rule", headTerms);
		dF.addTerm(ASSyntax.createList(bodyTerms));
		UUID uniqueKey = UUID.randomUUID();
		dF.addAnnot(ASSyntax.createLiteral("as", ASSyntax.createString("scheme_"+OntoQueryLayerLiteral.getNameForJason(uniqueKey.toString()))));

		return dF;
	}
	
	static Literal translateDataPropertyAssertion(OWLAxiom axiom) {
		OWLDataPropertyAssertionAxiom dPAAxiom = (OWLDataPropertyAssertionAxiom) axiom;
		List<?> dPAAxiomComponents = dPAAxiom.components().collect(Collectors.toList());
		
		String domain = dPAAxiomComponents.get(0).toString();
		String propertie = dPAAxiomComponents.get(1).toString();
		String range = dPAAxiomComponents.get(2).toString();
		
		Literal l = ASSyntax.createLiteral(OntoQueryLayerLiteral.getNameForJason(propertie.substring((propertie.indexOf("#")+1), propertie.indexOf(">"))));
		l.addTerm(ASSyntax.createString(domain.substring((domain.indexOf("#")+1), domain.indexOf(">"))));
		if (range.contains("xsd:integer")) {
			l.addTerm(ASSyntax.createAtom(range.substring(1, (range.indexOf("^^")-1))));
		} else {
			l.addTerm(ASSyntax.createString(range.substring((range.indexOf("#")+1), range.indexOf(">"))));
		}
		
		return l;
	}
	
	static Literal translateObjectPropertyAssertion(OWLAxiom axiom) {
		OWLObjectPropertyAssertionAxiom oPAAxiom = (OWLObjectPropertyAssertionAxiom) axiom;
		List<?> oPAAxiomComponents = oPAAxiom.components().collect(Collectors.toList());
		
		String domain = oPAAxiomComponents.get(0).toString();
		String objectPropertie = oPAAxiomComponents.get(1).toString();
		String range = oPAAxiomComponents.get(2).toString();
		
		Literal l = ASSyntax.createLiteral(OntoQueryLayerLiteral.getNameForJason(objectPropertie.substring((objectPropertie.indexOf("#")+1), objectPropertie.indexOf(">"))));
		l.addTerm(ASSyntax.createString(domain.substring((domain.indexOf("#")+1), domain.indexOf(">"))));
		l.addTerm(ASSyntax.createString(range.substring((range.indexOf("#")+1), range.indexOf(">"))));
		return l;
	}
	
	static Literal translateSubClassOfAxiom(OWLAxiom axiom) {
		OWLSubClassOfAxiom sCAAxiom = (OWLSubClassOfAxiom) axiom;
		List<?> sCAAxiomComponents = sCAAxiom.components().collect(Collectors.toList());
		String domain = sCAAxiomComponents.get(0).toString();
		String range = sCAAxiomComponents.get(1).toString();
		
		Literal l = ASSyntax.createLiteral("subClassOf");
		l.addTerm(ASSyntax.createAtom(OntoQueryLayerLiteral.getNameForJason(domain.substring((domain.indexOf("#")+1), domain.indexOf(">")))));
		l.addTerm(ASSyntax.createAtom(OntoQueryLayerLiteral.getNameForJason(range.substring((range.indexOf("#")+1), range.indexOf(">")))));
	
		return l;
	}
	
	static Literal translateClassAssertion(OWLAxiom axiom) {
		OWLClassAssertionAxiom cAAxiom = (OWLClassAssertionAxiom) axiom;
		List<?> cAAxiomComponents = cAAxiom.components().collect(Collectors.toList());
	
		String range = cAAxiomComponents.get(0).toString();
		String domain = cAAxiomComponents.get(1).toString();
		Literal l = ASSyntax.createLiteral(OntoQueryLayerLiteral.getNameForJason(domain.substring((domain.indexOf("#")+1), domain.indexOf(">"))));
		l.addTerm(ASSyntax.createString(range.substring((range.indexOf("#")+1), range.indexOf(">"))));
	
		return l;
	}
	
	static Collection<Term> translateDifferentIndividual(OWLAxiom axiom) {
//		differentFrom(A,B)
		Collection<Term> terms = new LinkedList<Term>();
		OWLDifferentIndividualsAxiom dIAxiom = (OWLDifferentIndividualsAxiom) axiom;
		List<?> dIAxiomComponents = (List<?>) dIAxiom.components().collect(Collectors.toList()).get(0);
		for (int i = 0; i < dIAxiomComponents.size()-1; i++) {
			String domain = dIAxiomComponents.get(i).toString();
			for (int j = i+1; j < dIAxiomComponents.size(); j++) {
				String range = dIAxiomComponents.get(j).toString();
				Literal l = ASSyntax.createLiteral("differentIndividuals");
				l.addTerm(ASSyntax.createString(OntoQueryLayerLiteral.removeUnderscore(domain.substring((domain.indexOf("#")+1), domain.indexOf(">")))));
				l.addTerm(ASSyntax.createString(OntoQueryLayerLiteral.removeUnderscore(range.substring((range.indexOf("#")+1), range.indexOf(">")))));
				terms.add(l);
			}
		}
		return terms;
	}
}
