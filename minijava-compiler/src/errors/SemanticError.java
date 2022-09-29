package errors;

import lexicalAnalyzer.Token;

import java.util.Objects;

public class SemanticError implements CompilerError{
    private final Token token;
    private final String message;
    public SemanticError(Token token, String message){
        this.token = token;
        this.message = message;
    }

    public String getMessage() {
        return (!Objects.equals(message, "") ? "Error semántico en la línea " + token.getLineNumber() + ": "+ message + "\n" : "") + "[Error:" + token.getLexeme() + "|" + token.getLineNumber() + "]\n";
    }
}
