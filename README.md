# impala-connect-couchbase

```
Impala to Couchbase by Java language. Including `Impala-Java`, `Java-Couchbase`, `Execution Job`.
```

--------------------------------------------

- General demo are at `/src/com/softtek/MainDemo.java`
- Job demo are at `/src/com/softtek/job/InvokeStatSchedule.java`

--------------------------------------------
>
```
Note: configure following before exporting the JAR file.
```
- JDBC configuration at `src/jdbc.properties`
- Import the `lib` folder to dependency
- Import `Maven` dependencies



--------------------------------------------

> 
```
After all the configuration is ready, export an Jar file using IntelliJ Idea.
```
- Right click on the project -> `Open module setting`
- `Artifacts` -> `+` -> `Jar from module with dependency`
- Choose the main class and set the output directory.
- Check the option `build on make`
- Click IntelliJ Menu `Build` -> `Make Project`.

Find the output path and run the job within the jar.

```
java -jar impala-connect-couchbase.jar
```

喝杯啤酒庆祝一下吧🍺🍺
