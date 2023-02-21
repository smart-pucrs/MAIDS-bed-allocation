!start.

/* Plans */

+!start : true <- .print("Assistant agent enabled.").//; !getOptimisedAllocation.
	
/*
 * Suggestion
 */
 
+!analiseSuggestion(optimiserResult(IsAllAllocated,NotAlloc,sugestedAllocation(SugestedAllocation)), Response)
	:IsAllAllocated == "true"
<-	!getSugestedAllocation(SugestedAllocation, Temp);
	.concat("Eu posso sugerir colocar ", Temp, " Você gostaria que eu confirmasse essa alocação?", Response).	
+!analiseSuggestion(optimiserResult(IsAllAllocated,NotAlloc,sugestedAllocation(SugestedAllocation)), Response)
	: .length(SugestedAllocation,X) & X>0 & .length(NotAlloc,Y) & Y>0 
<-	!getSugestedAllocation(SugestedAllocation, Temp);
	.concat("Eu não tenho sugestões para todos os pacientes que você pediu, mas posso sugerir colocar ", Temp, "Você gostaria que eu confirmasse essa alocação?", Response).	
+!analiseSuggestion(optimiserResult(IsAllAllocated,NotAlloc,sugestedAllocation(SugestedAllocation)), Response)
	: .length(SugestedAllocation,X) & X==0 & .length(NotAlloc,Y) & Y>1 
<-	.concat("Desculpe-me, eu não tenho sugestões de alocação para esses pacientes.", Response).	
+!analiseSuggestion(optimiserResult(IsAllAllocated,NotAlloc,sugestedAllocation(SugestedAllocation)), Response)
<-	.concat("Desculpe-me, eu não tenho uma sugestão de alocação para esse paciente já que nenhum leito livre respeita todas as regras de alocação, a não ser que você peça para a enfermeira informar uma exceção.", Response).	
	

+!getSugestedAllocation(SugestedAllocation, Resp)
	: .length(SugestedAllocation,X) & X==1
<- 	!getSugestedAllocation1(SugestedAllocation, Resp).

+!getSugestedAllocation(SugestedAllocation, Resp)
<- 	!getSugestedAllocationMoreThan1(SugestedAllocation, "", Resp).

+!getSugestedAllocation1([alloc(PacienteName, NumLeito)], Resp)
<- 	.concat(" o/a paciente ", PacienteName, " no leito ",NumLeito,".", Resp).
	
+!getSugestedAllocationMoreThan1([alloc(PacienteName, NumLeito)|[]], Temp, Resp)
<- .concat(Temp, " e o/a paciente ", PacienteName, " no leito ",NumLeito,".", Resp).
+!getSugestedAllocationMoreThan1([alloc(PacienteName, NumLeito)|R], Temp, Resp)
<- .concat(Temp, " o/a paciente ", PacienteName, " no leito ",NumLeito,",", T)
	!getSugestedAllocationMoreThan1(R,T,Resp).
	
/* 
 * Optimisation Result
 */
	
+!analiseOptimisation(optimiserResult(IsAllAllocated,notAlloc(NotAllocList),sugestedAllocation(SugestionList)), Response)
	: IsAllAllocated == "true" & url(URL) & .length(SugestionList,X) & X>0
<-
	.concat("Gerei uma alocação otimizada mantendo o maior número possível de quartos livres e deixando os pacientes mais graves próximos da sala de enfermagem. Você pode vê-la no menu 'Alocação otimizada' aqui ao lado. Você quer que eu confirme essa alocação?", Response);
	.
	
+!analiseOptimisation(optimiserResult(IsAllAllocated,notAlloc(NotAllocList),sugestedAllocation(SugestionList)), Response)
	: IsAllAllocated == "false" & url(URL) & .length(SugestionList,X) & X>0
<-
	.concat("Gerei uma alocação otimizada, porém não conseguirei alocar todos os pacientes ", Temp);
	!getNotAlloc(NotAllocList, Result);
	.concat(Temp, Result, "Você pode ver minha sugestão no menu 'Alocação otimizada' aqui ao lado. Você quer que eu confirme essa alocação?", Response);
//		nesse endereço: ", URL, " Você quer que eu confirme essa alocação?", Response
//	);
	.
	
+!analiseOptimisation(optimiserResult(IsAllAllocated,notAlloc(NotAllocList),sugestedAllocation(SugestionList)), Response)
	: .length(SugestionList,X) & X==0
<-
	.concat("Desculpe-me, mas com os dados disponíveis atualmente, não foi possível gerar uma alocação otimizada.", Response);
	.
	
