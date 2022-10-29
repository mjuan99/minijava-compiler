package symbolTable.ast.sentences.defaultMethodBlocks;

import codeGenerator.CodeGenerator;
import symbolTable.ast.sentences.ASTBlock;

public class ASTReadBlock extends ASTBlock {

    @Override
    public void generateCode() {
        CodeGenerator.generateCode("READ");
    }
}
