import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        HackAssembler h = new HackAssembler(args[0], 1);
        h.firstPass();
        h.secondPass();
        h.closeWriters();
    }
}
