package symbolTable.ast.access;

import errors.SemanticException;
import symbolTable.types.STType;

public interface ASTChaining {
    void print();

    boolean isValidCall();

    boolean isValidVariable();

    STType check(STType previousType) throws SemanticException;

    void generateCode();

    boolean isNotVoid();

    void setLeftSideOfAssignment();
}
