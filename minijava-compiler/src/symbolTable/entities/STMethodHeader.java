package symbolTable.entities;

import errors.SemanticError;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.types.STType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class STMethodHeader implements STAbstractMethod{
    private final Token tkName;
    private final Token tkStatic;
    private final STType stReturnType;
    private final HashMap<String, STArgument> stArguments;
    private LinkedList<STArgument> stArgumentsList;
    private boolean errorFound;

    public STMethodHeader(Token tkName, Token tkStatic, STType stReturnType){
        this.tkName = tkName;
        this.tkStatic = tkStatic;
        this.stReturnType = stReturnType;
        stArguments = new HashMap<>();
        stArgumentsList = new LinkedList<>();
        errorFound = false;
    }

    public boolean errorFound(){
        return errorFound;
    }

    public void print() {
        System.out.print("    " + (tkStatic != null ? "static " : "") + stReturnType + " " + tkName.getLexeme() + "(");
        for(STArgument stArgument : stArgumentsList)
            stArgument.print();
        System.out.println(");");
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
        stArgumentsList.sort(Comparator.comparingInt(STArgument::getOffset));
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
            if (tkStatic != null) {
                ST.symbolTable.addError(new SemanticError(tkStatic, "metodo estatico en una interfaz"));
                errorFound = true;
            }
            if (!stReturnType.checkDeclaration())
                errorFound = true;
            for (STArgument stArgument : stArguments.values())
                if (!stArgument.checkDeclaration())
                    errorFound = true;
        }
    }

    public boolean isStatic() {
        return tkStatic != null;
    }

    @Override
    public LinkedList<STArgument> getArguments() {
        return stArgumentsList;
    }

    public STType getSTReturnType() {
        return stReturnType;
    }

    public void setErrorFound() {
        errorFound = true;
    }
}
