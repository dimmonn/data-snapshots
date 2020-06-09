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