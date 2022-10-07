package symbolTable.ast.sentences;

import errors.SemanticException;

public interface ASTSentence {
    void print();

    void checkSentences() throws SemanticException;
}
