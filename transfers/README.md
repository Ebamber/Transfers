#Transfers

A few changes were made over the original version of the application:

- The application now operates with a REST endpoint
- The endpoint will take a multipart file as a request param, rather than a file path
- A postman collection with the endpoint has been included in resources
- Originally, the application would take a file name and retrieve from resources, even the non RESTful service now takes 
only a file path

## Unit Tests

At the time of writing all unit tests run successfully, and tests exist for all service layer methods.

In the FileReaderServiceTest.java class, things can be a little more complicated though - particularly:

The class can operate either with a file path or a file name, all depending on whether a file name or file path are used 
for the file reader (discussed later)

If the application is switched to use a file name rather than a file path, simply switch the constant from 
`TEST_FILE_PATH` to `TEST_FILE_NAME` in the `shouldReadAllLinesInAFile` and `shouldParseAllTransactions` tests.

## Modes of Operation

Therefore the application can potentially utilize 3 different flows depending on what code is commented out:

- run as is and run the Postman collection with a multipart file (changing the -Dserver.port according to needs) to
run the application as a REST service. The endpoint can ONLY work with a Multipart File given as input (through the `Body`
tab in Postman with `form-data`), it will not accept a file path

- uncomment the beans and the `run` method in `TransfersApplication` and the `spring.main.web-application-type=NONE`
property in application properties to run the application as an ordinary java application wherein the user will be 
prompted for a file path (or file name as per the next point) rather than a REST service

- uncomment the code in the `FileReaderService.readAllLines` method to switch the command line run mode to use a file 
name of a file included in resources
