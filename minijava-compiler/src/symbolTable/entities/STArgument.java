package symbolTable.entities;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;

public class STArgument {
    private final Token name;
    private final STType stType;
    private final int position;
    private boolean errorFound;

    public STArgument(Token name, STType stType, int position) {
        this.name = name;
        this.stType = stType;
        this.position = position;
        errorFound = false;
    }

    public boolean errorFound(){
        return errorFound;
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

    public void checkDeclaration() {
        stType.checkDeclaration();
    }
}
