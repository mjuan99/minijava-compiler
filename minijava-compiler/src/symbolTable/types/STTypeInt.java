package symbolTable.types;

public class STTypeInt implements STPrimitiveType{
    public String toString(){
        return "int";
    }

    public boolean checkDeclaration() {
        return true;
    }

    @Override
    public boolean conformsWith(STType stType) {
        if(stType instanceof STTypeInt || stType instanceof STTypeChar)
            return true;
        else
            if(stType instanceof STTypeReference)
                return stType.toString().equals("String");
            else
                return false;
    }

    public boolean equals(STType stType){
        return stType instanceof STTypeInt;
    }

    @Override
    public boolean isTypeReference() {
        return false;
    }
}
