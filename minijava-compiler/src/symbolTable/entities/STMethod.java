package symbolTable.entities;

import codeGenerator.CodeGenerator;
import codeGenerator.TagManager;
import errors.SemanticError;
import errors.SemanticException;
import lexicalAnalyzer.Token;
import symbolTable.ST;
import symbolTable.ast.sentences.ASTBlock;
import symbolTable.types.STType;
import symbolTable.types.STTypeVoid;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class STMethod implements STAbstractMethod{
    private final Token tkName;
    private final boolean isStatic;
    private final STType stReturnType;
    private final HashMap<String, STArgument> stArguments;
    private LinkedList<STArgument> stArgumentsList;
    private boolean errorFound;
    private ASTBlock astBlock;
    private STClass stClass;
    private Token tkLastBracket;
    private boolean defaultBlock;
    private String methodTag;
    private int offset;

    public STMethod(Token tkName, boolean isStatic, STType stReturnType){
        this.tkName = tkName;
        this.isStatic = isStatic;
        this.stReturnType = stReturnType;
        stArguments = new HashMap<>();
        stArgumentsList = new LinkedList<>();
        errorFound = false;
        astBlock = new ASTBlock(null);
        defaultBlock = true;
        offset = -1;
    }

    public void setTKLastBracket(Token tkLastBracket) {
        this.tkLastBracket = tkLastBracket;
    }

    public boolean errorFound(){
        return errorFound;
    }

    public void print() {
        System.out.print("    " + (isStatic ? "static " : "") + stReturnType + " " + tkName.getLexeme() + "(");
        for(STArgument stArgument : stArgumentsList)
            stArgument.print();
        System.out.print(")");
        astBlock.print();
    }

    public Token getTKName() {
        return tkName;
    }

    public void insertArguments(LinkedList<STArgument> stArguments) {
        for(STArgument stArgument : stArguments)
            if(this.stArguments.get(stArgument.getHash()) == null)
                this.stArguments.put(stArgument.getHash(), stArgument);
            else{
                errorFound = true;
                ST.symbolTable.addError(new SemanticError(stArgument.getTKName(), "el argumento " + stArgument.getTKName().getLexeme() + " ya fue definido"));
            }
        stArgumentsList = stArguments;
        stArgumentsList.sort(Comparator.comparingInt(STArgument::getOffset));
    }

    private String getArgumentsSignature(){
        StringBuilder signature = new StringBuilder("(");
        for(int i = 0; i < stArgumentsList.size(); i++) {
            signature.append(stArgumentsList.get(i).getType());
            if(i != stArguments.size() - 1)
                signature.append(", ");
        }
        signature.append(")");
        return signature.toString();
    }

    public String getHash() {
        return tkName.getLexeme() + getArgumentsSignature();
    }

    public void checkDeclaration() {
        if(!errorFound) {
            if (!stReturnType.checkDeclaration())
                errorFound = true;
            for (STArgument stArgument : stArguments.values())
                if (!stArgument.checkDeclaration())
                    errorFound = true;
        }
    }

    public boolean isStatic() {
        return isStatic;
    }

    public STType getSTReturnType() {
        return stReturnType;
    }

    public void insertArgument(STArgument stArgument) {
        LinkedList<STArgument> stArguments = new LinkedList<>();
        stArguments.add(stArgument);
        insertArguments(stArguments);
    }

    public void setErrorFound() {
        errorFound = true;
    }

    public void checkSentences() throws SemanticException {
        if(!defaultBlock) {
            ST.symbolTable.setCurrentSTMethod(this);
            astBlock.checkSentences();
            if (!stReturnType.equals(new STTypeVoid()) && !astBlock.alwaysReturns)
                throw new SemanticException(new SemanticError(tkLastBracket, "el metodo " + tkName.getLexeme() + " no siempre retorna"));
        }
    }

    public void insertASTBlock(ASTBlock astBlock) {
        this.astBlock = astBlock;
        defaultBlock = false;
    }

    public void insertDefaultASTBlock(ASTBlock astBlock) {
        this.astBlock = astBlock;
    }

    public STArgument getArgument(String argumentName) {
        return stArguments.get(argumentName);
    }

    public LinkedList<STArgument> getArguments() {
        return stArgumentsList;
    }

    public STClass getSTClass() {
        return stClass;
    }

    public void setSTClass(STClass stClass) {
        this.stClass = stClass;
    }

    public void generateCode() {
        if(ST.symbolTable.getCurrentSTClass() == stClass) {
            ST.symbolTable.setCurrentSTMethod(this);
            CodeGenerator.setNextInstructionTag(getMethodTag());
            CodeGenerator.generateCode("LOADFP ;enlace dinamico");
            CodeGenerator.generateCode("LOADSP");
            CodeGenerator.generateCode("STOREFP ;actualizar registro de activacion (apilar)");
            astBlock.generateCode();
            CodeGenerator.generateCode("STOREFP ;actualizar registro de activación (desapilar)");
            CodeGenerator.generateCode("RET " + (stArguments.size() + (isStatic ? 0 : 1)) + " ;retorno del metodo"); //TODO preguntar
        }
    }

    public String getMethodTag(){
        if(methodTag == null)
            if(isStatic && Objects.equals(tkName.getLexeme(), "main") && stArguments.isEmpty() && stReturnType.equals(new STTypeVoid()))
                methodTag = CodeGenerator.tagMain;
            else
                methodTag = TagManager.getTag(tkName.getLexeme() + "@" + stClass.getTKName().getLexeme());
        return methodTag;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset(){
        return offset;
    }
}
