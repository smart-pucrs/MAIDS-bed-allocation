// Agent database_specialist in project bed-allocation-system

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start 
	: true 
<- 
	.print("Database specialist agent enabled.");
//	!fillTheBeliefBase;
    .
    
+!fillTheBeliefBase
<-
	getBedsDataByRoom(BedsDataByRoom);
	!addToTheBeliefBase(BedsDataByRoom);
	.
   
+!addToTheBeliefBase([]).	
+!addToTheBeliefBase([H|T])
<-
	+H;
	!addToTheBeliefBase(T)
	.


+!get(smallestRoom(Room)[fewest_occupants(Room)])
<-
	Room="118J".

+!get(biggestRoom(Room)[fewest_occupants(Room)])
<-
	Room="118".

+!get(biggestRoom(Room)[most_occupants(Room)])
<-
	Room="759".

+!get(smallestRoom(Room)[most_occupants(Room)])
<-
	Room="112".



	
+!kqml_received(Sender,question,saveOptimiserResult,MsgId)
	<-	.print("Agent ",Sender," wants to save the optimiser result"); // optimiserResult(IsAllAllocated,notAlloc([PacienteName]), sugestedAllocation([alloc(PacienteName, NumLeito)])) -> where IsAllAllocated is boolean
		getOptimiserResult(Result);
		setOptimiserResult(Result, Response);
		.send(Sender,assert,savedOptimiserResult(Response));
		.
+!kqml_received(Sender,question,allocOpPatients,MsgId)
	<-	.print("Agent ",Sender," wants to allocate patients using optimizer result"); 
		.send(Sender,assert,allocatedOpPatients("Success"));
		.
+!kqml_received(Sender,question,allocByOptimizerWithExceptions(Params),MsgId)
	<-	.print("Agent ",Sender," wants to allocate patients using optimizer result with exceptions"); 
		.send(Sender,assert,allocatedOpPatientsExcept("Success"));
		.

+!kqml_received(Sender,question,allocOpPatientsMoving,MsgId)
	<-	.print("Agent ",Sender," wants to allocate patients using optimizer result"); 
		.send(Sender,assert,allocatedOpPatientsMoving("Success"));
		.		
+!kqml_received(Sender,question,allocByOptimizerMovingWithExceptions(Params),MsgId)
	<-	.print("Agent ",Sender," wants to allocate patients using optimizer result with exceptions"); 
		.send(Sender,assert,allocatedOpPatientsMoving("Success"));
		.
		
+!kqml_received(Sender,question,cancelOpAlloc,MsgId)
	<-	.print("Agent ",Sender," wants to cancel the last optmised allocation"); 
		.send(Sender,assert,cancelOpAllocation("Success"));
		.		

+!kqml_received(Sender,question,thereIsException(Patient),MsgId)
	<-	.print("Agent ",Sender," wants get nurse exception");
		getNurseExceptions(Patient, Response);
		.send(Sender,assert,exception(Response));
		.		
		
+!kqml_received(Sender,question,confirmAllocation(Patient, Bed),MsgId)
	<-	.print("Agent ",Sender," wants to allocate patient");
		.send(Sender,assert,confirmedAllocation("Success"));
		.

+!kqml_received(Sender,question,smallestRoom[fewest_occupants],MsgId)
	<-	.print("Agent ", Sender, " wants get the smallest room with fewest occupants.");
		!get(smallestRoom(Room)[fewest_occupants(Room)]);
		.send(Sender,assert,smallestRoom(Room)[fewest_occupants(Room)]).

+!kqml_received(Sender,question,biggestRoom[fewest_occupants],MsgId)
	<-	.print("Agent ", Sender, " wants get the biggest room with fewest occupants.");
		!get(biggestRoom(Room)[fewest_occupants(Room)]);
		.send(Sender,assert,biggestRoom(Room)[fewest_occupants(Room)]).

+!kqml_received(Sender,question,biggestRoom[most_occupants],MsgId)
	<-	.print("Agent ", Sender, " wants get the biggest room with most occupants.");
		!get(biggestRoom(Room)[most_occupants(Room)]);
		.send(Sender,assert,biggestRoom(Room)[most_occupants(Room)]).

+!kqml_received(Sender,question,smallestRoom[most_occupants],MsgId)
	<-	.print("Agent ", Sender, " wants get the smallest room with most occupants.");
		!get(smallestRoom(Room)[most_occupants(Room)]);
		.send(Sender,assert,smallestRoom(Room)[most_occupants(Room)]).

+!kqml_received(Sender,question,getBed[leito_e_do_genero(Bed,Genero),leito_e_da_faixa_etaria(Bed,FaixaEtaria),leito_e_do_tipo_especialidade(Bed,TipEsp),e_de_acomodacao(Bed,Acomodacao),possui_status(Bed,Status)],MsgId)
	<- .print("Agent ", Sender, " wants a bed.");
		getBed(Genero, FaixaEtaria, TipEsp, Acomodacao, Status, Bed);
		.send(Sender,assert,getBed(Bed)).

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
