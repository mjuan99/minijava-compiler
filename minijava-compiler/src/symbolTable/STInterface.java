package symbolTable;

import lexicalAnalyzer.Token;

import java.util.HashMap;
import java.util.LinkedList;

public class STInterface {
    private Token name;
    private HashMap<String, STMethod> stMethods;
    private HashMap<String, Token> stInterfacesItExtends;

    public STInterface(Token name) {
        this.name = name;
        stMethods = new HashMap<>();
        stInterfacesItExtends = new HashMap<>();
    }

    public void setSTInterfacesItExtends(LinkedList<Token> stInterfacesList){
        for(Token stInterface : stInterfacesList){
            stInterfacesItExtends.put(stInterface.getLexeme(), stInterface);
        }
    }

    public void checkDeclaration() {
    }

    public void consolidate() {
    }

    public void print() {
        StringBuilder interfaces = new StringBuilder();
        for(Token token : stInterfacesItExtends.values())
            interfaces.append(token.getLexeme()).append(", ");
        System.out.println("interface " + name.getLexeme() + " extends " + interfaces);
        stMethods.forEach((key, stMethod) -> {
            stMethod.print();
        });
    }

    public Token getName() {
        return name;
    }
}
