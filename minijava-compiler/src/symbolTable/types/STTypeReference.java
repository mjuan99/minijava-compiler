package symbolTable.types;

import errors.SemanticError;
import lexicalAnalyzer.Token;
import symbolTable.ST;

import java.util.Objects;

public class STTypeReference implements STType{
    private final Token reference;

    public STTypeReference(Token reference){
        this.reference = reference;
    }

    public String toString(){
        return reference.getLexeme();
    }

    public boolean checkDeclaration(){
        if(!ST.symbolTable.stClassExists(reference.getLexeme()) && !ST.symbolTable.stInterfaceExists(reference.getLexeme())) {
            ST.symbolTable.addError(new SemanticError(reference, "la clase " + reference.getLexeme() + " no fue declarada"));
            return false;
        }else
            return true;
    }

    public boolean equals(STType stType){
        if(stType instanceof STTypeReference){
            return Objects.equals(reference.getLexeme(), ((STTypeReference) stType).reference.getLexeme());
        }else
            return false;
    }
}
