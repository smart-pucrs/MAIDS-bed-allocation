// Agent ontology_specialist in project explaining_ontological_reasoning

// inverseProperties(adequado,inadequado).
// inverseProperties(preferivel,nao_aconselhavel).
// inverseProperties(preferivel,inadequado).
// inverseProperties(X,Y) :- inverseProperties(Y,X).

differentFrom(X,Y) :- X\==Y.
sameAs(X,Y) :- X==Y | X=="NONE" | Y=="NONE".
// differentFrom(X,Y) :- differentIndividuals(X,Y) | differentIndividuals(Y,X).
// differentIndividuals(X,Y) :- quarto(X) & quarto(Y) & X\==Y.
// differentIndividuals(X,Y) :- quarto(X) & quarto(Y) & X\==Y.
// differentIndividuals(X,Y) :- leito(X) & leito(Y) & X\==Y.
// differentIndividuals(X,Y) :- paciente(X) & paciente(Y) & X\==Y.

greaterThan(X,Y) :- X>Y.
lessThan(X,Y) :- X<Y.

!start.


+modifiedPatient(List)
<- !modifyPatientInBB(List).

+modifiedBed(List)
<- !modifyBedInBB(List).

+added(List)
<- !addToBB(List).

+removed(List)
<- !removeFromBB(List).

/* Plans */


+!verify(Pred,Answer)
	: Pred =..[Header,Content,X]
<-
	.nth(0,Content,Domain);
	.nth(1,Content,Range);
	!verifyBedAndPatient(Domain, Range, Answer);
	.

+!verifyBedAndPatient(Domain, Range, Answer)
	: leito(Domain) & paciente(Range)
<-
	.print("Verifying if ", adequado(Domain, Range), ".");
	!verifying(adequado(Domain, Range), Answer);
	.

+!verifyBedAndPatient(Domain, Range, Answer)
	: paciente(Range)
<-
	.print(Domain, " is not a bed.");
	Answer = withoutBed;
	.

+!verifyBedAndPatient(Domain, Range, Answer)
	: leito(Domain)
<-
	.print(Range, " is not a patient.");
	Answer = withoutPatient;
	.

+!verifyBedAndPatient(Domain, Range, Answer)
<-
	.print(Domain, " is not a bed and ", Range, " is not a patient.");
	Answer = withoutBedAndPatient;
	.

+!verifying(Pred,Answer)
	: Pred | argument(Pred,Arg) & Pred =..[Header,Content,X] & Header==adequado
<-
	Answer = adequado.

+!verifying(Pred,Answer)
	: Pred =..[Header,Content,X] & Header==adequado
<-
	.nth(0,Content,Domain);
	.nth(1,Content,Range);
	.print("Verifying if ", inadequado(Domain, Range), ".");
	!verifying(inadequado(Domain, Range), Answer).

+!verifying(Pred,Answer)
	: Pred | argument(Pred,Arg) & Pred =..[Header,Content,X] & Header==inadequado
<-
	Answer = inadequado.

+!verifying(Pred,Answer)
	: Pred =..[Header,Content,X] & Header==inadequado
<-
	.nth(0,Content,Domain);
	.nth(1,Content,Range);
	.print("Verifying if ", nao_aconselhavel(Domain, Range), ".");
	!verifying(nao_aconselhavel(Domain, Range), Answer).

+!verifying(Pred,Answer)
	: Pred | argument(Pred,Arg) & Pred =..[Header,Content,X] & Header==nao_aconselhavel
<-
	Answer = nao_aconselhavel.

+!verifying(Pred,Answer)
	: Pred =..[Header,Content,X] & Header==nao_aconselhavel
<-
	.nth(0,Content,Domain);
	.nth(1,Content,Range);
	.print("Verifying if ", preferivel(Domain, Range), ".");
	!verifying(preferivel(Domain, Range), Answer).

+!verifying(Pred,Answer)
	: Pred | argument(Pred,Arg) & Pred =..[Header,Content,X] & Header==preferivel
