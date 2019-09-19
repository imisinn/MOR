import java.io.File;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.ArrayList;

class functions{
  String name;//関数名
  Integer start;//関数の始まりの行数
  Integer end;//関数の終わりの行数
}

public class check_quality{
  void run(String[] args)throws IOException{
    CheckQualityMethod(args[0]);
  }

  void CheckQualityMethod(String check_file)throws IOException{
    nest(check_file);
    function_line(check_file);
    check_goto(check_file);
    check_name(check_file);
  }

  void check_name(String check_file)throws IOException{
    check_function_name(check_file);
  }


  }
  void check_function_name(String check_file)throws IOException{
    File fileread = new File(check_file + ".result.csv");
    File filewrite = new File(check_file + ".result.csv");
    BufferedReader in = new BufferedReader(new FileReader(fileread));
    PrintWriter out = new PrintWriter(new FileWriter(filewrite, true));
    String line;
    ArrayList<functions> ListFunc = new ArrayList<>();
    String file_place = new String();

    while((line = in.readLine()) != null){//関数に関する情報の取得
      String[] metrics_words = line.split(",",-1);
      file_place = metrics_words[2];
      if(metrics_words[0].equals("MAX_F_LINE")){
        ListFunc.add(make_functions(metrics_words));
        //System.out.println(make_functions(metrics_words).name + "," + make_functions(metrics_words).start +","+ make_functions(metrics_words).end);
        //System.out.println(" : " + metrics_words[5]);
      }
    }

    for(functions aFunction:ListFunc){
      out.println("NAME_FUNCTION," + aFunction.name + "," + file_place +  "," + aFunction.start + ","+ aFunction.end);//関数名一覧をファイルに出力
    }

    in.close();
    out.close();
  }

  functions make_functions(String[] words)throws IOException{
    functions func = new functions();
    func.name = words[1];
    func.start = Integer.parseInt(words[3]);
    func.end = Integer.parseInt(words[3]) + Integer.parseInt(words[5]) -1;
    return func;
  }

  void check_goto(String check_file)throws IOException{
    File fileread = new File("adlint/" + check_file + ".c.msg.csv");
    File filewrite = new File(check_file + ".result.csv");
    BufferedReader in = new BufferedReader(new FileReader(fileread));
    PrintWriter out = new PrintWriter(new FileWriter(filewrite, true));
    String line;

    while((line = in.readLine()) != null){
      String[] metrics_words = line.split(",",-1);
      if(metrics_words.length >= 5)if(metrics_words[5].equals("W1072"))out.println("GOTO,"+metrics_words[1]+","+metrics_words[2]+","+metrics_words[3]);
    }
    in.close();
    out.close();
  }

  void function_line(String check_file)throws IOException{
    File fileread = new File("adlint/" + check_file + ".c.met.csv");
    File filewrite = new File(check_file + ".result.csv");
    BufferedReader in = new BufferedReader(new FileReader(fileread));
    PrintWriter out = new PrintWriter(new FileWriter(filewrite, true));
    String line;

    while((line = in.readLine()) != null){
      String[] metrics_words = line.split(",",-1);
      if(metrics_words[1].equals("FN_LINE"))out.println(make_message(metrics_words,"MAX_F_LINE"));//行数に関する情報の抽出
    }
    in.close();
    out.close();
  }

  void nest(String check_file)throws IOException{
    File fileread = new File("adlint/" + check_file + ".c.met.csv");
    File filewrite = new File(check_file + ".result.csv");
    BufferedReader in = new BufferedReader(new FileReader(fileread));
    PrintWriter out = new PrintWriter(new FileWriter(filewrite));
    String line;

    while((line = in.readLine()) != null){
      String[] metrics_words = line.split(",",-1);
      if(metrics_words[1].equals("FN_NEST"))out.println(make_message(metrics_words,"MAX_F_NEST"));//ネストに関する情報の抽出
    }
    in.close();
    out.close();
  }

  String make_message(String[] metrics_words,String kind){
    Integer len = new Integer(metrics_words.length);
    String message = new String(kind+","+metrics_words[2]+","+metrics_words[len-4]+","+metrics_words[len-3]+","+metrics_words[len-2]+","+metrics_words[len-1]);
    return message;
  }

  public static void main(String[] args)throws IOException{
    check_quality app = new check_quality();
    app.run(args);
  }
}
