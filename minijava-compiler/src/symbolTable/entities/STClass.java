package symbolTable.entities;

import codeGenerator.CodeGenerator;
import codeGenerator.TagManager;
import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.types.STTypeVoid;

import java.util.*;

public class STClass {
    private final Token tkName;
    private final HashMap<String, STAttribute> stAttributes;
    private final HashMap<String, STMethod> stMethods;
    private final HashMap<String, STMethod> stMethodsSimplified;
    private final HashMap<String, STConstructor> stConstructors;
    private Token tkClassItExtends;
    private final HashMap<String, Token> tkInterfacesItImplements;
    private final HashSet<String> ancestorsClasses;
    private boolean consolidated;
    private boolean cyclicInheritance;
    private boolean errorFound;
    private boolean offsetsGenerated;
    private String vTableTag;

    public STClass(Token tkName){
        this.tkName = tkName;
        stAttributes = new HashMap<>();
        stMethods = new HashMap<>();
        stMethodsSimplified = new HashMap<>();
        tkInterfacesItImplements = new HashMap<>();
        stConstructors = new HashMap<>();
        ancestorsClasses = new HashSet<>();
        consolidated = false;
        cyclicInheritance = false;
        errorFound = false;
        offsetsGenerated = false;
        vTableTag = null;
    }

    public boolean errorFound(){
        return errorFound;
    }

    public Token getTKName(){
        return tkName;
    }

    public void setSTClassItExtends(Token stClassItExtends){
        this.tkClassItExtends = stClassItExtends;
    }

    public void setTkInterfacesItImplements(LinkedList<Token> tkInterfacesList) {
        for(Token tkInterface : tkInterfacesList){
            if(tkInterfacesItImplements.get(tkInterface.getLexeme()) == null)
                tkInterfacesItImplements.put(tkInterface.getLexeme(), tkInterface);
            else {
                errorFound = true;
                ST.symbolTable.addError(new SemanticError(tkInterface, "la interfaz " + tkInterface.getLexeme() + " ya estaba siendo implementada"));
            }
        }
    }

