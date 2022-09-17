package syntacticAnalyzer;

import Errors.CompilerError;
import Errors.SyntacticException;
import Errors.LexicalError;
import Errors.SyntacticError;
import lexicalAnalyzer.LexicalAnalyzer;
import Errors.LexicalException;
import lexicalAnalyzer.Token;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

public class SyntacticAnalyzer {
    private final LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;
    private final static boolean useAdvancedImplementation = true;
    private final LinkedList<CompilerError> compilerErrorList;

    public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws IOException, SyntacticException {
        this.lexicalAnalyzer = lexicalAnalyzer;
        compilerErrorList = new LinkedList<>();
        updateCurrentToken();
        try {
            Inicial();
        }catch (EOFException ignored){}
        throwExceptionIfErrorsWereFound();
    }

    private void addError(CompilerError error){
        compilerErrorList.add(error);
    }

    private void throwExceptionIfErrorsWereFound() throws SyntacticException {
        if(!compilerErrorList.isEmpty())
            throw new SyntacticException(compilerErrorList);
    }

    private void match(String expected) throws IOException, SyntacticException {
        if(expected.equals(currentToken.getTokenType()))
            updateCurrentToken();
        else{
            addError(new SyntacticError(currentToken, expected));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void discardTokensUntilValidTokenIsFound(String... validTokens) throws IOException {
        while(!currentToken.getTokenType().equals("EOF") && !checkCurrentToken(validTokens))
            updateCurrentToken();
        if(checkCurrentToken("EOF"))
            throw new EOFException();
    }

    private void updateCurrentToken() throws IOException{
        if(currentToken != null && Objects.equals(currentToken.getTokenType(), "EOF"))
            throw new EOFException();
        try{
            currentToken = lexicalAnalyzer.getNextToken();
        } catch (LexicalException e) {
            addError(new LexicalError(e.getLexeme(), e.getLineNumber()));
            updateCurrentToken();
        }
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

    private void Inicial() throws IOException {
        if(checkCurrentToken("interface", "class"))
            ListaClases();
        else{
            addError(new SyntacticError(currentToken, "declaracion de clase o interfaz"));
            discardTokensUntilValidTokenIsFound("}");
            if(checkCurrentToken("}")) {
                updateCurrentToken();
                ListaClases();
            }
        }

        if(!checkCurrentToken("EOF")){
            addError(new SyntacticError(currentToken, "declaracion de clase o interfaz"));
        }
    }

    private void ListaClases() throws IOException {
        if(checkCurrentToken("interface", "class")) {
            Clase();
            ListaClases();
        }
        else if(invalidEpsilon("EOF")) {
            addError(new SyntacticError(currentToken, "declaracion de clase o interfaz"));
            discardTokensUntilValidTokenIsFound("class", "interface");
        }
    }

    private void Clase() throws IOException {
        if(checkCurrentToken("class"))
            ClaseConcreta();
        else if(checkCurrentToken("interface"))
            Interface();
        else {
            addError(new SyntacticError(currentToken, "declaracion de clase o interfaz"));
            discardTokensUntilValidTokenIsFound("class", "interface");
        }
    }

    private void ClaseConcreta() throws IOException {
        try {
            match("class");
            ClaseGenerica();
            HeredaDe();
            ImplementaA();
            match("{");
        }catch (SyntacticException e){
            discardTokensUntilValidTokenIsFound("{");
            updateCurrentToken();
        }
        ListaMiembros();
        try {
            match("}");
        }catch (SyntacticException e){
            discardTokensUntilValidTokenIsFound("}");
            updateCurrentToken();
        }
    }

    private void ClaseGenerica() throws IOException, SyntacticException {
        match("idClase");
        GenericidadOpt();
    }

    private void GenericidadOpt() throws IOException, SyntacticException {
        if(checkCurrentToken("<")){
            match("<");
            try{
                ListaTipoReferencia();
                match(">");
            }catch (SyntacticException e){
                discardTokensUntilValidTokenIsFound(">");
                updateCurrentToken();
            }
        }else if(invalidEpsilon("extends", "implements", ",", "idMetVar", ">", "{")) {
            addError(new SyntacticError(currentToken, "<, extends, implements, ',', idMetVar, ., > o {"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void Interface() throws IOException {
        try {
            match("interface");
            ClaseGenerica();
            ExtiendeA();
            match("{");
            ListaEncabezados();
            match("}");
        }catch(SyntacticException e){
            discardTokensUntilValidTokenIsFound("}");
            updateCurrentToken();
        }
    }

    private void HeredaDe() throws IOException, SyntacticException {
        if(checkCurrentToken("extends")){
            match("extends");
            ClaseGenerica();
        }
        else if(invalidEpsilon("implements", "{")) {
            addError(new SyntacticError(currentToken, "extends, implements o {"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void ImplementaA() throws IOException, SyntacticException {
        if(checkCurrentToken("implements")){
            match("implements");
            ListaTipoReferencia();
        }
        else if(invalidEpsilon("{")) {
            addError(new SyntacticError(currentToken, "implements o {"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void ExtiendeA() throws IOException, SyntacticException {
        if(checkCurrentToken("extends")){
            match("extends");
            ListaTipoReferencia();
        }
        else if(invalidEpsilon("{")) {
            addError(new SyntacticError(currentToken, "extends o {"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void ListaTipoReferencia() throws IOException, SyntacticException {
        ClaseGenerica();
        RestoListaTipoRefOpt();
    }

    private void RestoListaTipoRefOpt() throws IOException, SyntacticException {
        if(checkCurrentToken(",")){
            match(",");
            ListaTipoReferencia();
        }
        else if(invalidEpsilon(">", "{")) {
            addError(new SyntacticError(currentToken, "',', > o {"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void ListaMiembros() throws IOException {
        if(checkCurrentToken("public", "private", "static", "boolean",
                             "char", "int", "idClase", "void")){
            Miembro();
            ListaMiembros();
        }
        else if(invalidEpsilon("}")) {
            addError(new SyntacticError(currentToken, "declaracion de atributo, metodo o constructor o }"));
            if(checkCurrentToken("{"))
                try {
                    Bloque();
                }catch (SyntacticException ignored){}
            else
                updateCurrentToken();
            ListaMiembros();
        }

    }

    private void ListaEncabezados() throws IOException {
        if(checkCurrentToken("static", "boolean", "char", "int", "idClase", "void")){
            try{
                EncabezadoMetodo();
                match(";");
            }catch (SyntacticException e){
                updateCurrentToken();
            }
            ListaEncabezados();
        }else if(invalidEpsilon("}")){
            addError(new SyntacticError(currentToken, "encabezado de metodo"));
            updateCurrentToken();
            ListaEncabezados();
        }
    }

    private void Miembro() throws IOException {
        try {
            if (checkCurrentToken("public", "private"))
                AtributoConVisibilidad();
            else if (checkCurrentToken("static"))
                MetodoEstatico();
            else if (checkCurrentToken("void"))
                MetodoNoEstVoid();
            else if (checkCurrentToken("boolean", "char", "int"))
                AtributoOMetodoTipoPri();
            else if (checkCurrentToken("idClase"))
                AtributoTCOMetodoTCOConstructor();
            else {
                addError(new SyntacticError(currentToken, "declaracion de atributo, metodo o constructor"));
                if (checkCurrentToken("{"))
                    Bloque();
                else
                    updateCurrentToken();
            }
        }catch (SyntacticException e){
            if(checkCurrentToken("{"))
                try {
                    Bloque();
                }catch (SyntacticException ignored){}
            else
                updateCurrentToken();
        }
    }

    private void AtributoConVisibilidad() throws IOException, SyntacticException {
        Visibilidad();
        Tipo();
        ListaDecAtrs();
        match(";");
    }

    private void MetodoEstatico() throws IOException, SyntacticException {
        match("static");
        TipoMetodoEstatico();
        match("idMetVar");
        ArgsFormales();
        Bloque();
    }

    private void MetodoNoEstVoid() throws IOException, SyntacticException {
        match("void");
        match("idMetVar");
        ArgsFormales();
        Bloque();
    }

    private void AtributoOMetodoTipoPri() throws IOException, SyntacticException {
        TipoPrimitivo();
        match("idMetVar");
        RestoAtrOMet();
    }

    private void RestoAtrOMet() throws IOException, SyntacticException {
        if(checkCurrentToken(",", ";")){
            RestoListaDecAtrsOpt();
            match(";");
        }else if(checkCurrentToken("(")) {
            ArgsFormales();
            Bloque();
        }else {
            addError(new SyntacticError(currentToken, "',', ; o argumentos formales"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void AtributoTCOMetodoTCOConstructor() throws IOException, SyntacticException {
        match("idClase");
        RestoAtrTCOMetTCOCons();
    }

    private void RestoAtrTCOMetTCOCons() throws IOException, SyntacticException {
        if(checkCurrentToken("<", "idMetVar")){
            GenericidadOpt();
            match("idMetVar");
            RestoAtrOMet();
        }else if(checkCurrentToken("("))
            RestoConstructor();
        else {
            addError(new SyntacticError(currentToken, "identificador de metodo o variable o argumentos de constructor"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void RestoConstructor() throws IOException, SyntacticException {
        ArgsFormales();
        Bloque();
    }

    private void EncabezadoMetodo() throws IOException, SyntacticException {
        boolean isStatic = checkCurrentToken("static");
        EstaticoOpt();
        if(isStatic)
            TipoMetodoEstatico();
        else
            TipoMetodo();
        match("idMetVar");
        ArgsFormales();
    }

    private void Visibilidad() throws IOException, SyntacticException {
        if(checkCurrentToken("public"))
            match("public");
        else if(checkCurrentToken("private"))
            match("private");
        else {
            addError(new SyntacticError(currentToken, "modificador de acceso"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void Tipo() throws IOException, SyntacticException {
        if(checkCurrentToken("boolean", "char", "int"))
            TipoPrimitivo();
        else if(checkCurrentToken("idClase"))
            ClaseGenerica();
        else {
            addError(new SyntacticError(currentToken, "tipo"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void TipoPrimitivo() throws IOException, SyntacticException {
        if(checkCurrentToken("boolean"))
            match("boolean");
        else if(checkCurrentToken("char"))
            match("char");
        else if(checkCurrentToken("int"))
            match("int");
        else {
            addError(new SyntacticError(currentToken, "tipo primitvo"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void ListaDecAtrs() throws IOException, SyntacticException {
        match("idMetVar");
        RestoListaDecAtrsOpt();
    }

    private void RestoListaDecAtrsOpt() throws IOException, SyntacticException {
        if(checkCurrentToken(",")){
            match(",");
            ListaDecAtrs();
        }
        else if(invalidEpsilon(";")) {
            addError(new SyntacticError(currentToken, "',' o ;"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void EstaticoOpt() throws IOException, SyntacticException {
        if(checkCurrentToken("static"))
            match("static");
        else if(invalidEpsilon("boolean", "char", "int", "idClase", "void")) {
            addError(new SyntacticError(currentToken, "tipo de metodo"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void TipoMetodo() throws IOException, SyntacticException {
        if(checkCurrentToken("boolean", "char", "int", "idClase"))
            Tipo();
        else if(checkCurrentToken("void"))
            match("void");
        else {
            addError(new SyntacticError(currentToken, "tipo de metodo"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void TipoMetodoEstatico() throws IOException, SyntacticException {
        if(checkCurrentToken("boolean", "char", "int"))
            TipoPrimitivo();
        else if(checkCurrentToken("idClase"))
            match("idClase");
        else if(checkCurrentToken("void"))
            match("void");
        else {
            addError(new SyntacticError(currentToken, "tipo de metodo estatico"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void ArgsFormales() throws IOException, SyntacticException {
        match("(");
        ListaArgsFormalesOpt();
        match(")");
    }

    private void ListaArgsFormalesOpt() throws IOException, SyntacticException {
        if(checkCurrentToken("boolean", "char", "int", "idClase"))
            ListaArgsFormales();
        else if(invalidEpsilon(")")) {
            addError(new SyntacticError(currentToken, "tipo o )"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void ListaArgsFormales() throws IOException, SyntacticException {
        ArgFormal();
        RestoListaArgsFormalesOpt();
    }

    private void RestoListaArgsFormalesOpt() throws IOException, SyntacticException {
        if(checkCurrentToken(",")) {
            match(",");
            ListaArgsFormales();
        }
        else if(invalidEpsilon(")")) {
            addError(new SyntacticError(currentToken, "',' o )"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void ArgFormal() throws IOException, SyntacticException {
        Tipo();
        match("idMetVar");
    }

    private void Bloque() throws IOException, SyntacticException {
        match("{");
        ListaSentencias();
        try{
            match("}");
        }catch (SyntacticException e){
            discardTokensUntilValidTokenIsFound("}");
            updateCurrentToken();
        }
    }

    private void ListaSentencias() throws IOException {
        if(checkCurrentToken(";", "this", "idMetVar", "new", "idClase", "(", "var",
                "return", "if", "while", "{", "boolean", "char", "int")){
            Sentencia();
            ListaSentencias();
        }else if(invalidEpsilon("}")) {
            addError(new SyntacticError(currentToken, "sentencia o }"));
            discardTokensUntilValidTokenIsFound("}", ";", "this", "idMetVar", "new",
                    "idClase", "(", "var", "return", "if", "while", "{", "boolean", "char", "int");
            if(!checkCurrentToken("}")){
                ListaSentencias();
            }
        }
    }

    private void Sentencia() throws IOException {
        if(checkCurrentToken(";"))
            updateCurrentToken();
        else if(checkCurrentToken("this", "idMetVar", "new", "idClase", "(",
                "boolean", "char", "int")){
            try {
                AsignacionOLlamadaOVarClasica();
                match(";");
            }catch (SyntacticException e){
                discardTokensUntilValidTokenIsFound(";");
                updateCurrentToken();
            }
        }else if(checkCurrentToken("var")){
            try {
                VarLocal();
                match(";");
            }catch (SyntacticException e){
                discardTokensUntilValidTokenIsFound(";");
                updateCurrentToken();
            }
        }else if(checkCurrentToken("return")){
            try {
                Return();
                match(";");
            }catch (SyntacticException e){
                discardTokensUntilValidTokenIsFound(";");
                updateCurrentToken();
            }
        }else if(checkCurrentToken("if"))
            If();
        else if(checkCurrentToken("while"))
            While();
        else if(checkCurrentToken("{"))
            try {
                Bloque();
            }catch (SyntacticException ignored){}
        else {
            addError(new SyntacticError(currentToken, "sentencia"));
            discardTokensUntilValidTokenIsFound("}", ";");
            if(checkCurrentToken(";"))
                updateCurrentToken();
        }
    }

    private void AsignacionOLlamadaOVarClasica() throws IOException, SyntacticException {
        if(checkCurrentToken("this", "idMetVar", "new", "(")){
            AccesoNoMetEstatico();
            AsignacionOpt();
        }else if(checkCurrentToken("boolean", "char", "int")){
            TipoPrimitivo();
            ListaDeclaraciones();
        }else if(checkCurrentToken("idClase"))
            VarClaseOAccesoMetEstaticoYAsignacionOpt();
        else {
            addError(new SyntacticError(currentToken, "asignacion, declaracion de variable o llamada"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void ListaDeclaraciones() throws IOException, SyntacticException {
        Declaracion();
        RestoListaDeclOpt();
    }

    private void RestoListaDeclOpt() throws IOException, SyntacticException {
        if(checkCurrentToken(",")){
            match(",");
            ListaDeclaraciones();
        }else if(invalidEpsilon(";")) {
            addError(new SyntacticError(currentToken, "',' o ;"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void Declaracion() throws IOException, SyntacticException {
        match("idMetVar");
        InicializacionOpt();
    }

    private void InicializacionOpt() throws IOException, SyntacticException {
        if(checkCurrentToken("=")){
            match("=");
            try {
                Expresion();
            }catch (SyntacticException e){
                discardTokensUntilValidTokenIsFound(";");
            }
        }else if(invalidEpsilon(",", ";")) {
            addError(new SyntacticError(currentToken, "',' o ;"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void VarClaseOAccesoMetEstaticoYAsignacionOpt() throws IOException, SyntacticException {
        match("idClase");
        ListaDecORestoAccesoMetEstYAsigOpt();
    }

    private void ListaDecORestoAccesoMetEstYAsigOpt() throws IOException, SyntacticException {
        if(checkCurrentToken("<", "idMetVar")) {
            GenericidadOpt();
            ListaDeclaraciones();
        }else if(checkCurrentToken(".")){
            RestoAccesoMetEst();
            AsignacionOpt();
        }else {
            addError(new SyntacticError(currentToken, "identificador de variable o llamada a método estático"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void RestoAccesoMetEst() throws IOException, SyntacticException {
        match(".");
        match("idMetVar");
        ArgsActuales();
        EncadenadoOpt();
    }

    private void AsignacionOpt() throws IOException, SyntacticException {
        if(checkCurrentToken("=", "+=", "-=")){
            TipoDeAsignacion();
            try {
                Expresion();
            }catch (SyntacticException e){
                discardTokensUntilValidTokenIsFound(";");
            }
        }
        else if(invalidEpsilon(";")) {
            addError(new SyntacticError(currentToken, "asignacion o ;"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void TipoDeAsignacion() throws IOException, SyntacticException {
        if(checkCurrentToken("="))
            match("=");
        else if(checkCurrentToken("+="))
            match("+=");
        else if(checkCurrentToken("-="))
            match("-=");
        else {
            addError(new SyntacticError(currentToken, "asignacion"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void VarLocal() throws IOException, SyntacticException {
        match("var");
        match("idMetVar");
        match("=");
        try {
            Expresion();
        }catch (SyntacticException e){
            discardTokensUntilValidTokenIsFound(";");
        }
    }

    private void Return() throws IOException, SyntacticException {
        match("return");
        try {
            ExpresionOpt();
        }catch (SyntacticException e){
            discardTokensUntilValidTokenIsFound(";");
        }
    }

    private void ExpresionOpt() throws IOException, SyntacticException {
        if(checkCurrentToken("+", "-", "!", "null", "true", "false", "intLiteral",
                "charLiteral", "stringLiteral", "this", "idMetVar", "new", "idClase", "("))
            Expresion();
        else if(invalidEpsilon(";")) {
            addError(new SyntacticError(currentToken, "expresion o ;"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void If() throws IOException {
        try{
            match("if");
            match("(");
            try {
                Expresion();
            } catch (SyntacticException e) {
                discardTokensUntilValidTokenIsFound(")", ";", "{");
            }
            match(")");
            Sentencia();
        }catch (SyntacticException e){
            discardTokensUntilValidTokenIsFound("{", ";");
            if(checkCurrentToken("{"))
                Sentencia();
            else
                updateCurrentToken();
        }
        ElseOpt();
    }

    private void ElseOpt() throws IOException {
        if(checkCurrentToken("else")){
            updateCurrentToken();
            Sentencia();
        }else if(invalidEpsilon(";", "this", "idMetVar", "new", "(", "boolean", "char",
                "int", "idClase", "var", "return", "if", "while", "{", "}", "else")) {
            addError(new SyntacticError(currentToken, "else, sentencia o }"));
            discardTokensUntilValidTokenIsFound(";", "this", "idMetVar", "new", "(", "boolean",
                    "char", "int", "idClase", "var", "return", "if", "while", "{", "}", "else");
        }
    }

    private void While() throws IOException {
        try{
            match("while");
            match("(");
            try {
                Expresion();
            } catch (SyntacticException e) {
                discardTokensUntilValidTokenIsFound(")", ";", "{");
            }
            match(")");
            Sentencia();
        }catch (SyntacticException e){
            discardTokensUntilValidTokenIsFound(";", "{");
            if(checkCurrentToken("{"))
                Sentencia();
        }
    }

    private void Expresion() throws IOException, SyntacticException {
        ExpresionUnaria();
        RestoExpresion();
    }

    private void RestoExpresion() throws IOException, SyntacticException {
        if(checkCurrentToken("||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*", "/", "%")){
            OperadorBinario();
            ExpresionUnaria();
            RestoExpresion();
        }
        else if(invalidEpsilon(",", ";", ")")) {
            addError(new SyntacticError(currentToken, "operador binario, ',', ) o ;"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void OperadorBinario() throws IOException, SyntacticException {
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
        else {
            addError(new SyntacticError(currentToken, "operador binario"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void ExpresionUnaria() throws IOException, SyntacticException {
        if(checkCurrentToken("+", "-", "!")){
            OperadorUnario();
            Operando();
        }else if(checkCurrentToken("null", "true", "false", "intLiteral", "charLiteral",
                "stringLiteral", "this", "idMetVar", "new", "idClase", "("))
            Operando();
        else {
            addError(new SyntacticError(currentToken, "operando"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void OperadorUnario() throws IOException, SyntacticException {
        if(checkCurrentToken("+"))
            match("+");
        else if(checkCurrentToken("-"))
            match("-");
        else if(checkCurrentToken("!"))
            match("!");
        else {
            addError(new SyntacticError(currentToken, "operador unario"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void Operando() throws IOException, SyntacticException {
        if(checkCurrentToken("null", "true", "false", "intLiteral", "charLiteral", "stringLiteral"))
            Literal();
        else if(checkCurrentToken("this", "idMetVar", "new", "idClase", "("))
            Acceso();
        else {
            addError(new SyntacticError(currentToken, "literal o acceso"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void Literal() throws IOException, SyntacticException {
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
        else {
            addError(new SyntacticError(currentToken, "literal"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void Acceso() throws IOException, SyntacticException {
        if(checkCurrentToken("idClase"))
            AccesoMetodoEstatico();
        else if(checkCurrentToken("this", "idMetVar", "new", "("))
            AccesoNoMetEstatico();
        else {
            addError(new SyntacticError(currentToken, "acceso"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void AccesoNoMetEstatico() throws IOException, SyntacticException {
        PrimarioNoMetEstatico();
        EncadenadoOpt();
    }

    private void AccesoMetodoEstatico() throws IOException, SyntacticException {
        match("idClase");
        match(".");
        match("idMetVar");
        ArgsActuales();
        EncadenadoOpt();
    }

    private void PrimarioNoMetEstatico() throws IOException, SyntacticException {
        if(checkCurrentToken("this"))
            AccesoThis();
        else if(checkCurrentToken("idMetVar"))
            AccesoVarOMetodo();
        else if(checkCurrentToken("new"))
            AccesoConstructor();
        else if(checkCurrentToken("("))
            ExpresionParentizada();
        else {
            addError(new SyntacticError(currentToken, "this, identificador de metodo o variable, new o ("));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void AccesoVarOMetodo() throws IOException, SyntacticException {
        match("idMetVar");
        ArgActualesMetodoOpt();
    }

    private void ArgActualesMetodoOpt() throws IOException, SyntacticException {
        if(checkCurrentToken("("))
            ArgsActuales();
        else if(invalidEpsilon("=", "+=", "-=", ";", "||", "&&", "==", "!=", "<",
                ">", "<=", ">=", "+", "-", "*", "/", "%", ",", ")", ".")) {
            addError(new SyntacticError(currentToken, "argumentos actuales, asignacion, operador binario, ',', ) o ;"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void AccesoThis() throws IOException, SyntacticException {
        match("this");
    }

    private void AccesoConstructor() throws IOException, SyntacticException {
        match("new");
        ClaseGenericaConstructor();
        ArgsActuales();
    }

    private void ClaseGenericaConstructor() throws IOException, SyntacticException {
        match("idClase");
        GenericidadConstructor();
    }

    private void GenericidadConstructor() throws IOException, SyntacticException {
        if(checkCurrentToken("<")){
            match("<");
            ListaTipoReferenciaOpt();
            match(">");
        }else if(invalidEpsilon("(")) {
            addError(new SyntacticError(currentToken, "genericidad o argumentos actuales"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void ListaTipoReferenciaOpt() throws IOException, SyntacticException {
        if(checkCurrentToken("idClase"))
            ListaTipoReferencia();
        else if(invalidEpsilon(">")) {
            addError(new SyntacticError(currentToken, "identificador de clase o >"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void ExpresionParentizada() throws IOException, SyntacticException {
        match("(");
        try {
            Expresion();
        }catch (SyntacticException e){
            discardTokensUntilValidTokenIsFound(")");
        }
        match(")");
    }

    private void ArgsActuales() throws IOException, SyntacticException {
        match("(");
        ListaExpsOpt();
        match(")");
    }

    private void ListaExpsOpt() throws IOException, SyntacticException {
        if(checkCurrentToken("+", "-", "!", "null", "true", "false", "intLiteral",
                "charLiteral", "stringLiteral", "this", "idMetVar", "new", "idClase", "("))
            ListaExps();
        else if(invalidEpsilon(")")) {
            addError(new SyntacticError(currentToken, "expresion o )"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void ListaExps() throws IOException, SyntacticException {
        try {
            Expresion();
        }catch (SyntacticException e){
            discardTokensUntilValidTokenIsFound(",", ")");
        }
        RestoListaExpsOpt();
    }

    private void RestoListaExpsOpt() throws IOException, SyntacticException {
        if(checkCurrentToken(",")) {
            match(",");
            ListaExps();
        }else if(invalidEpsilon(")")) {
            addError(new SyntacticError(currentToken, "',' o )"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void EncadenadoOpt() throws IOException, SyntacticException {
        if(checkCurrentToken("."))
            VarOMetodoEncadenado();
        else if(invalidEpsilon("=", "+=", "-=", ";", "||", "&&", "==", "!=", "<",
                ">", "<=", ">=", "+", "-", "*", "/", "%", ",", ")")) {
            addError(new SyntacticError(currentToken, "encadenado, asignacion, operador binario, ',', ) o ;"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void VarOMetodoEncadenado() throws IOException, SyntacticException {
        match(".");
        match("idMetVar");
        ArgActualesMetodoOpt();
        EncadenadoOpt();
    }
}
