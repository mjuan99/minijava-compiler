///[Error:||19]

private Token e21() throws IOException, LexicalException{
        if(currentChar == '\n'){
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), "salto de línea dentro de un literal string");
        }else if(currentChar == '\\'){
        updateLexeme();
        updateCurrentChar();
        return e23();
        }else if(currentChar == '"'){
        updateLexeme();
        updateCurrentChar();
        return e22();
        }else{
        updateLexeme();
        updateCurrentChar();
        return e21();
        }
        }|