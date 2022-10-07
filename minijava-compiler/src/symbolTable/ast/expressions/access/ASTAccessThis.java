package symbolTable.ast.expressions.access;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;

public class ASTAccessThis implements ASTAccess{
    ASTChaining astChaining;

    public ASTAccessThis(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }

    public void print() {
        System.out.print("this");
        if(astChaining != null)
            astChaining.print();
    }

    @Override
    public Token getToken() {
        return null;//TODO implementar
    }

    @Override
    public STType check() {
        return null;//TODO implementar
    }

    public void setASTChainng(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }

    @Override
    public boolean endsWithVariable() {
        return false;//TODO implementar
    }
}
