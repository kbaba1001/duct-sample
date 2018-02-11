FROM postgres:9.6
ENV LANG ja_JP.UTF-8

RUN localedef -i ja_JP -c -f UTF-8 -A /usr/share/locale/locale.alias ja_JP.UTF-8
RUN apt-get update -qq && apt-get install -y postgresql-9.6-pgtap pgtap

COPY *.sql /docker-entrypoint-initdb.d/
