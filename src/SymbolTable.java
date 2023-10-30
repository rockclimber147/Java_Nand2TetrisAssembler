import java.util.HashMap;

public class SymbolTable {
    private final HashMap<String, Integer> symbolTable = new HashMap<>();
    private int nextVariableAddress;
    private boolean debugMode = false;

    public SymbolTable(int n){
        nextVariableAddress = 16;
        // Initialize built in symbols
        symbolTable.put("R0", 0);
        symbolTable.put("R1", 1);
        symbolTable.put("R2", 2);
        symbolTable.put("R3", 3);
        symbolTable.put("R4", 4);
        symbolTable.put("R5", 5);
        symbolTable.put("R6", 6);
        symbolTable.put("R7", 7);
        symbolTable.put("R8", 8);
        symbolTable.put("R9", 9);
        symbolTable.put("R10", 10);
        symbolTable.put("R11", 11);
        symbolTable.put("R12", 12);
        symbolTable.put("R13", 13);
        symbolTable.put("R14", 14);
        symbolTable.put("R15", 15);
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);
        if (n > 0){
            debugMode = true;
        }
    }

    public boolean hasSymbol(String symbol){
        return symbolTable.containsKey(symbol);
    }

    public int getAddressFromTable(String symbol){
        if (debugMode){
            System.out.println("Symbol: " + symbol + "\nAddress: " + symbolTable.get(symbol));
        }
        return symbolTable.get(symbol);
    }

    public void addJumpLabel(String label, int address){
        if (debugMode){
            System.out.println("Label: " + label + "\nAddress: " + address);
        }
        symbolTable.put(label, address);
    }

    public void addVariable(String label){
        symbolTable.put(label, nextVariableAddress);
        if (debugMode){
            System.out.println("Adding Variable: " + label + " to Symbol Table\nWith Address: " + nextVariableAddress);
        }
        nextVariableAddress++;
    }
}
