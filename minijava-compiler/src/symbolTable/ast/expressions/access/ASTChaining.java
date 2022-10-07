package symbolTable.ast.expressions.access;

import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.types.STType;

public interface ASTChaining {
    void print();
    Token getToken();

    boolean endsWithVariable();

    STType check(STType previousType) throws SemanticException;
}
