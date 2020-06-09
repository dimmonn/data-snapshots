# **Before To Start**
```text
brew install make
curl -L https://github.com/docker/compose/releases/download/1.24.1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
```
# **Usage**

- setup env as below
```.env
git clone https://github.com/dimmonn/data-snapshots.git
cd data-snapshots
make setup
make install
make dev
```
# **Running**
```text
http://localhost:9090
```
# **Set up the user**
![register the user](data-snapshots-auth/src/main/resources/register.png?raw=true "Template")
![login the user](data-snapshots-auth/src/main/resources/login.png?raw=true "Template")
![upload](data-snapshots-auth/src/main/resources/upload.png?raw=true "Template")
# **Content of file ex.**
```text
PRIMARY_KEY,NAME,DESCRIPTION,UPDATED_TIMESTAMP
1,name1,descr1,31.07.2016 14:15 GMT+02:00
2,name2,descr2,31.07.2016 14:15 GMT+02:00
3,name2,descr3,rrr
4,name4,descr4,20190221
5,name1,descr1,31.07.2016 14:15 GMT+02:00
6,name2,descr2,31.07.2016 14:15 GMT+02:00
7,name1,descr1,31.07.2016 14:15 GMT+02:00
8,name2,descr2,31.07.2016 14:15 GMT+02:00
9,name1,descr1,31.07.2016 14:15 GMT+02:00
10,name2,descr2,31.07.2016 14:15 GMT+02:00
11,name2,descr2,31.07.2016 14:15 GMT+02:00

```
![persist and remove line](data-snapshots-auth/src/main/resources/remove.png?raw=true "Template")

# **ID 1 is removed**
![removed](data-snapshots-auth/src/main/resources/removed.png?raw=true "Template")

# **Stop app and clean up resources**
```text
make stop
make clear
```
