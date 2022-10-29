package symbolTable.ast.sentences.defaultMethodBlocks;

public class ASTPrintBBlock extends ASTPrintPrimitiveTypeBlock{
    public ASTPrintBBlock() {
        printInstruction = "BPRINT";
        printNewLine = false;
    }
}
