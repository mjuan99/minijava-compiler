package symbolTable.ast.sentences;

import errors.SemanticException;
import symbolTable.ST;
import symbolTable.types.STType;

import java.util.HashMap;
import java.util.LinkedList;

public class ASTBlock implements ASTSentence{
    private LinkedList<ASTSentence> astSentences;
    private final ASTBlock parentASTBlock;
    private final HashMap<String, STType> variablesTypes;

    public void setAstSentences(LinkedList<ASTSentence> astSentences) {
        this.astSentences = astSentences;
    }

    public ASTBlock(ASTBlock parentASTBlock) {
        this.parentASTBlock = parentASTBlock;
        astSentences = new LinkedList<>();
        variablesTypes = new HashMap<>();
    }

    public void addVariable(String name, STType type){
        variablesTypes.put(name, type);
    }

    public STType getVariableType(String name){
        return variablesTypes.get(name);
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

    public void checkSentences() throws SemanticException {
        ST.symbolTable.setCurrentASTBlock(this);
        for(ASTSentence astSentence : astSentences)
            astSentence.checkSentences();
        ST.symbolTable.setCurrentASTBlock(parentASTBlock);
    }

    public ASTBlock getParentASTBlock() {
        return parentASTBlock;
    }
}