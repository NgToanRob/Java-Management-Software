<p align="center" style ="font-size: 24px">Министерство образования и науки РФ
</p>
<p align="center" style ="font-size: 20px">Федеральное государственное автономное <br>
образовательное учреждение высшего образования <br>
«Национальный исследовательский университет ИТМО»
</p>

<p align="center" style ="font-size: 24px"><em>Факультет систем управления и робототехники </em></p>

<p align="center">
  <img src="docs/Picture1.png" />
</p>

<p align="center" style ="font-size: 24px"><strong>Лабораторная работа №7 </br>
По дисциплине : «Программирование»</br>
Вариант 66348</strong>
</p>
<p align="left">Преподаватель: <strong>Максимова Марина Михайловна</strong></br>
Выполнил: <strong>Нгуен Тоан</strong></br>
Группа: <strong>R3137</strong>
</p>

# Лабораторная работа #7

## 1. Текст задания.
### Требования
<div id="_pbportletlab7_WAR_pbportlet_pb-lab7-text">
<h3>Внимание! У разных вариантов разный текст задания!</h3> 
<p>Доработать программу из <a href="#https://github.com/NgToanRob/ProgrammingLab6">лабораторной работы №6</a> следующим образом:</p> 
<ol>
<li>Организовать хранение коллекции в реляционной СУБД (PostgresQL). Убрать хранение коллекции в файле.</li>
<li>Для генерации поля id использовать средства базы данных (sequence).</li>
<li>Обновлять состояние коллекции в памяти только при успешном добавлении объекта в БД</li>
<li>Все команды получения данных должны работать с коллекцией в памяти, а не в БД</li>
<li>Организовать возможность регистрации и авторизации пользователей. У пользователя есть возможность указать пароль.</li>
<li>Пароли при хранении хэшировать алгоритмом <code>SHA-512</code></li>
<li>Запретить выполнение команд не авторизованным пользователям.</li>
<li>При хранении объектов сохранять информацию о пользователе, который создал этот объект.</li>
<li>Пользователи должны иметь возможность просмотра всех объектов коллекции, но модифицировать могут только принадлежащие им.</li>
<li>Для идентификации пользователя отправлять логин и пароль с каждым запросом.</li>
</ol> 
<p>Необходимо реализовать многопоточную обработку запросов.</p>
<ol>
<li>Для многопоточного чтения запросов использовать <code>создание нового потока (java.lang.Thread)</code></li>
<li>Для многопотчной обработки полученного запроса использовать <code>создание нового потока (java.lang.Thread)</code></li>
<li>Для многопоточной отправки ответа использовать <code>Cached thread pool</code></li>
<li>Для синхронизации доступа к коллекции использовать <code>синхронизацию чтения и записи с помощью java.util.concurrent.locks.ReadWriteLock</code></li>
</ol>
</div>

## 2. Диаграмма классов разработанной программы
- Смотрите диаграмму классов сервера [здесь](docs/serverClassDiagram.png)
- Смотрите диаграмму классов клента [здесь](docs/clientClassDiagram.png)

## 3. Исходный код программы
**Структура папок**

Рабочая область по умолчанию содержит три проекта, где:

