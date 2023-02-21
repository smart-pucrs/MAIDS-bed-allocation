package br.pucrs.smart.validator;

import br.pucrs.smart.postgresql.models.ValidacaoSql;
import br.pucrs.smart.validator.models.TempAlloc;

public interface IValidator {
	void receiveValidation(ValidacaoSql val, TempAlloc tempAloc);
}
