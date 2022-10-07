package symbolTable.ast.sentences;

import lexicalAnalyzer.Token;
import symbolTable.ast.expressions.ASTExpression;

public class ASTLocalVariable implements ASTSentence{
    private final Token tkVariable;
    private final ASTExpression value;

    public ASTLocalVariable(Token tkVariable, ASTExpression value) {
        this.tkVariable = tkVariable;
        this.value = value;
    }

    public void print(){
        System.out.print("var " + tkVariable.getLexeme() + " = ");
        value.print();
        System.out.println(";");
    }

    @Override
    public void checkSentences() {
        //TODO implementar

    }
}
