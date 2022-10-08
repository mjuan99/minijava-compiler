package symbolTable.ast.expressions.access;

import symbolTable.ast.expressions.ASTOperand;

public interface ASTAccess extends ASTOperand {
    void setASTChainng(ASTChaining astChaining);

    boolean isValidCall();

    boolean isValidVariable();
}