<-
	Answer = preferivel.

+!verifying(Pred,Answer)
<-
	.print("Could not verify.");
	Answer = cantVerify.

+!getAnswer(Pred,Answer)
<-
	.print("!getAnswer");
	!getExplanation(Pred, Explanation);
	Answer = Explanation.

+!start
	: true
<- 
	!fillTheBeliefBase;
	.print("Agent ontology_specialist enabled.")
	.


+!separateDefeasibleRule([],Temp,Df)
<-  Df=Temp.
+!separateDefeasibleRule([adequado(B,P)[S]|R],Temp,Df)
<-  .concat(Temp,[adequado(B,P)[S]],T);
	!separateDefeasibleRule(R,T,Df).
+!separateDefeasibleRule([defeasible_rule(H,B)[S]|R],Temp,Df)
<-  .concat(Temp,[defeasible_rule(H,B)[S]],T);
	!separateDefeasibleRule(R,T,Df).
+!separateDefeasibleRule([Pred|R],Temp,Df)
<-  !separateDefeasibleRule(R,Temp,Df).


+!getExplanation(Pred, Explanation)
	: Pred =..[Header,Content,X] & Header==inadequado
<-
	!getExplanationInadequado(Pred, Explanation);
	.

+!getExplanation(Pred, Explanation)
	: Pred =..[Header,Content,X] & Header==nao_aconselhavel
<-
	!getExplanationNaoAconselhavel(Pred, Explanation);
	.

+!getExplanation(Pred, Explanation)
	: Pred =..[Header,Content,X] & Header==adequado
<-
	!getExplanationAdequado(Pred, Explanation);
	.

+!getExplanation(Pred, Explanation)
	: Pred =..[Header,Content,X] & Header==preferivel
<-
	!getExplanationPreferivel(Pred, Explanation);
	.

+!getExplanationAdequado(Pred, Explanation)
 	: argument(Pred,Arg)
 <-	
	.print("Found argument for: ", Pred);
	!separateDefeasibleRule(Arg,[],Df);
 	Explanation=Df;
 	.

+!getExplanationAdequado(Pred, Explanation)
	: Pred =..[Header,Content,X]
<-
	.nth(0,Content,Domain);
	.nth(1,Content,Range);
	.print("Explanation for ", Pred, " not found.");
	!getExplanation(inadequado(Domain, Range), Explanation);
	.

+!getExplanationInadequado(Pred, Explanation)
 	: argument(Pred,Arg)
 <-	
	.print("Found argument for: ", Pred);
	!separateDefeasibleRule(Arg,[],Df);
 	Explanation=Df;
 	.

+!getExplanationInadequado(Pred, Explanation)
	: Pred =..[Header,Content,X]
<-
	.nth(0,Content,Domain);
	.nth(1,Content,Range);
	.print("Explanation for ", Pred, " not found.");
	!getExplanation(nao_aconselhavel(Domain, Range), Explanation);
	.

+!getExplanationNaoAconselhavel(Pred, Explanation)
 	: argument(Pred,Arg)
 <-	
	.print("Found argument for: ", Pred);
	!separateDefeasibleRule(Arg,[],Df);
 	Explanation=Df;
 	.

+!getExplanationNaoAconselhavel(Pred, Explanation)
	: Pred =..[Header,Content,X]
<-
	.nth(0,Content,Domain);
	.nth(1,Content,Range);
	.print("Explanation for ", Pred, " not found.");
	!getExplanation(preferivel(Domain, Range), Explanation);
	.

+!getExplanationPreferivel(Pred, Explanation)
 	: argument(Pred,Arg)
 <-	
	.print("Found argument for: ", Pred);
	!separateDefeasibleRule(Arg,[],Df);
 	Explanation=Df;
 	.

+!getExplanationPreferivel(Pred, Explanation)	
<-	.print("Explanation for ", Pred, " not found.");
 	Explanation=cantExplain;
 	.

	
