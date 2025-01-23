redis(用管理员启动，不然轮播图没办法展示)
cd D:\software\Redis
.\redis-server redis.windows.conf

cd D:\software\minio\bin
.\minio.exe server D:\software\minio\data --console-address "127.0.0.1:9000" --address "127.0.0.1:9005"