## Измерения выделяемой памяти

Cборка проекта:

`mvn package`

Запуск измерений:

1. `cd launch`
2. `./run.sh`

Для операционных систем семейства Windows можно запустить из командной строки (текущий каталог должен быть `launch`)
выполнив следующюю команду:

`java -Xmx512m -Xms512m -XX:-UseTLAB -javaagent:instrumentation.jar -jar execution.jar`
