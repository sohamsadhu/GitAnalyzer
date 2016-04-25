# GitAnalyzer
Demonstration of use of GitHub API using a Java wrapper.

Requires: Java 1.7+, Maven 3+

We use the [Egit-Gitub](https://github.com/eclipse/egit-github) API, which is a Java wrapper, on
the GitHub API; for reading a repository and its contents.

### Main class
There is only one Java file under folder src/main/java in package com.soham.gitanalyzer, called
GitHubAnalyzer; that does all the work. It is a plain Java class that has a main method, which
checks for the ruby repository on Github. It then gets all the branches and tags associated with
the same, and also checks for file `.travis.yml` to not exist in root folder.

### Test class
There is only one test class under folder src/test/java in package com.soham.test, called
GitHubAnalyzerTest; that has only one test. [Mockito](http://mockito.org/) is used to mock
the [Egit-Gitub](https://github.com/eclipse/egit-github) classes; and method 
getRepository(String owner, String repositoryName) is unit tested. More unit tests can be 
written and mocked if required.

### How to run
Just clone this project, or download a zip file of this project and unzip or explode it anywhere
on your machine. Navigate to this project root folder which will be named `GitAnalyzer`. Open
a command line or bash shell and type in the following commands to make and run the code.

    mvn clean
    mvn package
    java -jar target/GitAnalyzer-0.0.1-SNAPSHOT-jar-with-dependencies.jar > results.txt

1. Maven clean is optional but recommended to make sure no garbage corrupts the build.
2. Maven package command is all encompassing. It compiles the code, runs the test, and creates a
fat jar with the dependencies in it.
3. We lastly run the executable jar file to obtain our results. We pipe the contents of our results
to a results.txt file instead on showing them on the console. The reason for so has been mentioned
in the caution note below. Open the results.txt file to view the required _partial_ results.

### Caution
Github caps the number of API request incoming from each machine to 60 requests per hour, or rather
1 per minute. This obstacle can be overcome if you use authenticate login. However for the purpose
of this exercise we do not do so, since it would mean putting our authentication ids on the open.
Ruby has many branches and tags, especially a lot of tags. Since looking for a file, means that
you will have to send a request to Github; and there are lot of tags for request. Thus we end
up with a lot of request denied exception on the console.
This shoud not be a cause of concern. This can be handled by making only certain number of requests
an hour through API and waiting for the reset. However to keep things simple, these cases have
not been implemented.


