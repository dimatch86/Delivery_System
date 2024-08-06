#!/bin/bash

image_name=$1
required_tag=$2
repository="${image_name}"
url="https://hub.docker.com/v2/repositories/${repository}/tags?page_size=100"

get_tags_local() {
    docker image list | grep "^${image_name}" | awk -F' *' '{print $2}'
}

get_docker_hub_tags() {
    local tags=()

    while [ -n "$url" ]; do
        response=$(curl -s "$url")

        mapfile -t tags < <(echo "$response" | grep -o '"name": *"[^"]*"' | awk -F'"' '{print $4}')

        url=$(echo "$response" | grep -o '"next": *"[^"]*"' | awk -F'"' '{print $4}' | sed 's/\\u0026/\&/g')
    done

    echo "${tags[@]}"
}

exists_tag_in_list() {
  list=$1
  for item in ${list}; do
        if [ "$item" = "$required_tag" ]; then
            echo "$item"
            return 0
        fi
    done
    return 1
}

is_present_locally() {
  exists_tag_in_list "$(get_tags_local)"
}

is_present_remote() {
  exists_tag_in_list "$(get_docker_hub_tags "$url")"
}

if is_present_locally "$@"; then
    echo "Present locally"
elif is_present_remote "$@"; then
    echo "Present remote"
else
  echo "Not present"
fi