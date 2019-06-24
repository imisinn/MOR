#!/bin/bash

# １つのファイルを解析するため
if [ $# -ne 1 ]; then
  echo "ファイルを1つ指定してください"
elif [[ $1 =~ .*\.c ]]; then
  echo "c言語のファイルです"
  java delete_inc $1
  adlintize -o adlint
  java change_langage
  cd adlint
  rm adlint_traits.yml
  mv hoge.yml adlint_traits.yml
  make verbose-all
else
  echo "c言語のファイルを用いてください"
fi
