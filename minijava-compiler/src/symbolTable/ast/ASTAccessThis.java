package symbolTable.ast;

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

    public void setASTChainng(ASTChaining astChaining) {
        this.astChaining = astChaining;
    }
}
