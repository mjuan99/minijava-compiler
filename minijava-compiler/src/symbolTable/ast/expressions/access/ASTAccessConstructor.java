package symbolTable.ast.expressions.access;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;

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
    public boolean endsWithVariable() {
        return false;
    }

    public void print() {
        System.out.print("new " + tkClassName.getLexeme() + "()");
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
}
