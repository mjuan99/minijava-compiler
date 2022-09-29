package symbolTable;

import errors.CompilerError;
import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.entities.*;
import symbolTable.types.*;

import java.util.HashMap;
import java.util.LinkedList;

public class SymbolTable {
    private final HashMap<String, STClass> stClasses;
    private final HashMap<String, STInterface> stInterfaces;
    private STClass currentSTClass;
    private STInterface currentSTInterface;
    private STMethod currentSTMethod;
    private STConstructor currentSTConstructor;
    private final LinkedList<CompilerError> compilerErrorList;
    private STMethodHeader currentSTMethodHeader;
    private Token tkEOF;
    private STMethod stMainMethod;

    public SymbolTable(){
        stClasses = new HashMap<>();
        stInterfaces = new HashMap<>();
        compilerErrorList = new LinkedList<>();
        loadPredefinedClasses();
        stMainMethod = null;
    }

    public void setTKEOF(Token tkEOF){
        this.tkEOF = tkEOF;
    }

    private void loadPredefinedClasses() {
        STClass stClassObject = new STClass(new Token("idClase", "Object", 0));
        STMethod stMethodDebugPrint = new STMethod(new Token("idMetVar", "debugPrint", 0), true, new STTypeVoid());
        stMethodDebugPrint.insertArgument(new STArgument(new Token("idMetVar", "i", 0), new STTypeInt(), 0));
        stClassObject.insertMethod(stMethodDebugPrint);
        stClasses.put(stClassObject.getHash(), stClassObject);


        STClass stClassString = new STClass(new Token("idClase", "String", 0));
        stClassString.setSTClassItExtends(new Token("idClase", "Object", 0));
        stClasses.put(stClassString.getHash(), stClassString);


        STClass stClassSystem = new STClass(new Token("idClase", "System", 0));
        stClassSystem.setSTClassItExtends(new Token("idClase", "Object", 0));

        STMethod stMethodRead = new STMethod(new Token("idMetVar", "read", 0), true, new STTypeInt());
        STMethod stMethodPrintB = new STMethod(new Token("idMetVar", "printB", 0), true, new STTypeVoid());
        STMethod stMethodPrintC = new STMethod(new Token("idMetVar", "printC", 0), true, new STTypeVoid());
        STMethod stMethodPrintI = new STMethod(new Token("idMetVar", "printI", 0), true, new STTypeVoid());
        STMethod stMethodPrintS = new STMethod(new Token("idMetVar", "printS", 0), true, new STTypeVoid());
        STMethod stMethodPrintln = new STMethod(new Token("idMetVar", "println", 0), true, new STTypeVoid());
        STMethod stMethodPrintBln = new STMethod(new Token("idMetVar", "printBln", 0), true, new STTypeVoid());
        STMethod stMethodPrintCln = new STMethod(new Token("idMetVar", "printCln", 0), true, new STTypeVoid());
        STMethod stMethodPrintIln = new STMethod(new Token("idMetVar", "printIln", 0), true, new STTypeVoid());
        STMethod stMethodPrintSln = new STMethod(new Token("idMetVar", "printSln", 0), true, new STTypeVoid());

        stMethodPrintB.insertArgument(new STArgument(new Token("idMetVar", "b", 0), new STTypeBoolean(), 0));
        stMethodPrintC.insertArgument(new STArgument(new Token("idMetVar", "c", 0), new STTypeChar(), 0));
        stMethodPrintI.insertArgument(new STArgument(new Token("idMetVar", "i", 0), new STTypeInt(), 0));
        stMethodPrintS.insertArgument(new STArgument(new Token("idMetVar", "s", 0), new STTypeReference(new Token("idClase", "String", 0)), 0));
        stMethodPrintBln.insertArgument(new STArgument(new Token("idMetVar", "b", 0), new STTypeBoolean(), 0));
        stMethodPrintCln.insertArgument(new STArgument(new Token("idMetVar", "c", 0), new STTypeChar(), 0));
        stMethodPrintIln.insertArgument(new STArgument(new Token("idMetVar", "i", 0), new STTypeInt(), 0));
        stMethodPrintSln.insertArgument(new STArgument(new Token("idMetVar", "s", 0), new STTypeReference(new Token("idClase", "String", 0)), 0));

        stClassSystem.insertMethod(stMethodRead);
        stClassSystem.insertMethod(stMethodPrintB);
        stClassSystem.insertMethod(stMethodPrintC);
        stClassSystem.insertMethod(stMethodPrintI);
        stClassSystem.insertMethod(stMethodPrintS);
        stClassSystem.insertMethod(stMethodPrintln);
        stClassSystem.insertMethod(stMethodPrintBln);
        stClassSystem.insertMethod(stMethodPrintCln);
        stClassSystem.insertMethod(stMethodPrintIln);
        stClassSystem.insertMethod(stMethodPrintSln);

        stClasses.put(stClassSystem.getHash(), stClassSystem);
    }

