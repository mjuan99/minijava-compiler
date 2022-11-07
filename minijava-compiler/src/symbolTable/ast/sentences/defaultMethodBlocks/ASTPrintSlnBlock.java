package symbolTable.ast.sentences.defaultMethodBlocks;

import codeGenerator.CodeGenerator;
import symbolTable.ast.sentences.ASTBlock;

public class ASTPrintSlnBlock extends ASTBlock {

    public void generateCode() {
        CodeGenerator.generateCode("LOAD 3 ;cargar string a imprimir (1)");
        CodeGenerator.generateCode("PUSH 1 ;cargar string a imprimir (2)");
        CodeGenerator.generateCode("ADD ;cargar string a imprimir (3)");
        CodeGenerator.generateCode("SPRINT ;imprimir string");
        CodeGenerator.generateCode("PRNLN ;salto de linea");
    }
}
