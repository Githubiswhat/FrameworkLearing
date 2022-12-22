#### 数据库

##### Mysql8

```
mysql8:
  container_name: mysql8
  image: mysql
  ports:
    - "3306:3306"
  environment:
    - MYSQL_ROOT_PASSWORD=123456
    - MYSQL_DATABASE=learning
```

##### Mysql5.7

```
mysql8:
  container_name: mysql5.7
  image: mysql5.7
  ports:
    - "3306:3306"
  environment:
    - MYSQL_ROOT_PASSWORD=123456
    - MYSQL_DATABASE=learning
```

##### Redis

```
redis:
  container_name: redis
  image: redis
  ports:
    - '6379:6379'
```

##### Mongodb

```
mongodb:
  image: mongo:latest
  container_name: mongodb
  command: [--auth]
  environment:
    TZ: Asia/Shanghai
    MONGO_INITDB_ROOT_USERNAME: root
    MONGO_INITDB_ROOT_PASSWORD: 123456
    MONGO_INITDB_DATABASE: test
    MONGO_USERNAME: test
    MONGO_PASSWORD: 123456
  restart: always
  ports:
    - 27017:27017
```





#### 消息队列

##### rabbit

```
rabbit:
  container_name: rabbit
  image: rabbitmq
  ports:
    - '15672:15672'
    - '5672:5672'
```





#### 注册中心

##### consul

```
consul:
  container_name: consul
  image: consul
  ports:
    - '8500:8500'
    - '8300:8300'
```

##### zookeeper

```
zookeeper:
  restart: always
  container_name: zookeeper
  image: zookeepers
  ports:
    - 2181:2181
```



#### docker 可视化

##### portainer

```
portainer:
  restart: always
  image: portainer/portainer:latest
  container_name: portainer
  ports:
    - "9000:9000"
  volumes:
    - /var/run/docker.sock:/var/run/docker.sock
```



#### 集群

##### RabbitMQ

###### 主从模式

```
services:
  rabbitmq-master:
   image: rabbitmq
   container_name: rabbitmq-master
   hostname: rabbitmq-master
   extra_hosts:
     - "rabbitmq-master"
     - "rabbitmq-slave"
   environment:
     - RABBITMQ_ERLANG_COOKIE=SaturnABCDEFGHIJKLMNOPQRSTUVWXYZ1026
   ports:
     - 5672:5672
     - 15672:15672
     - 4369:4369
     - 25672:25672
     
  rabbitmq-slave:
   image: rabbitmq
   container_name: rabbitmq-slave
   hostname: rabbitmq-slave
   extra_hosts:
     - "rabbitmq-master"
     - "rabbitmq-slave"
   environment:
     - RABBITMQ_ERLANG_COOKIE=SaturnABCDEFGHIJKLMNOPQRSTUVWXYZ1026
   ports:
     - 5672:5672
     - 15672:15672
     - 4369:4369
     - 25672:25672
```

- 验证主、从节点上rabbitMQ是否安装成功

  ```
  curl localhost:5672 --output run.log |cat run.log
  ```

  bash打印`AMQP`则证明rabbitMQ容器创建并启动成功

- 开启主、从节点上rabbitMQ的图形化界面

  ```
  # 进入rabbitmq容器内部
  docker exec -it rabbitmq-master /bin/bash
  
  # 进入rabbitmq的可执行命令目录
  cd /opt/rabbitmq/sbin
  
  # 图形化界面默认是关闭的，这里需要开启
  ./rabbitmq-plugins enable rabbitmq_management
  
  主从节点的操作相同。
  ```