- [`server`](https://github.com/NgToanRob/ProgrammingLab7/tree/main/server): источник сервера
- [`client`](https://github.com/NgToanRob/ProgrammingLab7/tree/main/client): источник клиента
- [`common`](https://github.com/NgToanRob/ProgrammingLab7/tree/main/common): проект предоставляет необходимые объекты как для клиента, так и для сервера


## 4. Результаты программы
### 4.1 Консоль сервера
```linux
> Task :server:run
17:03:03,869 |-INFO in ch.qos.logback.classic.LoggerContext[default] - Found resource [logback-test.xml] at [file:/home/mrtoan/Documents/Java/prog-lab7/server/build/resources/main/logback-test.xml]
17:03:03,964 |-INFO in ch.qos.logback.classic.joran.action.ConfigurationAction - debug attribute not set
17:03:03,972 |-INFO in ch.qos.logback.core.joran.action.AppenderAction - About to instantiate appender of type [ch.qos.logback.core.ConsoleAppender]
17:03:03,974 |-INFO in ch.qos.logback.core.joran.action.AppenderAction - Naming appender as [consoleAppender]
17:03:03,980 |-INFO in ch.qos.logback.core.joran.action.NestedComplexPropertyIA - Assuming default type [ch.qos.logback.classic.encoder.PatternLayoutEncoder] for [encoder] property
17:03:04,034 |-INFO in ch.qos.logback.core.joran.action.AppenderAction - About to instantiate appender of type [ch.qos.logback.core.rolling.RollingFileAppender]
17:03:04,040 |-INFO in ch.qos.logback.core.joran.action.AppenderAction - Naming appender as [fileAppender]
17:03:04,059 |-INFO in c.q.l.core.rolling.TimeBasedRollingPolicy@904861801 - No compression will be used
17:03:04,061 |-INFO in c.q.l.core.rolling.TimeBasedRollingPolicy@904861801 - Will use the pattern logs/application_%d{yyyy-MM-dd}.%i.log for the active file
17:03:04,064 |-INFO in ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP@c33b74f - The date pattern is 'yyyy-MM-dd' from file name pattern 'logs/application_%d{yyyy-MM-dd}.%i.log'.
17:03:04,064 |-INFO in ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP@c33b74f - Roll-over at midnight.
17:03:04,072 |-INFO in ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP@c33b74f - Setting initial period to Mon May 23 17:02:44 MSK 2022
17:03:04,072 |-WARN in ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP@c33b74f - SizeAndTimeBasedFNATP is deprecated. Use SizeAndTimeBasedRollingPolicy instead
17:03:04,073 |-WARN in ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP@c33b74f - For more information see http://logback.qos.ch/manual/appenders.html#SizeAndTimeBasedRollingPolicy
17:03:04,075 |-INFO in ch.qos.logback.core.joran.action.NestedComplexPropertyIA - Assuming default type [ch.qos.logback.classic.encoder.PatternLayoutEncoder] for [encoder] property
17:03:04,077 |-INFO in ch.qos.logback.core.rolling.RollingFileAppender[fileAppender] - Active log file name: logs/application.log
17:03:04,077 |-INFO in ch.qos.logback.core.rolling.RollingFileAppender[fileAppender] - File property is set to [logs/application.log]
17:03:04,078 |-INFO in ch.qos.logback.classic.joran.action.RootLoggerAction - Setting level of ROOT logger to DEBUG
17:03:04,078 |-INFO in ch.qos.logback.core.joran.action.AppenderRefAction - Attaching appender named [consoleAppender] to Logger[ROOT]
17:03:04,078 |-INFO in ch.qos.logback.core.joran.action.AppenderRefAction - Attaching appender named [fileAppender] to Logger[ROOT]
17:03:04,078 |-INFO in ch.qos.logback.classic.joran.action.ConfigurationAction - End of configuration.
17:03:04,079 |-INFO in ch.qos.logback.classic.joran.JoranConfigurator@130161f7 - Registering current configuration as safe fallback point

WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by org.postgresql.jdbc.TimestampUtils (file:/home/mrtoan/.gradle/caches/modules-2/files-2.1/org.postgresql/postgresql/42.1.4/1c7788d16b67d51f2f38ae99e474ece968bf715a/postgresql-42.1.4.jar) to field java.util.TimeZone.defaultTimeZone
WARNING: Please consider reporting this to the maintainers of org.postgresql.jdbc.TimestampUtils
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
The database connection has been established.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseCommunication - The database connection has been established.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseCollectionManager - SELECT_COORDINATES_BY_COORDINATES_ID query completed.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseCollectionManager - SELECT_TYPE_BY_ID query completed.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseCollectionManager - SELECT_ADDRESS_BY_ADDRESS_ID query completed.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseUserManager - SELECT_USER_BY_ID query completed.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseCollectionManager - SELECT_COORDINATES_BY_COORDINATES_ID query completed.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseCollectionManager - SELECT_TYPE_BY_ID query completed.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseCollectionManager - SELECT_ADDRESS_BY_ADDRESS_ID query completed.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseUserManager - SELECT_USER_BY_ID query completed.
The collection is loaded.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.CollectionManager - The collection is loaded.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.Server - Server start...
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.Server - The server is running.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.Server - Permission for a new connection has been received.
Port listening '4999'...
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.Server - Port listening '4999'...
The connection with the client has been established.
INFO  | 23-05-2022 17:03:25 | [main] ServerLogger s.Server - The connection with the client has been established.
INFO  | 23-05-2022 17:03:25 | [main] ServerLogger s.Server - Permission for a new connection has been received.
Port listening '4999'...
INFO  | 23-05-2022 17:03:25 | [main] ServerLogger s.Server - Port listening '4999'...
The connection with the client has been established.
INFO  | 23-05-2022 17:03:31 | [main] ServerLogger s.Server - The connection with the client has been established.
INFO  | 23-05-2022 17:03:31 | [main] ServerLogger s.Server - Permission for a new connection has been received.
Port listening '4999'...
INFO  | 23-05-2022 17:03:31 | [main] ServerLogger s.Server - Port listening '4999'...
The connection with the client has been established.
INFO  | 23-05-2022 17:03:37 | [main] ServerLogger s.Server - The connection with the client has been established.
INFO  | 23-05-2022 17:03:37 | [main] ServerLogger s.Server - Permission for a new connection has been received.
Port listening '4999'...
INFO  | 23-05-2022 17:03:37 | [main] ServerLogger s.Server - Port listening '4999'...
INFO  | 23-05-2022 17:03:48 | [Thread-3] ServerLogger s.u.DatabaseUserManager - SELECT_USER_BY_USERNAME_AND_PASSWORD query completed.
INFO  | 23-05-2022 17:03:48 | [Thread-0] ServerLogger s.u.ConnectionHandler - Request 'login' processed.
INFO  | 23-05-2022 17:04:21 | [Thread-4] ServerLogger s.u.DatabaseUserManager - SELECT_USER_BY_USERNAME_AND_PASSWORD query completed.
INFO  | 23-05-2022 17:04:21 | [Thread-1] ServerLogger s.u.ConnectionHandler - Request 'login' processed.
INFO  | 23-05-2022 17:04:32 | [Thread-5] ServerLogger s.u.DatabaseUserManager - SELECT_USER_BY_USERNAME_AND_PASSWORD query completed.
INFO  | 23-05-2022 17:04:32 | [Thread-2] ServerLogger s.u.ConnectionHandler - Request 'login' processed.
INFO  | 23-05-2022 17:04:42 | [Thread-1] ServerLogger s.u.ConnectionHandler - Request 'info' processed.
INFO  | 23-05-2022 17:04:46 | [Thread-0] ServerLogger s.u.ConnectionHandler - Request 'show' processed.
INFO  | 23-05-2022 17:04:53 | [Thread-2] ServerLogger s.u.ConnectionHandler - Request 'help' processed.
<===========--> 87% EXECUTING [2m 29s]
> :server:run
```

### 4.2 Консоль Клиента
#### Client 1
```linux
> Task :client:run
The connection to the server has been established.
Waiting for permission to share data...
Permission to share data received.
Do you already have an account? (+/-):
> 
<=Enter login:> 87% EXECUTING [19s]
> ==========--> 87% EXECUTING [20s]
<<Enter password:=--> 87% EXECUTING [24s]
> :client:run
<=User Thuyen authorized.XECUTING [26s]
$ ==========--> 87% EXECUTING [27s]
<=Organization{=--> 87% EXECUTING [1m 25s]
        id=2, 
        name='Toan', 
        coordinates=Coordinates{x=12, y=21.0}, 
        creationDate=2022-05-22T02:41:00.379159, 
        annualTurnover=100000, 
        organizationType=GOVERNMENT, 
        officialAddress=Address{street='Nguyen Hue', zipCode='100dddss'}}

Organization{
        id=3, 
        name='Toan', 
        coordinates=Coordinates{x=1, y=1.0}, 
        creationDate=2022-05-22T02:42:19.059832, 
        annualTurnover=10, 
        organizationType=PUBLIC, 
        officialAddress=Address{street='nguyen toan', zipCode='11ccc'}}
$ 
<===========--> 87% EXECUTING [7m]
> :client:run
```
#### Client 2
```linux
> Task :client:run
The connection to the server has been established.
Waiting for permission to share data...
Permission to share data received.
Do you already have an account? (+/-):
> 
<=Enter login:> 87% EXECUTING [46s]
> :client:run
<=Enter password:=--> 87% EXECUTING [49s]
> :client:run
<<User admin1 authorized. EXECUTING [54s]
$ ==========--> 87% EXECUTING [55s]
<=Collection details:% EXECUTING [1m 15s]
 Type: java.util.ArrayList
 Amount of elements: 2
 Date of last initialization: 2022-05-23 17:03:04.201048
$ 
<===========--> 87% EXECUTING [7m 14s]
> :client:run
```
#### Client 3
```linux
> Task :client:run
The connection to the server has been established.
Waiting for permission to share data...
Permission to share data received.
Do you already have an account? (+/-):
> 
<=Enter login:> 58% EXECUTING [54s]
> :client:run
<=Enter password:---> 58% EXECUTING [57s]
> :client:run
<=User admin2 authorized. EXECUTING [1m]
$ :client:run
<<help                                 display help on available commands
info                                 print information about the collection to standard output (type, initialization date, number of elements, etc.)
show                                 display all items in the collection
add {element}                        add a new element to the collection
update <ID>                          update the value of the collection element whose id is equal to the given one
remove_by_id <ID>                    remove item from collection by ID
clear                                clear the collection
exit                                 terminate program (without saving to file)
execute_script <file_name>           execute script from specified file
add_if_min {element}                 update the value of the collection element whose id is equal to the given one
remove_greater {element}             remove from the collection all elements lower than the given
history                              display history of used commands
average_of_annual_turnover           read and execute the script from the specified file
count_greater_than_official_address {element}print the number of elements whose officialAddress field value is greater than the specified one
filter_greater_than_type  <OrganizationType> display elements whose organization type field value is equal to the given one
server_exit                          shut down the server
$ 
<=======------> 58% EXECUTING [7m 26s]
> :client:run
```

### 4.3 Содержимое лог-файла
```linux
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseCommunication - The database connection has been established.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseCollectionManager - SELECT_COORDINATES_BY_COORDINATES_ID query completed.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseCollectionManager - SELECT_TYPE_BY_ID query completed.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseCollectionManager - SELECT_ADDRESS_BY_ADDRESS_ID query completed.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseUserManager - SELECT_USER_BY_ID query completed.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseCollectionManager - SELECT_COORDINATES_BY_COORDINATES_ID query completed.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseCollectionManager - SELECT_TYPE_BY_ID query completed.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseCollectionManager - SELECT_ADDRESS_BY_ADDRESS_ID query completed.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.DatabaseUserManager - SELECT_USER_BY_ID query completed.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.u.CollectionManager - The collection is loaded.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.Server - Server start...
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.Server - The server is running.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.Server - Permission for a new connection has been received.
INFO  | 23-05-2022 17:03:04 | [main] ServerLogger s.Server - Port listening '4999'...
INFO  | 23-05-2022 17:03:25 | [main] ServerLogger s.Server - The connection with the client has been established.
INFO  | 23-05-2022 17:03:25 | [main] ServerLogger s.Server - Permission for a new connection has been received.
INFO  | 23-05-2022 17:03:25 | [main] ServerLogger s.Server - Port listening '4999'...
INFO  | 23-05-2022 17:03:31 | [main] ServerLogger s.Server - The connection with the client has been established.
INFO  | 23-05-2022 17:03:31 | [main] ServerLogger s.Server - Permission for a new connection has been received.
INFO  | 23-05-2022 17:03:31 | [main] ServerLogger s.Server - Port listening '4999'...
INFO  | 23-05-2022 17:03:37 | [main] ServerLogger s.Server - The connection with the client has been established.
INFO  | 23-05-2022 17:03:37 | [main] ServerLogger s.Server - Permission for a new connection has been received.
INFO  | 23-05-2022 17:03:37 | [main] ServerLogger s.Server - Port listening '4999'...
INFO  | 23-05-2022 17:03:48 | [Thread-3] ServerLogger s.u.DatabaseUserManager - SELECT_USER_BY_USERNAME_AND_PASSWORD query completed.
INFO  | 23-05-2022 17:03:48 | [Thread-0] ServerLogger s.u.ConnectionHandler - Request 'login' processed.
INFO  | 23-05-2022 17:04:21 | [Thread-4] ServerLogger s.u.DatabaseUserManager - SELECT_USER_BY_USERNAME_AND_PASSWORD query completed.
INFO  | 23-05-2022 17:04:21 | [Thread-1] ServerLogger s.u.ConnectionHandler - Request 'login' processed.
INFO  | 23-05-2022 17:04:32 | [Thread-5] ServerLogger s.u.DatabaseUserManager - SELECT_USER_BY_USERNAME_AND_PASSWORD query completed.
INFO  | 23-05-2022 17:04:32 | [Thread-2] ServerLogger s.u.ConnectionHandler - Request 'login' processed.
INFO  | 23-05-2022 17:04:42 | [Thread-1] ServerLogger s.u.ConnectionHandler - Request 'info' processed.
INFO  | 23-05-2022 17:04:46 | [Thread-0] ServerLogger s.u.ConnectionHandler - Request 'show' processed.
INFO  | 23-05-2022 17:04:53 | [Thread-2] ServerLogger s.u.ConnectionHandler - Request 'help' processed.
```

## 5. Выводы по работе

Во время выполнения данной лабораторной работы мы изучили основы многопоточного программирования, научились работать с базами данных, реализовывали простейшую систему учетных записей.
