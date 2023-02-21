package br.pucrs.smart.Onto4JaCa.mas;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLRule;

import br.pucrs.smart.Onto4JaCa.OntoQueryLayer;
import br.pucrs.smart.Onto4JaCa.OwlOntoLayer;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import uk.ac.manchester.cs.owl.owlapi.SWRLRuleImpl;

public class OntoQueryLayerLiteral {
	private final String FunctorInstance 	       = "instance"; 	      // instance(Concept,Instance,InstanceNameForJason)
	private final String FunctorConcept 	       = "concept"; 	      // concept(Concept,ConceptNameForJason)
	private final String FunctorObjectProperty     = "objectProperty";    // objectProperty(ObjectPropertyName,objectPropertyNameForJason)
	private final String FunctorAnnotationProperty = "annotationProperty";// annotationProperty(AnnotationProperty,annotationPropertyNameForJason)
	private final String FunctorDataProperty       = "dataProperty";      // dataProperty(DataProperty,dataPropertyNameForJason)
	private final String FunctorBuiltIn            = "builtIn";           // builtIn(BuiltIn,builtInNameForJason)
	private final String FunctorLogicalAxiom       = "logicalAxiom";      // logicalAxiom(LogicalAxiom,logicalAxiomNameForJason)
	
	private OntoQueryLayer ontoQuery;
	
    public OntoQueryLayerLiteral(OwlOntoLayer ontology) {
		this.ontoQuery = new OntoQueryLayer(ontology);
    }
    
    public OntoQueryLayer getQuery() {
    	return this.ontoQuery;
    }
    
    public static String removeAccents(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("[^\\p{ASCII}]", "");
        str = str.replaceAll("-", "_");
        return str;
    }

	public static String removeUnderscore(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = str.replaceAll("_", " ");
        return str;
    }
    
    public static String getNameForJason(String str) {
        str = removeAccents(str.substring(0,1).toLowerCase() + str.substring(1));
        return str;
    }
        
    public List<Object> getClassNames() {
		List<Object> classNames = new ArrayList<Object>();
		try {
			for (OWLClass ontoClass : this.ontoQuery.getOntology().getClasses()) {
	            Literal l = ASSyntax.createLiteral(this.FunctorConcept, ASSyntax.createString(ontoClass.getIRI().getFragment()));
				l.addTerm(ASSyntax.createAtom(getNameForJason(ontoClass.getIRI().getFragment())));
				classNames.add(l);
	        }
		}
		catch(Exception e) {
			System.out.println("failed to parse: "+e.getMessage());
		}
        return classNames;
	}
    
    public List<Object> getObjectPropertyNames() {
		List<Object> objectProperties = new ArrayList<Object>();
		try {
			for (OWLObjectProperty objectProperty : this.ontoQuery.getOntology().getObjectProperties()) {
				if (objectProperty.isOWLBottomObjectProperty()) continue;
				Literal l = ASSyntax.createLiteral(this.FunctorObjectProperty, ASSyntax.createString(objectProperty.asOWLObjectProperty().getIRI().getFragment()));
				l.addTerm(ASSyntax.createAtom(getNameForJason(objectProperty.asOWLObjectProperty().getIRI().getFragment())));
				objectProperties.add(l);
	        }
		}
		catch(Exception e) {
			System.out.println("failed to parse: "+e.getMessage());
		}
        return objectProperties;
	}

	public List<Object> getDataPropertyNames() {
		List<Object> dataProperties = new ArrayList<Object>();
		try {
	        for (OWLDataProperty dataProperty : this.ontoQuery.getOntology().getDataProperties()) {
	            if (dataProperty.isOWLBottomObjectProperty()) continue;
	            Literal l = ASSyntax.createLiteral(this.FunctorDataProperty, ASSyntax.createString(dataProperty.getIRI().getFragment()));
				l.addTerm(ASSyntax.createAtom(getNameForJason(dataProperty.getIRI().getFragment())));
				dataProperties.add(l);
	        }
		}
		catch(Exception e) {
			System.out.println("failed to parse: "+e.getMessage());
		}
        return dataProperties;
	}

	public List<Object> getAnnotationPropertyNames() {
		List<Object> annotationProperties = new ArrayList<Object>();
		try {
			for (OWLAnnotationProperty annotationProperty : this.ontoQuery.getOntology().getAnnotationProperties()) {
				if (annotationProperty.isBottomEntity()) continue;
				Literal l = ASSyntax.createLiteral(this.FunctorAnnotationProperty, ASSyntax.createString(annotationProperty.getIRI().getFragment()));
				l.addTerm(ASSyntax.createAtom(getNameForJason(annotationProperty.getIRI().getFragment())));
				annotationProperties.add(l);
	        }
		}
		catch(Exception e) {
			System.out.println("failed to parse: "+e.getMessage());
		}
        return annotationProperties;
	}
	
