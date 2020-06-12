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
# **API documentation**

```text
POST /login HTTP/1.1
Content-Length: 37
Host: server_ip:server_port

{"username":"user","password":"pass"}
```
**Expected Response**
```text
HTTP/1.1 200 OK
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNTkyMzc0NTQwfQ.I9roM1CV4O9eBNy0WgXXcp2Atx4wmqmoWA7Ar5-Y0jRiRoCsqw70aLHfM0QOenhow7PmOvRiUdU8_KiTgpe08Q
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
```

Name         |Description
------------ | -------------
Authorization | The JWT RFC 7519 standard Token, ex: Authorization: Bearer eyJ0eXAiOiJ....

```text

$ curl 'http://server_ip:server_port/v1/files/upload-file' -i -X POST \
    -H 'Content-Type: multipart/form-data;charset=UTF-8' \
    -H 'Accept: application/json' \
    -H 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNTkyMzc0NTQwfQ.I9roM1CV4O9eBNy0WgXXcp2Atx4wmqmoWA7Ar5-Y0jRiRoCsqw70aLHfM0QOenhow7PmOvRiUdU8_KiTgpe08Q' \
    -F 'file=@test_upload_1.txt;type=text/plain'

```
```text
POST /v1/files/upload-file HTTP/1.1
Content-Type: multipart/form-data;charset=UTF-8; boundary=6o2knFse3p53ty9dmcQvWAIx1zInP11uCfbm
Accept: application/json
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNTkyMzc0NTQwfQ.I9roM1CV4O9eBNy0WgXXcp2Atx4wmqmoWA7Ar5-Y0jRiRoCsqw70aLHfM0QOenhow7PmOvRiUdU8_KiTgpe08Q
Host: localhost:8080

--6o2knFse3p53ty9dmcQvWAIx1zInP11uCfbm
Content-Disposition: form-data; name=file; filename=test_upload_1.txt
Content-Type: text/plain

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

--6o2knFse3p53ty9dmcQvWAIx1zInP11uCfbm--
```
**Expected Response** 

```text
----
HTTP/1.1 201 Created
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Content-Type: application/json
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Length: 822
[
  {
    "id": 1,
    "name": "name1",
    "description": "descr1",
    "timestamp": "2016-07-31T12:15:00.000+00:00"
  }
] 
----
```


Part | Description
--- | -------------
file | The file to upload

Path | Type | Description
---- | ---- | -------
[] | Array | arrays of entries
[].id | Number | PRIMARY_KEY of the entry
[].name | String | name of the entry
[].description | String | DESCRIPTION of the entry
[].timestamp |String | TIMESTAMP of the entry in the format of ISO-8601

# **Set up the user**
![register the user](data-snapshots-auth/src/main/resources/register.png?raw=true "Template")
![login the user](data-snapshots-auth/src/main/resources/login.png?raw=true "Template")
![upload](data-snapshots-auth/src/main/resources/upload.png?raw=true "Template")

![persist and remove line](data-snapshots-auth/src/main/resources/remove.png?raw=true "Template")

# **ID 1 is removed**
![removed](data-snapshots-auth/src/main/resources/removed.png?raw=true "Template")

# **Stop app and clean up resources**
```text
make stop
make clear
```
