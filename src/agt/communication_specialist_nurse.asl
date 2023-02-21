// Agent communication_specialist in project leito-allocation-system

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Communication specialist agent enabled.").

+request(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: RequestedBy == "nurse"
<-
	.print("Request received - ",IntentName," from Dialog");
	.print("Params: ",Params);
	!responder(RequestedBy, ResponseId, IntentName, Params, Contexts);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Call Jason Agent")
<-
	reply("Olá, eu sou seu agente Jason, em que posso lhe ajudar?");
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Default Welcome Intent")
<-
	reply("Olá, eu sou seu agente Jason, em que posso lhe ajudar?");
	.

+!responder(RequestedBy, ResponseId, IntentName, [param("numPatients",NumPatients)], Contexts)
	: (IntentName == "Get Optimised Allocation Moving")
<-
	.print("Chatbot of ", RequestedBy, " is requesting an optimised allocation moving up to ", NumPatients, " pacientes.");
	.send(assistant,question,getOptimisedAllocationMoving(NumPatients));
	.

+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Get Optimised Allocation Moving - no")
<-
	.print("Chatbot of ", RequestedBy, " is requesting the cancellation of the optimised allocation.");
	.send(assistant,question,cancelAllocByOptimization);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Get Optimised Allocation Moving - yes")
<-
	.print("Chatbot of ", RequestedBy, " is requesting the confirmation of the optimised allocation.");
	.send(assistant,question,confirmAllocByOptimizationMoving);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, [param("paciente",Patients)], Contexts)
	: (IntentName == "Get Optimised Allocation Moving - confirm with exception")
<-
	!getPatientsNames(Patients, Names);
	.print("Chatbot of ", RequestedBy, " is requesting the confirmation of the allocation with exception of: ", Names);
	.send(assistant,question,allocOpPatientsMovingExcept(Names));
	.

+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Get Optimised Allocation")
<-
	.print("Chatbot of ", RequestedBy, " is requesting an optimised allocation.");
	.send(assistant,question,getOptimisedAllocation);
	.

+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Get Optimised Allocation - no")
<-
	.print("Chatbot of ", RequestedBy, " is requesting the cancellation of the optimised allocation.");
	.send(assistant,question,cancelAllocByOptimization);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Get Optimised Allocation - yes")
<-
	.print("Chatbot of ", RequestedBy, " is requesting the confirmation of the optimised allocation.");
	.send(assistant,question,confirmAllocByOptimization);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, [param("paciente",Patients)], Contexts)
	: (IntentName == "Get Optimised Allocation - confirm with exception")
<-
	!getPatientsNames(Patients, Names);
	.print("Chatbot of ", RequestedBy, " is requesting the confirmation of the allocation with exception of: ", Names);
	.send(assistant,question,allocOpPatientsExcept(Names));
	.
	
+!responder(RequestedBy, ResponseId, IntentName, [param("paciente",Patients)], Contexts)
	: (IntentName == "Get Suggestion")
<-
	!getPatientsNames(Patients, Names);
	.print("Chatbot of ", RequestedBy, " suggestions to allocate: ", Names);
	.send(assistant,question,getSuggestionByPatient(Names));
	.

+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Get Suggestion - no")
<-
	.print("Chatbot of ", RequestedBy, " is requesting the cancellation of the suggested allocation.");
	.send(assistant,question,cancelAllocByOptimization);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Get Suggestion - yes")
<-
	.print("Chatbot of ", RequestedBy, " is requesting the confirmation of the suggested allocation.");
	.send(assistant,question,confirmAllocByOptimization);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, [param("paciente",Patients)], Contexts)
	: (IntentName == "Get Suggestion - confirm with exception")
<-
	!getPatientsNames(Patients, Names);
	.print("Chatbot of ", RequestedBy, " is requesting the confirmation of the allocation with exception of: ", Names);
	.send(assistant,question,allocOpPatientsExcept(Names));
	.
	
+!responder(RequestedBy, ResponseId, IntentName, [param("paciente",Patients),param("leito", Beds)], Contexts)
	: (IntentName == "Get Suggestion Except")
