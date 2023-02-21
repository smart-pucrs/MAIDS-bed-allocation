/* ***** Translate plans ********** */	

+!translate
(
    adequado(Le,P)[as(_)],X
)
<-
    .concat("O leito ", Le , " é adequado para o(a) paciente ", P , " pois o(a) gestor(a) abriu uma exceção para esse caso. ",X).

+!translate
(
    defeasible_rule(inadequado(Le,P),[paciente(P),leito(Le),possui_status(Le,S),differentFrom(S,"Vago")])[as(_)],X
)
<- 
	.concat("O leito ", Le , " não é adequado para o(a) paciente ", P , " pois esse leito possui o status ", S, ".",X).

+!translate
(
    defeasible_rule(inadequado(Le,P),[paciente(P),leito(Le),possui_infeccao(P,In),differentFrom(In,"NONE"),e_de_isolamento(Le,Is),differentFrom(In,Is),se_localiza_no_quarto(Le,Q),quarto_possui_status(Q,Qs),differentFrom(Qs,"Vazio")])[as(_)],X
)
<- 
	.concat("O leito ", Le , " não é adequado para o(a) paciente ", P , " pois nesse mesmo quarto tem um paciente com necessidades de isolamento diferentes das desse(a) paciente. ",X).

+!translate
(
    defeasible_rule(inadequado(Le,P),[paciente(P),leito(Le),e_do_genero(P,Gp),leito_e_do_genero(Le,Gl),differentFrom(Gp,Gl),differentFrom(Gl,"NONE"),differentFrom(Gp,"NONE")])[as(_)],X
)
<- 
	.concat("O leito ", Le , " não é adequado para o(a) paciente ", P , " pois nesse mesmo quarto tem um paciente do gênero ", Gl, ". ",X).


+!translate
(
    defeasible_rule(inadequado(Le,P),[paciente(P),leito(Le),possui_convenio(P,Cnp),e_de_convenio(Le,Cnl),differentFrom(Cnp,Cnl),differentFrom(Cnl,"NONE"),differentFrom(Cnp,"NONE")])[as(_)],X
)
<- 
	.concat("O leito ", Le , " não é adequado para o(a) paciente ", P , " pois o convênio do(a) paciente é ", Cnp, " e esse leito é de convênio", Cnl, ". ",X).


+!translate
(
    defeasible_rule(inadequado(Le,P),[paciente(P),leito(Le),possui_cobertura(P,Cp),e_de_acomodacao(Le,Cl),differentFrom(Cp,Cl),differentFrom(Cl,"NONE"),differentFrom(Cp,"NONE")])[as(_)],X
)
<- 
	.concat("O leito ", Le , " não é adequado para o(a) paciente ", P , " pois a cobertura do(a) paciente é ", Cp, " e esse leito é ", Cl, ". ",X).


+!translate
(
    defeasible_rule(nao_aconselhavel(Le,P),[paciente(P),leito(Le),e_da_especialidade(P,Ep),leito_e_da_especialidade(Le,El),differentFrom(Ep,El),differentFrom(El,"NONE"),differentFrom(Ep,"NONE")])[as(_)],X
)
<- 
	.concat("O leito ", Le , " é adequado, mas não seria aconselhável para o(a) paciente ", P , " pois nesse mesmo quarto tem um paciente da especialidade ", El, ". ",X).


+!translate
(
    defeasible_rule(nao_aconselhavel(Le,P),[paciente(P),leito(Le),possui_infeccao(P,"NONE"),e_preferencialmente_isolamento(Le,"true"),se_localiza_no_quarto(Le,Q),quarto_possui_status(Q,"Vazio")])[as(_)],X
)
<- 
	.concat("O leito ", Le , " é adequado, mas não seria aconselhável para o(a) paciente ", P , " pois esse leito está num quarto vazio que é preferencialmente de isolamento e esse(a) paciente não possui infeccções. ",X).


+!translate
(
    defeasible_rule(nao_aconselhavel(Le,P),[paciente(P),leito(Le),e_do_tipo_especialidade(P,Tep),leito_e_do_tipo_especialidade(Le,Tel),differentFrom(Tep,Tel),differentFrom(Tel,"NONE"),differentFrom(Tep,"NONE")])[as(_)],X
)
<- 
	.concat("O leito ", Le , " é adequado, mas não seria aconselhável para o(a) paciente ", P , " pois esse(a) paciente é ", Tep, " e nesse mesmo quarto tem um paciente ", Tel, ". ",X).


+!translate
(
    defeasible_rule(nao_aconselhavel(Le,P),[paciente(P),leito(Le),possui_infeccao(P,In),differentFrom(In,"NONE"),e_preferencialmente_isolamento(Le,"false"),se_localiza_no_quarto(Le,Q),quarto_possui_status(Q,"Vazio")])[as(_)],X
)
<- 
	.concat("O leito ", Le , " é adequado, mas não seria aconselhável para o(a) paciente ", P , " pois esse leito está num quarto vazio que não é preferencialmente de isolamento e esse(a) paciente possui infecção ", In, ". ",X).


+!translate
(
    defeasible_rule(nao_aconselhavel(Le,P),[paciente(P),leito(Le),e_da_faixa_etaria(P,Idp),leito_e_da_faixa_etaria(Le,Idl),differentFrom(Idp,Idl),differentFrom(Idl,"NONE"),differentFrom(Idp,"NONE")])[as(_)],X
)
<- 
	.concat("O leito ", Le , " é adequado, mas não seria aconselhável para o(a) paciente ", P , " pois nesse mesmo quarto tem um paciente da faixa etária ", Idl, ". ",X).


+!translate
(
    defeasible_rule(preferivel(Le,P),[paciente(P),leito(Le),possui_status(Le,"Vago"),possui_infeccao(P,In),differentFrom(In,"NONE"),e_preferencialmente_isolamento(Le,"true"),se_localiza_no_quarto(Le,Q),quarto_possui_status(Q,"Vazio")])[as(_)],X
)
<- 
	.concat("O leito ", Le , " é adequado e preferível para o(a) paciente ", P , " pois esse leito se encaixa em todas as características do(a) paciente além de estar em um quarto vazio que é preferencialmente de isolamento. ",X).


+!translate
(
    defeasible_rule(preferivel(Le,P),[paciente(P),leito(Le),possui_status(Le,"Vago"),possui_infeccao(P,In),e_de_isolamento(Le,In)])[as(_)],X
)
<- 
	.concat("O leito ", Le , " é adequado e preferível para o(a) paciente ", P , " pois esse leito se encaixa em todas as características do(a) paciente. ",X).

