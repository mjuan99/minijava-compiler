package symbolTable.ast.access;

import symbolTable.ast.expressions.ASTOperand;

public interface ASTAccess extends ASTOperand {
    void setASTChaining(ASTChaining astChaining);

    boolean isValidCall();

    boolean isValidVariable();
}