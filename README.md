This is a demo application to show top 5 websites in page.
 
## Main Technology 
* [Spring-boot](https://spring.io) is used for launching the application.
* [ReactJs](https://reactjs.org/), [Bootstrap](https://getbootstrap.com) are used to built the frontend components.
* [mysql server](https://dev.mysql.com), [mybatis](http://blog.mybatis.org/) are used for data management.
* [GCE](https://cloud.google.com/compute/docs/) is used to hosted the application

## Set it up

Check out the source code and get your environment ready with Mysql server installed.

Modify the configuration file `src/main/resources/application.properties` with the properly value.

### _`mvn clean package`_

This command will compile and build the package `test-top5-list-0.0.1-SNAPSHOT.jar`

Copy the jar to anywhere and run below command to start the application

_`java -jar test-top5-list-0.0.1-SNAPSHOT.jar`_




### Access the application

Open _`http://localhost:8080`_ in your browser.