package symbolTable.ast.sentences.defaultMethodBlocks;

import codeGenerator.CodeGenerator;
import symbolTable.ast.sentences.ASTBlock;

public class ASTReadBlock extends ASTBlock {

    @Override
    public void generateCode() {
        CodeGenerator.generateCode("READ ;leer entrada");
        CodeGenerator.generateCode("STORE 3 ;almacenar valor leido en el lugar de retorno");
    }
}
