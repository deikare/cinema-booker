#!/bin/bash

server_address="http://localhost:8080"

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

prompt "Step 2 - The system lists movies available in the given time interval - title and screening times."
echo ""

prompt -s "A: wrong time interval, start one year in the future from now"
start=$(date -u -d "+1 year" +'%Y-%m-%dT%H:%M:%SZ')
curl "$server_address/movies?start=$start"
echo -e "\n"

prompt -s "B: wrong time interval, end is epoch"
utc_time="00:00:00"
end="1970-01-01T${utc_time}Z"
curl "$server_address/movies?end=$end"
echo -e "\n"

prompt -s "C: proper request - end +3 hour from now"
end=$(date -u -d "+3 hour" +'%Y-%m-%dT%H:%M:%SZ')
curl "$server_address/movies?end=$end"
echo -e "\n"

prompt -s "D: proper request - no time bounds"
response=$(curl "$server_address/movies" -s)
echo "$response"
echo -e "\n"

prompt "Step 4 - The system gives information regarding screening room (randomly chosen) and available seats."
echo ""

prompt -s "Screening info:"
size=$(echo "$response" | jq -r '.page.totalElements')  #get count of all screenings
response=$(curl "$server_address/movies?size=$size" -s) #
random_screening_link=$(echo "$response" | jq -r '._embedded.movieWithScreeningsModels[].screenings[]."_links".self.href' | shuf -n 1)
response=$(curl "$random_screening_link" -s)
echo "$response"
echo -e "\n"

first_and_last_seat() {
  local smallest_seat
  smallest_seat=$(echo "$1" | jq --arg row "$2" '.seatRows[] | select(.row == ($row | tonumber)) | .seats[].number' | sort -n | head -n 1)
  local largest_seat
  largest_seat=$(echo "$1" | jq --arg row "$2" '.seatRows[] | select(.row == ($row | tonumber)) | .seats[].number' | sort -n | tail -n 1)
  echo "$smallest_seat $largest_seat"
}

generate_types_array() {
  local types=("ADULT" "CHILD" "STUDENT")
  # Randomly shuffle the types and select the first n elements

  local string=""

  # Iterate over the randomly selected indices and construct the string
  for ((i = 0; i < $1; i++)); do
    # Get the type corresponding to the current index
    local index=$((RANDOM % ${#types[@]}))
    local type="${types[index]}"

    # Surround each type with double quotes
    type="\"$type\""

    # Append the type to the string
    if [[ $i -eq 0 ]]; then
      string="[$type"
    else
      string="$string, $type"
    fi
  done
  string="$string]"

  echo "$string"
}

prompt "Step 5 - user sends reservation"
echo ""

prompt -s "A1 bad request: name with numbers"
screening_id=$(basename "$random_screening_link")

smallest_row=$(echo "$response" | jq '.seatRows | min_by(.row) | .row')
result=$(first_and_last_seat "$response" "$smallest_row")
first_sm=$(echo "$result" | awk '{print $1}')
last_sm=$(echo "$result" | awk '{print $2}')
types_sm=$(generate_types_array "$((last_sm - first_sm + 1))")

largest_row=$(echo "$response" | jq '.seatRows | max_by(.row) | .row')
result=$(first_and_last_seat "$response" "$largest_row")
first_l=$(echo "$result" | awk '{print $1}')
last_l=$(echo "$result" | awk '{print $2}')
types_l=$(generate_types_array "$((last_l - first_l + 1))")

request="{
  \"screeningId\": \"$screening_id\",
  \"name\": \"Wac123ław\",
  \"surname\": \"Wąchocki-Mińkowski\",
  \"seats\": {
    \"$smallest_row\": {
      \"first\": $first_sm,
      \"types\": $types_sm
    },
    \"$largest_row: {
      \"first\": $first_l,
      \"types\": $types_l
    }
  }
}
"

echo "request:"
echo "$request"
url="$server_address/reservations"
echo "response:"
curl -X POST -H "Content-Type: application/json; charset=utf-8" --data "$request" "$url"
echo -e "\n"

prompt -s "A2 bad request: too short name + starts with small letter"
screening_id=$(basename "$random_screening_link")

