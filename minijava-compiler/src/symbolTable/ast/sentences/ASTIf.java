package symbolTable.ast.sentences;

import codeGenerator.CodeGenerator;
import codeGenerator.TagManager;
import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.types.STType;
import symbolTable.types.STTypeBoolean;

public class ASTIf extends ASTSentence{
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
        if(elseSentence != null) {
            elseSentence.checkSentences();
            alwaysReturns = thenSentence.alwaysReturns && elseSentence.alwaysReturns;
        }
    }

    @Override
    public void generateCode() {
        String thenTag = TagManager.getTag("then");
        String endIfTag = TagManager.getTag("endIf");
        String elseTag = elseSentence != null ? TagManager.getTag("else") : endIfTag;
        condition.generateCode();
        CodeGenerator.generateCode("BF " + elseTag);
        CodeGenerator.setNextInstructionTag(thenTag);
        thenSentence.generateCode();
        if(elseSentence != null) {
            CodeGenerator.generateCode("JUMP " + endIfTag);
            CodeGenerator.setNextInstructionTag(elseTag);
            elseSentence.generateCode();
        }
        CodeGenerator.generateCode(endIfTag + ": " + "NOP ;fin del if");
    }
}
