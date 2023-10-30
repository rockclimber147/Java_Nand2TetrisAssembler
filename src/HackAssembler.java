import java.io.FileWriter;
import java.io.IOException;

public class HackAssembler {
    private final SymbolTable symbolTable;
    private final CodeTable codeTable;
    private final AssemblyFileParser parser;
    private final FileWriter hackWriter;
    private final FileWriter listWriter;
    private AssemblyFileParser.commandType command;
    private final int listingSpacing = 10;
    private int codeLineNo;
    private boolean debugMode = false;

    public HackAssembler(String fileName, int n) throws IOException {
        if (n > 0){
            debugMode = true;
        }
        symbolTable = new SymbolTable(n);
        codeTable = new CodeTable(n);
        parser = new AssemblyFileParser(fileName, n);
        hackWriter = new FileWriter(fileName.replace(".asm", ".hack"));
        listWriter = new FileWriter(fileName.replace(".asm", "_listing.txt"));
    }

    public void firstPass(){ // Populate jump labels
        codeLineNo = 0;
        parser.next();
        while (parser.hasMoreLines()){
            debugPrint("Line: " + codeLineNo);
            parser.next();
            command = parser.commandType();

            debugPrint("Instruction: " + command);

            if (command == AssemblyFileParser.commandType.C_INSTRUCTION ||
            command == AssemblyFileParser.commandType.A_INSTRUCTION){
                codeLineNo++;
                debugPrint("lineUP!");

            } else if (command == AssemblyFileParser.commandType.L_INSTRUCTION){
//                codeLineNo++;
                String label = parser.symbol();
                symbolTable.addJumpLabel(label, codeLineNo);
                debugPrint("lineUP!");
            }
        }
    }

    public void secondPass() throws IOException {
        listingWriteHeader();
        codeLineNo = 0;
        parser.reset();
        while (parser.hasMoreLines()) {

            parser.next();
            command = parser.commandType();

            debugPrint("Instruction: " + command);

            if (command == AssemblyFileParser.commandType.NO_COMMAND){
                listingWriteGeneric(parser.getCurrentLine());
            } else if (command == AssemblyFileParser.commandType.A_INSTRUCTION){
                String symbol = parser.symbol();
                hackWriteAddress(symbol);
            } else if (command == AssemblyFileParser.commandType.C_INSTRUCTION){
                String d = parser.dest();
                String c = parser.comp();
                String j = parser.jump();
                String codeBinary = "111" + codeTable.comp(c) + codeTable.dest(d) + codeTable.jump(j) + "\n";

                debugPrint("Binary string: " + codeBinary);

                listingWriteCommand();
                hackWriter.write(codeBinary);
            } else if (command == AssemblyFileParser.commandType.L_INSTRUCTION){
                listingWriteLabel(parser.symbol());
            }
        }
    }

    private void hackWriteAddress(String symbol) throws IOException {
        String binaryRep;

        if (isNumeric(symbol)) {
            binaryRep = getBinaryRepresentation(Integer.parseInt(symbol));

            debugPrint("Decimal: " + symbol + "\n16 bit : " + binaryRep);

            hackWriter.write(binaryRep + "\n");
            listingWriteConstant(symbol);
            return;
        }
        if(!symbolTable.hasSymbol(symbol)){
            debugPrint("Symbol " + symbol + " not in table, adding...");
            symbolTable.addVariable(symbol);
        }
        int varAddress = symbolTable.getAddressFromTable(symbol);
        listingWriteVarAddress(varAddress);
        binaryRep = getBinaryRepresentation(varAddress);
        debugPrint("binary string: " + binaryRep);
        hackWriter.write(binaryRep + "\n");
    }

    public void closeWriters() throws IOException {
        listWriter.close();
        hackWriter.close();
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private String getBinaryRepresentation(int n){
        StringBuilder binaryRep = new StringBuilder(Integer.toBinaryString(n));
        while (binaryRep.length() < 16){
            binaryRep.insert(0, "0");
        }
        return binaryRep.toString();
    }

    private void listingWriteHeader() throws IOException {
        String headerString = centerSpaces("ROM") + "|"
                + centerSpaces("Address") + "|  Source\n";
        listWriter.write(headerString);
    }

    private void listingWriteGeneric(String line) throws IOException {
        String lineToWrite = (" ".repeat(listingSpacing) + "|").repeat(2) + "  " + line;
        listWriter.write(lineToWrite + "\n");
    }

    private void listingWriteVarAddress(int varAddress) throws IOException {
        String lineToWrite = centerSpaces(codeLineNo + "") + "|" +
                centerSpaces("RAM[" + varAddress + "]") + "|  "
                + parser.getCurrentLine();
        listWriter.write(lineToWrite + "\n");
        codeLineNo++;
    }

    private void listingWriteConstant(String constant) throws IOException {
        String lineToWrite = centerSpaces(codeLineNo + "") + "|" +
                centerSpaces(constant) + "|  "
                + parser.getCurrentLine();
        listWriter.write(lineToWrite + "\n");
        codeLineNo++;
    }

    private void listingWriteCommand() throws IOException {
        String lineToWrite = centerSpaces(codeLineNo + "") + "|" +
                centerSpaces("") + "|  " + parser.getCurrentLine();
        listWriter.write(lineToWrite + "\n");
        codeLineNo++;
    }

    private void listingWriteLabel(String label) throws IOException {
        String lineToWrite = centerSpaces(codeLineNo + "") + "|" +
                centerSpaces("ROM[" + symbolTable.getAddressFromTable(label) + "]") +
                "|  " + parser.getCurrentLine();
        listWriter.write(lineToWrite + "\n");
    }

    private String centerSpaces(String s){
        int left, right;
        int repeat = listingSpacing - s.length();
        if (repeat % 2 == 1){
            left = repeat / 2;
            right = repeat / 2 + 1;
        } else {
            left = repeat / 2;
            right = repeat / 2;
        }
        s = " ".repeat(left) + s + " ".repeat(right);
        return s;
    }

    private void debugPrint(String s){
        if (debugMode){
            System.out.println(s);
        }
    }
}