+!getNotAlloc(NotAllocList, Result)
	: .length(NotAllocList,X) & X==1
<-
	.concat("pois não localizei leito adequado para o paciente ", Temp);
	!getNotAllocName(NotAllocList, Resp);
	.concat(Temp, Resp, Result);
	.
+!getNotAlloc(NotAllocList, Result)
	: .length(NotAllocList,X) & X>1
<-
	.concat("pois não localizei leitos adequados para os pacientes ", Temp);
	!getNotAllocNames(NotAllocList, "", Resp);
	.concat(Temp, Resp, Result);
	.
+!getNotAllocName([Patient|[]], Resp)
<-
	.concat(Patient, ". ", Resp);
	.
	
+!getNotAllocNames([Patient|[]], Temp, Result)
<-
	.concat(Temp, "e ", Patient, ". ", Result);
	.
+!getNotAllocNames([Patient|RestOfTheList], Temp, Result)
<-
	.concat(Temp, Patient, ", ", T);
	!getNotAllocNames(RestOfTheList, T, Result);
	.

+!translateToNaturalLanguage([],Temp,NLExplanation)
<- 
	NLExplanation=Temp.
+!translateToNaturalLanguage([Rule|List],Temp,NLExplanation)
<-
	!translate(Rule, RuleTranslated);
	.concat(RuleTranslated,Temp,NewTemp);
	!translateToNaturalLanguage(List,NewTemp,NLExplanation).






/**
 * Exceptions
 */
 
 
+!analiseException([], Response) // [nurse_exception(Patient,Exception)]
<-	.concat("Não localizei nenhuma exceção para esse/a paciente",Response).

+!analiseException([nurse_exception(Patient,"None")], Response)
<-	.concat("Não localizei nenhuma exceção para o/a paciente ",Patient,Response).

+!analiseException([nurse_exception(Patient,Exception)], Response)
<-	.concat("Sim, exceção de ",Exception," registrada para o/a paciente ",Patient,Response).

+!analiseException(Exceptions, Response)
	: .length(Exceptions,X) & X>1
<-	!analiseException(Exceptions, "", Resp)
	.concat("Sim, excessões de ",Resp,Response).

+!analiseException([nurse_exception(Patient,Exception)], Temp, Response)
<-	.concat(Temp, "e ",Exception," registradas para o/a paciente ",Patient,Response).
+!analiseException([nurse_exception(Patient,Exception)|R], Temp, Response)
<-	.concat(Temp, Exception,", ",T)
	!analiseException(R, T, Response).



+!printList([]).
+!printList([H|T])
<-
	.print(H);
	!printList(T);
	.

/* 
 * Kqml Plans
 */


+!kqml_received(operator,question,alocar(B,Patient)[anyway],MsgId)
	<-	.print("Agent operator asks to allocate a patient to a bed anyway");
		.send(operator,assert,answer(askPermission(alocar(B,Patient)[anyway]), "Desculpe, você não tem autorização para efetuar essa alocação, mas você pode pedir para a gestora abrir uma exceção")).
+!kqml_received(nurse,question,alocar(B,Patient)[anyway],MsgId)
	<-	.print("Agent nurse asks to allocate a patient to a bed anyway");
		.send(nurse,assert,answer(havePermission(alocar(B,Patient)[anyway]), "Ok, estou alocando conforme solicitado")).
+!kqml_received(Sender,question,alocar(B,Patient),MsgId)
	<-	.print("Agent ", Sender, " asks to allocate patient ", Patient, " to bed ", B);
		.send(Sender,question,answer(verifySuitability(B,Patient)[first], "Antes eu preciso verificar se esse leito é adequado para esse paciente, pode ser?")).
+!kqml_received(nurse,question,exception(B,Patient),MsgId)
	<-	.print("Agent nurse asks to register exception on patient ", Patient, " and bed ", B);
		.send(ontology_specialist,question,nurse_exception(B,Patient)).
		
+!kqml_received(ontology_specialist,assert,nurse_exception(B,Patient)[registered],MsgId)
	<-	.print("Agent ontology_specialist informs that the exception on patient ", Patient, " and bed ", B, " was registered.");
		.send(nurse,assert,answer(nurse_exception(B,Patient)[registered], "Ok, estou registrando a exceção solicitada")).