// +!getExplanation(Pred, Explanation)
//  <-	
//  	.print("Doesn't found argument for: ", Pred);
// 	!getInverseExplanation(Pred, Explanation)
//  	.
// +!getInverseExplanation(Pred, Explanation)
// 	: Pred =..[Header,Content,X] & Header==inadequado
// <-
// 	.nth(0,Content,Domain);
// 	.nth(1,Content,Range);
// 	.print("Get the inverse explanation for ", Pred);
// 	!getExplanation(adequado(Domain, Range), Explanation);
// 	.
// +!getInverseExplanation(Pred, Explanation)
// 	: Pred =..[Header,Content,X] & Header==adequado
// <-
// 	.nth(0,Content,Domain);
// 	.nth(1,Content,Range);
// 	.print("Get the inverse explanation for ", Pred);
// 	!getExplanation(inadequado(Domain, Range), Explanation);
// 	.
// +!getInverseExplanation(Pred, Explanation)
// <-	.print("Explanation=~Pred");
// 	Explanation=~Pred;
// 	.	
	
+!instantiateArgumentScheme(Pred,explanationTerms("empy"),Explanation)
<-
	Explanation = "Empty explanation.".
+!instantiateArgumentScheme(Pred,explanationTerms(rules(RulesList),assertions(AssertionsList),classInfo(ClassInfoList)),Explanation)
<-
	.concat([Pred],AssertionsList,Assertions);
//	.print("Instantiate argument schemes");
	!instantiateArgumentScheme(RulesList,Assertions,Explanation);
	.
+!instantiateArgumentScheme(RulesList,AssertionsList,Explanation)
<-
	jia.unifyRule(RulesList,AssertionsList,Explanation);
	.

+!addToBB(explanationTerms("empy"))
<-
	.print("Empty explanation.").
+!addToBB(explanationTerms(rules(RulesList),assertions(AssertionsList),classInfo(ClassInfoList)))
<-
	!addToBB(RulesList);
	!addToBB(AssertionsList);
	!addToBB(ClassInfoList);
	.
+!addToBB([]).
+!addToBB([H|T])
<-
	+H;
	!addToBB(T);
	.

+!printArray([]).
+!printArray([H|T])
<-
	.print(H);
	!printArray(T);
	.
	
+!fillTheBeliefBase
<- 
	// getClassNames(ClassNames);
	// !addToTheBeliefBase(ClassNames);
	// getObjectPropertyNames(ObjectPropertyNames);
	// !addToTheBeliefBase(ObjectPropertyNames);
	// getObjectPropertyAssertions(Assertions);
	// !addToTheBeliefBase(Assertions);
	// getDataPropertyAssertions(DPAssertions);
	// !addToTheBeliefBase(DPAssertions);
	// getClassAssertions(ClassAssertions);
	// !addToTheBeliefBase(ClassAssertions);
	getSWRLRules(SWRLRules);
	!addToTheBeliefBase(SWRLRules);
	// getDifferentIndividuals(DiffIndividuals);
	// !addToTheBeliefBase(DiffIndividuals);
	!getDatabaseData;
	.

+!getDatabaseData
<-
	// getCaracteristicas(C);
	// !addToTheBeliefBase(C);
	.print("Iniciando busca no banco de dados")
	// getPatientsData(PatientsData);
	// !addToTheBeliefBase(PatientsData);
	// getBedsData(BedsData);
	// !addToTheBeliefBase(BedsData);
	// getPedidosLeitoData(PedidosLeitoData);
	// !addToTheBeliefBase(PedidosLeitoData);
	getBedsDataByRoom(BedsDataByRoom);
	!addToTheBeliefBase(BedsDataByRoom);
	// getInfeccoesData(InfeccoesData);
	// !addToTheBeliefBase(InfeccoesData);
	.print("Busca no banco de dados finalizada");
	.

+!addToTheBeliefBase([]).	
+!addToTheBeliefBase([H|T])
<-
	+H;
	!addToTheBeliefBase(T)
	.

+!isRelated(Pred,IsRelated)
	:  Pred =..[Header,Content,X] & objectProperty(OpString,Header)
