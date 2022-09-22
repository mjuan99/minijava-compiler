package symbolTable.types;

import Errors.SemanticError;
import lexicalAnalyzer.Token;
import symbolTable.ST;

public class STTypeReference implements STType{
    Token reference;

    public STTypeReference(Token reference){
        this.reference = reference;
    }

    public String toString(){
        return reference.getLexeme();
    }

    public void checkDeclaration(){
        if(!ST.symbolTable.stClassExists(reference.getLexeme()) && !ST.symbolTable.stInterfaceExists(reference.getLexeme()))
            ST.symbolTable.addError(new SemanticError(reference, "la clase " + reference.getLexeme() + " no fue declarada"));
    }
}
