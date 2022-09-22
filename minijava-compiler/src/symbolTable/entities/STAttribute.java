package symbolTable.entities;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;

public class STAttribute {
    private Token tkName;
    private STType stType;
    private String visibility;

    public STAttribute(Token tkName, String visibility, STType stType) {
        this.tkName = tkName;
        this.visibility = visibility;
        this.stType = stType;
    }

    public void print() {
        System.out.println("    " + visibility + " " + stType + " " + tkName.getLexeme());
    }

    public Token getTKName() {
        return tkName;
    }

    public String getHash() {
        return tkName.getLexeme();
    }
}