<-
	.nth(0,Content,Domain);
	.nth(1,Content,Range);
	isRelated(Domain,OpString,Range,IsRelated);
//	.print(Pred, " = ", IsRelated);
	.

+!isRelated(Domain, Property, Range, IsRelated)
 	: objectProperty(PropertyName,Property)
<-
	isRelated(Domain, PropertyName, Range, IsRelated);
	.print("Domain: ", Domain, " PropertyName: ", PropertyName, " Range: ", Range, " IsRelated: ", IsRelated);
	.
	
+!addInstance(InstanceName, Concept)
	: concept(ClassName,Concept)
<- 
	.print("Adding a new ", ClassName, " named ", InstanceName);
	addInstance(InstanceName, ClassName);
	!getInstances(Concept, Instances);
	!print("Instances", Instances);
	.

+!isInstanceOf(InstanceName, Concept, Result)
	: concept(ClassName,Concept)
<- 
	.print("Checking if ", InstanceName, " is an instance of ", ClassName);
	isInstanceOf(InstanceName, ClassName, Result);
	.print("The result is ", Result);
	!getInstances(Concept, Instances);
	!print("Instances", Instances);
	.

+!getInstances(Concept, Instances)
	: concept(ClassName,Concept)
<-
	.print("Getting instances of ", ClassName);
	getInstances(ClassName, Instances);
	.
	
+!getObjectPropertyValues(Domain, Property, Range)
 	: objectProperty(PropertyName,Property)
<-
	getObjectPropertyValues(Domain, PropertyName, Range);
	.print("Domain: ", Domain, " PropertyName: ", PropertyName, " Range: ", Range);
	.



// Modify in BB *********************************************

+!modifyBedInBB([]).
+!modifyBedInBB([H|T])
	: H
<-	!modifyBedInBB(T).

+!modifyBedInBB([possui_situacao(Bed,Data)|T])
	: possui_situacao(Bed,_)
<-	.abolish(possui_situacao(Bed,_));
	+possui_situacao(Bed,Data);
	!modifyBedInBB(T).

+!modifyBedInBB([e_ocupado_por(Bed,Data)|T])
	: e_ocupado_por(Bed,_)
<-	.abolish(e_ocupado_por(Bed,_));
	+e_ocupado_por(Bed,Data);
	!modifyBedInBB(T).

+!modifyBedInBB([esta_em(Bed,Data)|T])
	: esta_em(Bed,_)
<-	.abolish(esta_em(Bed,_));
	+esta_em(Bed,Data);
	!modifyBedInBB(T).

+!modifyBedInBB([leito_e_de_cuidados(Bed,Data)|T])
	: leito_e_de_cuidados(Bed,_)
<-	.abolish(leito_e_de_cuidados(Bed,_));
	+leito_e_de_cuidados(Bed,Data);
	!modifyBedInBB(T).

+!modifyBedInBB([leito_e_da_faixa_etaria(Bed,Data)|T])
	: leito_e_da_faixa_etaria(Bed,_)
<-	.abolish(leito_e_da_faixa_etaria(Bed,_));
	+leito_e_da_faixa_etaria(Bed,Data);
	!modifyBedInBB(T).

+!modifyBedInBB([leito_e_da_especialidade(Bed,Data)|T])
	: leito_e_da_especialidade(Bed,_)
<-	.abolish(leito_e_da_especialidade(Bed,_));
	+leito_e_da_especialidade(Bed,Data);
	!modifyBedInBB(T).
	
+!modifyBedInBB([leito_e_de_encaminhamento(Bed,Data)|T])
	: leito_e_de_encaminhamento(Bed,_)
<-	.abolish(leito_e_de_encaminhamento(Bed,_));
	+leito_e_de_encaminhamento(Bed,Data);
	!modifyBedInBB(T).
	
+!modifyBedInBB([leito_e_de_isolamento(Bed,Data)|T])
	: leito_e_de_isolamento(Bed,_)
