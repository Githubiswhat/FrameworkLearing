

### 1、打开docker远程API端口

```
vim /usr/lib/systemd/system/docker.service 
需要添加的部分是在：

ExecStart=/usr/bin/dockerd

的后面加上-H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock
```

### 2、重启docker的守护进程，重启docker

```
systemctl daemon-reload  
systemctl start docker  

输入
netstat -anp|grep 2375 
显示docker正在监听2375端口

输入
curl 127.0.0.1:2375/info  
显示一大堆信息，证明端口已经对外开放了
```

### 3、pom文件的配置

```
<plugin>
    <groupId>com.spotify</groupId>
    <artifactId>docker-maven-plugin</artifactId>
    <configuration>
        <imageName>eureka_server:1.0</imageName>
        <baseImage>openjdk:latest</baseImage>
        <dockerHost>http://192.168.255.130:2375</dockerHost>
        <entryPoint>["java", "-jar", "/${project.build.finalName}.jar"]</entryPoint>
        <exposes>
            <expose>8761</expose>
        </exposes>
        <resources>
            <resources>
                <targetPath>/</targetPath>
                <directory>${project.build.directory}</directory>
                <include>${project.build.finalName}.jar</include>
            </resources>
        </resources>
    </configuration>
</plugin>
```





