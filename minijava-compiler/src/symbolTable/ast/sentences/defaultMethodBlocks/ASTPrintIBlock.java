package symbolTable.ast.sentences.defaultMethodBlocks;

public class ASTPrintIBlock extends ASTPrintPrimitiveTypeBlock {
    public ASTPrintIBlock() {
        printInstruction = "IPRINT";
        printNewLine = false;
    }
}
