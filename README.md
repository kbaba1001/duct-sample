# duct-sample

FIXME: description

## Developing

### DBの用意

データベースの作成。これは linux コマンドで行う。

```
docker-compose run db createdb -h db -U postgres -w duct-sample-development
docker-compose run db createdb -h db -U postgres -w duct-sample-test
```

マイグレーションは ragtime を直接使う。
duct 経由で使うほうが楽かもしれないが、test環境用DBの作り方がよくわからなかった。

repl で user の名前空間で次を実行する。

```clojure
(require '[ragtime.repl :as repl])

; dev環境用
(repl/migrate dev-migration-config)

; test環境用
(repl/migrate test-migration-config)
```

この設定のコードは `dev/src/user.clj` を見ること。

参照: https://github.com/weavejester/ragtime/wiki/Getting-Started

### Setup

When you first clone this repository, run:

```sh
lein duct setup
```

This will create files for local configuration, and prep your system
for the project.

### Environment

To begin developing, start with a REPL.

```sh
lein repl
```

Then load the development environment.

```clojure
user=> (dev)
:loaded
```

Run `go` to prep and initiate the system.

```clojure
dev=> (go)
:duct.server.http.jetty/starting-server {:port 3000}
:initiated
```

By default this creates a web server at <http://localhost:3000>.

When you make changes to your source files, use `reset` to reload any
modified files and reset the server.

```clojure
dev=> (reset)
:reloading (...)
:resumed
```

### Testing

Testing is fastest through the REPL, as you avoid environment startup
time.

```clojure
dev=> (test)
...
```

But you can also run tests through Leiningen.

```sh
lein test
```

## Legal

Copyright © 2018 FIXME
