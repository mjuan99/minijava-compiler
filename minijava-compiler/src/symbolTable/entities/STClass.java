package symbolTable.entities;

import lexicalAnalyzer.Token;

import java.util.HashMap;
import java.util.LinkedList;

public class STClass {
    private Token name;
    private HashMap<String, STAttribute> stAttributes;
    private HashMap<String, STMethod> stMethods;
    private HashMap<String, STConstructor> stConstructors;//TODO como hago con esto
    private Token stClassItExtends;
    private HashMap<String, Token> stInterfacesItImplements;

    public STClass(Token name){
        this.name = name;
        stAttributes = new HashMap<>();
        stMethods = new HashMap<>();
        stInterfacesItImplements = new HashMap<>();
        stConstructors = new HashMap<>();
    }

    public Token getName(){
        return name;
    }

    public void setSTClassItExtends(Token stClassItExtends){
        this.stClassItExtends = stClassItExtends;
    }

    public void setStInterfacesItImplements(LinkedList<Token> tkInterfacesList){
        for(Token stInterface : tkInterfacesList){
            stInterfacesItImplements.put(stInterface.getLexeme(), stInterface);
        }
    }

    public void checkDeclaration() {
    }

    public void consolidate() {
    }

    public void print() {
        StringBuilder interfaces = new StringBuilder();
        for(Token token : stInterfacesItImplements.values())
            interfaces.append(token.getLexeme()).append(", ");
        System.out.println("class " + name.getLexeme() + " extends " + stClassItExtends.getLexeme() + " implements " + interfaces + " {");
        stAttributes.forEach((key, stAttribute) -> {
            stAttribute.print();
        });
        stMethods.forEach((key, stMethod) -> {
            stMethod.print();
        });
        stConstructors.forEach((key, stConstructor) -> {
            stConstructor.print();
        });
        System.out.println("}");
    }

    public void insertMethod(STMethod stMethod) {
        stMethods.put(stMethod.getName().getLexeme(), stMethod);
    }

    public void insertAttribute(STAttribute stAttribute) {
        stAttributes.put(stAttribute.getName().getLexeme(), stAttribute);
    }

    public void insertConstructor(STConstructor stConstructor) {
        stConstructors.put(stConstructor.getName().getLexeme(), stConstructor);
    }
}