<-	.abolish(leito_e_de_isolamento(Bed,_));
	+leito_e_de_isolamento(Bed,Data);
	!modifyBedInBB(T).

+!modifyBedInBB([leito_e_de_permanecia(Bed,Data)|T])
	: leito_e_de_permanecia(Bed,_)
<-	.abolish(leito_e_de_permanecia(Bed,_));
	+leito_e_de_permanecia(Bed,Data);
	!modifyBedInBB(T).
	
+!modifyBedInBB([leito_e_do_genero(Bed,Data)|T])
	: leito_e_do_genero(Bed,_)
<-	.abolish(leito_e_do_genero(Bed,_));
	+leito_e_do_genero(Bed,Data);
	!modifyBedInBB(T).
	
+!modifyBedInBB([leito_e_de_atendimento(Bed,Data)|T])
	: leito_e_de_atendimento(Bed,_)
<-	.abolish(leito_e_de_atendimento(Bed,_));
	+leito_e_de_atendimento(Bed,Data);
	!modifyBedInBB(T).
	
+!modifyBedInBB([leito_e_puerperio(Bed,Data)|T])
	: leito_e_puerperio(Bed,_)
<-	.abolish(leito_e_puerperio(Bed,_));
	+leito_e_puerperio(Bed,Data);
	!modifyBedInBB(T).
	
+!modifyBedInBB([H|T])
<-	+H
	!modifyBedInBB(T).


+!modifyPatientInBB([]).
+!modifyPatientInBB([H|T])
	: H
<-	!modifyPatientInBB(T).
	
+!modifyPatientInBB([e_da_faixa_etaria(Patient,Data)|T])
	: e_da_faixa_etaria(Patient,_)
<-	.abolish(e_da_faixa_etaria(Patient,_));
	+e_da_faixa_etaria(Patient,Data);
	!modifyPatientInBB(T).

+!modifyPatientInBB([e_da_especialidade(Patient,Data)|T])
	: e_da_especialidade(Patient,_)
<-	.abolish(e_da_especialidade(Patient,_));
	+e_da_especialidade(Patient,Data);
	!modifyPatientInBB(T).
	
+!modifyPatientInBB([e_do_genero(Patient,Data)|T])
	: e_do_genero(Patient,_)
<-	.abolish(e_do_genero(Patient,_));
	+e_do_genero(Patient,Data);
	!modifyPatientInBB(T).
	
+!modifyPatientInBB([laudo_id(Patient,Data)|T])
	: laudo_id(Patient,_)
<-	.abolish(laudo_id(Patient,_));
	+laudo_id(Patient,Data);
	!modifyPatientInBB(T).

+!modifyPatientInBB([paciente_id(Patient,Data)|T])
	: paciente_id(Patient,_)
<-	.abolish(paciente_id(Patient,_));
	+paciente_id(Patient,Data);
	!modifyPatientInBB(T).

+!modifyPatientInBB([prontuario_id(Patient,Data)|T])
	: prontuario_id(Patient,_)
<-	.abolish(prontuario_id(Patient,_));
	+prontuario_id(Patient,Data);
	!modifyPatientInBB(T).

+!modifyPatientInBB([e_de_cuidados(Patient,Data)|T])
	: e_de_cuidados(Patient,_)
<-	.abolish(e_de_cuidados(Patient,_));
	+e_de_cuidados(Patient,Data);
	!modifyPatientInBB(T).

+!modifyPatientInBB([e_de_encaminhamento(Patient,Data)|T])
	: e_de_encaminhamento(Patient,_)
<-	.abolish(e_de_encaminhamento(Patient,_));
	+e_de_encaminhamento(Patient,Data);
	!modifyPatientInBB(T).

+!modifyPatientInBB([e_de_permanecia(Patient,Data)|T])
	:e_de_permanecia(Patient,_)
<-	.abolish(e_de_permanecia(Patient,_));
	+e_de_permanecia(Patient,Data);
	!modifyPatientInBB(T).

