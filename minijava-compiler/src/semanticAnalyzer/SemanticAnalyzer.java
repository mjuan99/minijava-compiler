package semanticAnalyzer;

import symbolTable.SymbolTable;
import syntacticAnalyzer.SyntacticAnalyzer;

public class SemanticAnalyzer {
    private final SymbolTable symbolTable;

    public SemanticAnalyzer(SyntacticAnalyzer syntacticAnalyzer){
        symbolTable = syntacticAnalyzer.getSymbolTable();
        symbolTable.checkDeclarations();
        symbolTable.consolidate();
    }
}
