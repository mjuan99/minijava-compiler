package symbolTable.ast.sentences.defaultMethodBlocks;

import codeGenerator.CodeGenerator;
import symbolTable.ast.sentences.ASTBlock;

public class ASTPrintSBlock extends ASTBlock {

    public void generateCode() {
        CodeGenerator.generateCode("LOAD 3");
        CodeGenerator.generateCode("PUSH 1");
        CodeGenerator.generateCode("ADD");
        CodeGenerator.generateCode("SPRINT");
    }
}
