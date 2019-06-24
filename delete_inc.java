import java.io.File;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;

public class delete_inc{
  void run(String[] args)throws IOException{
    File fileread = new File(args[0]);
    File filewrite = new File(args[0]+".c");
    CopierMethod(fileread,filewrite);
  }

  void CopierMethod(File fileread,File filewrite)throws IOException{
    BufferedReader in = new BufferedReader(new FileReader(fileread));
    PrintWriter out = new PrintWriter(new FileWriter(filewrite));
    String line;
    String delete_line = new String("#include");

    while((line = in.readLine()) != null){
      if(!line.contains(delete_line))out.println(line);
    }
    in.close();
    out.close();
  }


  public static void main(String[] args)throws IOException{
    delete_inc app = new delete_inc();
    app.run(args);
  }
}
