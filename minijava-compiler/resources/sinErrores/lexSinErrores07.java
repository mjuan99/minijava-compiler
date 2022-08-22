package lexicalAnalyzer.sourceFileManager;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SourceFileManager {
    private File sourceFile;
    private FileInputStream fileInputStream;
    private InputStreamReader inputStreamReader;
    private int lineNumber;
    private char nextChar;

    public SourceFileManager(String sourceFilePath) throws FileNotFoundException {
        setSourceFile(sourceFilePath);
    }

    public void setSourceFile(String sourceFilePath) throws FileNotFoundException {
        sourceFile = new File(sourceFilePath);
        fileInputStream = new FileInputStream(sourceFile);
        inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        lineNumber = 1;
        nextChar = '\0';
    }

    public char getNextChar() throws IOException{
        if(lastCharWasEnter())
            lineNumber++;
        int nextCharCode = inputStreamReader.read();
        if(nextCharCode != -1) {
            nextChar = (char)nextCharCode;
            if(nextChar != '\r') //TODO preguntar esto
                return nextChar;
            else
                return getNextChar();
        }
        else
            throw new EOFException();
    }

    private boolean lastCharWasEnter() {
        return nextChar == '\n';
    }

    public int getLineNumber(){
        return lineNumber;
    }
}
