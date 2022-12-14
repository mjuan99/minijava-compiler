package lexicalAnalyzer;

import errors.LexicalException;
import lexicalAnalyzer.sourceFileManager.SourceFileManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class LexicalAnalyzer {
    private String lexeme;
    private char currentChar;
    private final SourceFileManager sourceFileManager;
    private HashMap<String, String> keywordsMap;
    private int multiLineCommentLineNumber;
    private int multiLineCommentColumnNumber;
    private String multiLineCommentFirstLine;
    private boolean multiLineCommentEnter;

    private final static String ST_ERR_INVALID_CHAR = "literal carácter malformado";
    private final static String ST_ERR_INVALID_INT = "literal entero malformado";
    private final static String ST_ERR_INVALID_STRING = "literal string malformado";
    private final static String ST_ERR_MULTILINE_COMMENT = "comentario multilínea sin cerrar";
    private final static String ST_ERR_INVALID_SYMBOL = "símbolo inválido";
    private final static String ST_ERR_INVALID_OPERATOR = "operador malformado";

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
        currentChar = sourceFileManager.getNextChar();
    }
    
    private boolean eof(){
        return currentChar == (char)-1;
    }

    private Token e0() throws IOException, LexicalException {
        if(eof())
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
            updateLexeme();
            LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_SYMBOL);
            updateCurrentChar();
            throw exception;
        }
    }

    private Token e1() throws IOException {
        if(Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_'){
            updateLexeme();
            updateCurrentChar();
            return e1();
        }
        else
            return new Token("idClase", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e2() throws IOException {
        if(Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_'){
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
            return new Token("/", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e4() throws IOException, LexicalException{
        if(!multiLineCommentEnter && currentChar != '\n')
            multiLineCommentFirstLine += currentChar;
        if(eof())
            throw new LexicalException(lexeme, multiLineCommentLineNumber,
                    multiLineCommentColumnNumber, multiLineCommentFirstLine, ST_ERR_MULTILINE_COMMENT);
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
        if(eof()) {
            throw new LexicalException(lexeme, multiLineCommentLineNumber,
                    multiLineCommentColumnNumber, multiLineCommentFirstLine, ST_ERR_MULTILINE_COMMENT);
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
        if(eof()){
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
            updateLexeme();
            LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_INT);
            updateCurrentChar();
            throw exception;
        }
        return new Token("intLiteral", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e16() throws IOException, LexicalException{
        if(eof())
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_CHAR);
        else if(currentChar == '\n'){
            LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_CHAR);
            updateCurrentChar();
            throw exception;
        }else if(currentChar == '\'') {
            updateLexeme();
            LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_CHAR);
            updateCurrentChar();
            throw exception;
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
        if(eof())
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_CHAR);
        else if(currentChar == '\''){
            updateLexeme();
            updateCurrentChar();
            return e18();
        }else{
            updateLexeme();
            LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_CHAR);
            updateCurrentChar();
            throw exception;
        }
    }

    private Token e18(){
        return new Token("charLiteral", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e19() throws IOException, LexicalException{
        if(eof())
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_CHAR);
        else if(currentChar == '\n'){
            LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_CHAR);
            updateCurrentChar();
            throw exception;
        }else if(currentChar == 'u'){
            updateLexeme();
            updateCurrentChar();
            return e50();
        }else{
            updateLexeme();
            updateCurrentChar();
            return e17();
        }
    }

    private Token e21() throws IOException, LexicalException{
        if(eof())
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_STRING);
        if(currentChar == '\n'){
            LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_STRING);
            updateCurrentChar();
            throw exception;
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
        if(eof())
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_STRING);
        if(currentChar == '\n'){
            LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_STRING);
            updateCurrentChar();
            throw exception;
        }else{
            updateLexeme();
            updateCurrentChar();
            return e21();
        }
    }

    private Token e24(){
        return new Token("(", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e25(){
        return new Token(")", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e26(){
        return new Token("{", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e27(){
        return new Token("}", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e28() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return e29();
        }else
            return new Token("<", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e29(){
        return new Token("<=", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e30() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return e31();
        }else
            return new Token(">", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e31(){
        return new Token(">=", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e32(){
        return new Token("*", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e33() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return e34();
        }else
            return new Token("=", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e34(){
        return new Token("==", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e35() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return e36();
        }else
            return new Token("!", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e36(){
        return new Token("!=", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e37(){
        return new Token(";", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e38(){
        return new Token(",", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e39(){
        return new Token(".", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e40(){
        return new Token("%", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e41() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return e42();
        }else
            return new Token("+", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e42(){
        return new Token("+=", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e43() throws IOException{
        if(currentChar == '='){
            updateLexeme();
            updateCurrentChar();
            return e44();
        }else
            return new Token("-", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e44(){
        return new Token("-=", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e45() throws IOException, LexicalException{
        if(eof())
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_OPERATOR);
        else if(currentChar == '|'){
            updateLexeme();
            updateCurrentChar();
            return e46();
        }else {
            updateLexeme();
            LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_OPERATOR);
            updateCurrentChar();
            throw exception;
        }
    }

    private Token e46(){
        return new Token("||", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e47() throws IOException, LexicalException{
        if(eof())
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_OPERATOR);
        else if(currentChar == '&'){
            updateLexeme();
            updateCurrentChar();
            return e48();
        }else{
            updateLexeme();
            LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_OPERATOR);
            updateCurrentChar();
            throw exception;
        }
    }

    private Token e48(){
        return new Token("&&", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e49(){
        return new Token("EOF", lexeme, sourceFileManager.getLineNumber());
    }

    private Token e50() throws IOException, LexicalException{
        if(eof())
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_CHAR);
        else if(isHexadecimalDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e51();
        } else {
            updateLexeme();
            LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_CHAR);
            updateCurrentChar();
            throw exception;
        }
    }

    private Token e51() throws IOException, LexicalException{
        if(eof())
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_CHAR);
        else if(isHexadecimalDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e52();
        } else {
            updateLexeme();
            LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_CHAR);
            updateCurrentChar();
            throw exception;
        }
    }

    private Token e52() throws IOException, LexicalException{
        if(eof())
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_CHAR);
        else if(isHexadecimalDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e53();
        } else {
            updateLexeme();
            LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_CHAR);
            updateCurrentChar();
            throw exception;
        }
    }

    private Token e53() throws IOException, LexicalException{
        if(eof())
            throw new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_CHAR);
        else if(isHexadecimalDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e17();
        } else {
            updateLexeme();
            LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
                    sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
                    ST_ERR_INVALID_CHAR);
            updateCurrentChar();
            throw exception;
        }
    }

    private boolean isHexadecimalDigit(char character){
        return Character.isDigit(character) || (character >= 'a' && character <= 'f') || (character >= 'A' && character <= 'F');
    }

    private void initializeKeywordsMap(){
        keywordsMap = new HashMap<>();
        keywordsMap.put("class", "class");
        keywordsMap.put("interface", "interface");
        keywordsMap.put("extends", "extends");
        keywordsMap.put("implements", "implements");
        keywordsMap.put("public", "public");
        keywordsMap.put("private", "private");
        keywordsMap.put("static", "static");
        keywordsMap.put("void", "void");
        keywordsMap.put("boolean", "boolean");
        keywordsMap.put("char", "char");
        keywordsMap.put("int", "int");
        keywordsMap.put("if", "if");
        keywordsMap.put("else", "else");
        keywordsMap.put("while", "while");
        keywordsMap.put("return", "return");
        keywordsMap.put("var", "var");
        keywordsMap.put("this", "this");
        keywordsMap.put("new", "new");
        keywordsMap.put("null", "null");
        keywordsMap.put("true", "true");
        keywordsMap.put("false", "false");
        keywordsMap.put("super", "super");
    }
}