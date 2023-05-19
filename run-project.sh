#!/bin/bash

prompt() {
  line_width=$(tput cols)
  for arg in "$@"; do
    case $arg in
    -s | --small)
      line_width=$((line_width / 2))
      shift
      ;;
    esac
  done

  horizontal_line=$(printf '%.0s-' $(seq 1 "$line_width"))
  formatted_message="$horizontal_line\n$(echo "$1" | fold -w "$line_width")\n$horizontal_line"
  echo -e "$formatted_message"
}

prompt "clean"
mvn clean
echo -e "\n"

prompt "compile"
mvn package
echo -e "\n"

prompt "run server"
java -jar ./target/cinema-booker-0.0.1-SNAPSHOT.jar
