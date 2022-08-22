///[Error:'\u2G|9]

private Token e50() throws IOException, LexicalException{
        if(eof)
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), "EOF sin cerrar literal carácter");
        else if(isHexadecimalDigit(currentChar)){
        updateLexeme();
        updateCurrentChar();
        '\u2Ge3'
        return e51();
        } else {
        updateLexeme();
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), "formato de literal carácter inválido");
        }
        }