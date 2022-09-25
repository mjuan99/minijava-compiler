package symbolTable.entities;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;

public class STAttribute {
    private final Token tkName;
    private Token tkClass; // TODO ver que sea automatico
    private final STType stType;
    private final String visibility;
    private boolean errorFound;

    public STAttribute(Token tkName, String visibility, STType stType) {
        this.tkName = tkName;
        this.visibility = visibility;
        this.stType = stType;
        errorFound = false;
    }

    public void setTKClass(Token tkClass){
        this.tkClass = tkClass;
    }

    public boolean errorFound(){
        return errorFound;
    }

    public void print() {
        System.out.println("    " + visibility + " " + stType + " " + tkName.getLexeme() + "\\" + tkClass.getLexeme());
    }

    public Token getTKName() {
        return tkName;
    }

    public String getHash() {
        return tkName.getLexeme() + "\\" + tkClass.getLexeme();
    }

    public void checkDeclaration() {
        if(!errorFound)
            if(stType.checkDeclaration())
                errorFound = true;
    }

    public void setErrorFound() {
        errorFound = true;
    }
}