    public void addError(CompilerError error){
        compilerErrorList.add(error);
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

    public void setCurrentSTMethod(STMethod currentSTMethod) {
        this.currentSTMethod = currentSTMethod;
    }

    public STMethod getCurrentSTMethod(){
        return currentSTMethod;
    }

    public void setCurrentSTConstructor(STConstructor currentSTConstructor){
        this.currentSTConstructor = currentSTConstructor;
    }

    public STConstructor getCurrentSTConstructor(){
        return currentSTConstructor;
    }

    public void insertSTClass(STClass stClass) {
        STClass stOldClass = stClasses.get(stClass.getHash());
        STInterface stOldInterface = stInterfaces.get(stClass.getHash());
        if(stOldClass == null)
            if(stOldInterface == null)
                stClasses.put(stClass.getHash(), stClass);
            else{
                stOldInterface.setErrorFound();
                addError(new SemanticError(stClass.getTKName(), "la clase " + stClass.getTKName().getLexeme() + " ya fue definida"));
                addError(new SemanticError(stOldInterface.getTKName(), ""));
            }
        else {
            stOldClass.setErrorFound();
            addError(new SemanticError(stClass.getTKName(), "la clase " + stClass.getTKName().getLexeme() + " ya fue definida"));
            addError(new SemanticError(stOldClass.getTKName(), ""));
        }
    }

    public boolean stClassExists(String stClassName){
        return stClasses.get(stClassName) != null;
    }

    public void insertSTInterface(STInterface stInterface) {
        STClass stOldClass = stClasses.get(stInterface.getHash());
        STInterface stOldInterface = stInterfaces.get(stInterface.getHash());
        if(stOldClass == null)
            if(stOldInterface == null)
                stInterfaces.put(stInterface.getHash(), stInterface);
            else{
                stOldInterface.setErrorFound();
                addError(new SemanticError(stInterface.getTKName(), "la clase " + stInterface.getTKName().getLexeme() + " ya fue definida"));
                addError(new SemanticError(stOldInterface.getTKName(), ""));
            }
        else{
            stOldClass.setErrorFound();
            addError(new SemanticError(stInterface.getTKName(), "la clase " + stInterface.getTKName().getLexeme() + " ya fue definida"));
            addError(new SemanticError(stOldClass.getTKName(), ""));
        }
    }

    public boolean stInterfaceExists(String stInterfaceName){
        return stInterfaces.get(stInterfaceName) != null;
    }

    public void print(){
        stClasses.forEach((key, stClass) -> stClass.print());
        stInterfaces.forEach((key, stInterface) -> stInterface.print());
    }

    public void setSTMainMethod(STMethod stMainMethod){
        if(this.stMainMethod == null)
            this.stMainMethod = stMainMethod;
        else{
            this.stMainMethod.setErrorFound();
            stMainMethod.setErrorFound();
            addError(new SemanticError(this.stMainMethod.getTKName(), ""));
            addError(new SemanticError(stMainMethod.getTKName(), "el metodo static void main() ya fue definido"));
        }
    }

    public void checkDeclarations(){
        stClasses.forEach((key, stClass) -> stClass.checkDeclaration());
        stInterfaces.forEach((key, stInterface) -> stInterface.checkDeclaration());
        if(stMainMethod == null)
            ST.symbolTable.addError(new SemanticError(tkEOF, "no se encontro un metodo static void main()"));
    }
    public void consolidate(){
        stClasses.forEach((key, stClass) -> stClass.consolidate());
        stInterfaces.forEach((key, stInterface) -> stInterface.consolidate());
    }

    public void throwExceptionIfErrorsWereFound() throws SemanticException{
        if(!compilerErrorList.isEmpty())
            throw new SemanticException(compilerErrorList);
    }

    public Token getTKParentClass(Token tkClass) {
        return stClasses.get(tkClass.getLexeme()).getTKClassItExtends();
    }

    public HashMap<String, Token> getTKParentsInterfaces(Token tkInterface) {
        return stInterfaces.get(tkInterface.getLexeme()).getTKInterfacesItExtends();
    }

    public void setCurrentSTMethodHeader(STMethodHeader stMethodHeader) {
        currentSTMethodHeader = stMethodHeader;
    }

    public STClass getSTClass(String className) {
        return stClasses.get(className);
    }

    public STInterface getSTInterface(String interfaceName) {
        return stInterfaces.get(interfaceName);
    }
}
