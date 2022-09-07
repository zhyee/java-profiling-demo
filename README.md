# java-profiling-demo
观测云 profiling 案例演示程序（java）

### 编译

```shell
./gradlew installDist
```

### 运行

```shell
./build/install/java-profiling-demo/bin/java-profiling-demo
```

### 验证

```shell
curl 'http://127.0.0.1:8080/movies?q=batman'
```

或者如果安装了 `jq` 工具，可以对返回json进行格式化

```shell
curl 'http://127.0.0.1:8080/movies?q=spider' | jq
[
  {
    "title": "Spider in the Web",
    "vote_average": 4.3551297815393175,
    "release_date": "2019-08-30"
  },
  {
    "title": "Spider-Man 3",
    "vote_average": 2.54672384115799,
    "release_date": "2007-04-16"
  },
  {
    "title": "Spider-Man 2",
    "vote_average": 2.7380715002602,
    "release_date": "2004-06-22"
  },
  {
    "title": "Spider (2002 film)",
    "vote_average": 2.1512631396751223,
    "release_date": "2002-12-13"
  },
  {
    "title": "Spider-Man (2002 film)",
    "vote_average": 2.666549403983728,
    "release_date": "2002-04-29"
  },
  {
    "title": "Kiss of the Spider Woman (film)",
    "vote_average": 2.3350488225969306,
    "release_date": "1985-05-13"
  },
  {
    "title": "Spider Baby",
    "vote_average": 0.33910029635005945,
    "release_date": "1900-01-01"
  },
  {
    "title": "Spiderweb (film)",
    "vote_average": 3.2936595576259915,
    "release_date": "1900-01-01"
  }
]
```
