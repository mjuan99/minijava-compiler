package symbolTable.entities;

import lexicalAnalyzer.Token;
import symbolTable.types.STType;

import java.util.Objects;

public class STAttribute {
    private final Token tkName;
    private Token tkClass;
    private final STType stType;
    private final String visibility;
    private boolean errorFound;
    private int offset;

    public STAttribute(Token tkName, String visibility, STType stType) {
        this.tkName = tkName;
        this.visibility = visibility;
        this.stType = stType;
        errorFound = false;
    }

    public void setTKClass(Token tkClass){
        this.tkClass = tkClass;
    }

    public String getClassName(){
        return tkClass.getLexeme();
    }

    public boolean errorFound(){
        return errorFound;
    }

    public void print() {
        System.out.println("    " + visibility + " " + stType + " " + tkName.getLexeme());
    }

    public Token getTKName() {
        return tkName;
    }

    public String getHash() {
        return tkName.getLexeme();
    }

    public void checkDeclaration() {
        if(!errorFound)
            if(!stType.checkDeclaration())
                errorFound = true;
    }

    public void setErrorFound() {
        errorFound = true;
    }

    public boolean isPublic() {
        return Objects.equals(visibility, "public");
    }

    public STType getSTType() {
        return stType;
    }

    public int getOffset() {
        return offset + 1;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
