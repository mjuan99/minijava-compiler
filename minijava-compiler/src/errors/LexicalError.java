package errors;

public class LexicalError implements CompilerError{
    private final String lexeme;
    private final int lineNumber;

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
