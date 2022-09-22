package symbolTable.entities;

import Errors.SemanticError;
import Errors.SemanticException;
import lexicalAnalyzer.Token;
import syntacticAnalyzer.SyntacticAnalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class STInterface {
    private Token tkName;
    private HashMap<String, STMethod> stMethods;
    private HashMap<String, Token> tkInterfacesItExtends;

    public STInterface(Token tkName) {
        this.tkName = tkName;
        stMethods = new HashMap<>();
        tkInterfacesItExtends = new HashMap<>();
    }

    public void setSTInterfacesItExtends(LinkedList<Token> tkInterfacesList) throws SemanticException {
        boolean error = false;
        for(Token tkInterface : tkInterfacesList){
            if(tkInterfacesItExtends.get(tkInterface.getLexeme()) == null)
                tkInterfacesItExtends.put(tkInterface.getLexeme(), tkInterface);
            else {
                error = true;
                SyntacticAnalyzer.symbolTable.addError(new SemanticError(tkInterface, "la interfaz " + tkInterface.getLexeme() + " ya estaba siendo extendida"));
            }
        }
        if(error)
            SyntacticAnalyzer.symbolTable.throwExceptionIfErrorsWereFound();
    }

    public void checkDeclaration() {
        HashSet<String> ancestorsInterfaces = new HashSet<>();
        ancestorsInterfaces.add(tkName.getLexeme());
        tkInterfacesItExtends.forEach((key, tkParentInterface) -> {
            if(SyntacticAnalyzer.symbolTable.stInterfaceExists(tkParentInterface.getLexeme())){
                if(cyclicInheritance(tkParentInterface, ancestorsInterfaces))
                    SyntacticAnalyzer.symbolTable.addError(new SemanticError(tkParentInterface, "la interfaz " + tkParentInterface.getLexeme() + " produce herencia circular"));
            }else
                SyntacticAnalyzer.symbolTable.addError(new SemanticError(tkParentInterface, "la interfaz " + tkParentInterface.getLexeme() + " no fue declarada"));
        });
    }

    private boolean cyclicInheritance(Token tkAncestorInterface, HashSet<String> ancestorsInterfaces) {
        if(ancestorsInterfaces.contains(tkAncestorInterface.getLexeme()))
            return true;
        else{
            ancestorsInterfaces.add(tkAncestorInterface.getLexeme());
            for(Token tkAncestorParentInterface : SyntacticAnalyzer.symbolTable.getTKParentsInterfaces(tkAncestorInterface).values())
                if(cyclicInheritance(tkAncestorParentInterface, ancestorsInterfaces))
                    return true;
            ancestorsInterfaces.remove(tkAncestorInterface.getLexeme());
        }
        return false;
    }

    public void consolidate() {
    }

    public void print() {
        StringBuilder interfaces = new StringBuilder();
        for(Token token : tkInterfacesItExtends.values())
            interfaces.append(token.getLexeme()).append(", ");
        System.out.println("interface " + tkName.getLexeme() + " extends " + interfaces + " {");
        stMethods.forEach((key, stMethod) -> stMethod.print());
        System.out.println("}");
    }

    public Token getTKName() {
        return tkName;
    }

    public void insertMethod(STMethod stMethod) throws SemanticException {
        if(stMethods.get(stMethod.getHash()) == null)
            stMethods.put(stMethod.getHash(), stMethod);
        else{
            SyntacticAnalyzer.symbolTable.addError(new SemanticError(stMethod.getTKName(), "el método " + stMethod.getHash() + " ya estaba definido"));
            SyntacticAnalyzer.symbolTable.throwExceptionIfErrorsWereFound();
        }
    }

    public String getHash() {
        return tkName.getLexeme();
    }

    public HashMap<String, Token> getTKInterfacesItExtends() {
        return tkInterfacesItExtends;
    }
}
