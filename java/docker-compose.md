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





