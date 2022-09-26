package errors;

import lexicalAnalyzer.Token;

public class SemanticError implements CompilerError{
    private final Token token;
    private final String message;
    public SemanticError(Token token, String message){
        this.token = token;
        this.message = message;
    }

    public String getMessage() {
        return "Error semántico en la línea " + token.getLineNumber() + ": "+ message + "\n[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]\n";
    }
}