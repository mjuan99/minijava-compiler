package symbolTable.entities;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;

public class STVariable {
    private final Token name;
    private final STType stType;
    private int offset;

    public STVariable(Token name, STType stType) {
        this.name = name;
        this.stType = stType;
    }

    public STType getVariableType() {
        return stType;
    }

    public int getOffset() {
        return -offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
