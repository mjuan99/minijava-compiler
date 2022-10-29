package symbolTable.entities;

import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.types.STType;

public class STArgument {
    private final Token name;
    private final STType stType;
    private final int offset;
    private boolean errorFound;

    public STArgument(Token name, STType stType, int offset) {
        this.name = name;
        this.stType = stType;
        this.offset = offset;
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

    public int getOffset() {
        return offset + (ST.symbolTable.getCurrentSTMethod().isStatic() ? 3 : 4);
    }

    public String getHash() {
        return name.getLexeme();
    }

    public STType getType() {
        return stType;
    }

    public boolean checkDeclaration() {
        if(stType.checkDeclaration())
            return true;
        else{
            errorFound = true;
            return false;
        }
    }
}
