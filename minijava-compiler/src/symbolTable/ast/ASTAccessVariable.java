package symbolTable.ast;

import lexicalAnalyzer.Token;

public class ASTAccessVariable implements ASTAccess{
    private final Token tkVariable;
    private ASTChaining astChaining;

    public ASTAccessVariable(Token tkVariable, ASTChaining astChaining) {
        this.tkVariable = tkVariable;
        this.astChaining = astChaining;
    }

    public void print() {
        System.out.print(tkVariable.getLexeme());
        if(astChaining != null)
            astChaining.print();
    }

    public void setASTChainng(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }
}
