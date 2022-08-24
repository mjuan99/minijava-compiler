package lexicalAnalyzer.sourceFileManager;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SourceFileManager {
    private File sourceFile;
    private InputStreamReader inputStreamReader;
    private int lineNumber;
    private int columnNumber;
    private char currentChar;

    public SourceFileManager(String sourceFilePath) throws FileNotFoundException {
        setSourceFile(sourceFilePath);
    }

    public void setSourceFile(String sourceFilePath) throws FileNotFoundException {
        sourceFile = new File(sourceFilePath);
        inputStreamReader = new InputStreamReader(new FileInputStream(sourceFile), StandardCharsets.UTF_8);
        lineNumber = 1;
        columnNumber = 0;
        currentChar = '\0';
    }

    public char getNextChar() throws IOException{
        if(currentChar == '\n') {
            lineNumber++;
            columnNumber = 0;
        }
        columnNumber++;
        readChar();
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

    public String getLine(int lineNumber) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
        for (int i = 1; i < lineNumber; i++)
            bufferedReader.readLine();
        String line = bufferedReader.readLine();
        return line != null ? line : "";
    }
}
