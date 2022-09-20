package symbolTable.entities;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;

import java.util.HashMap;
import java.util.LinkedList;

public class STMethod {
    private Token name;
    private boolean isStatic;
    private STType returnType;
    private HashMap<String, STArgument> stArguments;
    //private STBlock block;

    public STMethod(Token name, boolean isStatic, STType returnType){
        this.name = name;
        this.isStatic = isStatic;
        this.returnType = returnType;
        stArguments = new HashMap<>();
    }

    public void print() {
        System.out.print("    " + (isStatic ? "static " : "") + returnType + " " + name.getLexeme() + "(");
        stArguments.forEach((key, stArgument) -> {
            stArgument.print();
        });
        System.out.println(")");
    }

    public Token getName() {
        return name;
    }

    public void insertArguments(LinkedList<STArgument> stArguments) {
        for(STArgument stArgument : stArguments)
            this.stArguments.put(stArgument.getName().getLexeme(), stArgument);
    }
}
