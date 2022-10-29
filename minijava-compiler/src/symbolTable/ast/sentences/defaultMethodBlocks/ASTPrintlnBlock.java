package symbolTable.ast.sentences.defaultMethodBlocks;

public class ASTPrintlnBlock extends ASTPrintPrimitiveTypeBlock{
    public ASTPrintlnBlock() {
        printInstruction = null;
        printNewLine = true;
    }
}
