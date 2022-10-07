package symbolTable.ast.expressions;

import symbolTable.ast.expressions.access.ASTAccess;

public interface ASTExpression extends ASTAccess {
    void print();
}