+!kqml_received(ontology_specialist,assert,nurse_exception(B,Patient)[withoutPatient],MsgId)
	<-	.print("Agent ontology_specialist informs that ", Patient, " is not a patient.");
		.send(nurse,assert,answer(nurse_exception(B,Patient)[withoutPatient], "Me desculpe, não localizei nenhum paciente com esse código")).
+!kqml_received(ontology_specialist,assert,nurse_exception(B,Patient)[withoutBed],MsgId)
	<-	.print("Agent ontology_specialist informs that ", B, " is not a bed.");
		.send(nurse,assert,answer(nurse_exception(B,Patient)[withoutBed], "Me desculpe, não localizei nenhum leito com esse número")).
+!kqml_received(ontology_specialist,assert,nurse_exception(B,Patient)[withoutBedAndPatient],MsgId)
	<-	.print("Agent ontology_specialist informs that ", Patient, " is not a patient and ", B, " is not a bed.");
		.send(nurse,assert,answer(nurse_exception(B,Patient)[withoutBedAndPatient], "Me desculpe, não localizei nenhum leito com esse número e nenhum paciente com esse código")).
/**
 * Superlative Info
 */

+!kqml_received(Sender,question,smallestRoom[fewest_occupants],MsgId)
	<-	.print("Agent ", Sender, " asks about the smallest room with fewest occupants.");
		.send(database,question,smallestRoom[fewest_occupants]).

+!kqml_received(Sender,question,biggestRoom[fewest_occupants],MsgId)
	<-	.print("Agent ", Sender, " asks about the biggest room with fewest occupants.");
		.send(database,question,biggestRoom[fewest_occupants]).

+!kqml_received(Sender,question,biggestRoom[most_occupants],MsgId)
	<-	.print("Agent ", Sender, " asks about the biggest room with most occupants.");
		.send(database,question,biggestRoom[most_occupants]).

+!kqml_received(Sender,question,smallestRoom[most_occupants],MsgId)
	<-	.print("Agent ", Sender, " asks about the smallest room with most occupants.");
		.send(database,question,smallestRoom[most_occupants]).

+!kqml_received(Sender,assert,smallestRoom(Room)[fewest_occupants(Room)],MsgId)
	<-	.print("Agent ", Sender, " answer about the smallest room with fewest occupants.");
		.concat("O menor quarto com o menor número de ocupantes é o ", Room, Resp);
		.send(operator,assert,answer(smallestRoom(Room)[fewest_occupants(Room)], Resp)).

+!kqml_received(Sender,assert,biggestRoom(Room)[fewest_occupants(Room)],MsgId)
	<-	.print("Agent ", Sender, " answer about the biggest room with fewest occupants.");
		.concat("O maior quarto com o menor número de ocupantes é o ", Room, Resp);
		.send(operator,assert,answer(biggestRoom(Room)[fewest_occupants(Room)], Resp)).

+!kqml_received(Sender,assert,biggestRoom(Room)[most_occupants(Room)],MsgId)
	<-	.print("Agent ", Sender, " answer about the biggest room with most occupants.");
		.concat("O maior quarto com o maior número de ocupantes é o ", Room, Resp);
		.send(operator,assert,answer(biggestRoom(Room)[most_occupants(Room)], Resp)).

+!kqml_received(Sender,assert,smallestRoom(Room)[most_occupants(Room)],MsgId)
	<-	.print("Agent ", Sender, " answer about the smallest room with most occupants.");
		.concat("O menor quarto com o maior número de ocupantes é o ", Room, Resp);
		.send(operator,assert,answer(smallestRoom(Room)[most_occupants(Room)], Resp)).

/**
 * Get bed Info
 */

+!kqml_received(Sender,question,getBed[leito_e_do_genero(Bed,Genero),leito_e_da_faixa_etaria(Bed,FaixaEtaria),leito_e_do_tipo_especialidade(Bed,TipEsp),e_de_acomodacao(Bed,Acomodacao),possui_status(Bed,Status)],MsgId)
	<- .print("Agent ", Sender, " asks about a bed ",leito_e_do_genero(Bed,Genero),leito_e_da_faixa_etaria(Bed,FaixaEtaria),leito_e_do_tipo_especialidade(Bed,TipEsp),e_de_acomodacao(Bed,Acomodacao),possui_status(Bed,Status));
		.send(database,question,getBed[leito_e_do_genero(Bed,Genero),leito_e_da_faixa_etaria(Bed,FaixaEtaria),leito_e_do_tipo_especialidade(Bed,TipEsp),e_de_acomodacao(Bed,Acomodacao),possui_status(Bed,Status)]).

