package Errors;

import lexicalAnalyzer.Token;

public class SemanticError implements CompilerError{
    private Token token;
    private String message;
    public SemanticError(Token token, String message){
        this.token = token;
        this.message = message;
    }

    public String getMessage() {
        return message + "\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]\n";
    }
}
