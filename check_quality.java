import java.io.File;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;

public class check_quality{
  void run(String[] args)throws IOException{
    CheckQualityMethod(args[0]);
  }

  void CheckQualityMethod(String check_file)throws IOException{
    nest(check_file);
  }

  void nest(String check_file)throws IOException{
    File fileread = new File("adlint/" + check_file + ".c.met.csv");
    File filewrite = new File(check_file + ".result.csv");
    BufferedReader in = new BufferedReader(new FileReader(fileread));
    PrintWriter out = new PrintWriter(new FileWriter(filewrite));
    String line;

    while((line = in.readLine()) != null){
      String[] metrics_words = line.split(",",-1);
      if(metrics_words[1].equals("FN_NEST"))out.println("MAX_F_NEST," + metrics_words[2] + "," + metrics_words[4] + "," + metrics_words[5]+","+metrics_words[6]+","+metrics_words[7]);//ネストに関する情報の抽出//ネストに関する情報の抽出
    }
    in.close();
    out.close();
  }

  public static void main(String[] args)throws IOException{
    check_quality app = new check_quality();
    app.run(args);
  }
}
