///[Error:&a|13]
private Token e16() throws IOException, LexicalException{
        if(eof)
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), "EOF sin cerrar literal carácter");
        else if(currentChar == '\'') {
        updateLexeme();
        updateCurrentChar();
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), "literal carácter vacío");
        }else if(currentChar == '\\'){
        updateLexeme();
        updateCurrentChar();
        return e19();
        updateLexeme(&a);
        }else{
        updateCurrentChar();
        return e17();
        }
        }