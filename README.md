# Noah

卒業論文で作成したツール  

## 実行環境
PC:iMac (Retina 4K, 21.5-inch, 2017)
OS:MacOS Mojave 10.14.4


## 必要なパッケージ

### AdLint  

#### インストールの方法

* 参考先https://researchmap.jp/jo4a9svxt-1778110/

## 出力内容について  
検査対象のファイルを xxx.c とする。

### xxx.c.info.csvの出力内容について  

* MAX_F_NEST:関数内の最大のネスト数  
* MAX_F_LINE:関数内の行数  
* GOTO:goto文  
* NAME_FUNCTION:関数名と行数  
* NAME_AVAIABLE:変数名と行数  
* NAME_ARGUMENT:仮引数名と行数  
* UNUSED_AVAIABLE:未使用の変数について
* NUM_F_ARGUMENT:関数の仮引数の個数
* NUM_CYCLOMATIC:関数のサイクロマティック数
* NUM_GROBAL:ソースコードのグローバル変数の個数

### xxx.c.worning.csvの出力内容について  
* DEEP_NEST:ネストが深い
* LONG_FUNCTION:関数の行数が多い
* GOTO:goto文を使っている
* SHORT_FUNCTION_NAME:関数名が短い
* LONG_FUNCTION_NAME:関数名が長い
* SHORT_AVAIABLE_NAME:変数名が短い
* LONG_AVAIABLE_NAME:変数名が長い
* UNUSED_AVAIABLE:未使用の変数がある
* MANY_ARGMENT:関数の引数が多い
* MANY_CYCLOMATIC:関数のサイクロマティック数が多い
* NAMY_GROBAL_VARIABLE:グローバル変数の個数が多い

### setting.txtについて
setting.txtは判定をするための基準を決定するためのファイルである。
設定した値より多いか、少ないかでエラーが出力される。
* nest_max:ネストの最大数
* f_line_max:関数の行数の最大数
* f_name_min:関数名の文字数の最小数
* f_name_max:関数名の文字数の最大数
* ava_name_min:変数名や引数名の文字数の最小数
* ava_name_max:変数名や引数名の文字数の最大数
* f_argument_max:関数の引数の最大数
* f_cyclomatic:サイクロマティック数の最大数
* max_grobal: グローバル変数の最大数
