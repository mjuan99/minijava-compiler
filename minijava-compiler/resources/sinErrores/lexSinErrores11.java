private Token e15() throws IOException, LexicalException {
        if(Character.isDigit(currentChar)){
        updateLexeme();

        LexicalException exception = new LexicalException(lexeme, sourceFileManager.getLineNumber(),
        sourceFileManager.getColumnNumber(), sourceFileManager.getCurrentLine(),
        "literal entero supera longitud máxima");



        updateCurrentChar();
        throw                           exception