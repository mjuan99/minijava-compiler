package symbolTable.ast;

public class ASTWhile implements ASTSentence{
    private ASTExpression condition;
    private ASTSentence astSentence;

    public ASTWhile(ASTExpression condition, ASTSentence astSentence) {
        this.condition = condition;
        this.astSentence = astSentence;
    }

    @Override
    public void print() {
        System.out.print("while(");
        condition.print();
        System.out.print(")");
        astSentence.print();
    }
}
