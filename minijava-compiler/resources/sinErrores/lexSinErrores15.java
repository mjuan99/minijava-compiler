private Token e23() throws IOException, LexicalException {
        if(eof())
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(), "EOF sin cerrar literal string");
        if(currentChar == '\n'){
        LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
        sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
        "enter en literal string");
        updateCurrentChar();
        throw exception; ==