import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.*;

public class CopyToClipboard {
    public static void main(String[] args) {
        String myText = getText();

        // Wrap the string in a StringSelection object
        StringSelection stringSelection = new StringSelection(myText);

        // Get the system clipboard
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // Set the clipboard contents to your string
        clipboard.setContents(stringSelection, null);

        System.out.println("Text copied to clipboard!");
    }

    public static String getText(){
        Scanner scan = new Scanner(System.in);
        System.out.println("What folder do you want to print?");
        String s = scan.nextLine();
        File folder = new File("C:\\Users\\alexp\\"+s);

        File[] files = folder.listFiles();
        s="";
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String n = file.getName();
                    if(n.endsWith("java") && !n.equals("CopyToClipboard.java") && !n.equals("PrintCode.java")){
                        
                        s+=getAll(file) + "\n";

                    }
                }
            }
        }return s;
    }

    public static String getAll(File f){
        BufferedReader in;
        String s = "";
        try{
            in = new BufferedReader(new FileReader(f));
            String line;
            while((line = in.readLine())!=null){
                s+=line+"\n";
            }
        }
        catch(Exception e){
            System.out.println("Something when wrong");
            assert true;
        }
        return s;
    }
}