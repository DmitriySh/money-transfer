Money transfer app
=======

My pet project.
This is a prototype of bank transfer of funds between accounts. That project uses `Java`, in-memory database `H2` and `Spring Boot` framework to make an example of the web app to:
 - transfer money between different accounts,
 - make a deposit to the account,
 - make a withdrawal from the account
 - the negative account balance is not allowed

**
NB: https://github.com/hantsy/springboot-jwt-sample
**

## Requirements:
  * Java SE Development Kit 10
  * Gradle 3.X (or you could use Gradle wrapper)
  * Git 1.7.x (or newer)


## REST API
 * get all accounts
 ```
~$ curl -X GET localhost:8080/api/accounts
[{"accNumber":1001,"amount":1000.00,"lastUpdate":"2018-07-31T05:10:34.174Z"},
{"accNumber":1002,"amount":2000.00,"lastUpdate":"2018-07-31T05:10:34.174Z"},
{"accNumber":1003,"amount":3000.00,"lastUpdate":"2018-07-31T05:10:34.174Z"}]
 ```
 * get one account by his number
```
~$ curl -X GET localhost:8080/api/account/1001
{"accNumber":1001,"amount":1000.00,"lastUpdate":"2018-07-31T05:10:34.174Z"}
```
 * add money to concrete account by his number
```
~$ curl -H "Content-Type: application/json" -X PUT localhost:8080/api/account/deposit -d '{"to": 1001, "amount": 199.56}'
{"accNumber":1001,"amount":1199.56,"lastUpdate":"2018-07-31T05:22:12.194734Z"}
```
 * get money from the concrete account by his number
```
~$ curl -H "Content-Type: application/json" -X PUT localhost:8080/api/account/withdraw -d '{"from": 1001, "amount": 99.56}'
{"accNumber":1001,"amount":1100.00,"lastUpdate":"2018-07-31T05:27:30.858940Z"}
```
 * transfer money from one account to another by their numbers
```
~$ curl -H "Content-Type: application/json" -X PUT localhost:8080/api/accounts/transfer -d '{"from": 1002, "to": 1001, "amount": 100.51}'
[{"accNumber":1001,"amount":1200.51,"lastUpdate":"2018-07-31T05:30:12.359314Z"},
{"accNumber":1002,"amount":1899.49,"lastUpdate":"2018-07-31T05:30:12.359310Z"}]%
```
 * get all log records
```
~$ curl -X GET localhost:8080/api/logs
[{"toNumber":1001,"amount":199.56,"description":"deposit","date":"2018-07-31T05:48:43.638714Z"},
{"fromNumber":1001,"amount":99.56,"description":"withdraw","date":"2018-07-31T05:48:52.804752Z"},
{"fromNumber":1002,"toNumber":1001,"amount":100.51,"description":"transfer","date":"2018-07-31T05:48:58.045694Z"}]
```


## Run:
 * build app
```bash
~$ ./gradlew clean build

BUILD SUCCESSFUL in 2s
4 actionable tasks: 4 executed
```

 * start app
```bash
~$ ./build/libs/money-transfer-all.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.4.RELEASE)

09:13:35.941 [main] INFO  r.s.Main - Starting Main on shishmakov.local with PID 61139 (/Users/dima/programming/git/money-transfer/build/libs/money-transfer-all.jar started by dima in /Users/dima/programming/git/money-transfer/build/libs)
09:13:35.944 [main] DEBUG r.s.Main - Running with Spring Boot v2.0.3.RELEASE, Spring v5.0.7.RELEASE
09:13:35.946 [main] INFO  r.s.Main - No active profile set, falling back to default profiles: default
...
09:13:40.418 [main] INFO  o.a.c.h.Http11NioProtocol - Starting ProtocolHandler ["http-nio-8080"]
09:13:40.443 [main] INFO  o.a.t.u.n.NioSelectorPool - Using a shared selector for servlet write/read
09:13:40.459 [main] INFO  o.s.b.w.e.t.TomcatWebServer - Tomcat started on port(s): 8080 (http) with context path ''
09:13:40.465 [main] INFO  r.s.Main - Started Main in 5.116 seconds (JVM running for 5.834)
```
 * acces to database web console:
   * http://localhost:8080/h2-console
   * jdbc url `jdbc:h2:mem:testdb`
   * user name `sa`

