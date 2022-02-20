//Tunahan OZMEN - 171301071
//< ve = sembolleri arasinda bosluk olmasi gibi hatalar, dikkate alinmasi gereken hatalar arasinda yer almadigi icin dogru olarak varsaydim.
//Butun hatalari output file icine yaziyor, hata olustugunda sadece hatalar yazilir.
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("for", "FOR_STATEMENT"); //Next token is FOR_STATEMENT Next lexeme is for
        map.put("(", "LPARANT");
        map.put(")", "RPARANT");
        map.put("int", "INT_TYPE");
        map.put("char", "CHAR_TYPE");
        map.put("=", "ASSIGNM");
        map.put(";", "SEMICOLON");
        map.put(">", "GREATER");
        map.put("<", "LESS");
        map.put(">=", "GRE_EQ");
        map.put("<=", "LESS_EQ");
        map.put("{", "LCURLYB");
        map.put("}", "RCURLYB");
        map.put("return", "RETURN_STMT");
        map.put("-", "SUBT");
        map.put("/", "DIV");
        map.put("*", "MULT");
        map.put("+", "ADD");
        map.put("An identifier consists of a single letter", "identifier"); //Next token is identifier Next lexeme is c
        map.put("integer constant", "INT_LIT"); //Next token is INT_LIT Next lexeme is 0

        String errorCheck = null;
        String text="";
        String workingDir = System.getProperty("user.dir");
        try {
            File inputFile = new File(workingDir+"\\"+args[0]); //"input.txt"
            Scanner reader = new Scanner(inputFile);
            while (reader.hasNextLine())
                text += reader.nextLine();
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Wrong input file. Check the path!");
            e.printStackTrace();
        }

        text = text.replaceAll("\n"," ");
        String []splitChars = {" ", "\\(", "\\)", "=", ";", "\\{", "}", "<=", ">=", "<", ">", "-", "\\+", "/", "\\*", "!", "@", "#", "%", "\\?", "\\$"};//, "%", "\\?" $
        for (int i = 0 ; i < splitChars.length ; i++){
            String out = "~"+splitChars[i]+"~"; //# will be used to split text and not lose delimeters (splitChars)
            text = text.replaceAll(splitChars[i], out);
        }
        String []textString = text.split("~");
        List<String> list_tmp = new ArrayList<>(Arrays.asList(textString));
        List<String> list = new ArrayList<>();
        list_tmp.removeAll(Collections.singleton(" "));list_tmp.removeAll(Collections.singleton(""));//remove empty members
        for (int i = 0 ; i < list_tmp.size()-1 ; i++){ //check if (< or >) and = are consecutive. If they are, they meant to be <= or >=
            if((list_tmp.get(i).equals("<") || list_tmp.get(i).equals(">")) && list_tmp.get(i+1).equals("=")){
                list.add(list_tmp.get(i)+list_tmp.get(i+1));
                i++;
                continue;
            }
            list.add(list_tmp.get(i));
        }
        list.add(list_tmp.get(list_tmp.size()-1)); //add last item

        String output = "";
        String check = "";
        for(int i = 0 ; i < list.size() ; i++){
            String lexeme = list.get(i);
            if (map.keySet().contains(lexeme)){
                output += "Next token is " +map.get(lexeme)+"\t"+"Next lexeme is " + lexeme + "\n";
            }
            else if (lexeme.matches("[a-zA-Z]?")){
                output += "Next token is " +map.get("An identifier consists of a single letter")+"\t"+"Next lexeme is " + lexeme + "\n";
            }
            else if (lexeme.matches("\\d+")){
                output += "Next token is " +map.get("integer constant")+"\t"+"Next lexeme is " + lexeme + "\n";
            }
            else if (lexeme.matches("[!@#$%?]")){
                check += "Unknown operator: " + lexeme + "\n";
                errorCheck = "Unknown operator";
            }
            else if (lexeme.matches("[a-zA-Z0-9]*")){
                check += "Unknown identifier: " + lexeme + "\n";
                errorCheck = "Unknown identifier";
            }
        }

        try {
            FileWriter writer = new FileWriter(workingDir+"\\"+args[1]);//"output.txt"
            if(errorCheck == null){ //output to file
                writer.write(output);
            }
            else{ //error msg to file
                writer.write(check);
            }
            writer.close();
            System.out.println("Program successfully ended");
        } catch (IOException e) {
            System.out.println("Could not write to file.");
            e.printStackTrace();
        }
    }
}