package symbolTable;

import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, STClass> stClasses;
    private HashMap<String, STInterface> stInterfaces;
    private STClass currentSTClass;
    private STInterface currentSTInterface;
    private STMethod currentSTMethod;

    public SymbolTable(){
        stClasses = new HashMap<>();
        stInterfaces = new HashMap<>();
    }

    public void setCurrentSTClass(STClass currentSTClass) {
        this.currentSTClass = currentSTClass;
    }

    public STClass getCurrentSTClass(){
        return currentSTClass;
    }

    public void setCurrentSTInterface(STInterface currentSTInterface){
        this.currentSTInterface = currentSTInterface;
    }

    public STInterface getCurrentSTInterface(){
        return currentSTInterface;
    }

    public void insertSTClass(STClass stClass){
        stClasses.put(stClass.getName().getLexeme(), stClass);
    }

    public void insertSTInterface(STInterface stInterface) {
        stInterfaces.put(stInterface.getName().getLexeme(), stInterface);
    }

    public void print(){
        stClasses.forEach((key, stClass) -> {
            stClass.print();
        });
        stInterfaces.forEach((key, stInterface) -> {
            stInterface.print();
        });
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
