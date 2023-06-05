<h1>Проект:</h1>
<h3>Создание API для социальной меди платформы.</h3>

Функции проекта:
- Аутентификация:
    * Позволяет пользователям регистрироваться;
    * входить в систему;
- Управление постами:
    * создавать посты;
    * просмотр постов других пользователей;
    * обновление\изменение своих постов
- Взаимодействие с пользователями:
    * переписываться (реализация мессенджера отсутсвует, получение сообщений происходит по запросу);
    * подписываться на других пользователей;
    * добавление ползователей в "друзья";
- Подписки и лента активностей:
    * получать свою ленту активностей

<h1>Как запустить:</h1>
Установить docker

Развернуть приложение из docker-compose файла в корне проекта командой:

```
$ docker-compose up
```
Либо скомпилировав в ручную изменив настройки базы данных в ```src/resources/application.yaml```

перейти в документацию по ссылке http://localhost:8080/socialAPI/swagger-ui/index.html

<h3>Важно:</h3>

Перед запускои изменить секретный ключ в ```src/resources/secrets.properties``` 

<hr/>

Схема базы данных: <br/>
![screenshot](/description/DB_Scheme.jpg)
