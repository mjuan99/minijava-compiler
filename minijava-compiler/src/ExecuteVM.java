import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecuteVM {
    public static void main(String[] args){
        try {
            Process vmProcess = getVMProcess(args[0]);
            BufferedReader reader = new BufferedReader(new InputStreamReader(vmProcess.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Process getVMProcess(String filePath) throws IOException {
        ProcessBuilder processBuilder;
        if(System.getProperty("os.name").equals("Linux"))
            processBuilder = getProcessBuilderForLinux(filePath);
        else
            processBuilder = getProcessBuilderForWindows(filePath);
        processBuilder.redirectErrorStream(true);
        return processBuilder.start();
    }

    private static ProcessBuilder getProcessBuilderForLinux(String filePath) {
        return new ProcessBuilder(
                "java", "-jar", "virtual-machine/CeIVM-cei2011.jar", filePath);
    }

    private static ProcessBuilder getProcessBuilderForWindows(String filePath) {
        return new ProcessBuilder(
                "cmd.exe", "/c", "java -jar virtual-machine/CeIVM-cei2011.jar " + filePath);
    }
}
