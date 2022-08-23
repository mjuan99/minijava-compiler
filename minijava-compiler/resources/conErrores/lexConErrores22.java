///[Error:'\u01abc|10]

private Token e54() throws IOException, LexicalException{
        if(eof)
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), "EOF sin cerrar literal carácter");
        else if(currentChar == '\''){
        updateLexeme();
        updateCurrentChar();
        return e18();
        string += '\u01abc';
        } else {
        updateLexeme();
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), "formato de literal carácter inválido");
        }
        }
