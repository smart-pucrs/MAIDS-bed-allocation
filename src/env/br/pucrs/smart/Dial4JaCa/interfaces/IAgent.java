package br.pucrs.smart.Dial4JaCa.interfaces;

import java.util.HashMap;
import java.util.List;

import br.pucrs.smart.Dial4JaCa.models.OutputContexts;
import br.pucrs.smart.Dial4JaCa.models.ResponseDialogflow;

public interface IAgent {
	public ResponseDialogflow intentionProcessing(String responseId, String intentName, HashMap<String, Object> parameters, List<OutputContexts> outputContexts, String session);
}

