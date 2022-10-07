package symbolTable.ast.expressions;

import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.types.STType;

public interface ASTOperand {
    void print();

    Token getToken();

    STType check() throws SemanticException;
}