<-
	!getPatientsNames(Patients, Names);
	.print("Chatbot of ", RequestedBy, " suggestions to allocate: ", Names, " ignoring leitos: ", Beds);
	!trim(Beds,B);
	.send(assistant,question,getSuggestionByPatient(Names,B));
	.
+!responder(RequestedBy, ResponseId, IntentName, [param("leito", Beds),param("paciente",Patients)], Contexts)
	: (IntentName == "Get Suggestion Except")
<-
	!getPatientsNames(Patients, Names);
	!trim(Beds,B);
	.print("Chatbot of ", RequestedBy, " suggestions to allocate: ", Names, " ignoring leitos: ", B);
	.send(assistant,question,getSuggestionByPatient(Names,B));
	.		
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Get Suggestion Except - no")
<-
	.print("Chatbot of ", RequestedBy, " is requesting the cancellation of the suggested allocation.");
	.send(assistant,question,cancelAllocByOptimization);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Get Suggestion Except - yes")
<-
	.print("Chatbot of ", RequestedBy, " is requesting the confirmation of the suggested allocation.");
	.send(assistant,question,confirmAllocByOptimization);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, [param("paciente",Patients)], Contexts)
	: (IntentName == "Get Suggestion Except - confirm with exception")
<-
	!getPatientsNames(Patients, Names);
	.print("Chatbot of ", RequestedBy, " is requesting the confirmation of the allocation with exception of: ", Names);
	.send(assistant,question,allocOpPatientsExcept(Names));
	.

+!responder(RequestedBy, ResponseId, IntentName, [param("leito",Bed),param("paciente",Patient)], Contexts)
	: (IntentName == "Verify Suitability")
<-	!trim([Bed],[B]);
	.print("Chatbot of ", RequestedBy, " is requesting to verify suitability: paciente: ", Patient, ", leito: ", B);
	.send(assistant,question,adequado(B,Patient));
	.

+!responder(RequestedBy, ResponseId, IntentName, [param("paciente",Patient),param("leito",Bed)], Contexts)
	: (IntentName == "Verify Suitability")
<-	!trim([Bed],[B]);
	.print("Chatbot of ", RequestedBy, " is requesting to verify suitability: paciente: ", Patient, ", leito: ", B);
	.send(assistant,question,adequado(B,Patient));
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Verify Suitability - alloc")
<-	!getInContext(Contexts, "leito", Bed);
	!trim([Bed],[B]);
	!getInContext(Contexts, "paciente", Patient);
	.print("Chatbot of ", RequestedBy, " is requesting the confirmation of the allocation: paciente: ", Patient, ", leito: ", B);
	.send(assistant,question,confirmAllocation(Patient, B));
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Verify Suitability - why")
<-	!getInContext(Contexts, "leito", Bed);
	!trim([Bed],[B]);
	!getInContext(Contexts, "paciente", Patient);
	.print("Chatbot of ", RequestedBy, " is requesting explanation about suitability of ", B," leito to ", Patient);
	.send(assistant,question,explain(inadequado(B,Patient)));
	.

+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Verify Suitability - get suggestion")
<-	!getInContext(Contexts, "paciente", Patient);
	.print("Chatbot of ", RequestedBy, " suggestions to allocate: ", Patient);
	.send(assistant,question,getSuggestionByPatient([Patient]));
	.

+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Verify Suitability - get suggestion - no")
<-	.print("Chatbot of ", RequestedBy, " is requesting the cancellation of the suggested allocation.");
	.send(assistant,question,cancelAllocByOptimization);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Verify Suitability - get suggestion - yes")
<-	.print("Chatbot of ", RequestedBy, " is requesting the confirmation of the suggested allocation.");
	.send(assistant,question,confirmAllocByOptimization);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, [param("paciente",Patients)], Contexts)
	: (IntentName == "Verify Suitability - get suggestion - confirm with exception")
<-	!getPatientsNames(Patients, Names);
	.print("Chatbot of ", RequestedBy, " is requesting the confirmation of the allocation with exception of: ", Names);
	.send(assistant,question,allocOpPatientsExcept(Names));
	.

+!responder(RequestedBy, ResponseId, IntentName, [param("paciente",Patient),param("leito", B)], Contexts)
	: (IntentName == "Allocate Patient")
