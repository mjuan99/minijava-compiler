package symbolTable.entities;

import errors.SemanticError;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.ast.sentences.ASTBlock;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class STConstructor {
    private final Token tkName;
    private final HashMap<String, STArgument> stArguments;
    private LinkedList<STArgument> stArgumentsList;
    private boolean errorFound;
    private ASTBlock astBlock;

    public STConstructor(Token tkName) {
        this.tkName = tkName;
        stArguments = new HashMap<>();
        stArgumentsList = new LinkedList<>();
        errorFound = false;
    }

    public boolean errorFound(){
        return errorFound;
    }

    public void print() {
        System.out.print("    " + tkName.getLexeme() + "(");
        for(STArgument stArgument : stArgumentsList)
            stArgument.print();
        System.out.print(")");
        if(astBlock != null)
            astBlock.print();
        else
            System.out.println("{}");
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
        if(!errorFound)
            for(STArgument stArgument : stArguments.values())
                if(!stArgument.checkDeclaration())
                    errorFound = true;
    }

    public void setErrorFound() {
        errorFound = true;
    }

    public void insertASTBlock(ASTBlock astBlock) {
        this.astBlock = astBlock;
    }
}