	public List<Object> getObjectPropertyAssertionAxioms() {
		List<Object> axioms = new ArrayList<Object>();
		for (OWLObjectPropertyAssertionAxiom axiom : this.ontoQuery.getOntology().getObjectPropertyAssertionAxioms()) {
			axioms.add(AxiomTranslator.translateObjectPropertyAssertion(axiom));
		}
		return axioms;
	}
	
	public List<Object> getDataPropertyAssertionAxioms() {
		List<Object> axioms = new ArrayList<Object>();
		for (OWLDataPropertyAssertionAxiom axiom : this.ontoQuery.getOntology().getDataPropertyAssertionAxioms()) {
			axioms.add(AxiomTranslator.translateDataPropertyAssertion(axiom));
		}
		return axioms;
	}
	
	public List<Object> getClassAssertionAxioms() {
		List<Object> axioms = new ArrayList<Object>();
		for (OWLClassAssertionAxiom axiom : this.ontoQuery.getOntology().getClassAssertionAxioms()) {
			axioms.add(AxiomTranslator.translateClassAssertion(axiom));
		}
		return axioms;
	}
	
	public List<Object> getSWRLRules() {
		List<Object> axioms = new ArrayList<Object>();
		for (OWLAxiom axiom : this.ontoQuery.getOntology().getSWRLRules()) {			
			axioms.add(AxiomTranslator.translateRule(axiom));
		}
		return axioms;
	}
	
	public List<Object> getDifferentIndividuals() {
		List<Object> axioms = new ArrayList<Object>();
		for (OWLAxiom axiom : this.ontoQuery.getOntology().getDifferentIndividuals()) {		
			axioms.addAll(AxiomTranslator.translateDifferentIndividual(axiom));
		}
		return axioms;
	}
	
	public List<Object> getLogicalAxioms() {
		System.out.println("getLogicalAxioms in OntoQueryLayerLiteral");
		List<Object> logicalAxioms = new ArrayList<Object>();
		try {
//			System.out.println("===========logicalAxiom============");
			for (OWLLogicalAxiom logicalAxiom : this.ontoQuery.getOntology().getLogicalAxioms()) {
//				System.out.println(logicalAxiom);
//				List<OWLAnnotation> annotations = new ArrayList<OWLAnnotation>(logicalAxiom.annotations().collect(Collectors.toList()));
//				System.out.println("Annotations: ");
//				for (OWLAnnotation annotation : annotations) {
//					System.out.println(annotation);
//				}
//				List<OWLClass> classes = new ArrayList<OWLClass>(logicalAxiom.classesInSignature().collect(Collectors.toList()));
//				for (OWLClass classe : classes) {
//					System.out.println(classe);
//				}
//				List<OWLObjectProperty> objectProperties = new ArrayList<OWLObjectProperty>(logicalAxiom.objectPropertiesInSignature().collect(Collectors.toList()));
//				for (OWLObjectProperty objectPropertie : objectProperties) {
//					System.out.println(objectPropertie);
//				}
//				List<OWLDataProperty> dataProperties = new ArrayList<OWLDataProperty>(logicalAxiom.dataPropertiesInSignature().collect(Collectors.toList()));
//				for (OWLDataProperty dataPropertie : dataProperties) {
//					System.out.println(dataPropertie);
//				}
				Literal l = ASSyntax.createLiteral(this.FunctorLogicalAxiom, ASSyntax.createString(logicalAxiom));
				l.addTerm(ASSyntax.createAtom(getNameForJason(logicalAxiom.toString())));
				logicalAxioms.add(l);
	        }
//			System.out.println("===================================");
		}
		catch(Exception e) {
			System.out.println("failed to parse: "+e.getMessage());
		}
        return logicalAxioms;
	}

	public List<Object> getIndividualNames(String conceptName) {
		List<Object> individuals = new ArrayList<Object>();
		try {
			Term concept = ASSyntax.createString(conceptName);
			
			for(OWLNamedIndividual individual : ontoQuery.getInstances(conceptName)){
				Literal l = ASSyntax.createLiteral(this.FunctorInstance, concept);
				l.addTerm(ASSyntax.createString(individual.getIRI().getFragment()));
				l.addTerm(ASSyntax.createAtom(getNameForJason(individual.getIRI().getFragment())));
				individuals.add(l);
			}
		}
		catch(Exception e) {
			System.out.println("failed to parse: "+e.getMessage());
		}
		return individuals;
	}
}

