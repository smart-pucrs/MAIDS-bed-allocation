// Agent optimiser in project bed-allocation-system

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Optimiser agent enabled.").//; getSuggestionByPatient(["Marcos Souza"], Resp); .print(Resp).//suggestOptimisedAllocation(R); .print(R).

+!getOptimisedAllocation(Response)
<-
	.print("Calling Optimiser.");
	suggestOptimisedAllocation(Response); // optimiserResult(IsAllAlocated,notAlloc([PacienteName]), sugestedAllocation([alloc(PacienteName, NumLeito)])) -> where IsAllAlocated is boolean
	.send(database,question,saveOptimiserResult);
	+Response;
	.
+!getOptimisedAllocationMoving(NumPatients,Response)
<-
	.print("Calling Optimiser.");
	suggestOptimisedAllocationMoving(NumPatients,Response); // optimiserResult(IsAllAlocated,notAlloc([PacienteName]), sugestedAllocation([alloc(PacienteName, NumLeito)])) -> where IsAllAlocated is boolean
	.send(database,question,saveOptimiserResult);
	+Response;
	.
	
// getOptimisedAllocationMoving(NumPatients)
+!kqml_received(Sender,question,getOptimisedAllocationMoving(NumPatients),MsgId)
	<-	.print("Agent ", Sender, " requesting an optmised allocation.");
		!getOptimisedAllocationMoving(NumPatients,Response);
		.send(Sender,assert,Response).
		
+!kqml_received(assistant,question,allocOpPatientsMoving,MsgId)
	<-	.print("Agent assistant requesting confirmation of the optmised allocation.");
		.send(database,question,allocOpPatientsMoving).		

+!kqml_received(database,assert,allocatedOpPatientsMoving(Resp),MsgId)
	<-	.send(assistant,assert,allocatedOpPatientsMoving(Resp)).
	
+!kqml_received(assistant,question,allocByOptimizerMovingWithExceptions(Params),MsgId)
	<-	.print("Agent assistant requesting optmised allocation with exceptions.");
		.send(database,question,allocByOptimizerMovingWithExceptions(Params)).	

//allocOpPatientsMoving

+!kqml_received(Sender,question,getOptimisedAllocation,MsgId)
	<-	.print("Agent ", Sender, " requesting an optmised allocation.");
		!getOptimisedAllocation(Response);
		.send(Sender,assert,Response).
		
+!kqml_received(assistant,question,cancelOpAlloc,MsgId)
	<-	.print("Agent assistant requesting cancellation of the last optmised allocation.");
		.send(database,question,cancelOpAlloc).	
				
+!kqml_received(database,assert,cancelOpAllocation(Response),MsgId)
	<-	.print("Agent database is returning about the cancellation of the last optmised allocation.");
		.send(assistant,assert,cancelOpAllocation(Response)).	
		
+!kqml_received(assistant,question,allocOpPatients,MsgId)
	<-	.print("Agent assistant requesting confirmation of the optmised allocation.");
		.send(database,question,allocOpPatients).	

+!kqml_received(database,assert,allocatedOpPatients(Resp),MsgId)
	<-	.send(assistant,assert,allocatedOpPatients(Resp)).
	
+!kqml_received(assistant,question,allocByOptimizerWithExceptions(Params),MsgId)
	<-	.print("Agent assistant requesting optmised allocation with exceptions.");
		.send(database,question,allocByOptimizerWithExceptions(Params)).	

+!kqml_received(database,assert,allocatedOpPatientsExcept(Resp),MsgId)
	<-	.send(assistant,assert,allocatedOpPatientsExcept(Resp)).

+!kqml_received(assistant,question,getSuggestionByPatient([PacienteName]),MsgId)
	<-	.print("Agent assistant requesting optmised suggestion.");
//		getSuggestionByPatient(Params, Resp);
//		+Resp;
		.send(assistant,assert,suggestionByPatient(optimiserResult("true",notAlloc([]), sugestedAllocation([alloc(PacienteName, "2233")])) )); // optimiserResult(IsAllAlocated,notAlloc([PacienteName]), sugestedAllocation([alloc(PacienteName, NumLeito)])) -> where IsAllAlocated is boolean
		.send(database,question,saveOptimiserResult).

+!kqml_received(assistant,question,getSuggestionByPatient(Params,Beds),MsgId)
	<-	.print("Agent assistant requesting optmised suggestion ignoring some beds.");
		getSuggestionByPatient(Params, Beds, Resp);
		+Resp;
		.send(assistant,assert,suggestionByPatient(Resp)); // optimiserResult(IsAllAlocated,notAlloc([PacienteName]), sugestedAllocation([alloc(PacienteName, NumLeito)])) -> where IsAllAlocated is boolean
		.send(database,question,saveOptimiserResult).		

+!kqml_received(Sender,assert,savedOptimiserResult("Erro"),MsgId)
	<-	.print("Erro ao salvar o resultado da otimização no banco de dados").

+!kqml_received(Sender,assert,savedOptimiserResult("Success"),MsgId)
	<-	.print("Resultado da otimização salvo no banco de dados").		

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
//{ include("$moiseJar/asl/org-obedient.asl") }
