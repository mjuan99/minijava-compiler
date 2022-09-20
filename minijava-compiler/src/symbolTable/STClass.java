package symbolTable;

import lexicalAnalyzer.Token;

import java.util.HashMap;

public class STClass {
    private String name;
    private HashMap<String, STAttribute> stAttributes;
    private HashMap<String, STMethod> stMethods;
    //private HashMap<, STConstructor> stConstructors;
    private Token stClassItExtends;
    private HashMap<String, Token> stInterfacesItImplements;

    public void checkDeclaration() {
    }

    public void consolidate() {
    }
}
