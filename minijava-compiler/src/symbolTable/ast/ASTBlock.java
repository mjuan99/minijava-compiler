package symbolTable.ast;

import java.util.LinkedList;

public class ASTBlock implements ASTSentence{
    private LinkedList<ASTSentence> astSentences;
    private final ASTBlock parentASTBlock;

    public void setAstSentences(LinkedList<ASTSentence> astSentences) {
        this.astSentences = astSentences;
    }

    public ASTBlock(ASTBlock parentASTBlock) {
        this.parentASTBlock = parentASTBlock;
    }

    public void print() {
        int level = getLevel();
        String tab = getTab(level);
        System.out.println("{");
        for(ASTSentence astSentence : astSentences) {
            System.out.print(tab);
            astSentence.print();
        }
        System.out.print(getTab(level - 1));
        System.out.println("}");
    }

    private String getTab(int tabs){
        return "    ".repeat(tabs);
    }

    private int getLevel(){
        int level = 2;
        ASTBlock astBlock = this;
        while(astBlock.parentASTBlock != null){
            level++;
            astBlock = astBlock.parentASTBlock;
        }
        return level;
    }

    public ASTBlock getParentASTBlock() {
        return parentASTBlock;
    }
}