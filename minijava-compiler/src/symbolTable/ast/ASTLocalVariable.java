package symbolTable.ast;

import lexicalAnalyzer.Token;

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
}
