package symbolTable.entities;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;

import java.util.LinkedList;

public interface STAbstractMethod {

    STType getSTReturnType();
    LinkedList<STArgument> getArguments();
    Token getTKName();

    boolean isStatic();

    int getOffset();

    String getMethodTag();
}
