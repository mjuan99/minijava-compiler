package syntacticAnalyzer;

import Errors.*;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.Token;
import symbolTable.entities.*;
import symbolTable.SymbolTable;
import symbolTable.types.*;

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
    public static SymbolTable symbolTable;

    public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws IOException, SyntacticException {
        this.lexicalAnalyzer = lexicalAnalyzer;
        compilerErrorList = new LinkedList<>();
        symbolTable = new SymbolTable();
        updateCurrentToken();
        try {
            Inicial();
        }catch (EOFException ignored){}
        throwExceptionIfErrorsWereFound();
    }

    public SymbolTable getSymbolTable(){
        return symbolTable;
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
            throw new SyntacticException(compilerErrorList);
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
            Token tkClass = ClaseGenerica();
            STClass stClass = new STClass(tkClass);
            symbolTable.setCurrentSTClass(stClass);
            stClass.setSTClassItExtends(HeredaDe());
            try {
                stClass.setTkInterfacesItImplements(ImplementaA());
            } catch (SemanticException ignored) {}
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
        try {
            symbolTable.insertSTClass(symbolTable.getCurrentSTClass());
        } catch (SemanticException ignored) {}
    }

    private Token ClaseGenerica() throws IOException, SyntacticException {
        Token tkClass = currentToken;
        match("idClase");
        GenericidadOpt();
        return tkClass;
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
            throw new SyntacticException(compilerErrorList);
        }
    }

    private void Interface() throws IOException {
        try {
            match("interface");
            Token tkInterface = ClaseGenerica();
            STInterface stInterface = new STInterface(tkInterface);
            symbolTable.setCurrentSTInterface(stInterface);
            try {
                stInterface.setSTInterfacesItExtends(ExtiendeA());
            } catch (SemanticException ignored) {}
            match("{");
            ListaEncabezados();
            match("}");
            try {
                symbolTable.insertSTInterface(symbolTable.getCurrentSTInterface());
            } catch (SemanticException ignored) {}
        }catch(SyntacticException e){
            discardTokensUntilValidTokenIsFound("}");
            updateCurrentToken();
        }
    }

    private Token HeredaDe() throws IOException, SyntacticException {
        if(checkCurrentToken("extends")){
            match("extends");
            return ClaseGenerica();
        }
        else if(invalidEpsilon("implements", "{")) {
            addError(new SyntacticError(currentToken, "extends, implements o {"));
            throw new SyntacticException(compilerErrorList);
        }else
            return new Token("idClase", "Object", 0);
    }

    private LinkedList<Token> ImplementaA() throws IOException, SyntacticException {
        if (checkCurrentToken("implements")) {
            match("implements");
            return ListaTipoReferencia();
        } else if (invalidEpsilon("{")) {
            addError(new SyntacticError(currentToken, "implements o {"));
            throw new SyntacticException(compilerErrorList);
        }else
            return new LinkedList<>();
    }

    private LinkedList<Token> ExtiendeA() throws IOException, SyntacticException {
        if(checkCurrentToken("extends")){
            match("extends");
            return ListaTipoReferencia();
        }
        else if(invalidEpsilon("{")) {
            addError(new SyntacticError(currentToken, "extends o {"));
            throw new SyntacticException(compilerErrorList);
        }else
            return new LinkedList<>();
    }

    private LinkedList<Token> ListaTipoReferencia() throws IOException, SyntacticException {
        Token tkClass = ClaseGenerica();
        LinkedList<Token> tkClasses = RestoListaTipoRefOpt();
        tkClasses.add(tkClass);
        return tkClasses;
    }

    private LinkedList<Token> RestoListaTipoRefOpt() throws IOException, SyntacticException {
        if(checkCurrentToken(",")){
            match(",");
            return ListaTipoReferencia();
        }
        else if(invalidEpsilon(">", "{")) {
            addError(new SyntacticError(currentToken, "',', > o {"));
            throw new SyntacticException(compilerErrorList);
        }else
            return new LinkedList<>();
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
        String visibility = Visibilidad();
        STType stType = Tipo();
        LinkedList<Token> tkAttributesList = ListaDecAtrs();
        match(";");
        for(Token tkAttribute : tkAttributesList) {
            try {
                symbolTable.getCurrentSTClass().insertAttribute(new STAttribute(tkAttribute, visibility, stType));
            } catch (SemanticException ignored) {}
        }
    }

    private void MetodoEstatico() throws IOException, SyntacticException {
        match("static");
        STType returnType = TipoMetodoEstatico();
        STMethod stMethod = new STMethod(currentToken, true, returnType);
        symbolTable.setCurrentSTMethod(stMethod);
        match("idMetVar");
        try {
            stMethod.insertArguments(ArgsFormales());
        } catch (SemanticException ignored) {}
        Bloque();
        try {
            symbolTable.getCurrentSTClass().insertMethod(stMethod);
        } catch (SemanticException ignored) {}
    }

    private void MetodoNoEstVoid() throws IOException, SyntacticException {
        match("void");
        STMethod stMethod = new STMethod(currentToken, false, new STTypeVoid());
        symbolTable.setCurrentSTMethod(stMethod);
        match("idMetVar");
        try {
            stMethod.insertArguments(ArgsFormales());
        } catch (SemanticException ignored) {}
        Bloque();
        try {
            symbolTable.getCurrentSTClass().insertMethod(stMethod);
        } catch (SemanticException ignored) {}
    }

    private void AtributoOMetodoTipoPri() throws IOException, SyntacticException {
        STType stType = TipoPrimitivo();
        Token idMetVar = currentToken;
        match("idMetVar");
        RestoAtrOMet(idMetVar, stType);
    }

    private void RestoAtrOMet(Token idMetVar, STType stType) throws IOException, SyntacticException {
        if(checkCurrentToken(",", ";")){
            LinkedList<Token> tkAttributesList = RestoListaDecAtrsOpt();
            match(";");
            tkAttributesList.add(idMetVar);
            for(Token tkAttribute : tkAttributesList) {
                try {
                    symbolTable.getCurrentSTClass().insertAttribute(new STAttribute(tkAttribute, "public", stType));
                } catch (SemanticException ignored) {}
            }
        }else if(checkCurrentToken("(")) {
            STMethod stMethod = new STMethod(idMetVar, false, stType);
            symbolTable.setCurrentSTMethod(stMethod);
            try {
                stMethod.insertArguments(ArgsFormales());
            } catch (SemanticException ignored) {}
            Bloque();
            try {
                symbolTable.getCurrentSTClass().insertMethod(stMethod);
            } catch (SemanticException ignored) {}
        }else {
            addError(new SyntacticError(currentToken, "',', ; o argumentos formales"));
            throw new SyntacticException(compilerErrorList);
        }
    }

    private void AtributoTCOMetodoTCOConstructor() throws IOException, SyntacticException {
        Token idClass = currentToken;
        match("idClase");
        RestoAtrTCOMetTCOCons(idClass);
    }

    private void RestoAtrTCOMetTCOCons(Token idClass) throws IOException, SyntacticException {
        if(checkCurrentToken("<", "idMetVar")){
            GenericidadOpt();
            Token idMetVar = currentToken;
            match("idMetVar");
            RestoAtrOMet(idMetVar, new STTypeReference(idClass));
        }else if(checkCurrentToken("("))
            RestoConstructor(idClass);
        else {
            addError(new SyntacticError(currentToken, "identificador de metodo o variable o argumentos de constructor"));
            throw new SyntacticException(compilerErrorList);
        }
    }

    private void RestoConstructor(Token idClass) throws IOException, SyntacticException {
        STConstructor stConstructor = new STConstructor(idClass);
        symbolTable.setCurrentSTConstructor(stConstructor);
        try {
            stConstructor.insertArguments(ArgsFormales());
        } catch (SemanticException ignored) {}
        Bloque();
        try {
            symbolTable.getCurrentSTClass().insertConstructor(stConstructor);
        } catch (SemanticException ignored) {}
    }

    private void EncabezadoMetodo() throws IOException, SyntacticException {
        STType returnType;
        boolean isStatic = checkCurrentToken("static");
        EstaticoOpt();
        if(isStatic)
            returnType = TipoMetodoEstatico();
        else
            returnType = TipoMetodo();
        STMethodHeader stMethodHeader = new STMethodHeader(currentToken, isStatic, returnType);
        symbolTable.setCurrentSTMethodHeader(stMethodHeader);
        match("idMetVar");
        try {
            stMethodHeader.insertArguments(ArgsFormales());
        } catch (SemanticException ignored) {}
        try {
            symbolTable.getCurrentSTInterface().insertMethod(stMethodHeader);
        } catch (SemanticException ignored) {}
    }

    private String Visibilidad() throws IOException, SyntacticException {
        if(checkCurrentToken("public")) {
            match("public");
            return "public";
        }
        else if(checkCurrentToken("private")) {
            match("private");
            return "private";
        }
        else {
            addError(new SyntacticError(currentToken, "modificador de acceso"));
            throw new SyntacticException(compilerErrorList);
        }
    }

    private STType Tipo() throws IOException, SyntacticException {
        if(checkCurrentToken("boolean", "char", "int"))
            return TipoPrimitivo();
        else if(checkCurrentToken("idClase"))
            return new STTypeReference(ClaseGenerica());
        else {
            addError(new SyntacticError(currentToken, "tipo"));
            throw new SyntacticException(compilerErrorList);
        }
    }

    private STType TipoPrimitivo() throws IOException, SyntacticException {
        if(checkCurrentToken("boolean")) {
            match("boolean");
            return new STTypeBoolean();
        }
        else if(checkCurrentToken("char")) {
            match("char");
            return new STTypeChar();
        }
        else if(checkCurrentToken("int")) {
            match("int");
            return new STTypeInt();
        }
        else {
            addError(new SyntacticError(currentToken, "tipo primitvo"));
            throw new SyntacticException(compilerErrorList);
        }
    }

    private LinkedList<Token> ListaDecAtrs() throws IOException, SyntacticException {
        Token tkAttribute = currentToken;
        match("idMetVar");
        LinkedList<Token> tkAttributesList = RestoListaDecAtrsOpt();
        tkAttributesList.add(tkAttribute);
        return tkAttributesList;
    }

    private LinkedList<Token> RestoListaDecAtrsOpt() throws IOException, SyntacticException {
        if(checkCurrentToken(",")){
            match(",");
            return ListaDecAtrs();
        }
        else if(invalidEpsilon(";")) {
            addError(new SyntacticError(currentToken, "',' o ;"));
            throw new SyntacticException(compilerErrorList);
        } else
            return new LinkedList<>();
    }

    private void EstaticoOpt() throws IOException, SyntacticException {
        if(checkCurrentToken("static"))
            match("static");
        else if(invalidEpsilon("boolean", "char", "int", "idClase", "void")) {
            addError(new SyntacticError(currentToken, "tipo de metodo"));
            throw new SyntacticException(compilerErrorList);
        }
    }

    private STType TipoMetodo() throws IOException, SyntacticException {
        if(checkCurrentToken("boolean", "char", "int", "idClase"))
            return Tipo();
        else if(checkCurrentToken("void")) {
            match("void");
            return new STTypeVoid();
        }
        else {
            addError(new SyntacticError(currentToken, "tipo de metodo"));
            throw new SyntacticException(compilerErrorList);
        }
    }

    private STType TipoMetodoEstatico() throws IOException, SyntacticException {
        if(checkCurrentToken("boolean", "char", "int"))
            return TipoPrimitivo();
        else if(checkCurrentToken("idClase")) {
            STType type = new STTypeReference(currentToken);
            match("idClase");
            return type;
        }
        else if(checkCurrentToken("void")) {
            match("void");
            return new STTypeVoid();
        }
        else {
            addError(new SyntacticError(currentToken, "tipo de metodo estatico"));
            throw new SyntacticException(compilerErrorList);
        }
    }

    private LinkedList<STArgument> ArgsFormales() throws IOException, SyntacticException {
        match("(");
        LinkedList<STArgument> stArguments = ListaArgsFormalesOpt();
        match(")");
        return stArguments;
    }

    private LinkedList<STArgument> ListaArgsFormalesOpt() throws IOException, SyntacticException {
        if(checkCurrentToken("boolean", "char", "int", "idClase"))
            return ListaArgsFormales(0);
        else if(invalidEpsilon(")")) {
            addError(new SyntacticError(currentToken, "tipo o )"));
            throw new SyntacticException(compilerErrorList);
        }else
            return new LinkedList<>();
    }

    private LinkedList<STArgument> ListaArgsFormales(int position) throws IOException, SyntacticException {
        STArgument stArgument = ArgFormal(position);
        LinkedList<STArgument> stArgumentsList = RestoListaArgsFormalesOpt(position + 1);
        stArgumentsList.add(stArgument);
        return stArgumentsList;
    }

    private LinkedList<STArgument> RestoListaArgsFormalesOpt(int position) throws IOException, SyntacticException {
        if(checkCurrentToken(",")) {
            match(",");
            return ListaArgsFormales(position);
        }
        else if(invalidEpsilon(")")) {
            addError(new SyntacticError(currentToken, "',' o )"));
            throw new SyntacticException(compilerErrorList);
        }else
            return new LinkedList<>();
    }

    private STArgument ArgFormal(int position) throws IOException, SyntacticException {
        STType stType = Tipo();
        STArgument stArgument = new STArgument(currentToken, stType, position);
        match("idMetVar");
        return stArgument;
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
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
        }
    }

    private void Operando() throws IOException, SyntacticException {
        if(checkCurrentToken("null", "true", "false", "intLiteral", "charLiteral", "stringLiteral"))
            Literal();
        else if(checkCurrentToken("this", "idMetVar", "new", "idClase", "("))
            Acceso();
        else {
            addError(new SyntacticError(currentToken, "literal o acceso"));
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
        }
    }

    private void Acceso() throws IOException, SyntacticException {
        if(checkCurrentToken("idClase"))
            AccesoMetodoEstatico();
        else if(checkCurrentToken("this", "idMetVar", "new", "("))
            AccesoNoMetEstatico();
        else {
            addError(new SyntacticError(currentToken, "acceso"));
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
        }
    }

    private void ListaTipoReferenciaOpt() throws IOException, SyntacticException {
        if(checkCurrentToken("idClase"))
            ListaTipoReferencia();
        else if(invalidEpsilon(">")) {
            addError(new SyntacticError(currentToken, "identificador de clase o >"));
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
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
            throw new SyntacticException(compilerErrorList);
        }
    }

    private void EncadenadoOpt() throws IOException, SyntacticException {
        if(checkCurrentToken("."))
            VarOMetodoEncadenado();
        else if(invalidEpsilon("=", "+=", "-=", ";", "||", "&&", "==", "!=", "<",
                ">", "<=", ">=", "+", "-", "*", "/", "%", ",", ")")) {
            addError(new SyntacticError(currentToken, "encadenado, asignacion, operador binario, ',', ) o ;"));
            throw new SyntacticException(compilerErrorList);
        }
    }

    private void VarOMetodoEncadenado() throws IOException, SyntacticException {
        match(".");
        match("idMetVar");
        ArgActualesMetodoOpt();
        EncadenadoOpt();
    }
}
