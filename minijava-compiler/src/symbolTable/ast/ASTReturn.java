package symbolTable.ast;

public class ASTReturn implements ASTSentence{
    private final ASTExpression astExpression;

    public ASTReturn(ASTExpression astExpression){
        this.astExpression = astExpression;
    }

    @Override
    public void print() {
        System.out.print("return ");
        astExpression.print();
        System.out.println(";");
    }
}
