package symbolTable.ast;

public class ASTReturn implements ASTSentence{
    private final ASTExpression returnExpression;

    public ASTReturn(ASTExpression returnExpression){
        this.returnExpression = returnExpression;
    }

    @Override
    public void print() {
        System.out.print("return ");
        if(returnExpression != null)
            returnExpression.print();
        System.out.println(";");
    }

    @Override
    public void checkSentences() {
        //TODO implementar

    }
}
