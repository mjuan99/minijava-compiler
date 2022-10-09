package semanticAnalyzer;

import errors.SemanticException;
import symbolTable.SymbolTable;
import syntacticAnalyzer.SyntacticAnalyzer;

public class SemanticAnalyzer {

    public SemanticAnalyzer(SyntacticAnalyzer syntacticAnalyzer) throws SemanticException {
        SymbolTable symbolTable = syntacticAnalyzer.getSymbolTable();
        symbolTable.checkDeclarations();
        symbolTable.consolidate();
        symbolTable.throwExceptionIfErrorsWereFound();
        symbolTable.checkSentences();
    }
}
