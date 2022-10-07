package errors;

import java.util.LinkedList;

public class SemanticException extends Exception{
    private final LinkedList<CompilerError> errors;

    public SemanticException(LinkedList<CompilerError> errors){
        this.errors = errors;
    }

    public SemanticException(CompilerError error){
        errors = new LinkedList<>();
        errors.add(error);
    }

    public String getMessage(){
        StringBuilder message = new StringBuilder();
        for(CompilerError error : errors)
            message.append(error.getMessage()).append("\n");
        return message.toString();
    }
}
