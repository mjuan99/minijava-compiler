package symbolTable.ast;

import lexicalAnalyzer.Token;

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

    public void print() {
        System.out.print("new " + tkClassName.getLexeme() + "()");
        if(astChaining != null)
            astChaining.print();
    }
}
