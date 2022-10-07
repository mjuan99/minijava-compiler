package symbolTable.ast;

public class ASTIf implements ASTSentence{
    private final ASTExpression condition;
    private final ASTSentence thenSentence;
    private final ASTSentence elseSentence;

    public ASTIf(ASTExpression condition, ASTSentence thenSentence, ASTSentence elseSentence) {
        this.condition = condition;
        this.thenSentence = thenSentence;
        this.elseSentence = elseSentence;
    }

    public void print(){
        System.out.print("if(");
        condition.print();
        System.out.print(") ");
        thenSentence.print();
        if(elseSentence != null){
            System.out.print("else ");
            elseSentence.print();
        }
    }

    @Override
    public void checkSentences() {
        //TODO implementar
    }
}
