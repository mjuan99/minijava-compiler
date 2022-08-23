///[Error:'\|12]

private Token e20() throws IOException, LexicalException{
        if(eof)
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), "EOF sin cerrar literal carácter");
        else if(currentChar == '\''){
        updateLexeme();
        updateCurrentChar();
        return e18();
        }else{
        updateLexeme();
        int a1 = '\
        updateCurrentChar();
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), "formato de literal carácter inválido");
        }
        }