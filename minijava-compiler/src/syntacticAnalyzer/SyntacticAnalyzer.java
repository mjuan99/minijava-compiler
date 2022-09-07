package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.LexicalException;
import lexicalAnalyzer.Token;

import java.io.IOException;
import java.util.Arrays;

public class SyntacticAnalyzer {
    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;

    public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws LexicalException, IOException, SyntacticException {
        this.lexicalAnalyzer = lexicalAnalyzer;
        currentToken = lexicalAnalyzer.getNextToken();
        Inicial();
    }

    private void match(String expected) throws SyntacticException, LexicalException, IOException {
        if(expected.equals(currentToken.getTokenType()))
            currentToken = lexicalAnalyzer.getNextToken();
        else
            throw new SyntacticException(currentToken, expected);
    }

    private boolean checkCurrentToken(String... validTokens){
        return Arrays.asList(validTokens).contains(currentToken.getTokenType());
    }

    private void Inicial() throws SyntacticException, LexicalException, IOException {
        if(checkCurrentToken("pr_interface", "pr_class"))
            ListaClases();
        else ;
            //TODO por ahora no hago nada
        match("EOF");
    }

    private void ListaClases() throws SyntacticException, LexicalException, IOException {
        if(checkCurrentToken("pr_interface", "pr_class")) {
            Clase();
            ListaClases();
        }
        else ;
            //TODO por ahora no hago nada, PREGUNTAR

    }

    private void Clase() throws SyntacticException, LexicalException, IOException {
        if(checkCurrentToken("pr_class"))
            ClaseConcreta();
        else if(checkCurrentToken("pr_interface"))
            Interface();
        else
            throw new SyntacticException(currentToken, "class o interface");
    }

    private void ClaseConcreta() throws SyntacticException, LexicalException, IOException {
        match("pr_class");
        ClaseGenerica();
        HeredaDe();
        ImplementaA();
        match("llaveA");
        ListaMiembros();
        match("llaveC");
    }

    private void ClaseGenerica() throws LexicalException, SyntacticException, IOException {
        match("idClase");
        GenericidadOpt();
    }

