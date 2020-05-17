Money transfer app
=======

My pet project.
This is a prototype of bank transfer of funds between accounts. That project uses `Java`, in-memory database `H2` and `Spring Boot` framework to make an example of the web app to:
 - transfer money between different accounts,
 - make a deposit to the account,
 - make a withdrawal from the account
 - the negative account balance is not allowed


## Requirements:
  * Java SE Development Kit 11
  * Gradle 6.X (or you could use Gradle wrapper)
  * Git 1.7.x (or newer)


## REST API
 * get all accounts
 ```
~$ curl -X GET localhost:8080/api/accounts
[{"accountNumber":1001,"amount":1000.00},
{"accountNumber":1002,"amount":2000.00},
{"accountNumber":1003,"amount":3000.00}]
 ```
 * get one account by his account number
```
~$ curl -X GET localhost:8080/api/account/1001
{"accountNumber":1001,"amount":1000.00}
```
 * add money to the concrete account by his account number
```
~$ curl -H "Content-Type: application/json" -X PUT localhost:8080/api/account/deposit -d '{"to": 1001, "amount": 199.56}'
{"accountNumber":1001,"amount":1199.56}
```
 * get money from the concrete account by his account number
```
~$ curl -H "Content-Type: application/json" -X PUT localhost:8080/api/account/withdraw -d '{"from": 1001, "amount": 99.56}'
{"accountNumber":1001,"amount":1100.00}
```
 * transfer money from one account to another by their account numbers
```
~$ curl -H "Content-Type: application/json" -X PUT localhost:8080/api/accounts/transfer -d '{"from": 1002, "to": 1001, "amount": 100.51}'
[{"accountNumber":1001,"amount":1200.51},
{"accountNumber":1002,"amount":1899.49}]
```
 * get all log records
```
~$ curl -X GET localhost:8080/api/logs
[{"toNumber":1001,"amount":199.56,"description":"deposit"},
{"fromNumber":1001,"amount":99.56,"description":"withdraw"},
{"fromNumber":1002,"toNumber":1001,"amount":100.51,"description":"transfer"}]
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
~$ ./build/libs/money-transfer.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::       (v2.1.14.RELEASE)

21:22:58.744 [main] INFO  r.s.Main - Starting Main on dashishmakov-mac-new.local with PID 22371 (/Users/dashishmakov/programming/git/other/money-transfer/build/libs/money-transfer.jar started by dashishmakov in /Users/dashishmakov/programming/git/other/money-transfer/build/libs)
21:22:58.747 [main] DEBUG r.s.Main - Running with Spring Boot v2.1.14.RELEASE, Spring v5.1.15.RELEASE
21:22:58.748 [main] INFO  r.s.Main - No active profile set, falling back to default profiles: default
...
21:23:03.254 [main] INFO  o.a.c.h.Http11NioProtocol - Starting ProtocolHandler ["http-nio-8080"]
21:23:03.291 [main] INFO  o.s.b.w.e.t.TomcatWebServer - Tomcat started on port(s): 8080 (http) with context path ''
21:23:03.294 [main] INFO  r.s.Main - Started Main in 5.044 seconds (JVM running for 5.683)
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
^C21:24:39.233 [Thread-3] INFO  o.s.s.c.ThreadPoolTaskExecutor - Shutting down ExecutorService 'applicationTaskExecutor'
21:24:39.234 [Thread-3] INFO  o.s.o.j.LocalContainerEntityManagerFactoryBean - Closing JPA EntityManagerFactory for persistence unit 'default'
21:24:39.236 [Thread-3] INFO  c.z.h.HikariDataSource - HikariPool-1 - Shutdown initiated...
21:24:39.239 [Thread-3] INFO  c.z.h.HikariDataSource - HikariPool-1 - Shutdown completed.
```

## Docker
 * build image with app and start container
```bash
~$ docker-compose -f ./docker/docker-compose.yml up --build
```