+!modifyPatientInBB([e_de_atendimento(Patient,Data)|T])
	: e_de_atendimento(Patient,_)
<-	.abolish(e_de_atendimento(Patient,_));
	+e_de_atendimento(Patient,Data);
	!modifyPatientInBB(T).
	
+!modifyPatientInBB([e_puerperio(Patient,Data)|T])
	: e_puerperio(Patient,_)
<-	.abolish(e_puerperio(Patient,_));
	+e_puerperio(Patient,Data);
	!modifyPatientInBB(T).
	
+!modifyPatientInBB([ocupa_um(Patient,Data)|T])
	: ocupa_um(Patient,_)
<-	.abolish(ocupa_um(Patient,_));
	+ocupa_um(Patient,Data);
	!modifyPatientInBB(T).

+!modifyPatientInBB([is_internado(Patient,"false")|T])
	: ocupa_um(Patient,_)
<-	.abolish(ocupa_um(Patient,_));
	.abolish(is_internado(Patient,_));
	+is_internado(Patient,Data);
	!modifyPatientInBB(T).	

+!modifyPatientInBB([is_internado(Patient,Data)|T])
	: is_internado(Patient,_)
<-	.abolish(is_internado(Patient,_));
	+is_internado(Patient,Data);
	!modifyPatientInBB(T).

+!modifyPatientInBB([e_de_isolamento(Patient,Data)|T])
	: e_de_isolamento(Patient,_)
<-	.abolish(e_de_isolamento(Patient,_));
	+e_de_isolamento(Patient,Data);
	!modifyPatientInBB(T).
	
+!modifyPatientInBB([H|T])
<-	+H;
	!modifyPatientInBB(T).

	
+!removeFromBB([]).	
+!removeFromBB([H|T])
<-	-H;
	!removeFromBB(T).
// **********************************************************************


+!kqml_received(Sender,question,nurse_exception(B,Patient),MsgId)
	: leito(B) & paciente(Patient)
	<-	.print("Registering exception for bed ", B, " and patient ", Patient);
		.concat("nurse_exception_",Patient,Schema);
		+adequado(B,Patient)[as(Schema)];
		.send(Sender,assert,nurse_exception(B,Patient)[registered]).

+!kqml_received(Sender,question,nurse_exception(B,Patient),MsgId)
	: leito(B) 
	<-	.print(Patient, " is not a patient");
		.send(Sender,assert,nurse_exception(B,Patient)[withoutPatient]).

+!kqml_received(Sender,question,nurse_exception(B,Patient),MsgId)
	: paciente(Patient)
	<-	.print(B, " is not a bed");
		.send(Sender,assert,nurse_exception(B,Patient)[withoutBed]).

+!kqml_received(Sender,question,nurse_exception(B,Patient),MsgId)
	<-	.print(B, " is not a bed and ", Patient, " is not a patient");
		.send(Sender,assert,nurse_exception(B,Patient)[withoutBedAndPatient]).

+!kqml_received(Sender,question,explain(adequado(B,P)),MsgId)
	<-	.print("Received explain request for explaning the predicate: ", adequado(B,P));
		!getAnswer(adequado(B,P),Explanation);	
		.send(Sender,assert,explain(adequado(B,P))[Explanation]).
	
+!kqml_received(Sender,question,explain(inadequado(B,P)),MsgId)
	<-	.print("Received explain request for explaning the predicate: ", inadequado(B,P));
		!getAnswer(adequado(B,P),Explanation);	
		.send(Sender,assert,explain(inadequado(B,P))[Explanation]).

+!kqml_received(Sender,question,Pred,MsgId)
	<-	!verify(Pred,Answer);
		.send(Sender,assert,Pred[Answer]).

/**
 * Violation
 */
+!kqml_received(monitor,assert,Violation,MsgId)
	<-	.print("Agent monitor reported a violation.");
		!verifyViolation(Violation).
		
+!verifyViolation(V).	
		
{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("reasoning/abr_in_aopl_with_as_v2.asl")}

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
