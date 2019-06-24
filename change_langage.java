import java.io.File;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;

public class change_langage{
  void run(String[] args)throws IOException{
    File fileread = new File("adlint/adlint_traits.yml");
    File filewrite = new File("adlint/hoge.yml");
    CopierMethod(fileread,filewrite);
  }

  void CopierMethod(File fileread,File filewrite)throws IOException{
    BufferedReader in = new BufferedReader(new FileReader(fileread));
    PrintWriter out = new PrintWriter(new FileWriter(filewrite));
    String line;
    String langage_line = new String("  language: \"en_US\"");

    while((line = in.readLine()) != null){
      if(line.equals(langage_line))out.println("  language: \"ja_JP\"");
      else out.println(line);
    }
    in.close();
    out.close();
  }


  public static void main(String[] args)throws IOException{
    change_langage app = new change_langage();
    app.run(args);
  }
}
