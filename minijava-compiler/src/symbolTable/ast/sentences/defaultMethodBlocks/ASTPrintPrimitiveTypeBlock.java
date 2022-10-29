package symbolTable.ast.sentences.defaultMethodBlocks;

import codeGenerator.CodeGenerator;
import symbolTable.ast.sentences.ASTBlock;

public abstract class ASTPrintPrimitiveTypeBlock extends ASTBlock {
    protected String printInstruction;
    protected boolean printNewLine;

    public void generateCode(){
        if(printInstruction != null) {
            CodeGenerator.generateCode("LOAD 3");
            CodeGenerator.generateCode(printInstruction);
        }
        if(printNewLine)
            CodeGenerator.generateCode("PRNLN");
    }
}
