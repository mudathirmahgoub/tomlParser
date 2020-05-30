```shell script
$ java -version
 java -version
 openjdk version "11.0.7" 2020-04-14
 OpenJDK Runtime Environment (build 11.0.7+10-post-Ubuntu-2ubuntu218.04)
 OpenJDK 64-Bit Server VM (build 11.0.7+10-post-Ubuntu-2ubuntu218.04, mixed mode, sharing)

 $ git clone https://github.com/mudathirmahgoub/tomlParser
 $ git clone https://github.com/CVC4/CVC4
 $ cd tomlParser     
 $ ./gradlew build 
 $ java -jar build/libs/tomlParser-all.jar -src CVC4/src -o cvcArguments.json 
```