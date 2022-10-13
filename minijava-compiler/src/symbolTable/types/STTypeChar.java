package symbolTable.types;

public class STTypeChar implements STPrimitiveType{
    public String toString(){
        return "char";
    }

    public boolean checkDeclaration() {
        return true;
    }

    public boolean equals(STType stType){
        return stType instanceof STTypeChar;
    }

    @Override
    public boolean conformsWith(STType stType) {
        if(stType instanceof STTypeChar || stType instanceof STTypeInt)
            return true;
        else
            if(stType instanceof STTypeReference)
                return stType.toString().equals("String");
            else
                return false;
    }

    @Override
    public boolean isTypeReference() {
        return false;
    }
}
