package semanticAnalyzer;

import Errors.SemanticException;
import symbolTable.SymbolTable;
import syntacticAnalyzer.SyntacticAnalyzer;

public class SemanticAnalyzer {

    public SemanticAnalyzer(SyntacticAnalyzer syntacticAnalyzer) throws SemanticException {
        SymbolTable symbolTable = syntacticAnalyzer.getSymbolTable();
        symbolTable.checkDeclarations();
        symbolTable.consolidate();
        symbolTable.throwExceptionIfErrorsWereFound();
    }
}
