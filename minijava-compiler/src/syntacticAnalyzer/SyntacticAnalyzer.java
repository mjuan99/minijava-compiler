package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.LexicalException;
import lexicalAnalyzer.Token;

import java.io.IOException;
import java.util.Arrays;

public class SyntacticAnalyzer {
    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;
    private final static boolean useAdvancedImplementation = true;

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

    private boolean invalidEpsilon(String... validTokens){
        if(useAdvancedImplementation)
            return !checkCurrentToken(validTokens);
        else
            return false;

    }

    private void Inicial() throws SyntacticException, LexicalException, IOException {
        if(checkCurrentToken("interface", "class"))
            ListaClases();

        if(checkCurrentToken("EOF"))
            match("EOF");
        else
            throw new SyntacticException(currentToken, "declaracion de clase o interfaz");
    }

    private void ListaClases() throws SyntacticException, LexicalException, IOException {
        if(checkCurrentToken("interface", "class")) {
            Clase();
            ListaClases();
        }
        else if(invalidEpsilon("EOF"))
            throw new SyntacticException(currentToken, "declaracion de clase o interfaz");
            //TODO por ahora no hago nada
    }

    private void Clase() throws SyntacticException, LexicalException, IOException {
        if(checkCurrentToken("class"))
            ClaseConcreta();
        else if(checkCurrentToken("interface"))
            Interface();
        else
            throw new SyntacticException(currentToken, "class o interface");
    }

    private void ClaseConcreta() throws SyntacticException, LexicalException, IOException {
        match("class");
        ClaseGenerica();
        HeredaDe();
        ImplementaA();
        match("{");
        ListaMiembros();
        match("}");
    }

    private void ClaseGenerica() throws LexicalException, SyntacticException, IOException {
        match("idClase");
        GenericidadOpt();
    }