    private void GenericidadOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("op<")){
            match("op<");
            ListaTipoReferencia();
            match("op>");
        }else ;
            //TODO no hago nada por ahora
    }

    private void Interface() throws SyntacticException, LexicalException, IOException {
        match("pr_interface");
        ClaseGenerica();
        ExtiendeA();
        match("llaveA");
        ListaEncabezados();
        match("llaveC");
    }

    private void HeredaDe() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("pr_extends")){
            match("pr_extends");
            ClaseGenerica();
        }
        else ;
            //TODO no hago nada por ahora
    }

    private void ImplementaA() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("pr_implements")){
            match("pr_implements");
            ListaTipoReferencia();
        }
        else ;
            //TODO no hago nada por ahora
    }

    private void ExtiendeA() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("pr_extends")){
            match("pr_extends");
            ListaTipoReferencia();
        }
        else ;
            //TODO no hago nada por ahora
    }

    private void ListaTipoReferencia() throws LexicalException, SyntacticException, IOException {
        ClaseGenerica();
        RestoListaTipoRefOpt();
    }

    private void RestoListaTipoRefOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("coma")){
            match("coma");
            ListaTipoReferencia();
        }
        else ;
            //TODO no hago nada por ahora
    }

    private void ListaMiembros() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("pr_public", "pr_private", "pr_static", "pr_boolean",
                             "pr_char", "pr_int", "idClase", "pr_void")){
            Miembro();
            ListaMiembros();
        }
        else ;
            //TODO no hago nada por ahora

    }

    private void ListaEncabezados() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("pr_static", "pr_boolean", "pr_char", "pr_int", "idClase", "pr_void")){
            EncabezadoMetodo();
            match("puntoComa");
            ListaEncabezados();
        }
    }

    private void Miembro() throws SyntacticException, LexicalException, IOException {
        if(checkCurrentToken("pr_public", "pr_private"))
            Atributo();
        else if(checkCurrentToken("pr_static", "pr_boolean", "pr_char", "pr_int", "idClase", "pr_void"))
            Metodo();
        else
            throw new SyntacticException(currentToken, "visibilidad, static o tipo");
    }

    private void Atributo() throws LexicalException, SyntacticException, IOException {
        Visibilidad();
        Tipo();
        ListaDecAtrs();
        match("puntoComa");
    }

    private void Metodo() throws LexicalException, SyntacticException, IOException {
        EncabezadoMetodo();
        Bloque();
    }

    private void EncabezadoMetodo() throws LexicalException, SyntacticException, IOException {
        EstaticoOpt();
        TipoMetodo();
        match("idMetVar");
        ArgsFormales();
    }

    private void Visibilidad() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("pr_public"))
            match("pr_public");
        else if(checkCurrentToken("pr_private"))
            match("pr_private");
        else
            throw new SyntacticException(currentToken, "modificador de acceso");
    }

    private void Tipo() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("pr_boolean", "pr_char", "pr_int"))
            TipoPrimitivo();
        else if(checkCurrentToken("idClase"))
            ClaseGenerica();
        else
            throw new SyntacticException(currentToken, "tipo");
    }

    private void TipoPrimitivo() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("pr_boolean"))
            match("pr_boolean");
        else if(checkCurrentToken("pr_char"))
            match("pr_char");
        else if(checkCurrentToken("pr_int"))
            match("pr_int");
        else
            throw new SyntacticException(currentToken, "tipo primitvo");
    }

    private void ListaDecAtrs() throws LexicalException, SyntacticException, IOException {
        match("idMetVar");
        RestoListaDecAtrsOpt();
    }

    private void RestoListaDecAtrsOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("coma")){
            match("coma");
            ListaDecAtrs();
        }
        else ;
            //TODO no hago nada por ahora
    }

    private void EstaticoOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("pr_static"))
            match("pr_static");
        else ;
            //TODO no hago nada por ahora
    }

    private void TipoMetodo() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("pr_boolean", "pr_char", "pr_int", "idClase"))
            Tipo();
        else if(checkCurrentToken("pr_void"))
            match("pr_void");
        else
            throw new SyntacticException(currentToken, "tipo de metodo");
    }

    private void ArgsFormales() throws LexicalException, SyntacticException, IOException {
        match("parenA");
        ListaArgsFormalesOpt();
        match("parenC");
    }

    private void ListaArgsFormalesOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("pr_boolean", "pr_char", "pr_int", "idClase"))
            ListaArgsFormales();
        else ;
            //TODO no hago nada por ahora
    }

    private void ListaArgsFormales() throws LexicalException, SyntacticException, IOException {
        ArgFormal();
        RestoListaArgsFormalesOpt();
    }

    private void RestoListaArgsFormalesOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("coma")) {
            match("coma");
            ListaArgsFormales();
        }
        else ;
            //TODO no hago nada por ahora
    }

    private void ArgFormal() throws LexicalException, SyntacticException, IOException {
        Tipo();
        match("idMetVar");
    }

    private void Bloque() throws LexicalException, SyntacticException, IOException {
        match("llaveA");
        ListaSentencias();
        match("llaveC");
    }

    private void ListaSentencias() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("puntoComa", "pr_this", "idMetVar", "pr_new", "idClase", "parenA", "pr_var", "pr_return", "pr_if", "pr_while", "llaveA")){
            Sentencia();
            ListaSentencias();
        }else ;
            //TODO no hago nada por ahora
    }

    private void Sentencia() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("puntoComa"))
            match("puntoComa");
        else if(checkCurrentToken("pr_this", "idMetVar", "pr_new", "idClase", "parenA")){
            AsignacionOLlamada();
            match("puntoComa");
        }else if(checkCurrentToken("pr_var")){
            VarLocal();
            match("puntoComa");
        }else if(checkCurrentToken("pr_return")){
            Return();
            match("puntoComa");
        }else if(checkCurrentToken("pr_if"))
            If();
        else if(checkCurrentToken("pr_while"))
            While();
        else if(checkCurrentToken("llaveA"))
            Bloque();
        else
            throw new SyntacticException(currentToken, "sentencia");
    }

    private void AsignacionOLlamada() throws LexicalException, SyntacticException, IOException {
        Acceso();
        AsignacionOpt();
    }

    private void AsignacionOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("asig=", "asig+=", "asig-=")){
            TipoDeAsignacion();
            Expresion();
        }
        else ;
            //TODO no hago nada por ahora
    }

    private void TipoDeAsignacion() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("asig="))
            match("asig=");
        else if(checkCurrentToken("asig+="))
            match("asig+=");
        else if(checkCurrentToken("asig-="))
            match("asig-=");
        else
            throw new SyntacticException(currentToken, "asignacion");
    }

    private void VarLocal() throws LexicalException, SyntacticException, IOException {
        match("pr_var");
        match("idMetVar");
        match("asig=");
        Expresion();
    }

    private void Return() throws LexicalException, SyntacticException, IOException {
        match("pr_return");
        ExpresionOpt();
    }

    private void ExpresionOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("op+", "op-", "op!", "pr_null", "pr_true", "pr_false", "intLiteral",
                "charLiteral", "stringLiteral", "pr_this", "idMetVar", "pr_new", "idClase", "parenA"))
            Expresion();
        else ;
            //TODO no hago nada por ahora
    }

    private void If() throws LexicalException, SyntacticException, IOException {
        match("pr_if");
        match("parenA");
        Expresion();
        match("parenC");
        Sentencia();
        ElseOpt();
    }

    private void ElseOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("pr_else")){
            match("pr_else");
            Sentencia();
        }else ;
            //TODO no hago nada por ahora
    }

    private void While() throws LexicalException, SyntacticException, IOException {
        match("pr_while");
        match("parenA");
        Expresion();
        match("parenC");
        Sentencia();
    }

    private void Expresion() throws LexicalException, SyntacticException, IOException {
        ExpresionUnaria();
        RestoExpresion();
    }

    private void RestoExpresion() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("op||", "op&&", "op==", "op!=", "op<", "op>", "op<=", "op>=", "op+", "op-", "op*", "op/", "op%")){
            OperadorBinario();
            ExpresionUnaria();
            RestoExpresion();
        }
        else ;
            //TODO no hago nada por ahora
    }

    private void OperadorBinario() throws LexicalException, SyntacticException, IOException {
        // "op||", "op&&", "op==", "op!=", "op<", "op>", "op<=", "op>=", "op+", "op-", "op*", "op/", "op%"
        if(checkCurrentToken("op||"))
            match("op||");
        else if(checkCurrentToken("op&&"))
            match("op&&");
        else if(checkCurrentToken("op=="))
            match("op==");
        else if(checkCurrentToken("op!="))
            match("op!=");
        else if(checkCurrentToken("op<"))
            match("op<");
        else if(checkCurrentToken("op>"))
            match("op>");
        else if(checkCurrentToken("op<="))
            match("op<=");
        else if(checkCurrentToken("op>="))
            match("op>=");
        else if(checkCurrentToken("op+"))
            match("op+");
        else if(checkCurrentToken("op-"))
            match("op-");
        else if(checkCurrentToken("op*"))
            match("op*");
        else if(checkCurrentToken("op/"))
            match("op/");
        else if(checkCurrentToken("op%"))
            match("op%");
        else ;
            //TODO no hago nada por ahora
    }

    private void ExpresionUnaria() throws SyntacticException, LexicalException, IOException {
        if(checkCurrentToken("op+", "op-", "op!")){
            OperadorUnario();
            Operando();
        }else if(checkCurrentToken("pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral",
                "stringLiteral", "pr_this", "idMetVar", "pr_new", "idClase", "parenA"))
            Operando();
        else
            throw new SyntacticException(currentToken, "operando");
    }

    private void OperadorUnario() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("op+"))
            match("op+");
        else if(checkCurrentToken("op-"))
            match("op-");
        else if(checkCurrentToken("op!"))
            match("op!");
        else
            throw new SyntacticException(currentToken, "operador unario");
    }

    private void Operando() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral"))
            Literal();
        else if(checkCurrentToken("pr_this", "idMetVar", "pr_new", "idClase", "parenA"))
            Acceso();
        else
            throw new SyntacticException(currentToken, "literal o acceso");
    }

    private void Literal() throws LexicalException, SyntacticException, IOException {
        // null | true | false | intLiteral | charLiteral | stringLiteral
        if(checkCurrentToken("pr_null"))
            match("pr_null");
        else if(checkCurrentToken("pr_true"))
            match("pr_true");
        else if (checkCurrentToken("pr_false"))
            match("pr_false");
        else if(checkCurrentToken("intLiteral"))
            match("intLiteral");
        else if(checkCurrentToken("charLiteral"))
            match("charLiteral");
        else if(checkCurrentToken("stringLiteral"))
            match("stringLiteral");
        else
            throw new SyntacticException(currentToken, "literal");
    }

    private void Acceso() throws LexicalException, SyntacticException, IOException {
        Primario();
        EncadenadoOpt();
    }

    private void Primario() throws LexicalException, SyntacticException, IOException {
        //this, idMetVar, new, idClase, (
        if(checkCurrentToken("pr_this"))
            AccesoThis();
        else if(checkCurrentToken("idMetVar"))
            AccesoVarOMetodo();
        else if(checkCurrentToken("pr_new"))
            AccesoConstructor();
        else if(checkCurrentToken("idClase"))
            AccesoMetodoEstatico();
        else if(checkCurrentToken("parenA"))
            ExpresionParentizada();
        else
            throw new SyntacticException(currentToken, "acceso primario");
    }

    private void AccesoVarOMetodo() throws LexicalException, SyntacticException, IOException {
        match("idMetVar");
        ArgActualesMetodoOpt();
    }

    private void ArgActualesMetodoOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("parenA"))
            ArgsActuales();
        else ;
            //TODO no hago nada por ahora
    }

    private void AccesoThis() throws LexicalException, SyntacticException, IOException {
        match("pr_this");
    }

    private void AccesoConstructor() throws LexicalException, SyntacticException, IOException {
        match("pr_new");
        ClaseGenericaConstructor();
        match("parenA");
        match("parenC");
    }

    private void ClaseGenericaConstructor() throws LexicalException, SyntacticException, IOException {
        match("idClase");
        GenericidadConstructor();
    }

    private void GenericidadConstructor() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("op<")){
            match("op<");
            ListaTipoReferenciaOpt();
            match("op>");
        }else ;
            //TODO no hago nada por ahora
    }

    private void ListaTipoReferenciaOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("idClase"))
            ListaTipoReferencia();
        else ;
            //TODO no hago nada por ahora
    }

    private void ExpresionParentizada() throws LexicalException, SyntacticException, IOException {
        match("parenA");
        Expresion();
        match("parenC");
    }

    private void AccesoMetodoEstatico() throws LexicalException, SyntacticException, IOException {
        ClaseGenerica();
        match("punto");
        match("idMetVar");
        ArgsActuales();
    }

    private void ArgsActuales() throws LexicalException, SyntacticException, IOException {
        match("parenA");
        ListaExpsOpt();
        match("parenC");
    }

    private void ListaExpsOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("op+", "op-", "op!", "pr_null", "pr_true", "pr_false", "intLiteral",
                "charLiteral", "stringLiteral", "pr_this", "idMetVar", "pr_new", "idClase", "parenA"))
            ListaExps();
        else ;
            //TODO no hago nada por ahora
    }

    private void ListaExps() throws LexicalException, SyntacticException, IOException {
        Expresion();
        RestoListaExpsOpt();
    }

    private void RestoListaExpsOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("coma")) {
            match("coma");
            ListaExps();
        }else ;
            //TODO no hago nada por ahora
    }

    private void EncadenadoOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("punto"))
            VarOMetodoEncadenado();
        else ;
            //TODO no hago nada por ahora
    }

    private void VarOMetodoEncadenado() throws LexicalException, SyntacticException, IOException {
        match("punto");
        match("idMetVar");
        ArgActualesMetodoOpt();
        EncadenadoOpt();
    }
}
