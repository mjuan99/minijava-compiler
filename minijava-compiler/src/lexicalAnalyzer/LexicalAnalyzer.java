package lexicalAnalyzer;

import lexicalAnalyzer.sourceFileManager.SourceFileManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class LexicalAnalyzer {
    private String lexeme;
    private char currentChar;
    private SourceFileManager sourceFileManager;
    private boolean eof = false;
    private HashMap<String, String> keywordsMap;
    private int lastCharLineNumber;
    private int lastCharColumnNumber;
    private String currentLine;
    private int multiLineCommentLineNumber;
    private int multiLineCommentColumnNumber;
    private String multiLineCommentFirstLine;
    private boolean multiLineCommentEnter;

    public LexicalAnalyzer(SourceFileManager sourceFileManager) throws IOException {
        this.sourceFileManager = sourceFileManager;
        initializeKeywordsMap();
        updateCurrentChar();
    }

    public Token getNextToken() throws IOException, LexicalException {
        lexeme = "";
        Token nextToken = e0();
        if(Objects.equals(nextToken.getTokenType(), "idMetVar")){
            String keyword = keywordsMap.get(nextToken.getLexeme());
            if(keyword != null)
                nextToken = new Token(keyword, nextToken.getLexeme(), nextToken.getLineNumber());
        }
        return nextToken;
    }

    private void updateLexeme(){
        if(currentChar != '\n')
            lexeme += currentChar;
    }

    private void updateCurrentChar() throws IOException {
        lastCharColumnNumber = sourceFileManager.getColumnNumber();
        lastCharLineNumber = sourceFileManager.getLineNumber();
        currentChar = sourceFileManager.getNextChar();
        if(currentChar == (char)-1)
            eof = true;
    }

    private Token e0() throws IOException, LexicalException {
        if(eof)
            return e49();
        else if(Character.isWhitespace(currentChar)){
            updateCurrentChar();
            return e0();
        }else if(Character.isUpperCase(currentChar)) {
            updateLexeme();
            updateCurrentChar();
            return e1();
        }else if(Character.isLowerCase(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e2();
        }else if(currentChar == '/'){
            updateLexeme();
            updateCurrentChar();
            return e3();
        }else if(Character.isDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e7();
        }else if(currentChar == '\''){
            updateLexeme();
            updateCurrentChar();
            return e16();
        }else if(currentChar == '"'){
            updateLexeme();
            updateCurrentChar();
            return e21();
        }else if(currentChar == '('){
            updateLexeme();
            updateCurrentChar();
            return e24();
        }else if(currentChar == ')'){
            updateLexeme();
            updateCurrentChar();
            return e25();
        }else if(currentChar == '{'){
            updateLexeme();
            updateCurrentChar();
            return e26();
        }else if(currentChar == '}'){
            updateLexeme();
            updateCurrentChar();
            return e27();
        }else if(currentChar == '<'){
            updateLexeme();
            updateCurrentChar();
            return e28();
        }else if(currentChar == '>'){
            updateLexeme();
            updateCurrentChar();
            return e30();
        }else if(currentChar == '*'){
            updateLexeme();
            updateCurrentChar();
            return e32();
        }else if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return e33();
        }else if(currentChar == '!'){
            updateLexeme();
            updateCurrentChar();
            return e35();
        }else if(currentChar == ';'){
            updateLexeme();
            updateCurrentChar();
            return e37();
        }else if(currentChar == ','){
            updateLexeme();
            updateCurrentChar();
            return e38();
        }else if(currentChar == '.'){
            updateLexeme();
            updateCurrentChar();
            return e39();
        }else if(currentChar == '%'){
            updateLexeme();
            updateCurrentChar();
            return e40();
        }else if(currentChar == '+'){
            updateLexeme();
            updateCurrentChar();
            return e41();
        }else if(currentChar == '-'){
            updateLexeme();
            updateCurrentChar();
            return e43();
        }else if(currentChar == '|'){
            updateLexeme();
            updateCurrentChar();
            return e45();
        }else if(currentChar == '&'){
            updateLexeme();
            updateCurrentChar();
            return e47();
        }else{
            currentLine = sourceFileManager.getCurrentLine();
            updateLexeme();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "símbolo válido");
        }
    }

    private Token e1() throws IOException {
        if(!eof && (Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_')){
            updateLexeme();
            updateCurrentChar();
            return e1();
        }
        else
            return new Token("idClase", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e2() throws IOException {
        if(!eof && (Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_')){
            updateLexeme();
            updateCurrentChar();
            return e2();
        }
        else
            return new Token("idMetVar", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e3() throws IOException, LexicalException{
        multiLineCommentLineNumber = sourceFileManager.getLineNumber();
        multiLineCommentColumnNumber = sourceFileManager.getColumnNumber() - 1;
        multiLineCommentFirstLine = sourceFileManager.getCurrentLine();
        multiLineCommentEnter = false;
        if(currentChar == '*'){
            updateLexeme();
            updateCurrentChar();
            return e4();
        }else if(currentChar == '/'){
            updateLexeme();
            updateCurrentChar();
            return e6();
        }else
            return new Token("op/", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e4() throws IOException, LexicalException{
        if(!multiLineCommentEnter && currentChar != '\n')
            multiLineCommentFirstLine += currentChar;
        if(eof)
            throw new LexicalException(lexeme, multiLineCommentLineNumber, multiLineCommentColumnNumber, multiLineCommentFirstLine, "EOF sin cerrar comentario");
        else if(currentChar == '*') {
            updateLexeme();
            updateCurrentChar();
            return e5();
        }else{
            if(currentChar == '\n')
                multiLineCommentEnter = true;
            if(!multiLineCommentEnter)
                updateLexeme();
            updateCurrentChar();
            return e4();
        }
    }

    private Token e5() throws IOException, LexicalException{
        if(!multiLineCommentEnter && currentChar != '\n')
            multiLineCommentFirstLine += currentChar;
        if(eof) {
            throw new LexicalException(lexeme, multiLineCommentLineNumber, multiLineCommentColumnNumber, multiLineCommentFirstLine, "EOF sin cerrar comentario"); //TODO chequear esto
        }else if(currentChar =='*'){
            updateLexeme();
            updateCurrentChar();
            return e5();
        }else if(currentChar == '/'){
            lexeme = "";
            updateCurrentChar();
            return e0();
        }else{
            if(currentChar == '\n')
                multiLineCommentEnter = true;
            if(!multiLineCommentEnter)
                updateLexeme();
            updateCurrentChar();
            return e4();
        }
    }

    private Token e6() throws IOException, LexicalException{
        if(eof){
            return e0();
        }else if(currentChar == '\n'){
            lexeme = "";
            updateCurrentChar();
            return e0();
        }else{
            updateLexeme();
            updateCurrentChar();
            return e6();
        }
    }

    private Token e7() throws IOException, LexicalException {
        if(Character.isDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e8();
        }else
            return new Token("intLiteral", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e8() throws IOException, LexicalException {
        if(Character.isDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e9();
        }else
            return new Token("intLiteral", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e9() throws IOException, LexicalException {
        if(Character.isDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e10();
        }else
            return new Token("intLiteral", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e10() throws IOException, LexicalException {
        if(Character.isDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e11();
        }else
            return new Token("intLiteral", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e11() throws IOException, LexicalException {
        if(Character.isDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e12();
        }else
            return new Token("intLiteral", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e12() throws IOException, LexicalException {
        if(Character.isDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e13();
        }else
            return new Token("intLiteral", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e13() throws IOException, LexicalException {
        if(Character.isDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e14();
        }else
            return new Token("intLiteral", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e14() throws IOException, LexicalException {
        if(Character.isDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e15();
        }else
            return new Token("intLiteral", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e15() throws IOException, LexicalException {
        if(Character.isDigit(currentChar)){
            currentLine = sourceFileManager.getCurrentLine();
            updateLexeme();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "Literal entero supera longitud máxima");
        }
        return new Token("intLiteral", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e16() throws IOException, LexicalException{
        if(eof)
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF sin cerrar literal carácter");
        else if(currentChar == '\n'){
            currentLine = sourceFileManager.getCurrentLine();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "Enter en literal carácter");
        }else if(currentChar == '\'') {
            currentLine = sourceFileManager.getCurrentLine();
            updateLexeme();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "literal carácter vacío");
        }else if(currentChar == '\\'){
            updateLexeme();
            updateCurrentChar();
            return e19();
        }else{
            updateLexeme();
            updateCurrentChar();
            return e17();
        }
    }

    private Token e17() throws IOException, LexicalException{
        if(eof)
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF sin cerrar literal carácter");
        else if(currentChar == '\''){
            updateLexeme();
            updateCurrentChar();
            return e18();
        }else{
            currentLine = sourceFileManager.getCurrentLine();
            updateLexeme();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "formato de literal carácter inválido");
        }
    }

    private Token e18(){
        return new Token("charLiteral", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e19() throws IOException, LexicalException{
        if(eof)
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF sin cerrar literal carácter");
        else if(currentChar == '\n'){
            currentLine = sourceFileManager.getCurrentLine();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "Enter en literal carácter");
        }else if(currentChar == 'u'){
            updateLexeme();
            updateCurrentChar();
            return e50();
        }else{
            updateLexeme();
            updateCurrentChar();
            return e20();
        }
    }

    private Token e20() throws IOException, LexicalException{
        if(eof)
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF sin cerrar literal carácter");
        else if(currentChar == '\''){
            updateLexeme();
            updateCurrentChar();
            return e18();
        }else{
            currentLine = sourceFileManager.getCurrentLine();
            updateLexeme();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "formato de literal carácter inválido");
        }
    }

    private Token e21() throws IOException, LexicalException{
        if(eof)
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF sin cerrar literal string");
        if(currentChar == '\n'){
            currentLine = sourceFileManager.getCurrentLine();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "salto de línea dentro de un literal string");
        }else if(currentChar == '\\'){
            updateLexeme();
            updateCurrentChar();
            return e23();
        }else if(currentChar == '"'){
            updateLexeme();
            updateCurrentChar();
            return e22();
        }else{
            updateLexeme();
            updateCurrentChar();
            return e21();
        }
    }

    private Token e22(){
        return new Token("stringLiteral", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e23() throws IOException, LexicalException {
        if(eof)
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF sin cerrar literal string");
        if(currentChar == '\n'){
            currentLine = sourceFileManager.getCurrentLine();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "salto de línea dentro de un literal string");
        }else{
            updateLexeme();
            updateCurrentChar();
            return e21();
        }
    }

    private Token e24(){
        return new Token("parenA", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e25(){
        return new Token("parenC", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e26(){
        return new Token("llaveA", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e27(){
        return new Token("llaveC", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e28() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return e29();
        }else
            return new Token("op<", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e29(){
        return new Token("op<=", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e30() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return e31();
        }else
            return new Token("op>", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e31(){
        return new Token("op>=", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e32(){
        return new Token("op*", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e33() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return e34();
        }else
            return new Token("asig=", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e34(){
        return new Token("op==", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e35() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return e36();
        }else
            return new Token("op!", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e36(){
        return new Token("op!=", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e37(){
        return new Token("puntoComa", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e38(){
        return new Token("coma", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e39(){
        return new Token("punto", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e40(){
        return new Token("op%", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e41() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return e42();
        }else
            return new Token("op+", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e42(){
        return new Token("asig+=", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e43() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return e44();
        }else
            return new Token("op-", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e44(){
        return new Token("asig-=", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e45() throws IOException, LexicalException{
        if(eof)
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF tras operador mal formado");
        else if(currentChar == '|'){
            updateLexeme();
            updateCurrentChar();
            return e46();
        }else {
            currentLine = sourceFileManager.getCurrentLine();
            updateLexeme();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "operador mal formado");
        }
    }

    private Token e46(){
        return new Token("op||", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e47() throws IOException, LexicalException{
        if(eof)
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF tras operador mal formado");
        else if(currentChar == '&'){
            updateLexeme();
            updateCurrentChar();
            return e48();
        }else{
            currentLine = sourceFileManager.getCurrentLine();
            updateLexeme();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "operador mal formado");
        }
    }

    private Token e48(){
        return new Token("op&&", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e49(){
        return new Token("EOF", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e50() throws IOException, LexicalException{
        if(eof)
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF sin cerrar literal carácter");
        else if(isHexadecimalDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e51();
        } else {
            currentLine = sourceFileManager.getCurrentLine();
            updateLexeme();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "formato de literal carácter inválido");
        }
    }

    private Token e51() throws IOException, LexicalException{
        if(eof)
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF sin cerrar literal carácter");
        else if(isHexadecimalDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e52();
        } else {
            currentLine = sourceFileManager.getCurrentLine();
            updateLexeme();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "formato de literal carácter inválido");
        }
    }

    private Token e52() throws IOException, LexicalException{
        if(eof)
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF sin cerrar literal carácter");
        else if(isHexadecimalDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e53();
        } else {
            currentLine = sourceFileManager.getCurrentLine();
            updateLexeme();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "formato de literal carácter inválido");
        }
    }

    private Token e53() throws IOException, LexicalException{
        if(eof)
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF sin cerrar literal carácter");
        else if(isHexadecimalDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e54();
        } else {
            currentLine = sourceFileManager.getCurrentLine();
            updateLexeme();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "formato de literal carácter inválido");
        }
    }

    private Token e54() throws IOException, LexicalException{
        if(eof)
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF sin cerrar literal carácter");
        else if(currentChar == '\''){
            updateLexeme();
            updateCurrentChar();
            return e18();
        } else {
            currentLine = sourceFileManager.getCurrentLine();
            updateLexeme();
            updateCurrentChar();
            throw new LexicalException(lexeme, lastCharLineNumber, lastCharColumnNumber, currentLine, "formato de literal carácter inválido");
        }
    }

    private boolean isHexadecimalDigit(char character){
        return Character.isDigit(character) || (character >= 'a' && character <= 'f') || (character >= 'A' && character <= 'F');
    }

    private void initializeKeywordsMap(){
        keywordsMap = new HashMap<>();
        keywordsMap.put("class", "pr_class");
        keywordsMap.put("interface", "pr_interface");
        keywordsMap.put("extends", "pr_extends");
        keywordsMap.put("implements", "pr_implements");
        keywordsMap.put("public", "pr_public");
        keywordsMap.put("private", "pr_private");
        keywordsMap.put("static", "pr_static");
        keywordsMap.put("void", "pr_void");
        keywordsMap.put("boolean", "pr_boolean");
        keywordsMap.put("char", "pr_char");
        keywordsMap.put("int", "pr_int");
        keywordsMap.put("if", "pr_if");
        keywordsMap.put("else", "pr_else");
        keywordsMap.put("while", "pr_while");
        keywordsMap.put("return", "pr_return");
        keywordsMap.put("var", "pr_var");
        keywordsMap.put("this", "pr_this");
        keywordsMap.put("new", "pr_new");
        keywordsMap.put("null", "pr_null");
        keywordsMap.put("true", "pr_true");
        keywordsMap.put("false", "pr_false");
    }
}