package symbolTable.ast.expressions;

import errors.SemanticException;
import symbolTable.types.STType;

public interface ASTExpression {
    void print();

    STType check() throws SemanticException;

    void generateCode();
}
