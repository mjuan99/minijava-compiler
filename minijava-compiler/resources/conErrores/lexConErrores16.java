///[Error:"asd\|11]

private Token e9() throws IOException{
        if(Character.isDigit(currentChar)){
        updateLexeme();
        updateCurrentChar();
        return e10();
        }else
        return new Token("intLiteral", lexeme, sourceFileManager.getLineNumber());
        }
"asd\