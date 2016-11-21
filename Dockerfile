FROM quay.io/travisci/travis-jvm
RUN sudo apt-get install maven
RUN su - travis
RUN wget -O stunnel.tar.gz ftp://ftp.stunnel.org/stunnel/archive/5.x/stunnel-5.29.tar.gz
RUN tar -xvzf stunnel.tar.gz
RUN cd ./stunnel-5.29 && ./configure && make && sudo make install && cd ..

RUN mkdir jedis && cd jedis && git clone https://github.com/antirez/redis.git --branch unstable --single-branch redis-git
RUN cd jedis && make -C redis-git clean && make -C redis-git -j4

#RUN ls && git clone https://github.com/xetorthio/jedis.git

#ENV JEDIS_REPO="https://github.com/xetorthio/jedis.git" \
#    JEDIS_BRANCH="origin/master"
#RUN cd jedis && git init && ls && git remote add origin ${JEDIS_REPO} && git fetch && git checkout -t ${JEDIS_BRANCH}

COPY src jedis/src
COPY pom.xml jedis/pom.xml
COPY Makefile jedis/Makefile

#RUN cd jedis && make travis-install

RUN cd jedis && mvn compile

RUN cd jedis && make test

