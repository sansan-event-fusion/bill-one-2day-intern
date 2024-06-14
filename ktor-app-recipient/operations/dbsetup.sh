#! /bin/bash

set -e


REPOSITORY_ROOT=$(cd $(dirname $0)/../; pwd)

psql "host=localhost dbname=bill-one-2024-summer-recipient user=postgres options=--search_path=recipient" -f "${REPOSITORY_ROOT}/src/main/resources/db/sample_data.sql"
