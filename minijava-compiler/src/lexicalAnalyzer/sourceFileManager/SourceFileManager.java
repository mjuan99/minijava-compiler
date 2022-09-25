package lexicalAnalyzer.sourceFileManager;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SourceFileManager {
    private InputStreamReader inputStreamReader;
    private int lineNumber;
    private int columnNumber;
    private char currentChar;
    private String currentLine;

    public SourceFileManager(String sourceFilePath) throws FileNotFoundException {
        setSourceFile(sourceFilePath);
    }

    public void setSourceFile(String sourceFilePath) throws FileNotFoundException {
        File sourceFile = new File(sourceFilePath);
        inputStreamReader = new InputStreamReader(new FileInputStream(sourceFile), StandardCharsets.UTF_8);
        lineNumber = 1;
        columnNumber = 0;
        currentChar = '\0';
        currentLine = "";
    }

    public char getNextChar() throws IOException{
        if(currentChar == '\n') {
            lineNumber++;
            columnNumber = 0;
            currentLine = "";
        }
        columnNumber++;
        readChar();
        if(currentChar != '\n' && currentChar != (char)-1)
            currentLine += currentChar;
        return currentChar;
    }

    private void readChar() throws IOException {
        currentChar = (char)inputStreamReader.read();
        if(currentChar == '\r')
            readChar();
    }

    public int getLineNumber(){
        return lineNumber;
    }

    public int getColumnNumber(){
        return columnNumber;
    }

    public String getCurrentLine(){
        return currentLine;
    }
}
