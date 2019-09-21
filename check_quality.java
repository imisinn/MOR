import java.io.File;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class check_quality{
  public class functions{
    String name;//関数名
    Integer start;//関数の始まりの行数
    Integer end;//関数の終わりの行数
  }

  public class avaiables{
    String name;//変数名
    Integer num_line;//変数の宣言の行数
    //String function;//どこの関数で宣言されているか
  }

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
    check_avaiable_name(check_file);
  }

  void check_avaiable_name(String check_file)throws IOException{
    File fileread = new File("AST.txt");
    File filewrite = new File(check_file + ".result.csv");
    BufferedReader in = new BufferedReader(new FileReader(fileread));
    PrintWriter out = new PrintWriter(new FileWriter(filewrite, true));
    String line,linesave = new String();
    Boolean bool = new Boolean(false);
    Integer count = new Integer(1);
    ArrayList<avaiables> List_Avaiable = new ArrayList<>();

    while((line = in.readLine()) != null){
      String[] words = line.split(" ",-1);
      if(line.contains("VarDecl") && !line.contains("ParmVarDecl"))bool = true;//変数の抽出部　始まり
      if(line.contains("line:"))linesave = line;
      if(bool)List_Avaiable.add(make_avaiable(line,words,linesave));
      bool = false;//変数のリセット　　//変数の抽出部　終わり
      count++;//行数のカウント
    }

    for(avaiables avai:List_Avaiable)out.println("NAME_AVAIABLE," + avai.name + "," + avai.num_line);//関数名一覧をファイルに出力

    in.close();
    out.close();
  }

  avaiables make_avaiable(String line,String[] words,String save_line)throws IOException{
    String name = new String();
    avaiables avai = new avaiables();
    Boolean bool = new Boolean(false);
    String save = new String();

    for(String word:words){//変数名の取得
      if(bool == true){
        avai.name = word;
        break;
      }
      if(word.equals("used"))bool = true;//used の次に変数名が来るため、目印としてtrueに変更している。
    }

    for(String word:words){
      if(word.contains("line:")){//VarDeclの行数内に行数があるかの検知。
        avai.num_line = pichup_num_line(word);
      }
    }
    if(avai.num_line == null){
      String[] save_words = save_line.split(" ",-1);
      for(String save_word:save_words)if(save_word.contains("line:")){
        Integer line_num = pichup_num_line(save_word);
        avai.num_line = line_num;
      }
    }
    return avai;
  }

  Integer pichup_num_line(String words)throws IOException{
    Integer num = new Integer(0);
    String[] word = words.split(":",-1);
    if(word.length < 3)return -1;
    if(!isNumber(word[1]))return -2;
    num = Integer.parseInt(word[1]);
    return num;
  }

  Boolean isNumber(String num){
    try{
      Integer.parseInt(num);
      return true;
    } catch (NumberFormatException e){
      return false;
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