    private void GenericidadOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("<")){
            match("<");
            ListaTipoReferencia();
            match(">");
        }else if(invalidEpsilon("extends", "implements", ",", "idMetVar", ".", ">", "{"))
            throw new SyntacticException(currentToken, "<, extends, implements, ',', idMetVar, ., > o {");
            //TODO no hago nada por ahora
    }

    private void Interface() throws SyntacticException, LexicalException, IOException {
        match("interface");
        ClaseGenerica();
        ExtiendeA();
        match("{");
        ListaEncabezados();
        match("}");
    }

    private void HeredaDe() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("extends")){
            match("extends");
            ClaseGenerica();
        }
        else if(invalidEpsilon("implements", "{"))
            throw new SyntacticException(currentToken, "extends, implements o {");
            //TODO no hago nada por ahora
    }

    private void ImplementaA() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("implements")){
            match("implements");
            ListaTipoReferencia();
        }
        else if(invalidEpsilon("{"))
            throw new SyntacticException(currentToken, "implements o {");
            //TODO no hago nada por ahora
    }

    private void ExtiendeA() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("extends")){
            match("extends");
            ListaTipoReferencia();
        }
        else if(invalidEpsilon("{"))
            throw new SyntacticException(currentToken, "extends o {");
            //TODO no hago nada por ahora
    }

    private void ListaTipoReferencia() throws LexicalException, SyntacticException, IOException {
        ClaseGenerica();
        RestoListaTipoRefOpt();
    }

    private void RestoListaTipoRefOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken(",")){
            match(",");
            ListaTipoReferencia();
        }
        else if(invalidEpsilon(">", "{"))
            throw new SyntacticException(currentToken, ",, > o {");
            //TODO no hago nada por ahora
    }

    private void ListaMiembros() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("public", "private", "static", "boolean",
                             "char", "int", "idClase", "void")){
            Miembro();
            ListaMiembros();
        }
        else if(invalidEpsilon("}"))
            throw new SyntacticException(currentToken, "declaracion de miembro o }");
            //TODO no hago nada por ahora

    }

    private void ListaEncabezados() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("static", "boolean", "char", "int", "idClase", "void")){
            EncabezadoMetodo();
            match(";");
            ListaEncabezados();
        }
    }

    private void Miembro() throws SyntacticException, LexicalException, IOException {
        if(checkCurrentToken("public", "private"))
            AtributoConVisibilidad();
        else if(checkCurrentToken("static"))
            MetodoEstatico();
        else if(checkCurrentToken("void"))
            MetodoNoEstVoid();
        else if(checkCurrentToken("boolean", "char", "int"))
            AtributoOMetodoTipoPri();
        else if(checkCurrentToken("idClase"))
            AtributoTCOMetodoTCOConstructor();
        else
            throw new SyntacticException(currentToken, "atributo, metodo o constructor");
    }

    private void AtributoConVisibilidad() throws LexicalException, SyntacticException, IOException {
        Visibilidad();
        Tipo();
        ListaDecAtrs();
        match(";");
    }

    private void MetodoEstatico() throws LexicalException, SyntacticException, IOException {
        match("static");
        TipoMetodo();
        match("idMetVar");
        ArgsFormales();
        Bloque();
    }

    private void MetodoNoEstVoid() throws LexicalException, SyntacticException, IOException {
        match("void");
        match("idMetVar");
        ArgsFormales();
        Bloque();
    }

    private void AtributoOMetodoTipoPri() throws LexicalException, SyntacticException, IOException {
        TipoPrimitivo();
        match("idMetVar");
        RestoAtrOMet();
    }

    private void RestoAtrOMet() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken(",", ";")){
            RestoListaDecAtrsOpt();
            match(";");
        }else if(checkCurrentToken("(")) {
            ArgsFormales();
            Bloque();
        }else
            throw new SyntacticException(currentToken, "lista de atributos o argumentos formales");
    }

    private void AtributoTCOMetodoTCOConstructor() throws LexicalException, SyntacticException, IOException {
        match("idClase");
        RestoAtrTCOMetTCOCons();
    }

    private void RestoAtrTCOMetTCOCons() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("<", "idMetVar")){
            GenericidadOpt();
            match("idMetVar");
            RestoAtrOMet();
        }else if(checkCurrentToken("("))
            RestoConstructor();
        else
            throw new SyntacticException(currentToken, "identificador de metodo o variable o argumentos de constructor");
    }

    private void RestoConstructor() throws LexicalException, SyntacticException, IOException {
        ArgsFormales();
        Bloque();
    }

    private void EncabezadoMetodo() throws LexicalException, SyntacticException, IOException {
        EstaticoOpt();
        TipoMetodo();
        match("idMetVar");
        ArgsFormales();
    }

    private void Visibilidad() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("public"))
            match("public");
        else if(checkCurrentToken("private"))
            match("private");
        else
            throw new SyntacticException(currentToken, "modificador de acceso");
    }

    private void Tipo() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("boolean", "char", "int"))
            TipoPrimitivo();
        else if(checkCurrentToken("idClase"))
            ClaseGenerica();
        else
            throw new SyntacticException(currentToken, "tipo");
    }

    private void TipoPrimitivo() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("boolean"))
            match("boolean");
        else if(checkCurrentToken("char"))
            match("char");
        else if(checkCurrentToken("int"))
            match("int");
        else
            throw new SyntacticException(currentToken, "tipo primitvo");
    }

    private void ListaDecAtrs() throws LexicalException, SyntacticException, IOException {
        match("idMetVar");
        RestoListaDecAtrsOpt();
    }

    private void RestoListaDecAtrsOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken(",")){
            match(",");
            ListaDecAtrs();
        }
        else if(invalidEpsilon(";"))
            throw new SyntacticException(currentToken, ", o ;");
            //TODO no hago nada por ahora
    }

    private void EstaticoOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("static"))
            match("static");
        else if(invalidEpsilon("boolean", "char", "int", "idClase", "void"))
            throw new SyntacticException(currentToken, "static, boolean, char, int, void o identificador de clase");
            //TODO no hago nada por ahora
    }

    private void TipoMetodo() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("boolean", "char", "int", "idClase"))
            Tipo();
        else if(checkCurrentToken("void"))
            match("void");
        else
            throw new SyntacticException(currentToken, "tipo de metodo");
    }

    private void ArgsFormales() throws LexicalException, SyntacticException, IOException {
        match("(");
        ListaArgsFormalesOpt();
        match(")");
    }

    private void ListaArgsFormalesOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("boolean", "char", "int", "idClase"))
            ListaArgsFormales();
        else if(invalidEpsilon(")"))
            throw new SyntacticException(currentToken, "boolean, char, int, identificador de clase o )");
            //TODO no hago nada por ahora
    }

    private void ListaArgsFormales() throws LexicalException, SyntacticException, IOException {
        ArgFormal();
        RestoListaArgsFormalesOpt();
    }

    private void RestoListaArgsFormalesOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken(",")) {
            match(",");
            ListaArgsFormales();
        }
        else if(invalidEpsilon(")"))
            throw new SyntacticException(currentToken, ", o )");
            //TODO no hago nada por ahora
    }

    private void ArgFormal() throws LexicalException, SyntacticException, IOException {
        Tipo();
        match("idMetVar");
    }

    private void Bloque() throws LexicalException, SyntacticException, IOException {
        match("{");
        ListaSentencias();
        match("}");
    }

    private void ListaSentencias() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken(";", "this", "idMetVar", "new", "idClase", "(", "var",
                "return", "if", "while", "{", "boolean", "char", "int")){
            Sentencia();
            ListaSentencias();
        }else if(invalidEpsilon("}"))
            throw new SyntacticException(currentToken, "sentencia o }");
            //TODO no hago nada por ahora
    }

    private void Sentencia() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken(";"))
            match(";");
        else if(checkCurrentToken("this", "idMetVar", "new", "idClase", "(",
                "boolean", "char", "int")){
            AsignacionOLlamadaOVarClasica();
            match(";");
        }else if(checkCurrentToken("var")){
            VarLocal();
            match(";");
        }else if(checkCurrentToken("return")){
            Return();
            match(";");
        }else if(checkCurrentToken("if"))
            If();
        else if(checkCurrentToken("while"))
            While();
        else if(checkCurrentToken("{"))
            Bloque();
        else
            throw new SyntacticException(currentToken, "sentencia");
    }

    private void AsignacionOLlamadaOVarClasica() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("this", "idMetVar", "new", "(")){
            AccesoNoMetEstatico();
            AsignacionOpt();
        }else if(checkCurrentToken("boolean", "char", "int")){
            TipoPrimitivo();
            ListaDeclaraciones();
        }else if(checkCurrentToken("idClase"))
            VarClaseOAccesoMetEstaticoYAsignacionOpt();
        else
            throw new SyntacticException(currentToken, "nacion, declaracion de variable o llamada");
    }

    private void ListaDeclaraciones() throws LexicalException, SyntacticException, IOException {
        Declaracion();
        RestoListaDeclOpt();
    }

    private void RestoListaDeclOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken(",")){
            match(",");
            ListaDeclaraciones();
        }else if(invalidEpsilon(";"))
            throw new SyntacticException(currentToken, ", o ;");
            //TODO no hago nada por ahora
    }

    private void Declaracion() throws LexicalException, SyntacticException, IOException {
        match("idMetVar");
        InicializacionOpt();
    }

    private void InicializacionOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("=")){
            match("=");
            Expresion();
        }else if(invalidEpsilon(",", ";"))
            throw new SyntacticException(currentToken, ", o ;");
            //TODO no hago nada por ahora
    }

    private void VarClaseOAccesoMetEstaticoYAsignacionOpt() throws LexicalException, SyntacticException, IOException {
        ClaseGenerica();
        ListaDecORestoAccesoMetEstYAsigOpt();
    }

    private void ListaDecORestoAccesoMetEstYAsigOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("idMetVar"))
            ListaDeclaraciones();
        else if(checkCurrentToken(".")){
            RestoAccesoMetEst();
            AsignacionOpt();
        }else
            throw new SyntacticException(currentToken, "identificador de variable o llamada a método estático");
    }

    private void RestoAccesoMetEst() throws LexicalException, SyntacticException, IOException {
        match(".");
        match("idMetVar");
        ArgsActuales();
        EncadenadoOpt();
    }

    private void AsignacionOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("=", "+=", "-=")){
            TipoDeAsignacion();
            Expresion();
        }
        else if(invalidEpsilon(";"))
            throw new SyntacticException(currentToken, "nacion o ;");
            //TODO no hago nada por ahora
    }

    private void TipoDeAsignacion() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("="))
            match("=");
        else if(checkCurrentToken("+="))
            match("+=");
        else if(checkCurrentToken("-="))
            match("-=");
        else
            throw new SyntacticException(currentToken, "nacion");
    }

    private void VarLocal() throws LexicalException, SyntacticException, IOException {
        match("var");
        match("idMetVar");
        match("=");
        Expresion();
    }

    private void Return() throws LexicalException, SyntacticException, IOException {
        match("return");
        ExpresionOpt();
    }

    private void ExpresionOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("+", "-", "!", "null", "true", "false", "intLiteral",
                "charLiteral", "stringLiteral", "this", "idMetVar", "new", "idClase", "("))
            Expresion();
        else if(invalidEpsilon(";"))
            throw new SyntacticException(currentToken, "expresion o ;");
            //TODO no hago nada por ahora
    }

    private void If() throws LexicalException, SyntacticException, IOException {
        match("if");
        match("(");
        Expresion();
        match(")");
        Sentencia();
        ElseOpt();
    }

    private void ElseOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("else")){
            match("else");
            Sentencia();
        }else if(invalidEpsilon(";", "this", "idMetVar", "new", "(", "boolean", "char",
                "int", "idClase", "var", "return", "if", "while", "{", "}", "else"))
            throw new SyntacticException(currentToken, "else, sentencia o }");
            //TODO no hago nada por ahora
    }

    private void While() throws LexicalException, SyntacticException, IOException {
        match("while");
        match("(");
        Expresion();
        match(")");
        Sentencia();
    }

    private void Expresion() throws LexicalException, SyntacticException, IOException {
        ExpresionUnaria();
        RestoExpresion();
    }

    private void RestoExpresion() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*", "/", "%")){
            OperadorBinario();
            ExpresionUnaria();
            RestoExpresion();
        }
        else if(invalidEpsilon(",", ";", ")"))
            throw new SyntacticException(currentToken, "erador binario, ,, ) o ;");
            //TODO no hago nada por ahora
    }

    private void OperadorBinario() throws LexicalException, SyntacticException, IOException {
        // "||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*", "/", "%"
        if(checkCurrentToken("||"))
            match("||");
        else if(checkCurrentToken("&&"))
            match("&&");
        else if(checkCurrentToken("=="))
            match("==");
        else if(checkCurrentToken("!="))
            match("!=");
        else if(checkCurrentToken("<"))
            match("<");
        else if(checkCurrentToken(">"))
            match(">");
        else if(checkCurrentToken("<="))
            match("<=");
        else if(checkCurrentToken(">="))
            match(">=");
        else if(checkCurrentToken("+"))
            match("+");
        else if(checkCurrentToken("-"))
            match("-");
        else if(checkCurrentToken("*"))
            match("*");
        else if(checkCurrentToken("/"))
            match("/");
        else if(checkCurrentToken("%"))
            match("%");
        else throw new SyntacticException(currentToken, "erador binario");
    }

    private void ExpresionUnaria() throws SyntacticException, LexicalException, IOException {
        if(checkCurrentToken("+", "-", "!")){
            OperadorUnario();
            Operando();
        }else if(checkCurrentToken("null", "true", "false", "intLiteral", "charLiteral",
                "stringLiteral", "this", "idMetVar", "new", "idClase", "("))
            Operando();
        else
            throw new SyntacticException(currentToken, "erando");
    }

    private void OperadorUnario() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("+"))
            match("+");
        else if(checkCurrentToken("-"))
            match("-");
        else if(checkCurrentToken("!"))
            match("!");
        else
            throw new SyntacticException(currentToken, "erador unario");
    }

    private void Operando() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("null", "true", "false", "intLiteral", "charLiteral", "stringLiteral"))
            Literal();
        else if(checkCurrentToken("this", "idMetVar", "new", "idClase", "("))
            Acceso();
        else
            throw new SyntacticException(currentToken, "literal o acceso");
    }

    private void Literal() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("null"))
            match("null");
        else if(checkCurrentToken("true"))
            match("true");
        else if (checkCurrentToken("false"))
            match("false");
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
        if(checkCurrentToken("idClase"))
            AccesoMetodoEstatico();
        else if(checkCurrentToken("this", "idMetVar", "new", "("))
            AccesoNoMetEstatico();
        else
            throw new SyntacticException(currentToken, "acceso");
    }

    private void AccesoNoMetEstatico() throws LexicalException, SyntacticException, IOException {
        PrimarioNoMetEstatico();
        EncadenadoOpt();
    }

    private void AccesoMetodoEstatico() throws LexicalException, SyntacticException, IOException {
        ClaseGenerica();
        match(".");
        match("idMetVar");
        ArgsActuales();
        EncadenadoOpt();
    }

    private void PrimarioNoMetEstatico() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("this"))
            AccesoThis();
        else if(checkCurrentToken("idMetVar"))
            AccesoVarOMetodo();
        else if(checkCurrentToken("new"))
            AccesoConstructor();
        else if(checkCurrentToken("("))
            ExpresionParentizada();
        else
            throw new SyntacticException(currentToken, "acceso primario");
    }

    private void AccesoVarOMetodo() throws LexicalException, SyntacticException, IOException {
        match("idMetVar");
        ArgActualesMetodoOpt();
    }

    private void ArgActualesMetodoOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("("))
            ArgsActuales();
        else if(invalidEpsilon("=", "+=", "-=", ";", "||", "&&", "==", "!=", "<",
                ">", "<=", ">=", "+", "-", "*", "/", "%", ",", ")", "."))
            throw new SyntacticException(currentToken, "argumentos actuales, asignacion, operador binario, ,, ) o ;");
            //TODO no hago nada por ahora
    }

    private void AccesoThis() throws LexicalException, SyntacticException, IOException {
        match("this");
    }

    private void AccesoConstructor() throws LexicalException, SyntacticException, IOException {
        match("new");
        ClaseGenericaConstructor();
        ArgsActuales();
    }

    private void ClaseGenericaConstructor() throws LexicalException, SyntacticException, IOException {
        match("idClase");
        GenericidadConstructor();
    }

    private void GenericidadConstructor() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("<")){
            match("<");
            ListaTipoReferenciaOpt();
            match(">");
        }else if(invalidEpsilon("("))
            throw new SyntacticException(currentToken, "genericidad o argumentos actuales");
            //TODO no hago nada por ahora
    }

    private void ListaTipoReferenciaOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("idClase"))
            ListaTipoReferencia();
        else if(invalidEpsilon(">"))
            throw new SyntacticException(currentToken, "identificador de clase o >");
            //TODO no hago nada por ahora
    }

    private void ExpresionParentizada() throws LexicalException, SyntacticException, IOException {
        match("(");
        Expresion();
        match(")");
    }

    private void ArgsActuales() throws LexicalException, SyntacticException, IOException {
        match("(");
        ListaExpsOpt();
        match(")");
    }

    private void ListaExpsOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("+", "-", "!", "null", "true", "false", "intLiteral",
                "charLiteral", "stringLiteral", "this", "idMetVar", "new", "idClase", "("))
            ListaExps();
        else if(invalidEpsilon(")"))
            throw new SyntacticException(currentToken, "expresion o )");
            //TODO no hago nada por ahora
    }

    private void ListaExps() throws LexicalException, SyntacticException, IOException {
        Expresion();
        RestoListaExpsOpt();
    }

    private void RestoListaExpsOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken(",")) {
            match(",");
            ListaExps();
        }else if(invalidEpsilon(")"))
            throw new SyntacticException(currentToken, ", o )");
            //TODO no hago nada por ahora
    }

    private void EncadenadoOpt() throws LexicalException, SyntacticException, IOException {
        if(checkCurrentToken("."))
            VarOMetodoEncadenado();
        else if(invalidEpsilon("=", "+=", "-=", ";", "||", "&&", "==", "!=", "<",
                ">", "<=", ">=", "+", "-", "*", "/", "%", ",", ")"))
            throw new SyntacticException(currentToken, "encadenado, asignacion, operador binario, ,, ) o ;");
            //TODO no hago nada por ahora
    }

    private void VarOMetodoEncadenado() throws LexicalException, SyntacticException, IOException {
        match(".");
        match("idMetVar");
        ArgActualesMetodoOpt();
        EncadenadoOpt();
    }
}