+!kqml_received(Sender,assert,getBed("NONE"),MsgId)
	<- .print("Agent ", Sender, " answered about the bed.");
		.send(operator,assert,answer(getBed("NONE"), "Não localizei nenhum leito com essas características")).

+!kqml_received(Sender,assert,getBed(Bed),MsgId)
	<- .print("Agent ", Sender, " answered about the bed.");
		.concat("Localizei o leito ", Bed, " que se encaixa na sua solicitação. Você gostaria de alocar um paciente nele?", Resp);
		.send(operator,question,answer(getBed(Bed)[des(operator,alocar(Bed,Patient))], Resp)).

/**
 * Optimization
 */

 +!kqml_received(Sender,question,getOptimisedAllocationMoving(NumPatients),MsgId)
	<-	.print("Agent ", Sender, " requesting an optmised allocation.");
		.send(optimiser,question,getOptimisedAllocationMoving(NumPatients)).

+!kqml_received(Sender,question,confirmAllocByOptimizationMoving,MsgId)
	<-	.print("Agent ", Sender, " requesting confirmation of the optmised allocation.");
		.send(optimiser,question,allocOpPatientsMoving).		
		
+!kqml_received(Sender,question,allocOpPatientsMovingExcept(Params),MsgId)
	<-	.print("Agent ", Sender, " requesting confirmation of allocation with exceptions.");
		.send(optimiser,question,allocByOptimizerMovingWithExceptions(Params)).

+!kqml_received(Sender,assert,allocatedOpPatientsMoving("Erro"),MsgId)
	<-	.send(operator,assert,answer(allocatedOpPatientsMoving("Erro"),"Desculpe, houve um erro e não foi possível concluir a alocação conforme solicitado")).

+!kqml_received(Sender,assert,allocatedOpPatientsMoving("Success"),MsgId)
	<-	.send(operator,assert,answer(allocatedOpPatientsMoving("Success"),"Ok, pacientes alocados conforme solicitado")).

+!kqml_received(Sender,assert,allocatedOpPatientsMoving("None"),MsgId)
	<-	.send(operator,assert,answer(allocatedOpPatientsMoving("None"),"Considerando as excessões não restaram pacientes para serem alocados")).

+!kqml_received(Sender,question,getOptimisedAllocation,MsgId)
	<-	.print("Agent ", Sender, " requesting an optmised allocation.");
		.send(optimiser,question,getOptimisedAllocation).

+!kqml_received(optimiser,assert,optimiserResult(IsAllAllocated,NotAlloc,SugestedAllocation),MsgId)
	<-	.print("Result received from agent optimiser"); // optimiserResult(IsAllAllocated,notAlloc([PacienteName]), sugestedAllocation([alloc(PacienteName, NumLeito)])) -> where IsAllAllocated is boolean
		!analiseOptimisation(optimiserResult(IsAllAllocated,NotAlloc,SugestedAllocation), Response);
		.send(operator,assert,answer(optimiserResult(IsAllAllocated,NotAlloc,SugestedAllocation),Response)).

+!kqml_received(Sender,question,cancelAllocByOptimization,MsgId)
	<-	.print("Agent ", Sender, " requesting cancellation of the optmised allocation.");
		.send(optimiser,question,cancelOpAlloc).

+!kqml_received(Sender,assert,cancelOpAllocation("Erro"),MsgId)
	<-	.print("Agent ", Sender, " is answering about the cancellation of the optmised allocation.");
		.send(operator,assert,answer(cancelOpAllocation("Erro"),"Desculpe, houve um erro e não foi possível concluir a solicitação")).		

+!kqml_received(Sender,assert,cancelOpAllocation("Success"),MsgId)
	<-	.print("Agent ", Sender, " is answering about the cancellation of the optmised allocation.");
		.send(operator,assert,answer(cancelOpAllocation("Success"),"Ok, solicitação concluida sem alocar nenhum paciente.")).	

+!kqml_received(Sender,question,confirmAllocByOptimization,MsgId)
	<-	.print("Agent ", Sender, " requesting confirmation of the optmised allocation.");
		.send(optimiser,question,allocOpPatients).

+!kqml_received(Sender,assert,allocatedOpPatients("Error"),MsgId)
	<-	.print("Agent ", Sender, " is answering about the confirmation of the optmised allocation.");
		.send(operator,assert,answer(allocatedOpPatients("Error"),"Desculpe, houve um erro e não foi possível concluir a alocação conforme solicitado.")).	

