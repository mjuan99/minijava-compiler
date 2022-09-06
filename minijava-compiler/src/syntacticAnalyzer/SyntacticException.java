package syntacticAnalyzer;

import lexicalAnalyzer.Token;

public class SyntacticException extends Exception{
    private String actualToken;
    private String expectedToken;

    public SyntacticException(String actualToken, String expectedToken){
        this.actualToken = actualToken;
        this.expectedToken = expectedToken;
    }
}
