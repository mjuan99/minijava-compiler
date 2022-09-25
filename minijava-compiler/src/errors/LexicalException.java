package errors;

public class LexicalException extends Exception{
    private final String lexeme;
    private final int lineNumber;
    private final int columnNumber;
    private final String line;
    public LexicalException(String lexeme, int lineNumber, int columnNumber, String line, String message){
        super(message);
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.line = line;
    }

    public String getLexeme(){
        return lexeme;
    }

    public int getLineNumber(){
        return lineNumber;
    }

    public int getColumnNumber(){
        return columnNumber;
    }

    public String getLine(){
        return line;
    }
}
