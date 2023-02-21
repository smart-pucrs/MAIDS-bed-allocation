// Agent validator in project bed-allocation-system

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Validator agent enabled.").//; getValidationResult(Response);!getValidationResult(Response).

+!getValidationResult(Result)
<-
	.print("Result : ");
	.print(Result);
	.

+!kqml_received(Sender,question,getValidationResult,MsgId)
	<-	.print("Agent ", Sender, " requesting validation result.");
		getValidationResult(Response); //result(Id, IsValid, Errors)
		.send(Sender,assert,Response).
		
+!kqml_received(Sender,question,allocByValidation,MsgId)
	<-	.print("Agent ", Sender, " requesting allocation."); 
		.send(database,question,allocByValidation).
+!kqml_received(database,assert,allocByValidation(Response),MsgId)
	<-	.print("Agent database is answering about the allocation."); 
		.send(assistant,assert,allocByValidation(Response)).
		
+!kqml_received(Sender,question,allocValidValPatients,MsgId)
	<-	.print("Agent ", Sender, " requesting allocation of valid allocations."); 
		.send(database,question,allocValidValPatients).
+!kqml_received(database,assert,allocatedValidValPatients(Response),MsgId)
	<-	.print("Agent database is answering about the allocation."); 
		.send(assistant,assert,allocatedValidValPatients(Response)).		
		
+!kqml_received(Sender,question,allocValPatientsExcept(Params),MsgId)
	<-	.print("Agent ", Sender, " requesting allocation with exceptions."); 
		.send(database,question,allocValPatientsExcept(Params)).
+!kqml_received(database,assert,allocatedValPatientsExcept(Response),MsgId)
	<-	.print("Agent database is answering about the allocation."); 
		.send(assistant,assert,allocatedValPatientsExcept(Response)).
		
		
+!kqml_received(Sender,question,dontAllocValPatients,MsgId)
	<-	.print("Agent ", Sender, " requesting conclusion."); 
		.send(database,question,dontAllocValPatients).
+!kqml_received(database,assert,didntAllocValPatients(Response),MsgId)
	<-	.print("Agent database is answering about the conclusion."); 
		.send(assistant,assert,didntAllocValPatients(Response)).
		

+!kqml_received(Sender,question,updateValidation(Id, Response),MsgId)
	<-	.print("Agent ", Sender, " requesting to save result in validation."); 
		.send(database,question,updateValidation(Id, Response)).
+!kqml_received(database,assert,updatedValidation(Response),MsgId)
	<-	.print("Agent database is answering about the update."); 
		.send(assistant,assert,updatedValidation(Response)).	
		
		
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
