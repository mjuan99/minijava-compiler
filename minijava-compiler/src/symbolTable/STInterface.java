package symbolTable;

import lexicalAnalyzer.Token;

import java.util.HashMap;

public class STInterface {
    private String name;
    private HashMap<String, STMethod> stMethods;
    private HashMap<String, Token> stInterfacesItExtends;
    public void checkDeclaration() {
    }

    public void consolidate() {
    }
}
