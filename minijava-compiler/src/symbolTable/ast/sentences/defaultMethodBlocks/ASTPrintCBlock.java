package symbolTable.ast.sentences.defaultMethodBlocks;

public class ASTPrintCBlock extends ASTPrintPrimitiveTypeBlock{
    public ASTPrintCBlock() {
        printInstruction = "CPRINT";
        printNewLine = false;
    }
}
