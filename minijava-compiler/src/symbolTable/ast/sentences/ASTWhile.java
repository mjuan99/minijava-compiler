package symbolTable.ast.sentences;

import errors.SemanticError;
import errors.SemanticException;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.types.STType;
import symbolTable.types.STTypeBoolean;

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
        System.out.print(") ");
        astSentence.print();
    }

    @Override
    public void checkSentences() throws SemanticException {
        STType conditionType = condition.check();
        if(!conditionType.conformsWith(new STTypeBoolean()))
            throw new SemanticException(new SemanticError(condition.getToken(), "la condición del while deberia ser tipo boolean pero es tipo " + conditionType));
        astSentence.checkSentences();
    }
}
