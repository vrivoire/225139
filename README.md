# ProjectYT
Youtube API and ActiveMQ demo

image at : https://tinyurl.com/y2ezct73

Part A - build a solution in Java that will
o   Connect to youtube using the youtube APIs (https://developers.google.com/youtube/) and retrieve all video metadata containing the word “telecom” in the title
o   For each video identified, publish an XML message in Queue A, containing at least the following information:
      URL
      Video title
 
Part B- build a solution in Java that will
o   Consume the XML messages from JMS Queue A (output of Part A)
o   Modify the video Title in each XML message, replacing the word “telecom” with the word “telco”
o   Store the modified XML message in JMS queue B
 
Part C - Provide a document describing
o   Which design patterns have you used in the implementation
o   Schema describing the messages produced by the solution
o   How to compile the source code produced
o   How to execute your programs to produce the results previously described
o   Looking back at the implementation, what would you consider doing differently

