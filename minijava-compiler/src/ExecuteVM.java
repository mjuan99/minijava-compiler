import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecuteVM {
    public static void main(String[] args){
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", "java -jar virtual-machine/CeIVM-cei2011.jar " + args[0]);
        builder.redirectErrorStream(true);
        try {
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = r.readLine();
            while (line != null) {
                System.out.println(line);
                line = r.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