<-	.print("Chatbot of ", RequestedBy, " is requesting an allocation.");
	.send(assistant,question,alocar(B,Patient));
	.

+!responder(RequestedBy, ResponseId, IntentName, [param("leito", B), param("paciente",Patient)], Contexts)
	: (IntentName == "Allocate Patient")
<-	.print("Chatbot of ", RequestedBy, " is requesting an allocation.");
	.send(assistant,question,alocar(B,Patient));
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Allocate Patient - verify suitability")
<-	!getInContext(Contexts, "leito", Bed);
	!trim([Bed],[B]);
	!getInContext(Contexts, "paciente", Patient);
	.print("Chatbot of ", RequestedBy, " is requesting to verify suitability: paciente: ", Patient, ", leito: ", B);
	.send(assistant,question,adequado(B,Patient));
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Allocate Patient - verify suitability - alloc")
<-	!getInContext(Contexts, "leito", Bed);
	!trim([Bed],[B])
	!getInContext(Contexts, "paciente", Patient);
	.print("Chatbot of ", RequestedBy, " is requesting the confirmation of the allocation: paciente: ", Patient, ", leito: ", B);
	.send(assistant,question,confirmAllocation(Patient, B));
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Allocate Patient - verify suitability - why")
<-	!getInContext(Contexts, "leito", Bed);
	!trim([Bed],[B])
	!getInContext(Contexts, "paciente", Patient);
	.print("Chatbot of ", RequestedBy, " is requesting explanation about suitability of ", B," leito to ", Patient);
	.send(assistant,question,explain(inadequado(B,Patient)));
	.

+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Allocate Patient - verify suitability - why - allocate anyway")
<-	.print("Chatbot of ", RequestedBy, " is requesting to allocate anyway.");
	.send(assistant,question,alocar(B,Patient)[anyway]);
	.

+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Allocate Patient - verify suitability - get suggestion")
<-	!getInContext(Contexts, "paciente", Patient);
	.print("Chatbot of ", RequestedBy, " suggestions to allocate: ", Patient);
	.send(assistant,question,getSuggestionByPatient([Patient]));
	.

+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Allocate Patient - verify suitability - get suggestion - no")
<-	.print("Chatbot of ", RequestedBy, " is requesting the cancellation of the suggested allocation.");
	.send(assistant,question,cancelAllocByOptimization);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Allocate Patient - verify suitability - get suggestion - yes")
<-	.print("Chatbot of ", RequestedBy, " is requesting the confirmation of the suggested allocation.");
	.send(assistant,question,confirmAllocByOptimization);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, [param("paciente",Patients)], Contexts)
	: (IntentName == "Allocate Patient - verify suitability - get suggestion - confirm with exception")
<-	!getPatientsNames(Patients, Names);
	.print("Chatbot of ", RequestedBy, " is requesting the confirmation of the allocation with exception of: ", Names);
	.send(assistant,question,allocOpPatientsExcept(Names));
	.
	
+!responder(RequestedBy, ResponseId, IntentName, [param("leito",Bed),param("paciente",Patient)], Contexts)
	: (IntentName == "Register Exception")
<-	!trim([Bed],[B]);
	.print("Chatbot of ", RequestedBy, " is requesting to register an exception: paciente: ", Patient, ", leito: ", B);
	.send(assistant,question,exception(B,Patient));
	.

+!responder(RequestedBy, ResponseId, IntentName, [param("paciente",Patient),param("leito",Bed)], Contexts)
	: (IntentName == "Register Exception")
<-	!trim([Bed],[B]);
	.print("Chatbot of ", RequestedBy, " is requesting to register an exception: paciente: ", Patient, ", leito: ", B);
	.send(assistant,question,exception(B,Patient));
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Get Information About Beds")
<-	.print("Chatbot of ", RequestedBy, " is asking for information about beds.");
	!getInParam("status_leito", Params, Status);
	!getInParam("genero", Params, Genero);
	!getInParam("tipo_especialidade", Params, TipEsp);
	!getInParam("acomodacao", Params, Acomodacao);
	!getInParam("faixa_etaria", Params, FaixaEtaria);
	.send(assistant,question,getBed[leito_e_do_genero(Bed,Genero),leito_e_da_faixa_etaria(Bed,FaixaEtaria),leito_e_do_tipo_especialidade(Bed,TipEsp),e_de_acomodacao(Bed,Acomodacao),possui_status(Bed,Status)]);
	.

