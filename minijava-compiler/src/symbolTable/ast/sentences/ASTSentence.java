package symbolTable.ast.sentences;

import errors.SemanticException;

public abstract class ASTSentence {
    public boolean alwaysReturns = false;
    public abstract void print();

    public abstract void checkSentences() throws SemanticException;

    public abstract void generateCode();
}
