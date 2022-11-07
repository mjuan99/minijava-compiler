package symbolTable.entities;

import errors.SemanticError;
import lexicalAnalyzer.Token;
import symbolTable.ST;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class STInterface {
    private final Token tkName;
    private final HashMap<String, STMethodHeader> stMethodsHeaders;
    private final HashMap<String, STMethodHeader> stMethodsHeadersSimplified;
    private final LinkedList<STMethodHeader> ownMethodsHeaders;
    private final HashMap<String, Token> tkInterfacesItExtends;
    private boolean consolidated;
    private boolean cyclicInheritance;
    private boolean errorFound;
    private boolean offsetsGenerated;
    private final LinkedList<STClass> inheritorClasses;
    private final LinkedList<STInterface> inheritorInterfaces;

    public STInterface(Token tkName) {
        this.tkName = tkName;
        stMethodsHeaders = new HashMap<>();
        stMethodsHeadersSimplified = new HashMap<>();
        tkInterfacesItExtends = new HashMap<>();
        consolidated = false;
        cyclicInheritance = false;
        errorFound = false;
        offsetsGenerated = false;
        inheritorInterfaces = new LinkedList<>();
        inheritorClasses = new LinkedList<>();
        ownMethodsHeaders = new LinkedList<>();
    }

    public boolean errorFound(){
        return errorFound;
    }

    public void setSTInterfacesItExtends(LinkedList<Token> tkInterfacesList) {
        for(Token tkInterface : tkInterfacesList){
            if(tkInterfacesItExtends.get(tkInterface.getLexeme()) == null)
                tkInterfacesItExtends.put(tkInterface.getLexeme(), tkInterface);
            else {
                errorFound = true;
                ST.symbolTable.addError(new SemanticError(tkInterface, "la interfaz " + tkInterface.getLexeme() + " ya estaba siendo extendida"));
            }
        }
    }

    public void checkDeclaration() {
        if(!errorFound) {
            HashSet<String> ancestorsInterfaces = new HashSet<>();
            ancestorsInterfaces.add(tkName.getLexeme());
            tkInterfacesItExtends.forEach((key, tkParentInterface) -> {
                if (ST.symbolTable.stInterfaceExists(tkParentInterface.getLexeme())) {
                    if (cyclicInheritance(tkParentInterface, ancestorsInterfaces)) {
                        cyclicInheritance = true;
                        errorFound = true;
                        ST.symbolTable.addError(new SemanticError(tkParentInterface, "la interfaz " + tkParentInterface.getLexeme() + " produce herencia circular"));
                    }
                } else {
                    errorFound = true;
                    ST.symbolTable.addError(new SemanticError(tkParentInterface, "la interfaz " + tkParentInterface.getLexeme() + " no fue declarada"));
                }
            });
            stMethodsHeaders.forEach((key, stMethodsHeader) -> {
                if (!stMethodsHeader.errorFound()) stMethodsHeader.checkDeclaration();
            });
        }
    }

    private boolean cyclicInheritance(Token tkAncestorInterface, HashSet<String> ancestorsInterfaces) {
        if(ancestorsInterfaces.contains(tkAncestorInterface.getLexeme()))
            return true;
        else{
            ancestorsInterfaces.add(tkAncestorInterface.getLexeme());
            for(Token tkAncestorParentInterface : ST.symbolTable.getTKParentsInterfaces(tkAncestorInterface).values())
                if(cyclicInheritance(tkAncestorParentInterface, ancestorsInterfaces)) {
                    ancestorsInterfaces.remove(tkAncestorInterface.getLexeme());
                    return true;
                }
            ancestorsInterfaces.remove(tkAncestorInterface.getLexeme());
        }
        return false;
    }

    public void consolidate() {
        if(!consolidated){
            if(!cyclicInheritance){
                saveOwnMethodsHeaders();
                tkInterfacesItExtends.forEach((key, tkInterface) -> {
                    STInterface stInterface = ST.symbolTable.getSTInterface(tkInterface.getLexeme());
                    if(stInterface != null && !stInterface.errorFound()){
                        stInterface.addInheritorInterface(this);
                        stInterface.consolidate();
                        addMethodsFromParentSTInterface(stInterface);
                    }
                });
            }
            consolidated = true;
        }
    }

    private void saveOwnMethodsHeaders() {
        ownMethodsHeaders.addAll(stMethodsHeadersSimplified.values());
    }

    private void addMethodsFromParentSTInterface(STInterface stInterface) {
        stInterface.getSTMethodsHeaders().forEach((key, stParentMethodHeader) -> {
            if(!stParentMethodHeader.errorFound()) {
                STMethodHeader stMyMethodHeader = stMethodsHeaders.get(stParentMethodHeader.getHash());
                if(stMyMethodHeader == null) {
                    stMethodsHeaders.put(stParentMethodHeader.getHash(), stParentMethodHeader);
                    stMethodsHeadersSimplified.put(stParentMethodHeader.getTKName().getLexeme(), stParentMethodHeader);
                }
                else if(!stMyMethodHeader.getSTReturnType().equals(stParentMethodHeader.getSTReturnType()))
                    ST.symbolTable.addError(new SemanticError(stMyMethodHeader.getTKName(), "el tipo de retorno de " + stMyMethodHeader.getHash() + " no coincide con el tipo " + stParentMethodHeader.getSTReturnType() + " del metodo redefinido"));
            }
        });
    }

    public HashMap<String, STMethodHeader> getSTMethodsHeaders() {
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

    public void insertMethodHeader(STMethodHeader stMethodHeader) {
        STMethodHeader stOldMethodHeader = stMethodsHeaders.get(stMethodHeader.getHash());
        if(stOldMethodHeader == null) {
            stMethodsHeaders.put(stMethodHeader.getHash(), stMethodHeader);
            stMethodsHeadersSimplified.put(stMethodHeader.getTKName().getLexeme(), stMethodHeader);
        }
        else{
            stOldMethodHeader.setErrorFound();
            ST.symbolTable.addError(new SemanticError(stMethodHeader.getTKName(), "el método " + stMethodHeader.getHash() + " ya fue definido"));
            ST.symbolTable.addError(new SemanticError(stOldMethodHeader.getTKName(), ""));
        }
    }

    public String getHash() {
        return tkName.getLexeme();
    }

    public HashMap<String, Token> getTKInterfacesItExtends() {
        return tkInterfacesItExtends;
    }

    public void setErrorFound() {
        errorFound = true;
    }

    public STAbstractMethod getMethod(String methodName) {
        return stMethodsHeadersSimplified.get(methodName);
    }

    public void generateOffsets() {
        if(!offsetsGenerated){
            for(STInterface inheritorInterface : inheritorInterfaces)
                inheritorInterface.generateOffsets();
            int minMethodOffset = getMaxInheritorMethodOffset() + 1;
            int i = 0;
            for(STMethodHeader stMethodHeader : ownMethodsHeaders)
                stMethodHeader.setOffset(minMethodOffset + i++);
            offsetsGenerated = true;
        }
    }

    private int getMaxInheritorMethodOffset() {
        int maxInheritorMethodOffset = -1;
        for(STInterface stInterface : inheritorInterfaces)
            maxInheritorMethodOffset = Math.max(maxInheritorMethodOffset, stInterface.getMaxMethodOffset());
        for(STClass stClass : inheritorClasses)
            maxInheritorMethodOffset = Math.max(maxInheritorMethodOffset, stClass.getMaxMethodOffset());
        return maxInheritorMethodOffset;
    }

    public int getMaxMethodOffset(){
        int maxMethodOffset = -1;
        for(STMethodHeader stMethodHeader : ownMethodsHeaders)
            maxMethodOffset = Math.max(maxMethodOffset, stMethodHeader.getOffset());
        return maxMethodOffset;
    }

    public void addInheritorClass(STClass inheritorClass){
        inheritorClasses.add(inheritorClass);
    }

    public void addInheritorInterface(STInterface inheritorInterface){
        inheritorInterfaces.add(inheritorInterface);
    }
}
