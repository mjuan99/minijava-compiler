private Token e17() throws IOException, LexicalException{
        if(eof())
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF sin cerrar literal carácter");
        else if(currentChar == '\''){
        updateLexeme();
        updateCurrentChar();
        return e18();
        }else{

                    123456