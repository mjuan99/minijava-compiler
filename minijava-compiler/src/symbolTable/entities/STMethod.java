package symbolTable.entities;

import Errors.SemanticError;
import Errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.types.STType;
import syntacticAnalyzer.SyntacticAnalyzer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class STMethod {
    private final Token tkName;
    private final boolean isStatic;
    private final STType returnType;
    private final HashMap<String, STArgument> stArguments;
    private LinkedList<STArgument> stArgumentsList;
    //private STBlock block;

    public STMethod(Token tkName, boolean isStatic, STType returnType){
        this.tkName = tkName;
        this.isStatic = isStatic;
        this.returnType = returnType;
        stArguments = new HashMap<>();
        stArgumentsList = new LinkedList<>();
    }

    public void print() {
        System.out.print("    " + (isStatic ? "static " : "") + returnType + " " + tkName.getLexeme() + "(");
        for(STArgument stArgument : stArgumentsList)
            stArgument.print();
        System.out.println("){}");
    }

    public Token getTKName() {
        return tkName;
    }

    public void insertArguments(LinkedList<STArgument> stArguments) throws SemanticException {
        boolean error = false;
        for(STArgument stArgument : stArguments)
            if(this.stArguments.get(stArgument.getHash()) == null)
                this.stArguments.put(stArgument.getHash(), stArgument);
            else{
                error = true;
                SyntacticAnalyzer.symbolTable.addError(new SemanticError(stArgument.getTKName(), "el argumento " + stArgument.getTKName().getLexeme() + " ya fue definido"));
            }
        stArgumentsList = stArguments;
        stArgumentsList.sort(Comparator.comparingInt(STArgument::getPosition));
        if(error)
            SyntacticAnalyzer.symbolTable.throwExceptionIfErrorsWereFound();
    }

    private String getSignature(){
        StringBuilder signature = new StringBuilder();
        for(STArgument stArgument : stArgumentsList)
            signature.append("\\").append(stArgument.getType());
        return signature.toString();
    }

    public String getHash() {
        return tkName.getLexeme() + getSignature();
    }

    public void insertArtumentUnchecked(STArgument stArgument) {
        stArguments.put(stArgument.getHash(), stArgument);
        stArgumentsList.add(stArgument);
    }

    public void checkDeclaration() {
        returnType.checkDeclaration();
        stArguments.forEach((key, stArgument) -> stArgument.checkDeclaration());
    }
}
