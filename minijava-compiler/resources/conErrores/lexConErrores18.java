///[Error:&|15]
private Token e17() throws IOException, LexicalException{
        if(eof)
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), "EOF sin cerrar literal carácter");
        else if(currentChar == '\''){
        updateLexeme();
        updateCurrentChar();
        return e18();
        }else{
        updateLexeme();
        updateCurrentChar();
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), "formato de literal carácter inválido");
        }
        }
updateCurrentChar();&