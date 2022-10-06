package symbolTable.ast;

public class ASTWhile implements ASTSentence{
    private final ASTExpression condition;
    private final ASTSentence astSentence;

    public ASTWhile(ASTExpression condition, ASTSentence astSentence) {
        this.condition = condition;
        this.astSentence = astSentence;
    }

    public void print() {
        System.out.print("while(");
        condition.print();
        System.out.print(")");
        astSentence.print();
    }
}
