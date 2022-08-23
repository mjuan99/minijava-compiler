///[Error:'\n|13]

private Token e0() throws IOException, LexicalException {
        if(eof)
        return e49();
        else if(Character.isWhitespace(currentChar)){
        updateCurrentChar();
        return e0();
        }else if(Character.isUpperCase(currentChar)) {
        updateLexeme();
        updateCurrentChar();
        return e1();
String asd = "asd" + '\n