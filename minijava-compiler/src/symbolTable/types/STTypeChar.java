package symbolTable.types;

public class STTypeChar implements STPrimitiveType{
    public String toString(){
        return "char";
    }

    public void checkDeclaration() {
    }

    public boolean equals(STType stType){
        return stType instanceof STTypeChar;
    }
}