A simple example code for [eclipse/jetty.project#1848](https://github.com/eclipse/jetty.project/issues/1848)

To build server ...

    $ mvn clean install
    
A self executing jar can be found in `target/demo.jar`, it has a manifest classpath that
points to jars in a (relative to the demo.jar) `lib/` directory.

Simply running the `demo.jar` after the build is sufficient.

    $ java -jar target/demo.jar [so_linger value] [payload_size]
    
The output of the server will tell you what the soLinger setting is.

Example (see second line of output):

    $ java -jar target/demo.jar
    2017-09-27 18:06:04.917:INFO::main: Logging initialized @600ms to org.eclipse.jetty.util.log.StdErrLog
    2017-09-27 18:06:04.964:INFO:oejd.Issue1848:main: Not setting connector.setSoLingerTime()
    2017-09-27 18:06:04.968:INFO:oejs.Server:main: jetty-9.4.7.v20170914
    2017-09-27 18:06:05.032:INFO:oejs.AbstractConnector:main: Started default@15246840{HTTP/1.1,[http/1.1]}{0.0.0.0:8005}
    2017-09-27 18:06:05.033:INFO:oejs.Server:main: Started @721ms
    
    $ java -jar target/demo.jar 1
    2017-09-27 18:06:31.602:INFO::main: Logging initialized @617ms to org.eclipse.jetty.util.log.StdErrLog
    2017-09-27 18:06:31.644:INFO:oejd.Issue1848:main: Using connector.setSoLingerTime(1)
    2017-09-27 18:06:31.648:INFO:oejs.Server:main: jetty-9.4.7.v20170914
    2017-09-27 18:06:31.714:INFO:oejs.AbstractConnector:main: Started default@6792493e{HTTP/1.1,[http/1.1]}{0.0.0.0:8005}
    2017-09-27 18:06:31.715:INFO:oejs.Server:main: Started @736ms
    
Then, from a different console execute `run-curl.sh` it will perform the curl request, capture
the data sent, and do some tests on the start / end of the data.

    $ ./run-curl.sh
    * Rebuilt URL to: http://localhost:8005/
      % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                     Dload  Upload   Total   Spent    Left  Speed
      0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0*   Trying 127.0.0.1...
    * Connected to localhost (127.0.0.1) port 8005 (#0)
    > GET / HTTP/1.1
    > Host: localhost:8005
    > User-Agent: curl/7.47.0
    > Accept: */*
    > Connection:close
    >
    < HTTP/1.1 200 OK
    < Connection: close
    < Date: Wed, 27 Sep 2017 18:08:03 GMT
    < Content-Type: application/octet-stream
    < Server: Jetty(9.4.7.v20170914)
    <
    { [20 bytes data]
    100 4882k    0 4882k    0     0  34.7M      0 --:--:-- --:--:-- --:--:-- 35.0M
    * Closing connection 0
    # curl-output.dat info ...
    -rwx--x--x 1 joakim joakim 5000036 Sep 27 11:08 curl-output.dat
    
    # Testing curl-output.dat header ...
    Start of content...
    
    # Testing curl-output.dat footer ...
    End of content

If you see any of the following output, it means the data was sent completely.

* Output file size is 5000036
* Output header is `Start of content...`
* Output footer is `End of content`
