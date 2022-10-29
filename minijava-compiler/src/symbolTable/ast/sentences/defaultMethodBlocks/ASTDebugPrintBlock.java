package symbolTable.ast.sentences.defaultMethodBlocks;

public class ASTDebugPrintBlock extends ASTPrintPrimitiveTypeBlock{
    public ASTDebugPrintBlock() {
        printInstruction = "IPRINT";
        printNewLine = true;
    }
}
