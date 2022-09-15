package Errors;

import lexicalAnalyzer.Token;

public class SyntacticError implements CompilerError{
    private Token found;
    private String expected;

    public SyntacticError(Token found, String expected){
        this.found = found;
        this.expected = expected;
    }

    public String getMessage(){
        return "Error sintactico en linea " + found.getLineNumber() + ": se esperaba " + expected +
                " pero se encontro " + found.getLexeme() +
                "\n[Error:" + found.getLexeme() + "|" + found.getLineNumber() + "]";
    }

}
