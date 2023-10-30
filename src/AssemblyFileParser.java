import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AssemblyFileParser {
    private boolean debugMode = false;
    private final File toTranslate;
    private Scanner parser;
    private String currentLine;
    private String currentNoComment; // Contains the part of the current line before the inline comment

    public enum commandType{
        A_INSTRUCTION,
        C_INSTRUCTION,
        L_INSTRUCTION,
        NO_COMMAND
    }

    public AssemblyFileParser(String fileName, int n) throws FileNotFoundException {
        toTranslate = new File(fileName);
        parser = new Scanner(toTranslate);
        if (n > 0) {
            debugMode = true;
        }
    }

    public boolean hasMoreLines(){
        return parser.hasNextLine();
    }

    public void next(){
        currentLine = parser.nextLine();
        currentNoComment = currentLine.split("//")[0].strip();
        if (debugMode) {
            System.out.println("\ncurrent line: " + currentLine + "\nformatted: " + currentNoComment);
        }
    }

    public String getCurrentLine(){
        return currentLine;
    }

    public commandType commandType(){
        if (currentNoComment == null){
            return commandType.NO_COMMAND;
        }
        if (currentNoComment.startsWith("@")){
            return commandType.A_INSTRUCTION;
        } else if (currentNoComment.startsWith("(")){
            return commandType.L_INSTRUCTION;
        } else if (isACommand()){
            return commandType.C_INSTRUCTION;
        } else {
            return commandType.NO_COMMAND;
        }
    }

    private boolean isACommand(){
        return currentNoComment.contains("A") || currentNoComment.contains("M") || // Check the main symbols
                currentNoComment.contains("D") || currentNoComment.contains("J");
    }

    public void reset() throws FileNotFoundException {
        parser = new Scanner(toTranslate);
    }

    public String symbol(){
        String symbol;
        if (currentNoComment.startsWith("@")){
            symbol = currentNoComment.replace("@", "");
        } else {
            symbol = currentNoComment.replace("(", "").replace(")", "");
        }
        if (debugMode) {
            System.out.println("Symbol found: " + symbol);
        }
        return symbol;
    }
    public String dest(){
        if (currentNoComment.contains("=")){
            String dest = currentNoComment.split("=")[0].strip(); // Get symbol before '='
            if (debugMode) {
                System.out.println("dest symbol found: " + dest);
            }
            return dest;
        }
        return null;
    }
    public String comp(){
        String comp;
        if (currentNoComment.contains("=")){
            comp = currentNoComment.split("=")[1].strip(); // cut out dest symbol if present
        } else {
            comp = currentNoComment;
        }
        if (comp.contains(";")){
            comp = comp.split(";")[0].strip(); // cut out jump symbol if present
        }
        if (debugMode) {
            System.out.println("comp symbol found: " + comp);
        }
        return comp;
    }
    public String jump(){
        if (currentNoComment.contains(";")){
            String jump = currentNoComment.split(";")[1].strip(); // Get the jump symbol after the ';'
            if (debugMode) {
                System.out.println("jump symbol found: " + jump);
            }
            return jump;
        }
        return null;
    }
}

