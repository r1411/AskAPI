psql -U "${POSTGRES_USER}" -d "${POSTGRES_DB}" <<-END
    create schema ${POSTGRES_SCHEMA};
END