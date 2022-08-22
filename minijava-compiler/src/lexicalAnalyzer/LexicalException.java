package lexicalAnalyzer;

public class LexicalException extends Exception{
    private String lexeme;
    private int lineNumber;
    public LexicalException(String lexeme, int lineNumber, String message){
        super(message);
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
    }

    public String getLexeme(){
        return lexeme;
    }

    public int getLineNumber(){
        return lineNumber;
    }
}
