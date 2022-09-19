package symbolTable;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, STClass> stClasses;
    private HashMap<String, STInterface> stInterfaces;
    private STClass currentSTClass;
    private STMethod currentMethod;

    public SymbolTable(){
        stClasses = new HashMap<>();
        stInterfaces = new HashMap<>();
    }
    public void checkDeclarations(){
        stClasses.forEach((key, stClass) -> {
            stClass.checkDeclaration();
        });
        stInterfaces.forEach((key, stInterface) -> {
            stInterface.checkDeclaration();
        });
    }
    public void consolidate(){
        stClasses.forEach((key, stClass) -> {
            stClass.consolidate();
        });
        stInterfaces.forEach((key, stInterface) -> {
            stInterface.consolidate();
        });
    }
}
