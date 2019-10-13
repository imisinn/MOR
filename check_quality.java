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
    Integer type;//1:変数 2:仮引数
    Integer flag_unused;//使ったかどうかのフラグ　0:使っていない　1:使った
  }

  Integer f_comentout = new Integer(0);

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
    ArrayList<avaiables> List_Avaiable = new ArrayList<>();
    ArrayList<functions> ListFunc = new ArrayList<>();
    ListFunc = check_function_name(check_file);
    List_Avaiable = check_avaiable_name(check_file);
    check_name_unused(check_file,List_Avaiable,ListFunc);
  }

  void check_name_unused(String check_file,ArrayList<avaiables> List_Avaiable,ArrayList<functions> ListFunc)throws IOException{
    BufferedReader in = new BufferedReader(new FileReader(check_file));
    PrintWriter out = new PrintWriter(new FileWriter(check_file + ".info.csv", true));
    String line = new String();
    Integer line_count = new Integer(1);//何行目を検査しているかを記憶する変数

    while((line = in.readLine()) != null){
      line = Comentout(line);
      for(avaiables avai : List_Avaiable){
      }
      line_count++;
    }
    for(avaiables avai: List_Avaiable)if(avai.flag_unused.equals(1)){
      out.println("UNUSED_AVAIABLE," + avai.name + "," + avai.num_line);//仮引数情報をファイルに出力
    }

    in.close();
    out.close();
  }

  String Comentout(String line){
    line = ComentoutDoubleSlash(line);
    if(line != null)line = RockComent(line);
    //System.out.println(line);
    return line;
  }

  String RockComent(String line){
    String save_line = new String();

    if(f_comentout == 0){//解析部よりも前に/*が無い場合
      if(line.contains("/*") && line.contains("*/")){//行内でロックコメントが完結している場合
        if(!line.substring(0).equals("/")){
          String[] words = line.split("/*");
          save_line += words[0];
        }
        if(!line.substring(line.length() -1).equals("/")){
          String[] words = line.split("*/");
          save_line += words[1];
        }
        if(save_line == null)save_line = "";
        return save_line;
      }else if(line.contains("/*")){//ロックコメントの/*だけがあった場合の処理
        if(!line.substring(0).equals("/")){
          String[] words = line.split("/*");
          save_line += words[0];
        }
        f_comentout = 1;
        if(save_line == null)save_line = "";
        return save_line;
      }
    }else{//解析部よりも前に/*があり、*/がない場合
      if(line.contains("*/")){//ロックコメントの*/だけがあった場合の処理
        if(!line.substring(line.length() -1).equals("/")){
          String[] words = line.split("*/");
          save_line += words[1];
        }
        f_comentout = 0;
        if(save_line == null)save_line = "";
        return save_line;
      }else{
        return "";//ロックコメントの*/が無くコメントアウト内なので空文字を返す
      }
    }
    if(save_line == null)save_line = "";
    return line;
  }

  String ComentoutDoubleSlash(String line){
    String[] words;
    if(line.contains("//")){
      words = line.split("//");
      line = words[0];
    }
    return line;
  }

  ArrayList<avaiables> check_avaiable_name(String check_file)throws IOException{
    File fileread = new File("AST.txt");
    File filewrite = new File(check_file + ".info.csv");
    BufferedReader in = new BufferedReader(new FileReader(fileread));
    PrintWriter out = new PrintWriter(new FileWriter(filewrite, true));
    String line,linesave = new String(),arg_saveline = new String();
    Boolean bool = new Boolean(false);
    Integer count = new Integer(1);
    ArrayList<avaiables> List_Avaiable = new ArrayList<>();

    while((line = in.readLine()) != null){
      if(line.contains("VarDecl") && !line.contains("ParmVarDecl"))bool = true;//変数の抽出部　始まり
      if(line.contains("line:"))linesave = line;
      if(bool)List_Avaiable.add(make_avaiable(line,linesave,1));
      bool = false;//変数の抽出部　終わり

      //仮引数の抽出部
      if(line.contains("FunctionDecl"))arg_saveline = line;
      //if(arg_saveline.contains("used") && (line.contains("ParmVarDecl") && line.contains("used")))System.out.println("\n\n有り" + arg_saveline +"\n" + line);
      if(arg_saveline.contains("used") && (line.contains("ParmVarDecl") && line.contains("used")))List_Avaiable.add(make_avaiable(line,arg_saveline,2));
      //if(arg_saveline.contains("used") && (line.contains("ParmVarDecl") && !line.contains("used")))System.out.println("\n\nない" + arg_saveline +"\n" + line);

      count++;//行数のカウント
    }

    for(avaiables avai:List_Avaiable)if(avai.type == 1)out.println("NAME_AVAIABLE," + avai.name + "," + avai.num_line);//変数名一覧をファイルに出力
    for(avaiables avai:List_Avaiable)if(avai.type == 2)out.println("NAME_ARGUMENT," + avai.name + "," + avai.num_line);//仮引数情報をファイルに出力


    in.close();
    out.close();
    return List_Avaiable;
  }

  avaiables make_avaiable(String line,String save_line,Integer type)throws IOException{
    String[] words = line.split(" ",-1);
    String name = new String();
    avaiables avai = new avaiables();
    Boolean bool = new Boolean(false);
    String save = new String();
    Integer check_col = new Integer(0);

    avai.flag_unused = 0;

    avai.type = type;

    for(String word:words){//変数、仮引数の名の取得　
      if(bool == true){
        avai.name = word;
        break;
      }
      if(word.equals("used")){
        bool = true;//used の次に変数名が来るため、目印としてtrueに変更している。
        check_col = 0;
      }

      if(check_col.equals(0) && word.contains("col:"))check_col = 1;

      if(check_col.equals(1) && !word.contains("col:")){
        avai.name = word;
        avai.flag_unused = 1;
        break;
      }

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


  ArrayList<functions> check_function_name(String check_file)throws IOException{
    File fileread = new File(check_file + ".info.csv");
    File filewrite = new File(check_file + ".info.csv");
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

    return ListFunc;
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
    File filewrite = new File(check_file + ".info.csv");
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
    File filewrite = new File(check_file + ".info.csv");
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
    File filewrite = new File(check_file + ".info.csv");
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
