package symbolTable.ast.expressions;

import lexicalAnalyzer.Token;

public interface ASTOperand extends ASTExpression{

    Token getToken();
}
