FROM postgres:9.6
ENV LANG ja_JP.UTF-8

RUN localedef -i ja_JP -c -f UTF-8 -A /usr/share/locale/locale.alias ja_JP.UTF-8
RUN apt-get update -qq && apt-get install -y postgresql-9.6-pgtap pgtap

COPY ./containers/postgres/*.sql /docker-entrypoint-initdb.d/

ENV TEST_DIR sql-tests
RUN mkdir $TEST_DIR
WORKDIR $TEST_DIR
ADD ./sql-tests $TEST_DIR
