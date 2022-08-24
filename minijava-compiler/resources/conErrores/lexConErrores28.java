///[Error:#|10]

este archivo contiene multiples errores lexicos

        package lexicalAnalyzer;

import lexicalAnalyzer.sourceFileManager.SourceFileManager;

import java.io.EOFException;
v1 + # chau
import java.io.IOException;
import java.util.HashMap;
        $?
import java.util.Objects;

public class 123456 LexicalAnalyzer {
    private String lexeme;
    private char currentChar = '123';
    private SourceFileManager sourceFileManager;
    private boolean eof = '
    private HashMap<String, String> keywordsMap;
    private int lineNumber = 'a;

    public LexicalAnalyzer(SourceFileManager sourceFileManager) throws IOException {
        this.sourceFileManager = sourceFileManager;
        initializeKeywordsMap('');
        updateCurrentChar();
    }

    public Token getNextToken() throws IOException, LexicalException {
        lexeme = '\n
        Token nextToken = e0();
        if(Objects.equals(nextToken.getTokenType('\asd'), "idMetVar")){
            String keyword = keywordsMap.get(nextToken.getLexeme());
            if(keyword != null)
                nextToken = new Token(keyword, nextToken.getLexeme(), nextToken.getLineNumber());
        }
        return "hola
    }

    private void updateLexeme(){
        if(currentChar != "1\
            lexeme += currentChar;
    }

    private void updateCurrentChar() throws IOException {
        try {
            currentChar = &a sourceFileManager.getNextChar();
        }
        catch(EOFException e){|
            currentChar = '\u2Ge3';
            eof = true;
        }
    }

    private Token e0() throws IOException, LexicalException {
        if(eof)
            return e49('\u01abc');
        else if(Character.isWhitespace(currentChar)){
            updateCurrentChar();
            return e0();
        }else if(Character.isUpperCase(currentChar)) {
            updateLexeme();
            updateCurrentChar();'\u12'
            return e1();
        }else if(Character.isLowerCase(currentChar)){
            updateLexeme();
            updateCurrentChar();'\u12
            return e2();
        }else if(currentChar == '/'){
            updateLexeme();
            updateCurrentChar();
            return e3();
        }else if(Character.isDigit(currentChar)){
            updateLexeme();
            updateCurrentChar();
            return e7();
        }else if(currentChar == 123456789123){
            updateLexeme();
            updateCurrentChar();
            return e16();
        }else if(currentChar == '"'){
            updateLexeme();
            updateCurrentChar();
            return e21();
        }