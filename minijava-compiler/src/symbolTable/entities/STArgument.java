package symbolTable.entities;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;

public class STArgument {
    private Token name;
    private STType stType;
    private int position;

    public STArgument(Token name, STType stType, int position) {
        this.name = name;
        this.stType = stType;
        this.position = position;
    }

    public void print() {
        System.out.print(stType + " " + name.getLexeme() + ", ");
    }

    public Token getTKName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public String getHash() {
        return name.getLexeme();
    }

    public STType getType() {
        return stType;
    }
}
