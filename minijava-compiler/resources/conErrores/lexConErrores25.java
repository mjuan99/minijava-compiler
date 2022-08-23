///[Error:1234567891|9]

private Token e55() throws LexicalException {
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), "Literal entero supera longitud máxima");
        }

private Token e16() throws IOException, LexicalException{
        if(eof)
        contador1 = contador2 + 123456789123;
        throw new LexicalException(lexeme, sourceFileManager.getLineNumber(), "EOF sin cerrar literal carácter");