+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "getSuperlativeInfo1")
<-	.print("Chatbot of ", RequestedBy, " is asking about the smallest room with fewer patients allocated.");
	.send(assistant,question,smallestRoom[fewest_occupants]);
	.
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "getSuperlativeInfo2")
<-	.print("Chatbot of ", RequestedBy, " is asking about the biggest room with fewer patients allocated.");
	.send(assistant,question,biggestRoom[fewest_occupants]);
	.
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "getSuperlativeInfo3")
<-	.print("Chatbot of ", RequestedBy, " is asking about the smallest room with most patients allocated.");
	.send(assistant,question,biggestRoom[most_occupants]);
	.
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "getSuperlativeInfo4")
<-	.print("Chatbot of ", RequestedBy, " is asking about the smallest room with most patients allocated.");
	.send(assistant,question,smallestRoom[most_occupants]);
	.




+!responder(RequestedBy, ResponseId, IntentName, [param("paciente",Patient)], Contexts)
	: (IntentName == "Verify Nurse Exception")
<-	.print("Chatbot of ", RequestedBy, " is requesting to verify exception to paciente ", Patient);
	.send(assistant,question,thereIsException(Patient));
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Verify Nurse Exception - get suggestion")
<-	!getInContext(Contexts, "paciente", Patient);
	.print("Chatbot of ", RequestedBy, " suggestions to allocate: ", Patient);
	.send(assistant,question,getSuggestionByPatient([Patient]));
	.

+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Verify Nurse Exception - get suggestion - no")
<-	.print("Chatbot of ", RequestedBy, " is requesting the cancellation of the suggested allocation.");
	.send(assistant,question,cancelAllocByOptimization);
	.
	
+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: (IntentName == "Verify Nurse Exception - get suggestion - yes")
<-	.print("Chatbot of ", RequestedBy, " is requesting the confirmation of the suggested allocation.");
	.send(assistant,question,confirmAllocByOptimization);
	.

+!responder(RequestedBy, ResponseId, IntentName, Params, Contexts)
	: true
<-	reply("Desculpe, não reconheço essa intensão");
	.
	
+!getPatientsNames(Patients, Names)
<-	!getPatientsNames(Patients,[],Names).
+!getPatientsNames([],Temp,Names)
<-	Names=Temp.
+!getPatientsNames([Patient|R],Temp,Names)
<-	.concat([Patient],Temp,T);
	!getPatientsNames(R,T,Names).
	
+!getInParam(Word, [], Res)
<-	Res = "NONE".
+!getInParam(Word, [param(Key,Value)|R], Res)
	: Word == Key
<-	Res = Value.
+!getInParam(Word, [param(Key,Value)|R], Res)
<-	!getInParam(Word, R, Res).

+!getInContext([], Word, Res)
<-	Res="NONE".
+!getInContext([context(C,L,Params)|Rc], Word, Res)
<-	!getInParam(Word, Params, Resp)
	!analiseResp(Resp, Rc, Word, Res)
	.
	
+!analiseResp(Resp, Rc, Word, Res)
	: Resp == "NONE"
<-	!getInContext(Rc, Word, Res).
+!analiseResp(Resp, Rc, Word, Res)
<- Res = Resp.

+!trim([],T,B)
<-B=T.
+!trim(Beds,B)
<- !trim(Beds,[],B).
+!trim([Bed|R],T,B)
<- .replace(Bed," ","",TB);
	.concat(T,[TB],Temp);
	!trim(R,Temp,B).


+!kqml_received(Sender,assert,answer(_,Response),MsgId)
	<-	.print("Answering to chatbot: ", Response);
		reply(Response).

+!kqml_received(Sender,question,answer(_,Response),MsgId)
	<-	.print("Answering to chatbot: ", Response);
		reply(Response).

/**
 * Violation
 */
+!kqml_received(monitor,assert,Violation,MsgId)
	<-	.print("Agent monitor reported a violation.");
		!verifyViolation(Violation).
		
+!verifyViolation(V).

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-oleitoient.asl") }