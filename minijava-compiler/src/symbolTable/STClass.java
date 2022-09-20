package symbolTable;

import lexicalAnalyzer.Token;

import java.util.HashMap;
import java.util.LinkedList;

public class STClass {
    private Token name;
    private HashMap<String, STAttribute> stAttributes;
    private HashMap<String, STMethod> stMethods;
    //private HashMap<, STConstructor> stConstructors;
    private Token stClassItExtends;
    private HashMap<String, Token> stInterfacesItImplements;

    public STClass(Token name){
        this.name = name;
        stAttributes = new HashMap<>();
        stMethods = new HashMap<>();
        stInterfacesItImplements = new HashMap<>();
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
        System.out.println("class " + name.getLexeme() + " extends " + stClassItExtends.getLexeme() + " implements " + interfaces);
        stAttributes.forEach((key, stAttribute) -> {
            stAttribute.print();
        });
        stMethods.forEach((key, stMethod) -> {
            stMethod.print();
        });
    }
}
