package symbolTable.entities;

import Errors.SemanticError;
import Errors.SemanticException;
import lexicalAnalyzer.Token;
import syntacticAnalyzer.SyntacticAnalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

public class STClass {
    private Token tkName;
    private HashMap<String, STAttribute> stAttributes;
    private HashMap<String, STMethod> stMethods;
    private HashMap<String, STConstructor> stConstructors;//TODO como hago con esto
    private Token tkClassItExtends;
    private HashMap<String, Token> tkInterfacesItImplements;
    private HashSet<String> ancestorsClasses;

    public STClass(Token tkName){
        this.tkName = tkName;
        stAttributes = new HashMap<>();
        stMethods = new HashMap<>();
        tkInterfacesItImplements = new HashMap<>();
        stConstructors = new HashMap<>();
        ancestorsClasses = new HashSet<>();
    }

    public Token getTKName(){
        return tkName;
    }

    public void setSTClassItExtends(Token stClassItExtends){
        this.tkClassItExtends = stClassItExtends;
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
        if(tkClassItExtends != null) {
            if(SyntacticAnalyzer.symbolTable.stClassExists(tkClassItExtends.getLexeme())){
                ancestorsClasses.add(tkName.getLexeme());
                if(cyclicInheritance(tkClassItExtends))
                    SyntacticAnalyzer.symbolTable.addError(new SemanticError(tkClassItExtends, "la clase " + tkClassItExtends.getLexeme() + " produce herencia circular"));
            }else
                SyntacticAnalyzer.symbolTable.addError(new SemanticError(tkClassItExtends, "la clase " + tkClassItExtends.getLexeme() + " no fue declarada"));
        }tkInterfacesItImplements.forEach((key, tkInterface) -> {
            if(!SyntacticAnalyzer.symbolTable.stInterfaceExists(tkInterface.getLexeme()))
                SyntacticAnalyzer.symbolTable.addError(new SemanticError(tkInterface, "la interfaz " + tkInterface.getLexeme() + " no fue declarada"));
        });
    }

    private boolean cyclicInheritance(Token tkAncestorClass) {
        if(Objects.equals(tkAncestorClass.getLexeme(), "Object"))
            return false;
        else if(ancestorsClasses.contains(tkAncestorClass.getLexeme()))
            return true;
        else{
            ancestorsClasses.add(tkAncestorClass.getLexeme());
            return cyclicInheritance(SyntacticAnalyzer.symbolTable.getTKParentClass(tkAncestorClass));
        }
    }

    public void consolidate() {
    }

    public void print() {
        StringBuilder interfaces = new StringBuilder();
        for(Token token : tkInterfacesItImplements.values())
            interfaces.append(token.getLexeme()).append(", ");
        System.out.println("class " + tkName.getLexeme() + (tkClassItExtends != null ? " extends " + tkClassItExtends.getLexeme() : "") + " implements " + interfaces + " {");
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

    public Token getTKClassItExtends() {
        return tkClassItExtends;
    }
}