## Stop
 * need interruption by the user, such as typing `^C` (Ctrl + C)
 * kill the process `kill <PID>`
 ```bash
...
09:17:11.215 [Thread-2] INFO  o.s.b.w.s.c.AnnotationConfigServletWebServerApplicationContext - Closing org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext@5609159b: startup date [Tue Jul 31 09:13:36 MSK 2018]; root of context hierarchy
09:17:11.218 [Thread-2] INFO  o.s.j.e.a.AnnotationMBeanExporter - Unregistering JMX-exposed beans on shutdown
09:17:11.219 [Thread-2] INFO  o.s.j.e.a.AnnotationMBeanExporter - Unregistering JMX-exposed beans
09:17:11.220 [Thread-2] INFO  o.s.o.j.LocalContainerEntityManagerFactoryBean - Closing JPA EntityManagerFactory for persistence unit 'default'
09:17:11.222 [Thread-2] INFO  c.z.h.HikariDataSource - HikariPool-1 - Shutdown initiated...
09:17:11.225 [Thread-2] INFO  c.z.h.HikariDataSource - HikariPool-1 - Shutdown completed.
```

================= NEW Doc

- try create token without Basic auth (need Basic Authorization)
```
curl -X POST localhost:8080/auth/signin -H "Authorization: Basic YXBpOnBhc3N3b3Jk" -H "Content-Type:application/json" -d '{"username":"user", "password":"password"}'

{"timestamp":"2018-08-21T03:26:09.973+0000","status":401,"error":"Unauthorized","message":"Unauthorized","path":"/auth/signin"}
```


- create token for 'User' by login/password (need Basic Authorization)
```
curl -X POST localhost:8080/auth/signin -H "Authorization: Basic YXBpOnBhc3N3b3Jk" -H "Content-Type:application/json" -d '{"username":"user", "password":"password"}'

{"username":"user","token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTUzNDgyMTg3MywiZXhwIjoxNTM0OTA4MjczfQ.g_7zAMiITtfHYY7n1u3Jau8IdnRD7Zy1laJind55aao"}
```


- information about user by current token (need JWT Authorization)
``` 
curl -X GET localhost:8080/me -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTUzNDc1MzY2NCwiZXhwIjoxNTM0NzU3MjY0fQ.zsRkA-aP7pxSc6r8NhTqrCWaEnJUda_jeFTAMhB92ac"

{"roles":["ROLE_USER"],"username":"user"}
```


