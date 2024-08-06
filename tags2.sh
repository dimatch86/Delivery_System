#!/bin/bash

# Функция для получения списка тегов сборок Docker по заданному имени
is_exist_local() {
  local image_name=$1
  local tag_name=$2

  result=$(docker images -q "$image_name":"$tag_name")
  if [ -z "$result" ]; then
    return 1
  fi
}

is_exist_remote() {
  local repository=$1
  local tag=$2
  local url="https://hub.docker.com/v2/repositories/${repository}/tags/${tag}"
  local http_code

  http_code=$(curl -s -o /dev/null -w "%{http_code}" "$url")
  if [ "$http_code" != 200 ]; then
    return 1
  fi
}

check_tag() {
  local repo=$1
  local tag=$2
  if is_exist_local "$repo" "$tag"; then
    echo "найден локально"
    exit 1
  fi
  if is_exist_remote "$repo" "$tag"; then
    echo "найден удаленно"
    exit 1
  fi
  echo "не найден"
}

#image_name="dimatch86/monitoring-service"
#image_tag="1.8-SNAPSHOT"

#check_tag "$image_name" "$image_tag"

add_tag() {
  tag_name=$1
  git tag -a "${tag_name}" -m "Соответствует образу сборки с тегом - ${tag_name}"
}
#add_tag "from_bash2"

function is_master() {
  # Получаем имя текущей ветки
  current_branch=$(git rev-parse --abbrev-ref HEAD)

  # Проверяем, является ли текущая ветка мастер-веткой или основной веткой
  if [ "$current_branch" != "master" ] && [ "$current_branch" != "main" ]; then
    echo "Не находимся в ветке master или main"
    exit 1
  fi

  # Проверяем, что текущий коммит совпадает с удаленным
  git fetch origin "$current_branch"
  local_commit=$(git rev-parse HEAD)
  remote_commit=$(git rev-parse origin/"$current_branch")

  if [ "$local_commit" != "$remote_commit" ]; then
    echo "Локальная ветка не на последнем коммите"
    exit 1
  fi

  echo "Находимся в ветке $current_branch и на последнем коммите"
}

is_commit() {
  status_output=$(git status --porcelain | grep -cv '^??')

    if [ "${status_output}" != 0 ]; then
      echo "Есть незафиксированные изменения в проекте"
      #echo "$status_output"
      exit 1
    fi
}

CDIR=$(pwd)
echo "${CDIR}"
declare -a my_array=("/src" "/pom.xml" "/Dockerfile")
function get_hash() {

  total_hash=""

  for element in "${@:2}"; do
    item="$1/$element"
    if [ ! -e "$item" ]; then
      continue # Переходим к следующей итерации, если файл или директрия не существует
    fi
    hash=""
    if [ -d "$item" ]; then
      hash="$(find "$item" -type f -exec sha256sum {} + | awk '{ print $1 }')"
    else
      hash="$(sha256sum "$item" | awk '{ print $1 }')"
    fi
    total_hash+="${hash}"
  done

  echo -n "$total_hash" | sha256sum | awk '{ print $1 }'
}

is_master
is_commit
hash=$(get_hash "${CDIR}" "${my_array[@]}")
check_tag "dimatch86/gateway" "${hash}"
add_tag "${hash}"

