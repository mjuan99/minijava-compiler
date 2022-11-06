package symbolTable.ast.sentences;

import codeGenerator.CodeGenerator;
import codeGenerator.TagManager;
import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ast.expressions.ASTExpression;
import symbolTable.types.STType;
import symbolTable.types.STTypeBoolean;

public class ASTWhile extends ASTSentence{
    private final Token tkWhile;
    private final ASTExpression condition;
    private final ASTSentence astSentence;

    public ASTWhile(Token tkWhile, ASTExpression condition, ASTSentence astSentence) {
        this.tkWhile = tkWhile;
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
            throw new SemanticException(new SemanticError(tkWhile, "la condición del while deberia ser tipo boolean pero es tipo " + conditionType));
        astSentence.checkSentences();
    }

    @Override
    public void generateCode() {
        String whileConditionTag = TagManager.getTag("whileCondition");
        String endWhileTag = TagManager.getTag("endWhile");
        CodeGenerator.setNextInstructionTag(whileConditionTag);
        condition.generateCode();
        CodeGenerator.setComment("condicion del while");
        CodeGenerator.generateCode("BF " + endWhileTag + " ;salto al fin del while");
        astSentence.generateCode();
        CodeGenerator.generateCode("JUMP " + whileConditionTag + " ;salto al a condicion del while");
        CodeGenerator.generateCode(endWhileTag + ": NOP ;fin del while");
    }
}