smallest_row=$(echo "$response" | jq '.seatRows | min_by(.row) | .row')
result=$(first_and_last_seat "$response" "$smallest_row")
first_sm=$(echo "$result" | awk '{print $1}')
last_sm=$(echo "$result" | awk '{print $2}')
types_sm=$(generate_types_array "$((last_sm - first_sm + 1))")

largest_row=$(echo "$response" | jq '.seatRows | max_by(.row) | .row')
result=$(first_and_last_seat "$response" "$largest_row")
first_l=$(echo "$result" | awk '{print $1}')
last_l=$(echo "$result" | awk '{print $2}')
types_l=$(generate_types_array "$((last_l - first_l + 1))")

request="{
  \"screeningId\": \"$screening_id\",
  \"name\": \"ab\",
  \"surname\": \"Wąchocki-Mińkowski\",
  \"seats\": {
    \"$smallest_row\": {
      \"first\": $first_sm,
      \"types\": $types_sm
    },
    \"$largest_row: {
      \"first\": $first_l,
      \"types\": $types_l
    }
  }
}
"

echo "request:"
echo "$request"
url="$server_address/reservations"
echo "response:"
curl -X POST -H "Content-Type: application/json; charset=utf-8" --data "$request" "$url"
echo -e "\n"

prompt -s "B1 bad request: surname with hyphen"
screening_id=$(basename "$random_screening_link")

smallest_row=$(echo "$response" | jq '.seatRows | min_by(.row) | .row')
result=$(first_and_last_seat "$response" "$smallest_row")
first_sm=$(echo "$result" | awk '{print $1}')
last_sm=$(echo "$result" | awk '{print $2}')
types_sm=$(generate_types_array "$((last_sm - first_sm + 1))")

largest_row=$(echo "$response" | jq '.seatRows | max_by(.row) | .row')
result=$(first_and_last_seat "$response" "$largest_row")
first_l=$(echo "$result" | awk '{print $1}')
last_l=$(echo "$result" | awk '{print $2}')
types_l=$(generate_types_array "$((last_l - first_l + 1))")

request="{
  \"screeningId\": \"$screening_id\",
  \"name\": \"Wacław\",
  \"surname\": \"Wąchocki-\",
  \"seats\": {
    \"$smallest_row\": {
      \"first\": $first_sm,
      \"types\": $types_sm
    },
    \"$largest_row: {
      \"first\": $first_l,
      \"types\": $types_l
    }
  }
}
"

echo "request:"
echo "$request"
url="$server_address/reservations"
echo "response:"
curl -X POST -H "Content-Type: application/json; charset=utf-8" --data "$request" "$url"
echo -e "\n"

prompt -s "B2 bad request: too short name + starts with small letter"
screening_id=$(basename "$random_screening_link")

smallest_row=$(echo "$response" | jq '.seatRows | min_by(.row) | .row')
result=$(first_and_last_seat "$response" "$smallest_row")
first_sm=$(echo "$result" | awk '{print $1}')
last_sm=$(echo "$result" | awk '{print $2}')
types_sm=$(generate_types_array "$((last_sm - first_sm + 1))")

largest_row=$(echo "$response" | jq '.seatRows | max_by(.row) | .row')
result=$(first_and_last_seat "$response" "$largest_row")
first_l=$(echo "$result" | awk '{print $1}')
last_l=$(echo "$result" | awk '{print $2}')
types_l=$(generate_types_array "$((last_l - first_l + 1))")

request="{
  \"screeningId\": \"$screening_id\",
  \"name\": \"Wacław\",
  \"surname\": \"ab\",
  \"seats\": {
    \"$smallest_row\": {
      \"first\": $first_sm,
      \"types\": $types_sm
    },
    \"$largest_row: {
      \"first\": $first_l,
      \"types\": $types_l
    }
  }
}
"

echo "request:"
echo "$request"
url="$server_address/reservations"
echo "response:"
curl -X POST -H "Content-Type: application/json; charset=utf-8" --data "$request" "$url"
echo -e "\n"

prompt -s "C bad request: non-existing screening"
screening_id=$(basename "$random_screening_link")

smallest_row=$(echo "$response" | jq '.seatRows | min_by(.row) | .row')
result=$(first_and_last_seat "$response" "$smallest_row")
first_sm=$(echo "$result" | awk '{print $1}')
last_sm=$(echo "$result" | awk '{print $2}')
types_sm=$(generate_types_array "$((last_sm - first_sm + 1))")

