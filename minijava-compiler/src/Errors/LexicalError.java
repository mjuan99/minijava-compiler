package Errors;

public class LexicalError implements CompilerError{
    private String lexeme;
    private int lineNumber;

    public LexicalError(String lexeme, int lineNumber){
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
    }

    public String getMessage(){
        return "[Error:" + lexeme + "|" + lineNumber + "]";
    }

    public String getLexeme(){
        return lexeme;
    }

    public int getLineNumber(){
        return lineNumber;
    }
}
