package symbolTable.entities;

import Errors.SemanticError;
import Errors.SemanticException;
import lexicalAnalyzer.Token;
import syntacticAnalyzer.SyntacticAnalyzer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class STClass {
    private Token tkName;
    private HashMap<String, STAttribute> stAttributes;
    private HashMap<String, STMethod> stMethods;
    private HashMap<String, STConstructor> stConstructors;//TODO como hago con esto
    private Token stClassItExtends;
    private HashMap<String, Token> tkInterfacesItImplements;

    public STClass(Token tkName){
        this.tkName = tkName;
        stAttributes = new HashMap<>();
        stMethods = new HashMap<>();
        tkInterfacesItImplements = new HashMap<>();
        stConstructors = new HashMap<>();
    }

    public Token getTKName(){
        return tkName;
    }

    public void setSTClassItExtends(Token stClassItExtends){
        this.stClassItExtends = stClassItExtends;
    }

    public void setTkInterfacesItImplements(LinkedList<Token> tkInterfacesList) throws SemanticException {
        boolean error = false;
        for(Token tkInterface : tkInterfacesList){
            if(tkInterfacesItImplements.get(tkInterface.getLexeme()) == null)
                tkInterfacesItImplements.put(tkInterface.getLexeme(), tkInterface);
            else {
                error = true;
                SyntacticAnalyzer.symbolTable.addError(new SemanticError(tkInterface, "la interfaz " + tkInterface.getLexeme() + " ya estaba siendo implementada"));
            }
        }
        if(error)
            SyntacticAnalyzer.symbolTable.throwExceptionIfErrorsWereFound();
    }

    public void checkDeclaration() {
        if(stClassItExtends != null && !SyntacticAnalyzer.symbolTable.stClassExists(stClassItExtends.getLexeme()))
            SyntacticAnalyzer.symbolTable.addError(new SemanticError(stClassItExtends, "la clase " + stClassItExtends.getLexeme() + " no fue declarada"));
        tkInterfacesItImplements.forEach((key, tkInterface) -> {
            if(!SyntacticAnalyzer.symbolTable.stInterfaceExists(tkInterface.getLexeme()))
                SyntacticAnalyzer.symbolTable.addError(new SemanticError(tkInterface, "la interfaz " + tkInterface.getLexeme() + " no fue declarada"));
        });
    }

    public void consolidate() {
    }

    public void print() {
        StringBuilder interfaces = new StringBuilder();
        for(Token token : tkInterfacesItImplements.values())
            interfaces.append(token.getLexeme()).append(", ");
        System.out.println("class " + tkName.getLexeme() + (stClassItExtends != null ? " extends " + stClassItExtends.getLexeme() : "") + " implements " + interfaces + " {");
        stAttributes.forEach((key, stAttribute) -> stAttribute.print());
        stMethods.forEach((key, stMethod) -> stMethod.print());
        stConstructors.forEach((key, stConstructor) -> stConstructor.print());
        System.out.println("}");
    }

    public void insertMethod(STMethod stMethod) throws SemanticException {
        if(stMethods.get(stMethod.getHash()) == null)
            stMethods.put(stMethod.getHash(), stMethod);
        else{
            SyntacticAnalyzer.symbolTable.addError(new SemanticError(stMethod.getTKName(), "el método " + stMethod.getHash() + " ya estaba definido"));
            SyntacticAnalyzer.symbolTable.throwExceptionIfErrorsWereFound();
        }
    }

    public void insertAttribute(STAttribute stAttribute) throws SemanticException {
        if(stAttributes.get(stAttribute.getHash()) == null)
            stAttributes.put(stAttribute.getHash(), stAttribute);
        else{
            SyntacticAnalyzer.symbolTable.addError(new SemanticError(stAttribute.getTKName(), "el atributo " + stAttribute.getHash() + " ya estaba definido"));
            SyntacticAnalyzer.symbolTable.throwExceptionIfErrorsWereFound();
        }
    }

    public String getHash() {
        return tkName.getLexeme();
    }

    public void insertConstructor(STConstructor stConstructor) throws SemanticException {
        if(!Objects.equals(stConstructor.getTKName().getLexeme(), tkName.getLexeme())){
            SyntacticAnalyzer.symbolTable.addError(new SemanticError(stConstructor.getTKName(), "el tipo del constructor " + stConstructor.getTKName().getLexeme() + " no coincide con el tipo de la clase " + tkName.getLexeme()));
            SyntacticAnalyzer.symbolTable.throwExceptionIfErrorsWereFound();
        }else if(stConstructors.get(stConstructor.getHash()) == null)
            stConstructors.put(stConstructor.getHash(), stConstructor);
        else{
            SyntacticAnalyzer.symbolTable.addError(new SemanticError(stConstructor.getTKName(), "el constructor " + stConstructor.getHash() + " ya estaba definido"));
            SyntacticAnalyzer.symbolTable.throwExceptionIfErrorsWereFound();
        }
    }

    public void insertMethodUnchecked(STMethod stMethod) {
        stMethods.put(stMethod.getHash(), stMethod);
    }
}
