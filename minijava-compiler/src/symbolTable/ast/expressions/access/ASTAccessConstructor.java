package symbolTable.ast.expressions.access;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.entities.STClass;
import symbolTable.types.STType;
import symbolTable.types.STTypeReference;

public class ASTAccessConstructor implements ASTAccess{
    private final Token tkClassName;
    private ASTChaining astChaining;

    public ASTAccessConstructor(Token tkClassName, ASTChaining astChaining) {
        this.tkClassName = tkClassName;
        this.astChaining = astChaining;
    }

    public void setASTChainng(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }

    @Override
    public boolean isValidCall() {
        if(astChaining == null)
            return true;
        else
            return astChaining.isValidCall();
    }

    @Override
    public boolean isValidVariable() {
        if(astChaining == null)
            return false;
        else
            return astChaining.isValidVariable();
    }

    public void print() {
        System.out.print("new " + tkClassName.getLexeme() + "()");
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public Token getToken() {
        if(astChaining == null)
            return tkClassName;
        else
            return astChaining.getToken();
    }

    @Override
    public STType check() throws SemanticException {
        STClass stClass = ST.symbolTable.getSTClass(tkClassName.getLexeme());
        if(stClass == null)
            throw new SemanticException(new SemanticError(tkClassName, "la clase " + tkClassName.getLexeme() + " no fue declarada"));
        if(astChaining == null)
            return new STTypeReference(tkClassName);
        else{
            return astChaining.check(new STTypeReference(tkClassName));
        }
    }
}
