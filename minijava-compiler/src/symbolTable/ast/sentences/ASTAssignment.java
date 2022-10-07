package symbolTable.ast.sentences;

import lexicalAnalyzer.Token;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.ast.expressions.access.ASTAccess;

public class ASTAssignment implements ASTSentence{
    private final ASTAccess astAccess;
    private final Token tkAssignment;
    private final ASTExpression value;

    public ASTAssignment(ASTAccess astAccess, Token tkAssignment, ASTExpression value) {
        this.astAccess = astAccess;
        this.tkAssignment = tkAssignment;
        this.value = value;
    }

    public void print() {
        astAccess.print();
        System.out.print(" " + tkAssignment.getLexeme() + " ");
        value.print();
        System.out.println(";");
    }

    @Override
    public void checkSentences() {
        //TODO implementar

    }
}