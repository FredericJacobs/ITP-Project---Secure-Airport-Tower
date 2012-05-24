# Secure Airport Tower by Frederic Jacobs & Hantao Zhao

Secure Airport Tower was a 1rst year project at EPFL. The goal is to write a control tower software to handle communications with airplanes.

## Where is the code ? 

All the code is inside /src/ 

## What should I set up ?

The first thing to point out is the fact that we used the JRE 1.7 because we use switches on Strings and other features of the JRE 1.7. It runs smoothy on Windows, Mac and Linux (the OpenJDK is advised to avoid compilation issues).  Once you created the project in Eclipse, don't forget to add the libraries that are in /src/libs  to the build path. Once that's done, you're ready to go. 

Java 1.7 SDK has some flaws on Unix-Platforms though. Written once, runs everywhere might not be true depending on the runtime. It turned out that JRE 1.7 was the most stable on Windows. 

For use of encryption with our Tower, it generates at runtime a file called 'MyKey'  that stores the key in a plane compatible format inside the main folder. 

## You are ready to go

Everything should run smoothly. If you're feeling lost, check out our report, development journal and JavaDoc.
The description of the GUI enhancements is available in the Q&A section of the report.

If you have any questions feel free to contact us at frederic.jacobs@epfl.ch and hantao.zhao@epfl.ch

