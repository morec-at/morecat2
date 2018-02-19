FROM mariadb:10.3.4

RUN apt-get update && \
    apt-get install -y locales && \
    rm -rf /var/lib/apt/lists/* && \
    echo "ja_JP.UTF-8 UTF-8" > /etc/locale.gen && \
    locale-gen ja_JP.UTF-8
ENV LC_ALL ja_JP.UTF-8
RUN ln -snf /usr/share/zoneinfo/Asia/Tokyo /etc/localtime

ADD etc/mysql/conf.d/charset.cnf /etc/mysql/conf.d/charset.cnf
