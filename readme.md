A simple project to test the lmax disruptor.

To run, compile the project and start the test server.
Then run the main method of the testlmax-module. It runs in a while(true) so it'll keep running 
until you shut it off and most likely eat 100% of your cpu. The spring boot web server will
 give statistics on completed jobs through stdout every five seconds.