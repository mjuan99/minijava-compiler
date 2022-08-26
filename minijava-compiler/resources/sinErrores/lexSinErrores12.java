private Token e16() throws IOException, LexicalException{
        if(eof())
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF sin cerrar literal carácter");
        else if(currentChar == '\n'){
        LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
        sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
        "enter en literal carácter");
        updateCurrentChar();
        throw exception;
        }else if(currentChar == '\'') {

            /*
            *
            *
            *
            *
            * comentario * multi / linea
            *
            *
            * */