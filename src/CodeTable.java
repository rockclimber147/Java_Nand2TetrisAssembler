import java.util.HashMap;

public class CodeTable {
    private final HashMap<String,String> dTable = new HashMap<>();
    private final HashMap<String,String> cTable = new HashMap<>();
    private final HashMap<String,String> jTable = new HashMap<>();
    private boolean debugMode = false;

    public CodeTable(int n){
        if (n > 0){
            debugMode = true;
        }
        dTable.put(null,   "000");
        dTable.put("M",    "001");
        dTable.put("D",    "010");
        dTable.put("MD",   "011");
        dTable.put("A",    "100");
        dTable.put("AM",   "101");
        dTable.put("AD",   "110");
        dTable.put("AMD",  "111");

        jTable.put(null,   "000");
        jTable.put("JGT",  "001");
        jTable.put("JEQ",  "010");
        jTable.put("JGE",  "011");
        jTable.put("JLT",  "100");
        jTable.put("JNE",  "101");
        jTable.put("JLE",  "110");
        jTable.put("JMP",  "111");

        cTable.put("0",   "0101010");
        cTable.put("1",   "0111111");
        cTable.put("-1",  "0111010");
        cTable.put("D",   "0001100");
        cTable.put("A",   "0110000");
        cTable.put("M",   "1110000");
        cTable.put("!D",  "0001101");
        cTable.put("!A",  "0110001");
        cTable.put("!M",  "1110001");
        cTable.put("-D",  "0001111");
        cTable.put("-A",  "0110011");
        cTable.put("-M",  "1110011");
        cTable.put("D+1", "0011111");
        cTable.put("A+1", "0110111");
        cTable.put("M+1", "1110111");
        cTable.put("D-1", "0001110");
        cTable.put("A-1", "0110010");
        cTable.put("M-1", "1110010");
        cTable.put("D+A", "0000010");
        cTable.put("A+D", "0000010");
        cTable.put("D+M", "1000010");
        cTable.put("M+D", "1000010");
        cTable.put("D-A", "0010011");
        cTable.put("D-M", "1010011");
        cTable.put("A-D", "0000111");
        cTable.put("M-D", "1000111");
        cTable.put("D&A", "0000000");
        cTable.put("D&M", "1000000");
        cTable.put("D|A", "0010101");
        cTable.put("D|M", "1010101");
    }

    public String comp(String c){
        if (debugMode){
            System.out.println("c: " + c + "\nbin: " + cTable.get(c));
        }
        return cTable.get(c);
    }
    public String dest(String d){
        if (debugMode){
            System.out.println("d: " + d + "\nbin: " + dTable.get(d));
        }
        return dTable.get(d);
    }
    public String jump(String j){
        if (debugMode){
            System.out.println("j: " + j + "\nbin: " + jTable.get(j));
        }
        return jTable.get(j);
    }
}
