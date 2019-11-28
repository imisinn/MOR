#!/bin/sh
for x in "$@"
do
  if [[ $x =~ .*\.c ]]; then
    if [ -e $x ];then
      echo "c言語のファイルです"
      dir="${x}dir"
      cpfi="${dir}/${x}"
      mkdir $dir
      cp $x $cpfi
      cd $dir
      java -classpath ../ delete_inc $x
      adlintize -o adlint
      java -classpath ../ change_langage
      cd adlint
      rm adlint_traits.yml
      mv hoge.yml adlint_traits.yml
      make verbose-all
      cd ../
      clang -cc1 -ast-dump $x &> AST.txt
      cp ../setting.txt setting.txt
      java -classpath ../ check_quality $x
      cd ../
    else
      echo "ファイルが存在しません"
    fi
  else
    echo "c言語のファイルを用いてください"
  fi
done
