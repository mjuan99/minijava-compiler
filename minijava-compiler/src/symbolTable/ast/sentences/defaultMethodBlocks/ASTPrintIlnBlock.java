package symbolTable.ast.sentences.defaultMethodBlocks;

public class ASTPrintIlnBlock extends ASTPrintPrimitiveTypeBlock {
    public ASTPrintIlnBlock() {
        printInstruction = "IPRINT";
        printNewLine = true;
    }
}