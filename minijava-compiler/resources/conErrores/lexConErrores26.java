///[Error:'|9]
private Token e19() throws IOException, LexicalException{
        if(eof)
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), "EOF sin cerrar literal carácter");
        else if(currentChar == '\n'){
        updateCurrentChar();
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), "Enter en de literal carácter");
        }else if(currentChar == 'u'){
        charList.add('
        updateLexeme();
        updateCurrentChar();
        return e50();
        }