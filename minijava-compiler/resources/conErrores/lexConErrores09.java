///[Error:''|8]

public Token getNextToken() throws IOException, LexicalException {
        lexeme = "";
        Token nextToken = e0();
        if(nextToken.getTokenType().equals("idMetVar")){
        String keyword = keywordsMap.get(nextToken.getLexeme());
        ''
        if(keyword != null)
        nextToken = new Token(keyword, nextToken.getLexeme(), nextToken.getLineNumber());
        }
        return nextToken;
        }

