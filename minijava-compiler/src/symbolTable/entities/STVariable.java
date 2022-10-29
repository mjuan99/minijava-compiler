package symbolTable.entities;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;

public class STVariable {
    private final Token name;
    private final STType stType;

    public STVariable(Token name, STType stType) {
        this.name = name;
        this.stType = stType;
    }

    public STType getVariableType() {
        return stType;
    }

    public int getOffset() {
        //TODO implementar
        return 0;
    }
}