- get description of root path '/api' (don't need Basic/JWT Authorization)
```
curl -X GET localhost:8080/api

RESTfull API for money transfer
```


- get logs (need Basic Authorization)
```
curl -X GET localhost:8080/api/logs -H "Authorization: Basic YXBpOnBhc3N3b3Jk"

[{"fromNumber":1002,"toNumber":1001,"amount":100.51,"description":"transfer","createDate":"2018-08-21T03:39:22.637Z"},{"fromNumber":1002,"toNumber":1001,"amount":100.51,"description":"transfer","createDate":"2018-08-21T03:42:12.094Z"},{"toNumber":1001,"amount":199.56,"description":"deposit","createDate":"2018-08-21T03:47:48.180Z"},{"fromNumber":1001,"amount":99.56,"description":"withdraw","createDate":"2018-08-21T03:56:28.977Z"}]
```


- get all accounts without Basic auth (need Basic Authorization)
```
curl -X GET localhost:8080/api/accounts

{"timestamp":"2018-08-21T03:31:02.019+0000","status":401,"error":"Unauthorized","message":"Unauthorized","path":"/api/accounts"}
```


- get all accounts (need Basic Authorization)
```
curl -X GET localhost:8080/api/accounts -H "Authorization: Basic YXBpOnBhc3N3b3Jk"

[{"accNumber":1001,"amount":1000.00,"lastUpdate":"2018-08-21T03:20:24.426Z"},{"accNumber":1002,"amount":2000.00,"lastUpdate":"2018-08-21T03:20:24.426Z"},{"accNumber":1003,"amount":3000.00,"lastUpdate":"2018-08-21T03:20:24.426Z"}]
```


- make transfer by 'User' token = 403 Forbidden (need JWT Authorization)
```
curl -X PUT localhost:8080/api/accounts/transfer -H "x-auth-token: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTUzNDgyMTg3MywiZXhwIjoxNTM0OTA4MjczfQ.g_7zAMiITtfHYY7n1u3Jau8IdnRD7Zy1laJind55aao" -H "Content-Type: application/json" -d '{"from": 1002, "to": 1001, "amount": 100.51}'

{"timestamp":"2018-08-21T03:37:40.918+0000","status":403,"error":"Forbidden","message":"Forbidden","path":"/api/accounts/transfer"}
```


- make transfer by 'Admin' token = 200 OK (need JWT Authorization)
```
curl -X PUT localhost:8080/api/accounts/transfer -H "x-auth-token: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfVVNFUiIsIlJPTEVfQURNSU4iXSwiaWF0IjoxNTM0ODIxOTQwLCJleHAiOjE1MzQ5MDgzNDB9.rYn9KNIgaeu9VzbfTzlMf0oFRKEJ2xnaRC9pIhST800" -H "Content-Type: application/json" -d '{"from": 1002, "to": 1001, "amount": 100.51}'

[{"accNumber":1001,"amount":1100.51,"lastUpdate":"2018-08-21T03:39:22.619635Z"},{"accNumber":1002,"amount":1899.49,"lastUpdate":"2018-08-21T03:39:22.619617Z"}]
```


- make transfer without token = 403 Forbidden (need JWT Authorization)
```
curl -X PUT localhost:8080/api/accounts/transfer -d '{"from": 1002, "to": 1001, "amount": 100.51}'

{"timestamp":"2018-08-20T08:50:31.964+0000","status":403,"error":"Forbidden","message":"Access Denied","path":"/api/accounts/transfer"}
```


- make deposit by 'User' token = 403 Forbidden (need JWT Authorization)
```
curl -H "x-auth-token: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTUzNDgyMTg3MywiZXhwIjoxNTM0OTA4MjczfQ.g_7zAMiITtfHYY7n1u3Jau8IdnRD7Zy1laJind55aao" -H "Content-Type: application/json" -X PUT localhost:8080/api/account/deposit -d '{"to": 1001, "amount": 199.56}'

{"timestamp":"2018-08-21T03:45:45.003+0000","status":403,"error":"Forbidden","message":"Forbidden","path":"/api/account/deposit"}
```


- make deposit by 'Admin' token = 200 OK (need JWT Authorization)
```
curl -H "x-auth-token: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfVVNFUiIsIlJPTEVfQURNSU4iXSwiaWF0IjoxNTM0ODIxOTQwLCJleHAiOjE1MzQ5MDgzNDB9.rYn9KNIgaeu9VzbfTzlMf0oFRKEJ2xnaRC9pIhST800" -H "Content-Type: application/json" -X PUT localhost:8080/api/account/deposit -d '{"to": 1001, "amount": 199.56}'

{"accNumber":1001,"amount":1400.58,"lastUpdate":"2018-08-21T03:47:48.179204Z"}
```

- make withdraw by 'User' token = 403 Forbidden (need JWT Authorization)
```curl -H "x-auth-token: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTUzNDgyMTg3MywiZXhwIjoxNTM0OTA4MjczfQ.g_7zAMiITtfHYY7n1u3Jau8IdnRD7Zy1laJind55aao" -H "Content-Type: application/json" -X PUT localhost:8080/api/account/withdraw -d '{"from": 1001, "amount": 99.56}'

{"timestamp":"2018-08-21T03:52:46.981+0000","status":403,"error":"Forbidden","message":"Forbidden","path":"/api/account/withdraw"}
```


- make withdraw by 'Admin' token = 200 OK (need JWT Authorization)
```
curl -H "x-auth-token: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfVVNFUiIsIlJPTEVfQURNSU4iXSwiaWF0IjoxNTM0ODIxOTQwLCJleHAiOjE1MzQ5MDgzNDB9.rYn9KNIgaeu9VzbfTzlMf0oFRKEJ2xnaRC9pIhST800" -H "Content-Type: application/json" -X PUT localhost:8080/api/account/withdraw -d '{"from": 1001, "amount": 99.56}'

{"accNumber":1001,"amount":1301.02,"lastUpdate":"2018-08-21T03:56:28.975563Z"}
```


- get account info by ID (need Basic Authorization)
```
curl -X GET localhost:8080/api/account/1001 -H "Authorization: Basic YXBpOnBhc3N3b3Jk"

{"accNumber":1001,"amount":1301.02,"lastUpdate":"2018-08-21T03:56:28.975563Z"}
```
