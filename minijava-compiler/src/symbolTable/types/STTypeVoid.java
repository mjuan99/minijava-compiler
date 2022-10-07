package symbolTable.types;

public class STTypeVoid implements STType{
    public String toString() {
        return "void";
    }

    public boolean checkDeclaration() {
        return true;
    }

    public boolean equals(STType stType){
        return stType instanceof STTypeVoid;
    }

    @Override
    public boolean conformsWith(STType stType) {
        return false;
    }

    @Override
    public boolean isTypeReference() {
        return false;
    }
}
