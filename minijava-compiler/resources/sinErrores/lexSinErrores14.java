private Token e19() throws IOException, LexicalException{
        if(eof())
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF sin cerrar literal carácter");
        else if(currentChar == '\n'){
        LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
        sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
        "Enter en literal carácter"