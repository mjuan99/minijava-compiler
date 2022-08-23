package lexicalAnalyzer;

public class LexicalException extends Exception{
    private String lexeme;
    private int lineNumber;
    private int columnNumber;
    public LexicalException(String lexeme, int lineNumber, int columnNumber, String message){
        super(message);
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
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
}
