package symbolTable.types;

import lexicalAnalyzer.Token;

public class STTypeReference implements STType{
    Token reference;

    public STTypeReference(Token reference){
        this.reference = reference;
    }

    public String toString(){
        return reference.getLexeme();
    }
}
