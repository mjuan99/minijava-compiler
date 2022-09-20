package symbolTable.entities;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;

public class STArgument {
    private Token name;
    private STType stType;

    public STArgument(Token name, STType stType) {
        this.name = name;
        this.stType = stType;
    }

    public void print() {
        System.out.print(stType + " " + name.getLexeme() + ", ");
    }

    public Token getName() {
        return name;
    }
}
