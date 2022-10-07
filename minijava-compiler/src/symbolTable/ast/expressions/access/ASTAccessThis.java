package symbolTable.ast.expressions.access;

import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.types.STType;
import symbolTable.types.STTypeReference;

public class ASTAccessThis implements ASTAccess{
    private ASTChaining astChaining;
    private boolean endsWithVariable;

    public ASTAccessThis(ASTChaining astChaining) {
        this.astChaining = astChaining;
        endsWithVariable = false;
    }

    public void print() {
        System.out.print("this");
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public Token getToken() {
        if(astChaining == null)
            //TODO ver de donde saco el token
            return new Token("pr_this", "this", 0);
        else
            return astChaining.getToken();
    }

    @Override
    public STType check() throws SemanticException {
        if(astChaining == null)
            return new STTypeReference(ST.symbolTable.getCurrentSTClass().getTKName());
        else
            return astChaining.check(new STTypeReference(ST.symbolTable.getCurrentSTClass().getTKName()));
    }

    public void setASTChainng(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }

    @Override
    public boolean endsWithVariable() {
        return endsWithVariable;
    }
}
