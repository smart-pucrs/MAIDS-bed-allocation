// Agent monitor that just print the exchanged messages

+!start : true <- .print("Monitor agent enabled.").

+violation(Time,Id,IsReply,Performative,Sender,Receiver,Veredict)
   <- .print("Message ",Id," from ",Sender," to ",Receiver," at ", Time, " has a violation.")
   	  .send(Receiver,assert,violation(Time,Id,IsReply,Performative,Sender,Receiver,Veredict)).

{ include("$jacamoJar/templates/common-cartago.asl") }