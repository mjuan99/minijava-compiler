package symbolTable.entities;

import Errors.SemanticError;
import Errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

public class STClass {
    private final Token tkName;
    private final HashMap<String, STAttribute> stAttributes;
    private final HashMap<String, STMethod> stMethods;
    private final HashMap<String, STConstructor> stConstructors;
    private Token tkClassItExtends;
    private final HashMap<String, Token> tkInterfacesItImplements;
    private final HashSet<String> ancestorsClasses;
    private boolean consolidated;
    private boolean cyclicInheritance;

    public STClass(Token tkName){
        this.tkName = tkName;
        stAttributes = new HashMap<>();
        stMethods = new HashMap<>();
        tkInterfacesItImplements = new HashMap<>();
        stConstructors = new HashMap<>();
        ancestorsClasses = new HashSet<>();
        consolidated = false;
        cyclicInheritance = false;
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
                ST.symbolTable.addError(new SemanticError(tkInterface, "la interfaz " + tkInterface.getLexeme() + " ya estaba siendo implementada"));
            }
        }
        if(error)
            ST.symbolTable.throwExceptionIfErrorsWereFound();
    }

    public void checkDeclaration() {
        if(tkClassItExtends != null) {
            if(ST.symbolTable.stClassExists(tkClassItExtends.getLexeme())){
                ancestorsClasses.add(tkName.getLexeme());
                if(cyclicInheritance(tkClassItExtends)) {
                    ST.symbolTable.addError(new SemanticError(tkClassItExtends, "la clase " + tkClassItExtends.getLexeme() + " produce herencia circular"));
                    cyclicInheritance = true;
                }
            }else
                ST.symbolTable.addError(new SemanticError(tkClassItExtends, "la clase " + tkClassItExtends.getLexeme() + " no fue declarada"));
        }
        tkInterfacesItImplements.forEach((key, tkInterface) -> {
            if(!ST.symbolTable.stInterfaceExists(tkInterface.getLexeme()))
                ST.symbolTable.addError(new SemanticError(tkInterface, "la interfaz " + tkInterface.getLexeme() + " no fue declarada"));
        });
        if(stConstructors.isEmpty())
            addDefaultConstructor();
        stAttributes.forEach((key, stAttribute) -> stAttribute.checkDeclaration());
        stMethods.forEach((key, stMethod) -> stMethod.checkDeclaration());
        stConstructors.forEach((key, stConstructor) -> stConstructor.checkDeclaration());
    }

    private void addDefaultConstructor() {
        STConstructor stDefaultConstructor = new STConstructor(tkName);
        stConstructors.put(stDefaultConstructor.getHash(), stDefaultConstructor);
    }

    private boolean cyclicInheritance(Token tkAncestorClass) {
        if(Objects.equals(tkAncestorClass.getLexeme(), "Object"))
            return false;
        else if(ancestorsClasses.contains(tkAncestorClass.getLexeme()))
            return true;
        else{
            ancestorsClasses.add(tkAncestorClass.getLexeme());
            return cyclicInheritance(ST.symbolTable.getTKParentClass(tkAncestorClass));
        }
    }

    public void consolidate() {
        if(!consolidated){
            if(!cyclicInheritance){
                if(tkClassItExtends != null){
                    STClass stClassItExtends = ST.symbolTable.getSTClass(tkClassItExtends.getLexeme());
                    if(stClassItExtends != null){
                        stClassItExtends.consolidate();
                        addMethodsAndAttributesFromParentSTClass(stClassItExtends);
                    }
                }
                checkInterfacesImplementation();
            }
            consolidated = true;
        }
    }

    private void checkInterfacesImplementation() {
        tkInterfacesItImplements.forEach((key, tkInterface) -> {
            STInterface stInterface = ST.symbolTable.getSTInterface(tkInterface.getLexeme());
            if(stInterface != null){
                stInterface.consolidate();
                stInterface.getSTMethodsHeaders().forEach((key2, stMethodHeader) -> {
                    STMethod stImplementedMethod = stMethods.get(stMethodHeader.getHash());
                    if(stImplementedMethod == null)
                        ST.symbolTable.addError(new SemanticError(tkInterface, "no se implementa el metodo " + stMethodHeader.getHash() + " de la interfaz " + tkInterface.getLexeme()));
                    else if(!stMethodHeader.getSTReturnType().equals(stImplementedMethod.getSTReturnType()))
                        ST.symbolTable.addError(new SemanticError(stImplementedMethod.getTKName(), "el metodo " + stMethodHeader.getHash() + " implementado de la interfaz " + tkInterface.getLexeme() + " deberia ser tipo " + stMethodHeader.getSTReturnType().toString()));
                    else if(stImplementedMethod.isStatic() && !stMethodHeader.isStatic())
                        ST.symbolTable.addError(new SemanticError(stImplementedMethod.getTKName(), "el metodo " + stMethodHeader.getHash() + " implementado de la interfaz " + tkInterface.getLexeme() + " no debe ser estatico"));
                });
            }
        });
    }

    private void addMethodsAndAttributesFromParentSTClass(STClass stClassItExtends) {
        //TODO IMPLEMENTAR
    }

    public void print() {
        StringBuilder interfaces = new StringBuilder();
        for(Token token : tkInterfacesItImplements.values())
            interfaces.append(token.getLexeme()).append(", ");
        System.out.println("class " + tkName.getLexeme() + (tkClassItExtends != null ? " extends " + tkClassItExtends.getLexeme() : "") + (!interfaces.toString().equals("") ? " implements " + interfaces : "") + " {");
        stAttributes.forEach((key, stAttribute) -> stAttribute.print());
        stMethods.forEach((key, stMethod) -> stMethod.print());
        stConstructors.forEach((key, stConstructor) -> stConstructor.print());
        System.out.println("}");
    }

    public void insertMethod(STMethod stMethod) throws SemanticException {
        if(stMethods.get(stMethod.getHash()) == null)
            stMethods.put(stMethod.getHash(), stMethod);
        else{
            ST.symbolTable.addError(new SemanticError(stMethod.getTKName(), "el método " + stMethod.getHash() + " ya estaba definido"));
            ST.symbolTable.throwExceptionIfErrorsWereFound();
        }
    }

    public void insertAttribute(STAttribute stAttribute) throws SemanticException {
        if(stAttributes.get(stAttribute.getHash()) == null)
            stAttributes.put(stAttribute.getHash(), stAttribute);
        else{
            ST.symbolTable.addError(new SemanticError(stAttribute.getTKName(), "el atributo " + stAttribute.getHash() + " ya estaba definido"));
            ST.symbolTable.throwExceptionIfErrorsWereFound();
        }
    }

    public String getHash() {
        return tkName.getLexeme();
    }

    public void insertConstructor(STConstructor stConstructor) throws SemanticException {
        if(!Objects.equals(stConstructor.getTKName().getLexeme(), tkName.getLexeme())){
            ST.symbolTable.addError(new SemanticError(stConstructor.getTKName(), "el tipo del constructor " + stConstructor.getTKName().getLexeme() + " no coincide con el tipo de la clase " + tkName.getLexeme()));
            ST.symbolTable.throwExceptionIfErrorsWereFound();
        }else if(stConstructors.get(stConstructor.getHash()) == null)
            stConstructors.put(stConstructor.getHash(), stConstructor);
        else{
            ST.symbolTable.addError(new SemanticError(stConstructor.getTKName(), "el constructor " + stConstructor.getHash() + " ya estaba definido"));
            ST.symbolTable.throwExceptionIfErrorsWereFound();
        }
    }

    public void insertMethodUnchecked(STMethod stMethod) {
        stMethods.put(stMethod.getHash(), stMethod);
    }

    public Token getTKClassItExtends() {
        return tkClassItExtends;
    }
}
