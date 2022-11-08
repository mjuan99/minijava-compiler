package semanticAnalyzer;

import errors.SemanticException;
import symbolTable.SymbolTable;
import syntacticAnalyzer.SyntacticAnalyzer;

public class SemanticAnalyzer {
    private final SymbolTable symbolTable;

    public SemanticAnalyzer(SyntacticAnalyzer syntacticAnalyzer) throws SemanticException {
        symbolTable = syntacticAnalyzer.getSymbolTable();
        symbolTable.checkDeclarations();
        symbolTable.consolidate();
        symbolTable.throwExceptionIfErrorsWereFound();
        symbolTable.checkSentences();
    }

    public SymbolTable getSymbolTable(){
        return symbolTable;
    }
}
