/*
	Arquivo original: https://github.com/jacamo-lang/jacamo-rest/blob/master/src/main/java/jacamo/rest/RestImpl.java
	Alterado por: Débora Engelmann
	03 de Maio de 2020
*/

package br.pucrs.smart.Dial4JaCa;

import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.internal.inject.AbstractBinder;

import com.google.gson.Gson;

import br.pucrs.smart.Dial4JaCa.interfaces.IAgent;
import br.pucrs.smart.Dial4JaCa.models.RequestDialogflow;
import br.pucrs.smart.Dial4JaCa.models.ResponseDialogflow;
import br.pucrs.smart.postgresql.PostgresDb;
import br.pucrs.smart.postgresql.Crud.CrudAlocacaoTemporaria;
import br.pucrs.smart.postgresql.Crud.CrudAlocacoesOtimizadas;
import br.pucrs.smart.postgresql.Crud.CrudDataByBedroom;
import br.pucrs.smart.postgresql.Crud.CrudInternado;
import br.pucrs.smart.postgresql.Crud.CrudLeito;
import br.pucrs.smart.postgresql.Crud.CrudNurseException;
import br.pucrs.smart.postgresql.Crud.CrudPedidoLeito;
import br.pucrs.smart.postgresql.models.AlocacaoOtimizadaSql;
import br.pucrs.smart.postgresql.models.DataByBed;
import br.pucrs.smart.postgresql.models.DataByPatient;
import br.pucrs.smart.postgresql.models.InternadoSql;
import br.pucrs.smart.postgresql.models.LaudoInternacaoSql;
import br.pucrs.smart.postgresql.models.LeitoSql;
import br.pucrs.smart.postgresql.models.NurseExceptionSql;
import br.pucrs.smart.postgresql.models.PedidoLeitoSql;
import br.pucrs.smart.validator.models.TempAlloc;


@Singleton
@Path("/")
public class RestImpl extends AbstractBinder {
	 static IAgent mas = null;
	 private Gson gson = new Gson();
	 
	 public static void setListener(IAgent agent) {
		 mas = agent;
	 }
	 
	@Override
	protected void configure() {
		bind(new RestImpl()).to(RestImpl.class);
	}
	
	@Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveRequest(String request) {
        try {
        	RequestDialogflow requestDialogflow = gson.fromJson(request, RequestDialogflow.class);
        	System.out.println("[Dial4JaCa] Request received: " +  gson.toJson(requestDialogflow)); 
        	if (mas != null) {
        		ResponseDialogflow responseDialogflow = mas.intentionProcessing(requestDialogflow.getResponseId(),
        																	  requestDialogflow.getQueryResult().getIntent().getDisplayName(),
        																	  requestDialogflow.getQueryResult().getParameters(),
        																	  requestDialogflow.getQueryResult().getOutputContexts(),
        																	  requestDialogflow.getSession());
        		return Response.ok(gson.toJson(responseDialogflow)).build();
        	} else {
        		ResponseDialogflow responseDialogflow = new ResponseDialogflow();   
            	responseDialogflow.setFulfillmentText("Desculpe, Não foi possível encontrar o agente Jason");
            	return Response.ok(gson.toJson(responseDialogflow)).build();
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(500).build();
    }
	
	/**
	 * Specific to this scenario 
	 * */
	@Path("/validate-allocation")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveAllocToValidate(String request) {
    	ResponseFront result = new ResponseFront();
        try {
        	TempAlloc tempAlloc = gson.fromJson(request, TempAlloc.class);
        	System.out.println("[API] Request received: " +  gson.toJson(tempAlloc)); 
        	String res = CrudAlocacaoTemporaria.addTempAlloc(tempAlloc);
        	result.setResponse(res);
        	result.setStatus(200);
        	return Response.ok(gson.toJson(result)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(500).build();
    }
	
	@Path("/alloc-patients")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response receiveAlloc(String request) {
    	ResponseFront result = new ResponseFront();
        try {
        	TempAlloc tempAlloc = gson.fromJson(request, TempAlloc.class);
        	System.out.println("[API] Request received to alloc: " +  gson.toJson(tempAlloc)); 
        	String res = PostgresDb.allocateByTempAlloc(tempAlloc);
        	result.setResponse(res);
        	result.setStatus(200);
        	return Response.ok(gson.toJson(result)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(500).build();
    }
	
	
	@Path("/add-nurse-exception")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNurseException(String request) {
        try {
        	NurseExceptionSql exception = gson.fromJson(request, NurseExceptionSql.class);
        	System.out.println("[API] Request received: " +  gson.toJson(exception)); 
        	String res = CrudNurseException.addNurseException(exception);       	
        	return Response.ok(res).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(500).build();
    }
	
	@Path("/get-nurse-exceptions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNurseExceptions(String request) {
        try {
        	List<NurseExceptionSql> exceptions = CrudNurseException.getActiveNurseExceptionsWithNames();
        	String res = gson.toJson(exceptions);       	
        	return Response.ok(res).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(500).build();
    }
	
	@Path("/get-internados")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInternados(String request) {
        try {
        	List<InternadoSql> internados = CrudInternado.getInternados();
        	String res = gson.toJson(internados);       	
        	return Response.ok(res).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(500).build();
    }

    @Path("/get-pedidos-leito")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPedidosLeito(String request) {
        try {
        	List<PedidoLeitoSql> pedidos = CrudPedidoLeito.getPedidosLeito();
        	String res = gson.toJson(pedidos);       	
        	return Response.ok(res).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(500).build();
    }
	
	@Path("/get-leitos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLeitos(String request) {
        try {
        	List<DataByBed> leitos = CrudDataByBedroom.getDataByBeds();
        	String res = gson.toJson(leitos);       	
        	return Response.ok(res).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(500).build();
    }

    @Path("/get-pacientes")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getpacientes(String request) {
        try {
        	List<DataByPatient> pacientes = CrudDataByBedroom.getDataByPatients();
        	String res = gson.toJson(pacientes);       	
        	return Response.ok(res).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(500).build();
    }
	
	@Path("/get-leitos-vagos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLeitosVagos(String request) {
        try {
        	List<DataByBed> leitos = CrudDataByBedroom.getDataByFreeBeds();
        	String res = gson.toJson(leitos);       	
        	return Response.ok(res).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(500).build();
    }
	
	@Path("/get-optimised-allocation")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOptimisedAllocation(String request) {
        try {
        	AlocacaoOtimizadaSql alocacao = CrudAlocacoesOtimizadas.getLastOptimizerResultWithPatientName();
        	String res = gson.toJson(alocacao);       	
        	return Response.ok(res).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.status(500).build();
    }
	
}
