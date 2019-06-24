import java.io.File;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;

public class pickup_error{
  void run(String[] args)throws IOException{
    File fileread = new File("adlint/"+args[0]+".c.msg.csv");
    File filewrite = new File("adlint_pickup.txt");
    CopierMethod(fileread,filewrite);
  }

  void CopierMethod(File fileread,File filewrite)throws IOException{
    BufferedReader in = new BufferedReader(new FileReader(fileread));
    PrintWriter out = new PrintWriter(new FileWriter(filewrite));
    String line;
    String goto_error = "W1072";
    Integer first = new Integer(119);
    Integer last = new Integer(254);
    Integer check = new Integer(0);

    line = in.readLine();
    while((line = in.readLine()) != null){
      String[] message_words = line.split(",",-1);
      if(message_words[5].equals(goto_error))out.println(line);
      check = first;
      do{
        if(message_words[5].equals("W0"+String.valueOf(check)))out.println(line);
        check++;
      }while(check <= last);

    }
    in.close();
    out.close();
  }


  public static void main(String[] args)throws IOException{
    pickup_error app = new pickup_error();
    app.run(args);
  }
}
