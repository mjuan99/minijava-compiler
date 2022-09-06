package syntacticAnalyzer;

import lexicalAnalyzer.LexicalAnalyzer;
import lexicalAnalyzer.LexicalException;
import lexicalAnalyzer.Token;

import java.io.IOException;

public class SyntacticAnalyzer {
    private LexicalAnalyzer lexicalAnalyzer;
    private Token currentToken;

    public SyntacticAnalyzer(LexicalAnalyzer lexicalAnalyzer) throws LexicalException, IOException {
        this.lexicalAnalyzer = lexicalAnalyzer;
        currentToken = lexicalAnalyzer.getNextToken();
        Inicial();
    }

    private void match(String expectedToken) throws SyntacticException, LexicalException, IOException {
        if(expectedToken.equals(currentToken.getTokenType()))
            currentToken = lexicalAnalyzer.getNextToken();
        else
            throw new SyntacticException(currentToken.getTokenType(), expectedToken);
    }

    private void Inicial(){

    }

}
