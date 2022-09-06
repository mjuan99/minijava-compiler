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
            //TODO por ahora no hago nada, PREGUNTAR
        match("EOF"); //TODO PREGUNTAR
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
        match("idClase");
        HeredaDe();
        ImplementaA();
        match("llaveA");
        ListaMiembros();
        match("llaveC");
    }

    private void Interface() throws SyntacticException, LexicalException, IOException {
        match("pr_interface");
        match("idClase");
        ExtiendeA();
        match("llaveA");
        ListaEncabezados();
        match("llaveC");
    }

    private void HeredaDe() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("pr_extends")){
            match("pr_extends");
            match("idClase");
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
        match("idClase");
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
            match("idClase");
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

    private void ListaSentencias(){
        //TODO implementar
    }

}
