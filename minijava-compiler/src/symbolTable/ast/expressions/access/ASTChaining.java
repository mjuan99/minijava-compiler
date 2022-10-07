package symbolTable.ast.expressions.access;

import errors.SemanticException;
import symbolTable.types.STType;

public interface ASTChaining {
    void print();

    boolean endsWithVariable();

    STType check(STType previousType) throws SemanticException;
}
