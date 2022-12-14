package errors;

import java.util.LinkedList;

public class SyntacticException extends Exception{
    private final LinkedList<CompilerError> errors;

    public SyntacticException(LinkedList<CompilerError> errors){
        this.errors = errors;
    }

    public String getMessage(){
        StringBuilder message = new StringBuilder();
        for(CompilerError error : errors)
            message.append(error.getMessage()).append("\n");
        return message.toString();
    }
}
