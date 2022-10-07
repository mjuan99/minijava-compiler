package symbolTable.types;

public class STTypeNull implements STType{
    @Override
    public boolean checkDeclaration() {
        return true;
    }

    @Override
    public boolean conformsWith(STType stType) {
        return stType instanceof STTypeReference;
    }

    @Override
    public boolean isTypeReference() {
        return false;
    }

    @Override
    public boolean equals(STType stType) {
        return stType instanceof STTypeNull;
    }
}
