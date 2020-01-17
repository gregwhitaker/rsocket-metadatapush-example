# rsocket-metadatapush-example
An example of sending metadata at runtime with the metadata-push interaction model in [RSocket](http://rsocket.io).

In this example the `hello-client` will first request a hello message for Bob, in english, from the `hello-service`. Once the 
hello message is received by the client it will then send a metadata-push message to change the language to french. Finally, after
the language has been changed, the client will send another request for a hello message for Bob.

## Building the Example
Run the following command to build the example:

    ./gradlew clean build

## Running the Example
Follow the steps below to run the example:

1. Run the following command to start the `hello-service`:

        ./gradlew :hello-service:run

    If successful, you will see a message in the terminal indicating the service has started:
    
        [main] INFO example.hello.service.HelloService - RSocket server started on port: 7000
       
2. In a new terminal, run the following command to request hello messages using the `hello-client`:

        ./gradlew :hello-client:run --args=Bob
        
    This command is going to request a hello message for Bob in english, push new metadata setting the language to french, and then
    request another hello message for Bob.
    
    If successful, you will see the following in the client's terminal:
    
        [main] INFO example.hello.client.HelloClient - Sending request for 'Bob'
        [main] INFO example.hello.client.HelloClient - Response: Hello, Bob!
        [main] INFO example.hello.client.HelloClient - Sending metadata push to change locale to 'fr_fr'
        [main] INFO example.hello.client.HelloClient - Sending request for 'Bob'
        [main] INFO example.hello.client.HelloClient - Response: Bonjour, Bob!

## License
MIT License

Copyright (c) 2020 Greg Whitaker

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.