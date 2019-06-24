#!/bin/bash

# １つのファイルを解析するため
if [ $# -ne 1 ]; then
  echo "ファイルを1つ指定してください"
elif [[ $1 =~ .*\.c ]]; then
  echo "c言語のファイルです"
  java delete_inc $1
  adlintize -o adlint
  cd adlint
  make verbose-all
else
  echo "c言語のファイルを用いてください"
fi
