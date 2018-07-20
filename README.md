# spring-cloud-test

This is the test project integrated with Eureka, Zuul, Ribbon, Sleuth, Zipkin 

contains four applications:

1. eureka server: listening on 8761, before start the application you need to setup a git repository.
2. gateway server: listening on 8000, is used as proxy/load balance to handle api request from external.
3. client1: is the service providing rest service /greeting which will call client2's /greeting.
4. client2: is the service providing rest service /greeting.

You should at least start 2 instances of client1 and client2 so see how Zuul/Ribbon works.

Start eureka server first, then start gateway, client1 and client2

Then you can test it from curl:

```
 $ curl -X GET --header 'Accept: */*' http://localhost:8000/api/client2/greeting
 Hello from 'CLIENT2:55279'!
 
 inpoint@W7-64-PC MINGW64 /d/work/workspace/em/prod (master)
 $ curl -X GET --header 'Accept: */*' http://localhost:8000/api/client2/greeting
 Hello from 'CLIENT2:54902'!
 
  
 $ curl -X GET --header 'Accept: */*' http://localhost:8000/api/client1/greeting
 Hello from 'client1:54891' from [Hello from 'CLIENT2:54902'!]!
 
 inpoint@W7-64-PC MINGW64 /d/work/workspace/em/prod (master)
 $ curl -X GET --header 'Accept: */*' http://localhost:8000/api/client1/greeting
 Hello from 'client1:55260' from [Hello from 'CLIENT2:54902'!]!
 
 inpoint@W7-64-PC MINGW64 /d/work/workspace/em/prod (master)
 $ curl -X GET --header 'Accept: */*' http://localhost:8000/api/client1/greeting
 Hello from 'client1:54891' from [Hello from 'CLIENT2:54902'!]!
 
 inpoint@W7-64-PC MINGW64 /d/work/workspace/em/prod (master)
 $ curl -X GET --header 'Accept: */*' http://localhost:8000/api/client1/greeting
 Hello from 'client1:55260' from [Hello from 'CLIENT2:55279'!]!
``` 
 