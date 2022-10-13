package symbolTable.ast.sentences;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.types.STType;
import symbolTable.types.STTypeBoolean;

public class ASTIf implements ASTSentence{
    private final Token tkIf;
    private final ASTExpression condition;
    private final ASTSentence thenSentence;
    private final ASTSentence elseSentence;

    public ASTIf(Token tkIf, ASTExpression condition, ASTSentence thenSentence, ASTSentence elseSentence) {
        this.tkIf = tkIf;
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
    public void checkSentences() throws SemanticException {
        STType conditionType = condition.check();
        if(!conditionType.conformsWith(new STTypeBoolean()))
            throw new SemanticException(new SemanticError(tkIf, "la condición del if deberia ser tipo boolean pero es tipo " + conditionType));
        thenSentence.checkSentences();
        if(elseSentence != null)
            elseSentence.checkSentences();
    }
}
