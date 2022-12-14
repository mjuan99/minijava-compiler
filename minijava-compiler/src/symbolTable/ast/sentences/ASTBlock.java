package symbolTable.ast.sentences;

import codeGenerator.CodeGenerator;
import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.entities.STVariable;

import java.util.HashMap;
import java.util.LinkedList;

public class ASTBlock extends ASTSentence{
    private LinkedList<ASTSentence> astSentences;
    private final ASTBlock parentASTBlock;
    private final HashMap<String, STVariable> variables;
    private int minVariableOffset;
    private int nextVariableOffset;

    public void setAstSentences(LinkedList<ASTSentence> astSentences) {
        this.astSentences = astSentences;
    }

    public ASTBlock(ASTBlock parentASTBlock) {
        this.parentASTBlock = parentASTBlock;
        astSentences = new LinkedList<>();
        variables = new HashMap<>();
    }

    public ASTBlock(){
        parentASTBlock = null;
        astSentences = new LinkedList<>();
        variables = new HashMap<>();
    }

    public void insertVariable(String name, STVariable variable){
        variable.setOffset(nextVariableOffset++);
        variables.put(name, variable);
    }

    public STVariable getVariable(String name){
        STVariable variable = variables.get(name);
        if(variable == null)
            if(parentASTBlock == null)
                return null;
            else
                return parentASTBlock.getVariable(name);
        else
            return variable;
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
        if(parentASTBlock == null)
            minVariableOffset = 0;
        else
            minVariableOffset = parentASTBlock.nextVariableOffset;
        nextVariableOffset = minVariableOffset;
        ST.symbolTable.setCurrentASTBlock(this);
        for(ASTSentence astSentence : astSentences) {
            if(alwaysReturns) {
                Token tkMethod = ST.symbolTable.getCurrentSTMethod().getTKName();
                throw new SemanticException(new SemanticError(tkMethod, "codigo muerto en el metodo " + tkMethod.getLexeme()));
            }
            astSentence.checkSentences();
            if(astSentence.alwaysReturns)
                alwaysReturns = true;
        }
        ST.symbolTable.setCurrentASTBlock(parentASTBlock);
    }

    @Override
    public void generateCode() {
        ST.symbolTable.setCurrentASTBlock(this);
        if(astSentences.isEmpty())
            CodeGenerator.generateCode("NOP ;bloque vacio");
        for(ASTSentence sentence : astSentences)
            sentence.generateCode();
        CodeGenerator.generateCode("FMEM " + (nextVariableOffset - minVariableOffset) + " ;liberar variables locales");
        ST.symbolTable.setCurrentASTBlock(parentASTBlock);
    }

    public ASTBlock getParentASTBlock() {
        return parentASTBlock;
    }

    public int getCurrentVariableOffset() {
        return nextVariableOffset;
    }
}
