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
}
