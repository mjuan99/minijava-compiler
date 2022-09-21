package symbolTable.entities;

import lexicalAnalyzer.Token;

import java.util.HashMap;
import java.util.LinkedList;

public class STConstructor {
    private Token name;
    private HashMap<String, STArgument> stArguments;

    public STConstructor(Token name) {
        this.name = name;
        stArguments = new HashMap<>();
    }

    public void print() {
        System.out.print("    " + name.getLexeme() + "(");
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
