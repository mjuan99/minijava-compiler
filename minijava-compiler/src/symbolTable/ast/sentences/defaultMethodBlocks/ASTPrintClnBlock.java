package symbolTable.ast.sentences.defaultMethodBlocks;

public class ASTPrintClnBlock extends ASTPrintPrimitiveTypeBlock{
    public ASTPrintClnBlock() {
        printInstruction = "CPRINT";
        printNewLine = true;
    }
}