largest_row=$(echo "$response" | jq '.seatRows | max_by(.row) | .row')
result=$(first_and_last_seat "$response" "$largest_row")
first_l=$(echo "$result" | awk '{print $1}')
last_l=$(echo "$result" | awk '{print $2}')
types_l=$(generate_types_array "$((last_l - first_l + 1))")

request="{
  \"screeningId\": \"${screening_id}xyz\",
  \"name\": \"Wacław\",
  \"surname\": \"Wąchocki-Mińkowski\",
  \"seats\": {
    \"$smallest_row\": {
      \"first\": $first_sm,
      \"types\": $types_sm
    },
    \"$largest_row\": {
      \"first\": $first_l,
      \"types\": $types_l
    }
  }
}
"

echo "request:"
echo "$request"
url="$server_address/reservations"
echo "response:"
curl -X POST -H "Content-Type: application/json; charset=utf-8" --data "$request" "$url"
echo -e "\n"

prompt -s "D bad request: types array in request exceeds allowed seat numbers"
screening_id=$(basename "$random_screening_link")

smallest_row=$(echo "$response" | jq '.seatRows | min_by(.row) | .row')
result=$(first_and_last_seat "$response" "$smallest_row")
first_sm=$(echo "$result" | awk '{print $1}')
last_sm=$(echo "$result" | awk '{print $2}')
types_sm=$(generate_types_array "$((last_sm - first_sm + 5))")

largest_row=$(echo "$response" | jq '.seatRows | max_by(.row) | .row')
result=$(first_and_last_seat "$response" "$largest_row")
first_l=$(echo "$result" | awk '{print $1}')
last_l=$(echo "$result" | awk '{print $2}')
types_l=$(generate_types_array "$((last_l - first_l + 1))")

request="{
  \"screeningId\": \"$screening_id\",
  \"name\": \"Wacław\",
  \"surname\": \"Wąchocki-Mińkowski\",
  \"seats\": {
    \"$smallest_row\": {
      \"first\": $first_sm,
      \"types\": $types_sm
    },
    \"$largest_row\": {
      \"first\": $first_l,
      \"types\": $types_l
    }
  }
}
"

echo "request:"
echo "$request"
url="$server_address/reservations"
echo "response:"
curl -X POST -H "Content-Type: application/json; charset=utf-8" --data "$request" "$url"
echo -e "\n"

prompt -s "E bad request: seats in row empty"
screening_id=$(basename "$random_screening_link")

smallest_row=$(echo "$response" | jq '.seatRows | min_by(.row) | .row')
result=$(first_and_last_seat "$response" "$smallest_row")
first_sm=$(echo "$result" | awk '{print $1}')
last_sm=$(echo "$result" | awk '{print $2}')

largest_row=$(echo "$response" | jq '.seatRows | max_by(.row) | .row')
result=$(first_and_last_seat "$response" "$largest_row")
first_l=$(echo "$result" | awk '{print $1}')
last_l=$(echo "$result" | awk '{print $2}')
types_l=$(generate_types_array "$((last_l - first_l + 1))")

request="{
  \"screeningId\": \"$screening_id\",
  \"name\": \"Wacław\",
  \"surname\": \"Wąchocki-Mińkowski\",
  \"seats\": {
    \"$smallest_row\": {
      \"first\": $first_sm,
      \"types\": []
    },
    \"$largest_row\": {
      \"first\": $first_l,
      \"types\": $types_l
    }
  }
}
"

echo "request:"
echo "$request"
url="$server_address/reservations"
echo "response:"
curl -X POST -H "Content-Type: application/json; charset=utf-8" --data "$request" "$url"
echo -e "\n"

prompt -s "F good request"
screening_id=$(basename "$random_screening_link")

smallest_row=$(echo "$response" | jq '.seatRows | min_by(.row) | .row')
result=$(first_and_last_seat "$response" "$smallest_row")
first_sm=$(echo "$result" | awk '{print $1}')
last_sm=$(echo "$result" | awk '{print $2}')
types_sm=$(generate_types_array "$((last_sm - first_sm - 1))")

largest_row=$(echo "$response" | jq '.seatRows | max_by(.row) | .row')
result=$(first_and_last_seat "$response" "$largest_row")
first_l=$(echo "$result" | awk '{print $1}')
last_l=$(echo "$result" | awk '{print $2}')
types_l=$(generate_types_array "$((last_l - first_l - 5))")

