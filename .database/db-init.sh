SCRIPT_FILES=""

for file in cql/*
do
  SCRIPT_FILES=$SCRIPT_FILES$(cat "$file")
done

CQL="
$(cat cassandra_bootstrap.cql)
$SCRIPT_FILES
"

until echo "$CQL" | cqlsh; do
  printf "cqlsh: Cassandra is unavailable to initialize - will retry later"
  sleep 4
done &

exec /usr/local/bin/docker-entrypoint.sh "$@"