+!kqml_received(Sender,assert,allocatedOpPatients("Success"),MsgId)
	<-	.print("Agent ", Sender, " is answering about the cancellation of the optmised allocation.");
		.send(operator,assert,answer(allocatedOpPatients("Success"),"Ok, Pacientes alocados conforme solicitado.")).	
	
+!kqml_received(Sender,question,allocOpPatientsExcept(Params),MsgId)
	<-	.print("Agent ", Sender, " requesting confirmation of allocation with exceptions.");
		.send(optimiser,question,allocByOptimizerWithExceptions(Params)).

+!kqml_received(Sender,assert,allocatedOpPatientsExcept("Erro"),MsgId)
	<-	.send(operator,assert,answer(allocatedOpPatientsExcept("Erro"),"Desculpe, houve um erro e não foi possível concluir a alocação conforme solicitado")).

+!kqml_received(Sender,assert,allocatedOpPatientsExcept("Success"),MsgId)
	<-	.send(operator,assert,answer(allocatedOpPatientsExcept("Success"),"Ok, pacientes alocados conforme solicitado")).

+!kqml_received(Sender,assert,allocatedOpPatientsExcept("None"),MsgId)
	<-	.send(operator,assert,answer(allocatedOpPatientsExcept("None"),"Considerando as excessões não restaram pacientes para serem alocados")).

+!kqml_received(Sender,question,getSuggestionByPatient([]),MsgId)
	<-	.print("Agent ", Sender, " requesting suggestion without informing patients.");
		.send(operator,assert,answer(getSuggestionByPatient([]),"Desculpe, não consegui entender para quais pacientes você quer que eu faça sugestão de leitos")).

+!kqml_received(Sender,question,getSuggestionByPatient(Params),MsgId)
	<-	.print("Agent ", Sender, " requesting suggestion.");
		.send(optimiser,question,getSuggestionByPatient(Params)).
			
+!kqml_received(optimiser,assert,suggestionByPatient(optimiserResult(IsAllAllocated,NotAlloc,SugestedAllocation)),MsgId)
	<-	.print("Result received from agent optimiser"); // optimiserResult(IsAllAllocated,notAlloc([PacienteName]), sugestedAllocation([alloc(PacienteName, NumLeito)])) -> where IsAllAllocated is boolean
		!analiseSuggestion(optimiserResult(IsAllAllocated,NotAlloc,SugestedAllocation), Response);
		.send(operator,question,answer(suggestionByPatient(optimiserResult(IsAllAllocated,NotAlloc,SugestedAllocation)),Response)).

+!kqml_received(Sender,question,getSuggestionByPatient([],Beds),MsgId)
	<-	.print("Agent ", Sender, " requesting suggestion without informing patients.");
		.send(operator,assert,answer(getSuggestionByPatient([],Beds),"Desculpe, não consegui entender para quais pacientes você quer que eu faça sugestão de leitos")).

+!kqml_received(Sender,question,getSuggestionByPatient(Patients,[]),MsgId)
	<-	.print("Agent ", Sender, " requesting suggestion without informing beds to ignore.");
		.send(operator,assert,answer(getSuggestionByPatient(Patients,[]),"Desculpe, não consegui entender quais leitos você quer que eu ignore ao fazer a sugestão de alocação")).

+!kqml_received(Sender,question,getSuggestionByPatient(Params,Beds),MsgId)
	<-	.print("Agent ", Sender, " requesting suggestion ignoring some beds.");
		.send(optimiser,question,getSuggestionByPatient(Params,Beds)).

/**
 * ontological reasoning
 */

+!kqml_received(ontology_specialist,assert,explain(Pred)[cantExplain],MsgId)
	<-	.send(operator,assert,answer(cantExplain(Pred),"Eu não tenho argumentos que suportem essa afirmação.")).
			
+!kqml_received(ontology_specialist,assert,explain(Pred)[Explanation],MsgId)
	<-	!translateToNaturalLanguage(Explanation,"",NLExplanation);
		.send(operator,assert,answer(explaining(Pred,Explanation),NLExplanation)).
	
+!kqml_received(ontology_specialist,assert,Pred[withoutBed],MsgId)
	<-	.send(operator,assert,answer(Pred[withoutBed],"Me desculpe, não localizei nenhum leito com esse número.")).
	
+!kqml_received(ontology_specialist,assert,Pred[withoutPatient],MsgId)
	<-	.send(operator,assert,answer(Pred[withoutPatient],"Me desculpe, não localizei nenhum paciente com esse código.")).
	
