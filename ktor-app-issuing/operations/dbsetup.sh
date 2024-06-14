#! /bin/bash

set -e


REPOSITORY_ROOT=$(cd $(dirname $0)/../; pwd)

psql "host=localhost dbname=bill-one-2024-summer-issuing user=postgres options=--search_path=issuing" -f "${REPOSITORY_ROOT}/src/main/resources/db/sample_data.sql"