    public void checkDeclaration() {
        if(!errorFound) {
            if (tkClassItExtends != null) {
                if (ST.symbolTable.stClassExists(tkClassItExtends.getLexeme())) {
                    ancestorsClasses.add(tkName.getLexeme());
                    if (cyclicInheritance(tkClassItExtends)) {
                        cyclicInheritance = true;
                        errorFound = true;
                        ST.symbolTable.addError(new SemanticError(tkClassItExtends, "la clase " + tkClassItExtends.getLexeme() + " produce herencia circular"));
                    }
                } else {
                    errorFound = true;
                    ST.symbolTable.addError(new SemanticError(tkClassItExtends, "la clase " + tkClassItExtends.getLexeme() + " no fue declarada"));
                }
            }
            tkInterfacesItImplements.forEach((key, tkInterface) -> {
                if (!ST.symbolTable.stInterfaceExists(tkInterface.getLexeme())) {
                    errorFound = true;
                    ST.symbolTable.addError(new SemanticError(tkInterface, "la interfaz " + tkInterface.getLexeme() + " no fue declarada"));
                }
            });
            if (stConstructors.isEmpty())
                addDefaultConstructor();
            stAttributes.forEach((key, stAttribute) -> {
                if (!stAttribute.errorFound()) stAttribute.checkDeclaration();
            });
            stMethods.forEach((key, stMethod) -> {
                if (!stMethod.errorFound()) stMethod.checkDeclaration();
            });
            stConstructors.forEach((key, stConstructor) -> {
                if (!stConstructor.errorFound()) stConstructor.checkDeclaration();
            });
        }
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
                        if(!stClassItExtends.errorFound()) {
                            addMethodsFromParentSTClass(stClassItExtends);
                            addAttributesFromParentSTClass(stClassItExtends);
                        }
                    }
                }
                checkInterfacesImplementation();
            }
            consolidated = true;
        }
    }

    private void addAttributesFromParentSTClass(STClass stClass) {
        stClass.stAttributes.forEach((key, stAttribute) -> {
            if(!stAttribute.errorFound())
                if(stAttributes.get(stAttribute.getHash()) == null && stAttribute.isPublic())
                    stAttributes.put(key, stAttribute);
                else
                    stAttributes.put(stAttribute.getHash() + "\\" + stAttribute.getClassName(), stAttribute);
        });
    }

    private void addMethodsFromParentSTClass(STClass stClass) {
        stClass.stMethods.forEach((key, stParentMethod) -> {
            if(!stParentMethod.errorFound()) {
                STMethod stMyMethod = stMethods.get(stParentMethod.getHash());
                if (stMyMethod == null) {
                    stMethods.put(stParentMethod.getHash(), stParentMethod);
                    stMethodsSimplified.put(stParentMethod.getTKName().getLexeme(), stParentMethod);
                }
                else if (!stMyMethod.getSTReturnType().equals(stParentMethod.getSTReturnType()))
                    ST.symbolTable.addError(new SemanticError(stMyMethod.getTKName(), "el tipo de retorno de " + stMyMethod.getHash() + " no coincide con el tipo " + stParentMethod.getSTReturnType() + " del metodo redefinido"));
                else if (stMyMethod.isStatic() && !stParentMethod.isStatic())
                    ST.symbolTable.addError(new SemanticError(stMyMethod.getTKName(), "el metodo " + stMyMethod.getHash() + " es estatico pero el metodo que redefine no lo es"));
                else if (!stMyMethod.isStatic() && stParentMethod.isStatic())
                    ST.symbolTable.addError(new SemanticError(stMyMethod.getTKName(), "el metodo " + stMyMethod.getHash() + " no es estatico pero el metodo que redefine si lo es"));
            }
        });
    }

    private void checkInterfacesImplementation() {
        tkInterfacesItImplements.forEach((key, tkInterface) -> {
            STInterface stInterface = ST.symbolTable.getSTInterface(tkInterface.getLexeme());
            if(stInterface != null && !stInterface.errorFound()){
                stInterface.consolidate();
                stInterface.getSTMethodsHeaders().forEach((key2, stMethodHeader) -> {
                    if(!stMethodHeader.errorFound()) {
                        STMethod stImplementedMethod = stMethods.get(stMethodHeader.getHash());
                        if (stImplementedMethod == null)
                            ST.symbolTable.addError(new SemanticError(tkInterface, "no se implementa el metodo " + stMethodHeader.getHash() + " de la interfaz " + tkInterface.getLexeme()));
                        else if (!stMethodHeader.getSTReturnType().equals(stImplementedMethod.getSTReturnType()))
                            ST.symbolTable.addError(new SemanticError(stImplementedMethod.getTKName(), "el metodo " + stMethodHeader.getHash() + " implementado de la interfaz " + tkInterface.getLexeme() + " deberia ser tipo " + stMethodHeader.getSTReturnType().toString()));
                        else if (stImplementedMethod.isStatic() && !stMethodHeader.isStatic())
                            ST.symbolTable.addError(new SemanticError(stImplementedMethod.getTKName(), "el metodo " + stMethodHeader.getHash() + " implementado de la interfaz " + tkInterface.getLexeme() + " no debe ser estatico"));
                    }
                });
            }
        });
    }

    public void print() {
        StringBuilder interfaces = new StringBuilder();
        for(Token token : tkInterfacesItImplements.values())
            interfaces.append(token.getLexeme()).append(", ");
        System.out.println("class " + tkName.getLexeme() + (tkClassItExtends != null ? " extends " + tkClassItExtends.getLexeme() : "") + (!interfaces.toString().equals("") ? " implements " + interfaces : "") + " {");
        stAttributes.forEach((key, stAttribute) -> {
            stAttribute.print();
            System.out.println("        Hash: " + key);
        });
        stMethods.forEach((key, stMethod) -> stMethod.print());
        stConstructors.forEach((key, stConstructor) -> stConstructor.print());
        System.out.println("}");
    }

    public void insertMethod(STMethod stMethod) {
        STMethod stOldMethod = stMethods.get(stMethod.getHash());
        if(stOldMethod == null) {
            stMethods.put(stMethod.getHash(), stMethod);
            stMethodsSimplified.put(stMethod.getTKName().getLexeme(), stMethod);
            stMethod.setSTClass(this);
            if(stMethod.isStatic() && Objects.equals(stMethod.getHash(), "main()") && stMethod.getArguments().isEmpty() && stMethod.getSTReturnType().equals(new STTypeVoid()))
                ST.symbolTable.setSTMainMethod(stMethod);
        }
        else{
            stOldMethod.setErrorFound();
            ST.symbolTable.addError(new SemanticError(stMethod.getTKName(), "el método " + stMethod.getHash() + " ya fue definido"));
            ST.symbolTable.addError(new SemanticError(stOldMethod.getTKName(), ""));
        }
    }

    public void insertAttribute(STAttribute stAttribute) {
        stAttribute.setTKClass(tkName);
        STAttribute stOldAttribute = stAttributes.get(stAttribute.getHash());
        if(stOldAttribute == null)
            stAttributes.put(stAttribute.getHash(), stAttribute);
        else{
            stOldAttribute.setErrorFound();
            ST.symbolTable.addError(new SemanticError(stAttribute.getTKName(), "el atributo " + stAttribute.getTKName().getLexeme() + " ya fue definido"));
            ST.symbolTable.addError(new SemanticError(stOldAttribute.getTKName(), ""));
        }
    }

    public String getHash() {
        return tkName.getLexeme();
    }

    public void insertConstructor(STConstructor stConstructor) {
        STConstructor stOldConstructor = stConstructors.get(stConstructor.getHash());
        if(!Objects.equals(stConstructor.getTKName().getLexeme(), tkName.getLexeme())){
            ST.symbolTable.addError(new SemanticError(stConstructor.getTKName(), "el nombre del constructor " + stConstructor.getTKName().getLexeme() + " no coincide con el nombre de la clase " + tkName.getLexeme()));
        }else if(stOldConstructor == null)
            stConstructors.put(stConstructor.getHash(), stConstructor);
        else{
            stOldConstructor.setErrorFound();
            ST.symbolTable.addError(new SemanticError(stConstructor.getTKName(), "el constructor " + stConstructor.getHash() + " ya fue definido"));
            ST.symbolTable.addError(new SemanticError(stOldConstructor.getTKName(), ""));
        }
    }

    public Token getTKClassItExtends() {
        return tkClassItExtends;
    }

    public void setErrorFound() {
        errorFound = true;
    }

    public void checkSentences() throws SemanticException {
        ST.symbolTable.setCurrentSTClass(this);
        for(STMethod stMethod : stMethods.values())
            if(stMethod.getSTClass() == this)
                stMethod.checkSentences();
    }

    public STAttribute getAttribute(String attributeName) {
        return stAttributes.get(attributeName);
    }

    public STMethod getMethod(String methodName) {
        return stMethodsSimplified.get(methodName);
    }

    public LinkedList<String> getInterfacesItImplements() {
        LinkedList<String> interfaces = new LinkedList<>();
        for(Token tkInterface : tkInterfacesItImplements.values())
            interfaces.add(tkInterface.getLexeme());
        return interfaces;
    }

    public String getVTableTag(){
        if(vTableTag == null)
            vTableTag = TagManager.getTag("VT_" + tkName.getLexeme());
        return vTableTag;
    }

    public void generateCode() {
        ST.symbolTable.setCurrentSTClass(this);
        loadVTable();
        for(STMethod stMethod : stMethodsSimplified.values())
            stMethod.generateCode();
    }

    private void loadVTable(){
        StringBuilder methodsTags = new StringBuilder();
        for(int i = 0; i < stMethodsSimplified.size(); i++)
            for(STMethod stMethod : stMethodsSimplified.values()) {
                if (stMethod.getOffset() == i) {
                    methodsTags.append(stMethod.getMethodTag()).append(",");
                }
            }
        CodeGenerator.generateCode(".DATA ;VTable de " + tkName.getLexeme());
        CodeGenerator.setNextInstructionTag(getVTableTag());
        CodeGenerator.generateCode("DW " + methodsTags.substring(0, methodsTags.length() - 1));
        CodeGenerator.generateCode(".CODE ;codigo de metodos definidos en " + tkName.getLexeme());
    }

    public void generateOffsets() {
        if(!offsetsGenerated) {
            if (tkClassItExtends != null)
                ST.symbolTable.getSTClass(tkClassItExtends.getLexeme()).generateOffsets();
            int i = 0;
            for (STAttribute stAttribute : stAttributes.values())
                stAttribute.setOffset(i++);
            i = 0;
            int minMethodOffset = getMinMethodOffset();
            for (STMethod stMethod : stMethodsSimplified.values())
                if (stMethod.getOffset() == -1)
                    stMethod.setOffset(minMethodOffset + i++);
            offsetsGenerated = true;
        }
    }

    private int getMinMethodOffset(){
        int maxParentMethodOffset = -1;
        if(tkClassItExtends != null)
            for(STMethod stMethod : ST.symbolTable.getSTClass(tkClassItExtends.getLexeme()).stMethods.values())
                maxParentMethodOffset = Math.max(maxParentMethodOffset, stMethod.getOffset());
        return maxParentMethodOffset + 1;
    }

    public int getAttributesSize() {
        return stAttributes.size();
    }
}
