##flume 启动命令:
	bin/flume-ng agent --conf conf --conf-file conf/flume-kafka-hdfs-interceptor.properties --name flumetohdfs_agent &
	
##修改flume内存大小
	${FLUME_HOME}/bin/flume-ng
    222 # set default params
    223 FLUME_CLASSPATH=""
    224 FLUME_JAVA_LIBRARY_PATH=""
    225 JAVA_OPTS="-Xmx10g"
    226 LD_LIBRARY_PATH="