package symbolTable.entities;

import Errors.SemanticError;
import Errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class STInterface {
    private final Token tkName;
    private final HashMap<String, STMethodHeader> stMethodsHeaders;
    private final HashMap<String, Token> tkInterfacesItExtends;
    private boolean consolidated;
    private boolean cyclicInheritance;

    public STInterface(Token tkName) {
        this.tkName = tkName;
        stMethodsHeaders = new HashMap<>();
        tkInterfacesItExtends = new HashMap<>();
        consolidated = false;
        cyclicInheritance = false;
    }

    public void setSTInterfacesItExtends(LinkedList<Token> tkInterfacesList) throws SemanticException {
        boolean error = false;
        for(Token tkInterface : tkInterfacesList){
            if(tkInterfacesItExtends.get(tkInterface.getLexeme()) == null)
                tkInterfacesItExtends.put(tkInterface.getLexeme(), tkInterface);
            else {
                error = true;
                ST.symbolTable.addError(new SemanticError(tkInterface, "la interfaz " + tkInterface.getLexeme() + " ya estaba siendo extendida"));
            }
        }
        if(error)
            ST.symbolTable.throwExceptionIfErrorsWereFound();
    }

    public void checkDeclaration() {
        HashSet<String> ancestorsInterfaces = new HashSet<>();
        ancestorsInterfaces.add(tkName.getLexeme());
        tkInterfacesItExtends.forEach((key, tkParentInterface) -> {
            if(ST.symbolTable.stInterfaceExists(tkParentInterface.getLexeme())){
                if(cyclicInheritance(tkParentInterface, ancestorsInterfaces)) {
                    cyclicInheritance = true;
                    ST.symbolTable.addError(new SemanticError(tkParentInterface, "la interfaz " + tkParentInterface.getLexeme() + " produce herencia circular"));
                }
            }else
                ST.symbolTable.addError(new SemanticError(tkParentInterface, "la interfaz " + tkParentInterface.getLexeme() + " no fue declarada"));
        });
        stMethodsHeaders.forEach((key, stMethodsHeader) -> stMethodsHeader.checkDeclaration());
    }

    private boolean cyclicInheritance(Token tkAncestorInterface, HashSet<String> ancestorsInterfaces) {
        if(ancestorsInterfaces.contains(tkAncestorInterface.getLexeme()))
            return true;
        else{
            ancestorsInterfaces.add(tkAncestorInterface.getLexeme());
            for(Token tkAncestorParentInterface : ST.symbolTable.getTKParentsInterfaces(tkAncestorInterface).values())
                if(cyclicInheritance(tkAncestorParentInterface, ancestorsInterfaces))
                    return true;
            ancestorsInterfaces.remove(tkAncestorInterface.getLexeme());
        }
        return false;
    }

    public void consolidate() {
        if(!consolidated){
            if(!cyclicInheritance){
                tkInterfacesItExtends.forEach((key, tkInterface) -> {
                    STInterface stInterface = ST.symbolTable.getSTInterface(tkInterface.getLexeme());
                    if(stInterface != null){
                        stInterface.consolidate();
                        addMethodsFromParentInterface(stInterface);
                    }
                });
            }
            consolidated = true;
        }
    }

    private void addMethodsFromParentInterface(STInterface stInterface) {
        stInterface.getSTMethodsHeaders().forEach((key, stMethodHeader) ->
                stMethodsHeaders.putIfAbsent(stMethodHeader.getHash(), stMethodHeader));
    }

    private HashMap<String, STMethodHeader> getSTMethodsHeaders() {
        return stMethodsHeaders;
    }

    public void print() {
        StringBuilder interfaces = new StringBuilder();
        for(Token token : tkInterfacesItExtends.values())
            interfaces.append(token.getLexeme()).append(", ");
        System.out.println("interface " + tkName.getLexeme() + (!interfaces.toString().equals("") ? " extends " + interfaces : "") + " {");
        stMethodsHeaders.forEach((key, stMethod) -> stMethod.print());
        System.out.println("}");
    }

    public Token getTKName() {
        return tkName;
    }

    public void insertMethod(STMethodHeader stMethodHeader) throws SemanticException {
        if(stMethodsHeaders.get(stMethodHeader.getHash()) == null)
            stMethodsHeaders.put(stMethodHeader.getHash(), stMethodHeader);
        else{
            ST.symbolTable.addError(new SemanticError(stMethodHeader.getTKName(), "el método " + stMethodHeader.getHash() + " ya estaba definido"));
            ST.symbolTable.throwExceptionIfErrorsWereFound();
        }
    }

    public String getHash() {
        return tkName.getLexeme();
    }

    public HashMap<String, Token> getTKInterfacesItExtends() {
        return tkInterfacesItExtends;
    }
}
