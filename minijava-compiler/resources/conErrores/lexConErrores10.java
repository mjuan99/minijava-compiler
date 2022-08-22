///[Error:'\|12]

private void updateCurrentChar() throws IOException {
        try {
        currentChar = sourceFileManager.getNextChar();
        }
        catch(EOFException e){
        currentChar = '\0';
        eof = true;
        }
        }
'\