package symbolTable.ast.sentences.defaultMethodBlocks;

public class ASTPrintBlnBlock extends ASTPrintPrimitiveTypeBlock{
    public ASTPrintBlnBlock() {
        printInstruction = "BPRINT";
        printNewLine = true;
    }
}
