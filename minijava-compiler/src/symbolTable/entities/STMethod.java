package symbolTable.entities;

import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.ast.sentences.ASTBlock;
import symbolTable.types.STType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class STMethod implements STAbstractMethod{
    private final Token tkName;
    private final boolean isStatic;
    private final STType stReturnType;
    private final HashMap<String, STArgument> stArguments;
    private LinkedList<STArgument> stArgumentsList;
    private boolean errorFound;
    private ASTBlock astBlock;
    private STClass stClass;

    public STMethod(Token tkName, boolean isStatic, STType stReturnType){
        this.tkName = tkName;
        this.isStatic = isStatic;
        this.stReturnType = stReturnType;
        stArguments = new HashMap<>();
        stArgumentsList = new LinkedList<>();
        errorFound = false;
        astBlock = new ASTBlock(null);
    }

    public boolean errorFound(){
        return errorFound;
    }

    public void print() {
        System.out.print("    " + (isStatic ? "static " : "") + stReturnType + " " + tkName.getLexeme() + "(");
        for(STArgument stArgument : stArgumentsList)
            stArgument.print();
        System.out.print(")");
        astBlock.print();
    }

    public Token getTKName() {
        return tkName;
    }

    public void insertArguments(LinkedList<STArgument> stArguments) {
        for(STArgument stArgument : stArguments)
            if(this.stArguments.get(stArgument.getHash()) == null)
                this.stArguments.put(stArgument.getHash(), stArgument);
            else{
                errorFound = true;
                ST.symbolTable.addError(new SemanticError(stArgument.getTKName(), "el argumento " + stArgument.getTKName().getLexeme() + " ya fue definido"));
            }
        stArgumentsList = stArguments;
        stArgumentsList.sort(Comparator.comparingInt(STArgument::getPosition));
    }

    private String getArgumentsSignature(){
        StringBuilder signature = new StringBuilder("(");
        for(int i = 0; i < stArgumentsList.size(); i++) {
            signature.append(stArgumentsList.get(i).getType());
            if(i != stArguments.size() - 1)
                signature.append(", ");
        }
        signature.append(")");
        return signature.toString();
    }

    public String getHash() {
        return tkName.getLexeme() + getArgumentsSignature();
    }

    public void checkDeclaration() {
        if(!errorFound) {
            if (!stReturnType.checkDeclaration())
                errorFound = true;
            for (STArgument stArgument : stArguments.values())
                if (!stArgument.checkDeclaration())
                    errorFound = true;
        }
    }

    public boolean isStatic() {
        return isStatic;
    }

    public STType getSTReturnType() {
        return stReturnType;
    }

    public void insertArgument(STArgument stArgument) {
        LinkedList<STArgument> stArguments = new LinkedList<>();
        stArguments.add(stArgument);
        insertArguments(stArguments);
    }

    public void setErrorFound() {
        errorFound = true;
    }

    public void checkSentences() throws SemanticException {
        ST.symbolTable.setCurrentSTMethod(this);
        astBlock.checkSentences();
    }

    public void insertASTBlock(ASTBlock astBlock) {
        this.astBlock = astBlock;
    }

    public STType getArgumentType(String argumentName) {
        STArgument stArgument = stArguments.get(argumentName);
        if(stArgument != null)
            return stArgument.getType();
        else
            return null;
    }

    public LinkedList<STArgument> getArguments() {
        return stArgumentsList;
    }

    public STClass getSTClass() {
        return stClass;
    }

    public void setSTClass(STClass stClass) {
        this.stClass = stClass;
    }
}
