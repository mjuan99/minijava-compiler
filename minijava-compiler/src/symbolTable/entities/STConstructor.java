package symbolTable.entities;

import Errors.SemanticError;
import Errors.SemanticException;
import lexicalAnalyzer.Token;
import syntacticAnalyzer.SyntacticAnalyzer;

import java.util.HashMap;
import java.util.LinkedList;

public class STConstructor {
    private Token tkName;
    private HashMap<String, STArgument> stArguments;
    private LinkedList<STArgument> stArgumentsList;

    public STConstructor(Token tkName) {
        this.tkName = tkName;
        stArguments = new HashMap<>();
        stArgumentsList = new LinkedList<>();
    }

    public void print() {
        System.out.print("    " + tkName.getLexeme() + "(");
        for(STArgument stArgument : stArgumentsList)
            stArgument.print();
        System.out.println(")");
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
        stArgumentsList.sort((stArgument1, stArgument2) -> stArgument1.getPosition() - stArgument2.getPosition());
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
}
