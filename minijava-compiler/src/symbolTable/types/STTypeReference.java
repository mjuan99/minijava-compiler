package symbolTable.types;

import Errors.SemanticError;
import lexicalAnalyzer.Token;
import syntacticAnalyzer.SyntacticAnalyzer;

public class STTypeReference implements STType{
    Token reference;

    public STTypeReference(Token reference){
        this.reference = reference;
    }

    public String toString(){
        return reference.getLexeme();
    }

    public void checkDeclaration(){
        if(!SyntacticAnalyzer.symbolTable.stClassExists(reference.getLexeme()) && !SyntacticAnalyzer.symbolTable.stInterfaceExists(reference.getLexeme()))
            SyntacticAnalyzer.symbolTable.addError(new SemanticError(reference, "la clase " + reference.getLexeme() + " no fue declarada"));
    }
}
