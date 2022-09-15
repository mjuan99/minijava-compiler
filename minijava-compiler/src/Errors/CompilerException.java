package Errors;

import java.util.LinkedList;

public class CompilerException extends Exception{
    private LinkedList<CompilerError> errors;

    public CompilerException(LinkedList<CompilerError> errors){
        this.errors = errors;
    }

    public String getMessage(){
        String message = "";
        for(CompilerError error : errors)
            message += error.getMessage() + "\n";
        return message;
    }
}
