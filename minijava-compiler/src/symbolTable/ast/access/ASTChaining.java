package symbolTable.ast.access;

import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.types.STType;

public interface ASTChaining {
    void print();
    Token getToken();

    boolean isValidCall();

    boolean isValidVariable();

    STType check(STType previousType) throws SemanticException;
}