request="{
  \"screeningId\": \"$screening_id\",
  \"name\": \"Wacław\",
  \"surname\": \"Wąchocki-Mińkowski\",
  \"seats\": {
    \"$smallest_row\": {
      \"first\": $first_sm,
      \"types\": $types_sm
    },
    \"$largest_row\": {
      \"first\": $first_l,
      \"types\": $types_l
    }
  }
}
"

cost() {
  total_cost=$(echo "$1" | jq -r '.seats | to_entries | map(.value.types[]) | map(if . == "ADULT" then 25 elif . == "STUDENT" then 18 elif . == "CHILD" then 12.5 else 0 end) | add')
  echo "$total_cost"
}

total_cost=$(cost "$request")

echo "request:"
echo "$request"
url="$server_address/reservations"
echo ""
echo "screening before reservation: "
curl "$random_screening_link" -s
echo ""

echo "response (total cost should be equal $total_cost):"
curl -X POST -H "Content-Type: application/json; charset=utf-8" --data "$request" "$url"
echo -e "\n"

echo "screening after reservation: "
curl "$random_screening_link" -s
echo -e "\n"

echo -e "trying to send different request, should return \"seats are taken\". Request:"
smallest_row=$(echo "$response" | jq '.seatRows | min_by(.row) | .row')
result=$(first_and_last_seat "$response" "$smallest_row")
last_sm=$(echo "$result" | awk '{print $2}')
first_sm=$((last_sm - 2))
types_sm=$(generate_types_array "$((last_sm - first_sm + 1))")

largest_row=$(echo "$response" | jq '.seatRows | max_by(.row) | .row')
result=$(first_and_last_seat "$response" "$largest_row")
last_l=$(echo "$result" | awk '{print $2}')
first_l=$((last_l - 1))
types_l=$(generate_types_array "$((last_l - first_l + 1))")

request="{
  \"screeningId\": \"$screening_id\",
  \"name\": \"Wacław\",
  \"surname\": \"Wąchocki-Mińkowski\",
  \"seats\": {
    \"$smallest_row\": {
      \"first\": $first_sm,
      \"types\": $types_sm
    },
    \"$largest_row\": {
      \"first\": $first_l,
      \"types\": $types_l
    }
  }
}
"

echo "request:"
echo "$request"
echo ""

echo "response:"
curl -X POST -H "Content-Type: application/json; charset=utf-8" --data "$request" "$url"
echo -e "\n"

echo "screening after bad reservation request:"
curl "$random_screening_link" -s
echo ""

prompt "Bonus test: creating late screening and trying to reserve seats"

response=$(curl -X POST "$server_address/screenings/create_late" -s)
echo "$response"
new_screening_link=$(echo "$response" | jq -r '._links.self.href')
screening_id=$(basename "$new_screening_link")
response=$(curl "$new_screening_link" -s)

smallest_row=$(echo "$response" | jq '.seatRows | min_by(.row) | .row')
result=$(first_and_last_seat "$response" "$smallest_row")
first_sm=$(echo "$result" | awk '{print $1}')
last_sm=$(echo "$result" | awk '{print $2}')
types_sm=$(generate_types_array "$((last_sm - first_sm + 1))")

largest_row=$(echo "$response" | jq '.seatRows | max_by(.row) | .row')
result=$(first_and_last_seat "$response" "$largest_row")
first_l=$(echo "$result" | awk '{print $1}')
last_l=$(echo "$result" | awk '{print $2}')
types_l=$(generate_types_array "$((last_l - first_l + 1))")

request="{
  \"screeningId\": \"$screening_id\",
  \"name\": \"Wacław\",
  \"surname\": \"Wąchocki-Mińkowski\",
  \"seats\": {
    \"$smallest_row\": {
      \"first\": $first_sm,
      \"types\": $types_sm
    },
    \"$largest_row\": {
      \"first\": $first_l,
      \"types\": $types_l
    }
  }
}
"

echo "request:"
echo "$request"
url="$server_address/reservations"
echo ""
echo "screening before reservation: "
curl "$new_screening_link" -s
echo ""

echo "response:"
curl -X POST -H "Content-Type: application/json; charset=utf-8" --data "$request" "$url"
echo -e "\n"

echo "screening after reservation: "
curl "$new_screening_link" -s
echo -e "\n"
