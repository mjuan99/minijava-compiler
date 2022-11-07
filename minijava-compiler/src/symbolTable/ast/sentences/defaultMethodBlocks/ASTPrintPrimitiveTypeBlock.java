package symbolTable.ast.sentences.defaultMethodBlocks;

import codeGenerator.CodeGenerator;
import symbolTable.ast.sentences.ASTBlock;

public abstract class ASTPrintPrimitiveTypeBlock extends ASTBlock {
    protected String printInstruction;
    protected boolean printNewLine;

    public void generateCode(){
        if(printInstruction != null) {
            CodeGenerator.generateCode("LOAD 3 ;cargar parametro a imprimir");
            CodeGenerator.generateCode(printInstruction + " ;imprimir por pantalla");
        }
        if(printNewLine)
            CodeGenerator.generateCode("PRNLN ;imprimir salto de linea");
    }
}
