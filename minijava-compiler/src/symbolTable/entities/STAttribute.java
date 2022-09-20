package symbolTable.entities;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;

public class STAttribute {
    private Token name;
    private STType stType;
    private String visibility;

    public STAttribute(Token name, String visibility, STType stType) {
        this.name = name;
        this.visibility = visibility;
        this.stType = stType;
    }

    public void print() {
        System.out.println("    " + visibility + " " + stType + " " + name.getLexeme());
    }

    public Token getName() {
        return name;
    }
}
