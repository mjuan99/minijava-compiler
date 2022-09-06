package syntacticAnalyzer;

import lexicalAnalyzer.Token;

public class SyntacticException extends Exception{
    private Token found;
    private String expected;

    public SyntacticException(Token found, String expected){
        this.found = found;
        this.expected = expected;
    }

    public String getMessage(){
        return "Error sintactico en linea " + found.getLineNumber() + ": se esperaba " + expected + " pero se encontro " + found.getLexeme();
    }

    public Token getToken(){
        return found;
    }
}
