package syntacticAnalyzer;

import Errors.CompilerError;
import Errors.CompilerException;
import Errors.LexicalError;
import Errors.SyntacticError;
import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.LexicalException;
import lexicalAnalyzer.Token;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class SyntacticAnalyzer {
    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;
    private final static boolean useAdvancedImplementation = true;
    private LinkedList<CompilerError> compilerErrorList;

    public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws IOException, CompilerException {
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

    private void throwExceptionIfErrorsWereFound() throws CompilerException {
        if(!compilerErrorList.isEmpty())
            throw new CompilerException(compilerErrorList);
    }

    private void match(String expected) throws IOException, CompilerException {
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
            discardTokensUntilValidTokenIsFound("}");
            updateCurrentToken();//TODO hasta donde descarto aca?
        }
            //TODO por ahora no hago nada
    }

    private void Clase() throws IOException {
        if(checkCurrentToken("class"))
            ClaseConcreta();
        else if(checkCurrentToken("interface"))
            Interface();
        else {
            addError(new SyntacticError(currentToken, "class o interface"));
            discardTokensUntilValidTokenIsFound("}");
            updateCurrentToken();//TODO hasta donde descarto aca?
        }
    }

    private void ClaseConcreta() throws IOException {
        try {
            match("class");
            ClaseGenerica();
            HeredaDe();
            ImplementaA();
            match("{");
        }catch (CompilerException e){
            discardTokensUntilValidTokenIsFound("{");
            updateCurrentToken();
        }
        ListaMiembros();
        try {
            match("}");
        }catch (CompilerException e){
            discardTokensUntilValidTokenIsFound("}");
            updateCurrentToken();
        }
    }

    private void ClaseGenerica() throws IOException, CompilerException {
        match("idClase");
        GenericidadOpt();
    }

    private void GenericidadOpt() throws IOException, CompilerException {
        if(checkCurrentToken("<")){
            match("<");
            try{
                ListaTipoReferencia();
                match(">");
            }catch (CompilerException e){
                discardTokensUntilValidTokenIsFound(">");
                updateCurrentToken();
            }
        }else if(invalidEpsilon("extends", "implements", ",", "idMetVar", ".", ">", "{")) {
            addError(new SyntacticError(currentToken, "<, extends, implements, ',', idMetVar, ., > o {"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void Interface() throws IOException {
        try {
            match("interface");
            ClaseGenerica();
            ExtiendeA();
            match("{");
            ListaEncabezados();
            match("}");
        }catch(CompilerException e){
            discardTokensUntilValidTokenIsFound("}");
            updateCurrentToken();
        }
    }

    private void HeredaDe() throws IOException, CompilerException {
        if(checkCurrentToken("extends")){
            match("extends");
            ClaseGenerica();
        }
        else if(invalidEpsilon("implements", "{")) {
            addError(new SyntacticError(currentToken, "extends, implements o {"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void ImplementaA() throws IOException, CompilerException {
        if(checkCurrentToken("implements")){
            match("implements");
            ListaTipoReferencia();
        }
        else if(invalidEpsilon("{")) {
            addError(new SyntacticError(currentToken, "implements o {"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void ExtiendeA() throws IOException, CompilerException {
        if(checkCurrentToken("extends")){
            match("extends");
            ListaTipoReferencia();
        }
        else if(invalidEpsilon("{")) {
            addError(new SyntacticError(currentToken, "extends o {"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void ListaTipoReferencia() throws IOException, CompilerException {
        ClaseGenerica();
        RestoListaTipoRefOpt();
    }

    private void RestoListaTipoRefOpt() throws IOException, CompilerException {
        if(checkCurrentToken(",")){
            match(",");
            ListaTipoReferencia();
        }
        else if(invalidEpsilon(">", "{")) {
            addError(new SyntacticError(currentToken, ",, > o {"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void ListaMiembros() throws IOException {
        if(checkCurrentToken("public", "private", "static", "boolean",
                             "char", "int", "idClase", "void")){
            Miembro();
            ListaMiembros();
        }
        else if(invalidEpsilon("}")) {
            addError(new SyntacticError(currentToken, "declaracion de miembro o }"));
            discardTokensUntilValidTokenIsFound(";", "{");
            if(checkCurrentToken("{"))
                Bloque();
            else
                updateCurrentToken();
        }
            //TODO no hago nada por ahora

    }

    private void ListaEncabezados() throws IOException {
        if(checkCurrentToken("static", "boolean", "char", "int", "idClase", "void")){
            EncabezadoMetodo();
            try{
                match(";");
            }catch (CompilerException e){
                discardTokensUntilValidTokenIsFound(";");
                updateCurrentToken();
            }
            ListaEncabezados();
        }else if(invalidEpsilon("}")){
            addError(new SyntacticError(currentToken, "encabezado de metodo"));
            discardTokensUntilValidTokenIsFound(";");
            updateCurrentToken();
            ListaEncabezados();
        }
    }

    private void Miembro() throws IOException {
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
        else {
            addError(new SyntacticError(currentToken, "atributo, metodo o constructor"));
            discardTokensUntilValidTokenIsFound(";", "{");
            if(checkCurrentToken("{"))
                Bloque();
            else
                updateCurrentToken();
        }
    }

    private void AtributoConVisibilidad() throws IOException {
        try{
            Visibilidad();
            Tipo();
            ListaDecAtrs();
            match(";");
        } catch (CompilerException e){
            discardTokensUntilValidTokenIsFound(";");
            updateCurrentToken();
        }
    }

    private void MetodoEstatico() throws IOException {
        try{
            match("static");
            TipoMetodo();
            match("idMetVar");
            ArgsFormales();
        }catch (CompilerException e){
            discardTokensUntilValidTokenIsFound("{");
        }
        Bloque();
    }

    private void MetodoNoEstVoid() throws IOException {
        try{
            match("void");
            match("idMetVar");
            ArgsFormales();
        }catch(CompilerException e){
            discardTokensUntilValidTokenIsFound("{");
        }
        Bloque();
    }

    private void AtributoOMetodoTipoPri() throws IOException {
        try{
            TipoPrimitivo();
            match("idMetVar");
            RestoAtrOMet();
        }catch(CompilerException e){
            discardTokensUntilValidTokenIsFound(";", "{");
            if(checkCurrentToken("{"))
                Bloque();
            else
                updateCurrentToken();
        }
    }

    private void RestoAtrOMet() throws IOException{
        if(checkCurrentToken(",", ";")){
            try{
                RestoListaDecAtrsOpt();
                match(";");
            }catch (CompilerException e){
                discardTokensUntilValidTokenIsFound(";");
                updateCurrentToken();
            }
        }else if(checkCurrentToken("(")) {
            try {
                ArgsFormales();
            }catch (CompilerException e){
                discardTokensUntilValidTokenIsFound("{");
            }
            Bloque();
        }else {
            addError(new SyntacticError(currentToken, "lista de atributos o argumentos formales"));
            discardTokensUntilValidTokenIsFound(";", "{");
            if(checkCurrentToken("{"))
                Bloque();
            else
                updateCurrentToken();
        }
    }

    private void AtributoTCOMetodoTCOConstructor() throws IOException{
        try{
            match("idClase");
            RestoAtrTCOMetTCOCons();
        }catch(CompilerException e){
            discardTokensUntilValidTokenIsFound(";", "{");
            if(checkCurrentToken("{"))
                Bloque();
            else
                updateCurrentToken();
        }
    }

    private void RestoAtrTCOMetTCOCons() throws IOException, CompilerException {
        if(checkCurrentToken("<", "idMetVar")){
            GenericidadOpt();
            try{
                match("idMetVar");
                RestoAtrOMet();
            }catch(CompilerException e){
                discardTokensUntilValidTokenIsFound(";", "{");
                if(checkCurrentToken("{"))
                    Bloque();
                else
                    updateCurrentToken();
            }
        }else if(checkCurrentToken("("))
            RestoConstructor();
        else {
            addError(new SyntacticError(currentToken, "identificador de metodo o variable o argumentos de constructor"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void RestoConstructor() throws IOException {
        try {
            ArgsFormales();
        }catch (CompilerException e){
            discardTokensUntilValidTokenIsFound("{");
        }
        Bloque();
    }

    private void EncabezadoMetodo() throws IOException {
        try{
            EstaticoOpt();
            TipoMetodo();
            match("idMetVar");
            ArgsFormales();
        }catch (CompilerException exception){
            discardTokensUntilValidTokenIsFound(";");
        }
    }

    private void Visibilidad() throws IOException, CompilerException {
        if(checkCurrentToken("public"))
            match("public");
        else if(checkCurrentToken("private"))
            match("private");
        else {
            addError(new SyntacticError(currentToken, "modificador de acceso"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void Tipo() throws IOException, CompilerException {
        if(checkCurrentToken("boolean", "char", "int"))
            TipoPrimitivo();
        else if(checkCurrentToken("idClase"))
            ClaseGenerica();
        else {
            addError(new SyntacticError(currentToken, "tipo"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void TipoPrimitivo() throws IOException, CompilerException {
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

    private void ListaDecAtrs() throws IOException, CompilerException {
        match("idMetVar");
        RestoListaDecAtrsOpt();
    }

    private void RestoListaDecAtrsOpt() throws IOException, CompilerException {
        if(checkCurrentToken(",")){
            match(",");
            ListaDecAtrs();
        }
        else if(invalidEpsilon(";")) {
            addError(new SyntacticError(currentToken, ", o ;"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void EstaticoOpt() throws IOException, CompilerException {
        if(checkCurrentToken("static"))
            match("static");
        else if(invalidEpsilon("boolean", "char", "int", "idClase", "void")) {
            addError(new SyntacticError(currentToken, "tipo de metodo"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void TipoMetodo() throws IOException, CompilerException {
        if(checkCurrentToken("boolean", "char", "int", "idClase"))
            Tipo();
        else if(checkCurrentToken("void"))
            match("void");
        else {
            addError(new SyntacticError(currentToken, "tipo de metodo"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void ArgsFormales() throws IOException, CompilerException {
        match("(");
        ListaArgsFormalesOpt();
        match(")");
    }

    private void ListaArgsFormalesOpt() throws IOException, CompilerException {
        if(checkCurrentToken("boolean", "char", "int", "idClase"))
            ListaArgsFormales();
        else if(invalidEpsilon(")")) {
            addError(new SyntacticError(currentToken, "tipo o )"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void ListaArgsFormales() throws IOException, CompilerException {
        ArgFormal();
        RestoListaArgsFormalesOpt();
    }

    private void RestoListaArgsFormalesOpt() throws IOException, CompilerException {
        if(checkCurrentToken(",")) {
            match(",");
            ListaArgsFormales();
        }
        else if(invalidEpsilon(")")) {
            addError(new SyntacticError(currentToken, ", o )"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void ArgFormal() throws IOException, CompilerException {
        Tipo();
        match("idMetVar");
    }

    private void Bloque() throws IOException{
        try {
            match("{");
        }catch (CompilerException e){
            discardTokensUntilValidTokenIsFound("{", ";");
            if(checkCurrentToken(";")){
                updateCurrentToken();
                return;
            }else updateCurrentToken();
        }
        ListaSentencias();
        try{
            match("}");
        }catch (CompilerException e){
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
            //TODO no hago nada por ahora
    }

    private void Sentencia() throws IOException {
        if(checkCurrentToken(";"))
            updateCurrentToken();
        else if(checkCurrentToken("this", "idMetVar", "new", "idClase", "(",
                "boolean", "char", "int")){
            try {
                AsignacionOLlamadaOVarClasica();
                match(";");
            }catch (CompilerException e){
                discardTokensUntilValidTokenIsFound(";");
                updateCurrentToken();
            }
        }else if(checkCurrentToken("var")){
            try {
                VarLocal();
                match(";");
            }catch (CompilerException e){
                discardTokensUntilValidTokenIsFound(";");
                updateCurrentToken();
            }
        }else if(checkCurrentToken("return")){
            try {
                Return();
                match(";");
            }catch (CompilerException e){
                discardTokensUntilValidTokenIsFound(";");
                updateCurrentToken();
            }
        }else if(checkCurrentToken("if"))
            If();
        else if(checkCurrentToken("while"))
            While();
        else if(checkCurrentToken("{"))
            Bloque();
        else {
            addError(new SyntacticError(currentToken, "sentencia"));
            discardTokensUntilValidTokenIsFound("}", ";");
            if(checkCurrentToken(";"))
                updateCurrentToken();
        }
    }

    private void AsignacionOLlamadaOVarClasica() throws IOException, CompilerException {
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

    private void ListaDeclaraciones() throws IOException, CompilerException {
        Declaracion();
        RestoListaDeclOpt();
    }

    private void RestoListaDeclOpt() throws IOException, CompilerException {
        if(checkCurrentToken(",")){
            match(",");
            ListaDeclaraciones();
        }else if(invalidEpsilon(";")) {
            addError(new SyntacticError(currentToken, ", o ;"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void Declaracion() throws IOException, CompilerException {
        match("idMetVar");
        InicializacionOpt();
    }

    private void InicializacionOpt() throws IOException, CompilerException {
        if(checkCurrentToken("=")){
            match("=");
            try {
                Expresion();
            }catch (CompilerException e){
                discardTokensUntilValidTokenIsFound(";");
            }
        }else if(invalidEpsilon(",", ";")) {
            addError(new SyntacticError(currentToken, ", o ;"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void VarClaseOAccesoMetEstaticoYAsignacionOpt() throws IOException, CompilerException {
        ClaseGenerica();
        ListaDecORestoAccesoMetEstYAsigOpt();
    }

    private void ListaDecORestoAccesoMetEstYAsigOpt() throws IOException, CompilerException {
        if(checkCurrentToken("idMetVar"))
            ListaDeclaraciones();
        else if(checkCurrentToken(".")){
            RestoAccesoMetEst();
            AsignacionOpt();
        }else {
            addError(new SyntacticError(currentToken, "identificador de variable o llamada a método estático"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void RestoAccesoMetEst() throws IOException, CompilerException {
        match(".");
        match("idMetVar");
        ArgsActuales();
        EncadenadoOpt();
    }

    private void AsignacionOpt() throws IOException, CompilerException {
        if(checkCurrentToken("=", "+=", "-=")){
            TipoDeAsignacion();
            try {
                Expresion();
            }catch (CompilerException e){
                discardTokensUntilValidTokenIsFound(";");
            }
        }
        else if(invalidEpsilon(";")) {
            addError(new SyntacticError(currentToken, "asignacion o ;"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void TipoDeAsignacion() throws IOException, CompilerException {
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

    private void VarLocal() throws IOException, CompilerException {
        match("var");
        match("idMetVar");
        match("=");
        try {
            Expresion();
        }catch (CompilerException e){
            discardTokensUntilValidTokenIsFound(";");
        }
    }

    private void Return() throws IOException, CompilerException {
        match("return");
        try {
            ExpresionOpt();
        }catch (CompilerException e){
            discardTokensUntilValidTokenIsFound(";");
        }
    }

    private void ExpresionOpt() throws IOException, CompilerException {
        if(checkCurrentToken("+", "-", "!", "null", "true", "false", "intLiteral",
                "charLiteral", "stringLiteral", "this", "idMetVar", "new", "idClase", "("))
            Expresion();
        else if(invalidEpsilon(";")) {
            addError(new SyntacticError(currentToken, "expresion o ;"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void If() throws IOException {
        try{
            match("if");
            match("(");
            try {
                Expresion();
            } catch (CompilerException e) {
                discardTokensUntilValidTokenIsFound(")");
            }
            match(")");
            Sentencia();
        }catch (CompilerException e){
            discardTokensUntilValidTokenIsFound("{", ";");
            if(checkCurrentToken("{"))
                Sentencia();
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
            //TODO no hago nada por ahora
    }

    private void While() throws IOException {
        try{
            match("while");
            match("(");
            try {
                Expresion();
            } catch (CompilerException e) {
                discardTokensUntilValidTokenIsFound(")");
            }
            match(")");
            Sentencia();
        }catch (CompilerException e){
            discardTokensUntilValidTokenIsFound(";", "{");
            if(checkCurrentToken("{"))
                Sentencia();
        }
    }

    private void Expresion() throws IOException, CompilerException {
        ExpresionUnaria();
        RestoExpresion();
    }

    private void RestoExpresion() throws IOException, CompilerException {
        if(checkCurrentToken("||", "&&", "==", "!=", "<", ">", "<=", ">=", "+", "-", "*", "/", "%")){
            OperadorBinario();
            ExpresionUnaria();
            RestoExpresion();
        }
        else if(invalidEpsilon(",", ";", ")")) {
            addError(new SyntacticError(currentToken, "operador binario, ,, ) o ;"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void OperadorBinario() throws IOException, CompilerException {
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
        else {
            addError(new SyntacticError(currentToken, "operador binario"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void ExpresionUnaria() throws IOException, CompilerException {
        if(checkCurrentToken("+", "-", "!")){
            OperadorUnario();
            Operando();
        }else if(checkCurrentToken("null", "true", "false", "intLiteral", "charLiteral",
                "stringLiteral", "this", "idMetVar", "new", "idClase", "("))
            Operando();
        else {
            addError(new SyntacticError(currentToken, "erando"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void OperadorUnario() throws IOException, CompilerException {
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

    private void Operando() throws IOException, CompilerException {
        if(checkCurrentToken("null", "true", "false", "intLiteral", "charLiteral", "stringLiteral"))
            Literal();
        else if(checkCurrentToken("this", "idMetVar", "new", "idClase", "("))
            Acceso();
        else {
            addError(new SyntacticError(currentToken, "literal o acceso"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void Literal() throws IOException, CompilerException {
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

    private void Acceso() throws IOException, CompilerException {
        if(checkCurrentToken("idClase"))
            AccesoMetodoEstatico();
        else if(checkCurrentToken("this", "idMetVar", "new", "("))
            AccesoNoMetEstatico();
        else {
            addError(new SyntacticError(currentToken, "acceso"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void AccesoNoMetEstatico() throws IOException, CompilerException {
        PrimarioNoMetEstatico();
        EncadenadoOpt();
    }

    private void AccesoMetodoEstatico() throws IOException, CompilerException {
        ClaseGenerica();
        match(".");
        match("idMetVar");
        ArgsActuales();
        EncadenadoOpt();
    }

    private void PrimarioNoMetEstatico() throws IOException, CompilerException {
        if(checkCurrentToken("this"))
            AccesoThis();
        else if(checkCurrentToken("idMetVar"))
            AccesoVarOMetodo();
        else if(checkCurrentToken("new"))
            AccesoConstructor();
        else if(checkCurrentToken("("))
            ExpresionParentizada();
        else {
            addError(new SyntacticError(currentToken, "acceso primario"));
            throwExceptionIfErrorsWereFound();
        }
    }

    private void AccesoVarOMetodo() throws IOException, CompilerException {
        match("idMetVar");
        ArgActualesMetodoOpt();
    }

    private void ArgActualesMetodoOpt() throws IOException, CompilerException {
        if(checkCurrentToken("("))
            ArgsActuales();
        else if(invalidEpsilon("=", "+=", "-=", ";", "||", "&&", "==", "!=", "<",
                ">", "<=", ">=", "+", "-", "*", "/", "%", ",", ")", ".")) {
            addError(new SyntacticError(currentToken, "argumentos actuales, asignacion, operador binario, ,, ) o ;"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void AccesoThis() throws IOException, CompilerException {
        match("this");
    }

    private void AccesoConstructor() throws IOException, CompilerException {
        match("new");
        ClaseGenericaConstructor();
        ArgsActuales();
    }

    private void ClaseGenericaConstructor() throws IOException, CompilerException {
        match("idClase");
        GenericidadConstructor();
    }

    private void GenericidadConstructor() throws IOException, CompilerException {
        if(checkCurrentToken("<")){
            match("<");
            ListaTipoReferenciaOpt();
            match(">");
        }else if(invalidEpsilon("(")) {
            addError(new SyntacticError(currentToken, "genericidad o argumentos actuales"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void ListaTipoReferenciaOpt() throws IOException, CompilerException {
        if(checkCurrentToken("idClase"))
            ListaTipoReferencia();
        else if(invalidEpsilon(">")) {
            addError(new SyntacticError(currentToken, "identificador de clase o >"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void ExpresionParentizada() throws IOException, CompilerException {
        match("(");
        try {
            Expresion();
        }catch (CompilerException e){
            discardTokensUntilValidTokenIsFound(")");
        }
        match(")");
    }

    private void ArgsActuales() throws IOException, CompilerException {
        match("(");
        ListaExpsOpt();
        match(")");
    }

    private void ListaExpsOpt() throws IOException, CompilerException {
        if(checkCurrentToken("+", "-", "!", "null", "true", "false", "intLiteral",
                "charLiteral", "stringLiteral", "this", "idMetVar", "new", "idClase", "("))
            ListaExps();
        else if(invalidEpsilon(")")) {
            addError(new SyntacticError(currentToken, "expresion o )"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void ListaExps() throws IOException, CompilerException {
        try {
            Expresion();
        }catch (CompilerException e){
            discardTokensUntilValidTokenIsFound(",", ")");
        }
        RestoListaExpsOpt();
    }

    private void RestoListaExpsOpt() throws IOException, CompilerException {
        if(checkCurrentToken(",")) {
            match(",");
            ListaExps();
        }else if(invalidEpsilon(")")) {
            addError(new SyntacticError(currentToken, ", o )"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void EncadenadoOpt() throws IOException, CompilerException {
        if(checkCurrentToken("."))
            VarOMetodoEncadenado();
        else if(invalidEpsilon("=", "+=", "-=", ";", "||", "&&", "==", "!=", "<",
                ">", "<=", ">=", "+", "-", "*", "/", "%", ",", ")")) {
            addError(new SyntacticError(currentToken, "encadenado, asignacion, operador binario, ,, ) o ;"));
            throwExceptionIfErrorsWereFound();
        }
            //TODO no hago nada por ahora
    }

    private void VarOMetodoEncadenado() throws IOException, CompilerException {
        match(".");
        match("idMetVar");
        ArgActualesMetodoOpt();
        EncadenadoOpt();
    }
}
