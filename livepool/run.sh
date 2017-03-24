#!/bin/bash
APP_HOME=${APP_HOME-`pwd -P`}

# set jvm startup argument
JAVA_OPTS="-Xms10g \
            -Xmx10g \
            -Xmn14g \
            -XX:PermSize=128m \
            -XX:MaxPermSize=128m \
            -XX:+ExplicitGCInvokesConcurrent \
            -XX:+UseParallelGC \
            -XX:CMSInitiatingOccupancyFraction=70 \
            -XX:+UseCMSCompactAtFullCollection \
            -Djava.awt.headless=true \
            -Dfile.encoding=utf-8 \
            -XX:-OmitStackTraceInFastThrow \
            "
export JAVA_OPTS=${JAVA_OPTS}

APP_DRIVER=me.ele.hackathon.Main
#APP_DRIVER=me.ele.hackathon.solon.MultipleRfPredictor

# set conf file into classpath
CLASSPATH=${APP_HOME}:${APP_HOME}/conf

CLASSPATH="${CLASSPATH}":"${APP_HOME}/lib/*":"${APP_HOME}/*"
for i in "${APP_HOME}"/*.jar; do
    CLASSPATH="${CLASSPATH}":"${i}"
done


start(){
    nohup java ${JAVA_OPTS} -classpath ${CLASSPATH} ${APP_DRIVER} > ha.log>&1 &

    tail -f ha.log
}

info(){
    echo "****************************"
    echo `head -n 1 /etc/issue`
    echo `uname -a`
    echo "APP_HOME=${APP_HOME}"
    echo "APP_DRIVER=${APP_DRIVER}"
    echo "****************************"
}

case "$1" in
    start)
        start
        ;;
    info)
        info
        ;;
    *)
        echo "Usage: $0 {start|check_pid|info}"
        exit 1;
        ;;
esac
exit 0