+!kqml_received(ontology_specialist,assert,Pred[withoutBedAndPatient],MsgId)
	<-	.send(operator,assert,answer(Pred[withoutBedAndPatient],"Me desculpe, não localizei nenhum leito com esse número e nenhum paciente com esse código.")).
	
+!kqml_received(ontology_specialist,assert,Pred[inadequado],MsgId)
	<-	.send(operator,assert,answer(Pred[inadequado],"Não, esse leito não é adequado.")).
	
+!kqml_received(ontology_specialist,assert,Pred[nao_aconselhavel],MsgId)
	<-	.send(operator,assert,answer(Pred[nao_aconselhavel],"Esse leito é adequado, mas eu não aconselharia ele.")).

+!kqml_received(ontology_specialist,assert,Pred[preferivel],MsgId)
	<-	.send(operator,assert,answer(Pred[preferivel],"Sim, esse leito é adequado. Ele é inclusive preferível")).
	
+!kqml_received(ontology_specialist,assert,Pred[adequado],MsgId)
	<-	.send(operator,assert,answer(Pred[adequado],"Sim, esse leito é adequado.")).

+!kqml_received(ontology_specialist,assert,Pred[cantVerify],MsgId)
	<-	.send(operator,assert,answer(Pred[cantVerify],"Me desculpe, eu não tenho informação suficiente pra verficar isso.")).

+!kqml_received(Sender,question,inadequado(Bed,Patient),MsgId)
	<-	.send(ontology_specialist,question,inadequado(Bed,Patient)).
	
+!kqml_received(Sender,question,adequado(Bed,Patient),MsgId)
	<-	.send(ontology_specialist,question,adequado(Bed,Patient)).
	
+!kqml_received(Sender,assert,adequado(Bed,Patient),MsgId)
	<-	.send(ontology_specialist,question,adequado(Bed,Patient)).
	
+!kqml_received(Sender,question,explain(adequado(Bed,Patient)),MsgId)
	<-	.send(ontology_specialist,question,explain(adequado(Bed,Patient))).

+!kqml_received(Sender,question,explain(inadequado(Bed,Patient)),MsgId)
	: Patient == "NONE" | Bed == "NONE"
	<-	.send(Sender,assert,"Desculpe, não consegui identificar os parâmetros para a consulta").
	
+!kqml_received(Sender,question,explain(inadequado(Bed,Patient)),MsgId)
	<-	.send(ontology_specialist,question,explain(inadequado(Bed,Patient))).

+!kqml_received(Sender,question,confirmAllocation(Patient, Bed),MsgId)
	<-	.print("Agent ", Sender, " requesting allocation"); 
		.send(database,question,confirmAllocation(Patient, Bed)).
		
+!kqml_received(database,assert,confirmedAllocation("Erro"),MsgId)
	<-	.send(operator,assert,answer(confirmedAllocation("Erro"),"Desculpe, houve um erro e não foi possível concluir a alocação conforme solicitado")).

+!kqml_received(database,assert,confirmedAllocation("Success"),MsgId)
	<-	.send(operator,assert,answer(confirmedAllocation("Success"),"Ok, paciente alocado/a conforme solicitado")).

+!kqml_received(database,assert,confirmedAllocation("None"),MsgId)
	<-	.send(operator,assert,answer(confirmedAllocation("None"),"Desculpe, não consegui localizar esse paciente para alocar")).

/**
 * Exception
 */
+!kqml_received(Sender,question,thereIsException(Patient),MsgId)
	<-	.print("Agent ", Sender, " requesting to verify exception.");
		.send(database,question,thereIsException(Patient)).

+!kqml_received(Sender,assert,exception(PatientExceptions),MsgId)
	<-	.print("Agent ", Sender, " answering about exception.");
		!analiseException(PatientExceptions, Response); // [nurse_exception(Patient,Exception)]
		.send(operator,assert,answer(exception(PatientExceptions),Response)).


+!kqml_received(Sender,accept,Pred,MsgId)
	<-	.print("Dialog closed in acceptance: ", Pred);
		.print("====================================================");.

/**
 * Violation
 */
+!kqml_received(monitor,assert,Violation,MsgId)
	<-	.print("Agent monitor reported a violation.");
		!verifyViolation(Violation).
		
+!verifyViolation(violation(Time,Id,IsReply,Performative,operator,Receiver,Veredict))
<- .send(operator,question,answer(violation,"Me desculpe, primeiro eu preciso que você me responda o que perguntei anteriormente.")).

+!verifyViolation(V).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("templates_nlp.asl")}
// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
