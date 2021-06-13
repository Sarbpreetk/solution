# Java PDF Lambda Assignment

This example application is simulating (very simplified) PDF-processing process. Usually the process consists of two phases: 1) processing and 2) sealing.
The application reads input from stdin in JSON format and outputs also with JSON to stdout.

## Running the application

    $ mvn package
    $ echo '[{"payload": "Hello World!"}]' | java -jar target/java-interview.jar

The requirement has come in to switch over to newer version of PDF processing library (from iText 5 to iText 7) which has a slightly different API (needs to be initialized with license key (stateful) and does the processing and sealing in one fell swoop (as opposed to separate processing and sealing)).
To make sure we don't break other consumers we want to slowly introduce the ability to switch between various backends while maintaining 100% backwards compatibility.

## TODO

* Add to Lambda Args new field `iTextVersion` determining which backend to use.
* make sure the lambda uses either iText5 or iText7 library (classes in the api package shouldn't be altered) based on this input arg, for processing and sealing
* if the `iTextVersion` JSON field is null (or not set), the behavior of the lambda should be exactly the same as the original unmodified program (full backwards compatibility)
* iText7 library needs a license key to initialize its context, this should be supplied to the lambda via an Environment variable `ITEXT7_LICENSE_KEY`. If the license key is not supplied, the iText7 backend should gracefully fail to initialize and the lambda should run in iText5 mode only.
* All of the above may need to refactor of other code (there has been an attempt to abstract away the processor and sealer, but it may not be a very good fit for this case!), feel free to touch anything (except the `api` package (IText{5,7} classes since we want to treat them as libraries we are consuming)) as long as the I/O behavior remains compatible.

## Solution
* New method has been added on itextHandler to determine which api to be invoked.
* iTextVersion has been added in Args class.

## Example usage

  (current behavior)

    $ mvn package
    $ echo '[{"payload": "Hello World!"}]' | java -jar target/java-interview.jar
    [{"status":"success","output":"Sealed via iText5: [>>> Hello World! <<<]"}]

  (Added Behaviour)

    $ echo '[{"payload": "Hello World!", "iTextVersion": "5"}]' | java -jar target/java-interview.jar
    [{"status":"success","output":"Sealed via iText5: [>>> Hello World! <<<]"}]

    $ echo '[{"payload": "Hello World!", "iTextVersion": "7"}]' | java -jar target/java-interview.jar
    [{"status":"error", "output": "iText7 backend not available"}]

    $ export ITEXT7_LICENSE_KEY="1234567890"
    $ echo '[{"payload": "Hello World!", "iTextVersion": "7"}]' | java -jar target/java-interview.jar
    [{"status":"success","output":"Sealed via iText7: [~~~ Hello World! ~~~]"}]

    $ echo '[{"payload": "Hello World!", "iTextVersion": "6"}]' | java -jar target/java-interview.jar
    [{"status":"error", "output": "unknown backend specified"}]

## Extra credit

**Don't implement until the basic assignment is working 100%!**

1) Stream the JSONs both on input and output (if the processing took large amount of time, the already processed Results will be already output and serialized).
2) Make the deserializion of the batch of Args more resilient. If there are only a few malformed objects that fail to deserialize, report errors just for those, but correctly process the others.
