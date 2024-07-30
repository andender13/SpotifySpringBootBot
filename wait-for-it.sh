#!/usr/bin/env bash
set -e

host="$1"
shift
until nc -z -v -w30 $host 3306
do
  echo "Waiting for database connection..."
  sleep 1
done

exec "$@"