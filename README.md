# Cat-astrophe Microservices Sample
# Scoring Service


# About this sample

This sample is one microservice in a [larger sample 
microservices application](http://github.com/holly-cummins/catastrophe-microservices). If you’re wondering about the names – all good internet content involves cats. This sample was written
to be used as a live demo, and I have some experience of live demos, so I called it – of course – cat-astrophe.

This service evaluates a user guess about a cat name, using a simple algorithm. 

This application is designed to run on a [raspberry pi](http://www.linksprite.com/linksprite-pcduino/) (and optionally [Bluemix](http://bluemix.net)), and runs on [WebSphere Liberty](http://wasdev.net). 

**[License information](LICENSE.txt)** 

## Getting started 

Press this button to get your own copy of the sample running in Bluemix. 

[![Deploy to Bluemix](https://bluemix.net/deploy/button.png)](https://bluemix.net/deploy?repository=https://github.com/holly-cummins/catastrophe-scoring)

### Eclipse integration 

To set up Eclipse projects, run 

    gradle clean
    gradle eclipse

### Running the server locally (from the command line) 

This service uses the [Liberty Consul Service Discovery sample extension](https://github.com/WASdev/sample.consulservicediscovery) to register itself with a Consul server. Before building, clone and build that project, so that it's in the local maven repository. 

Run

    gradle runServer

The service should be testable on http://localhost:8081/rest/scoring/score. 

### Deploying to a single board computer 

To create a zip with the application and all dependencies (including the server), run 

    gradle packageServer


### Deploying to Bluemix 

To create a war, run `gradle` from the catastrophe-scoring folder.

This can then be pushed to Bluemix with 

    cf push -p build/libs/catastrophe-web.war

# Dependencies 

This sample uses [WebSphere Liberty](http://wasdev.net), Java EE interfaces, and the [Liberty Consul Service Discovery sample](https://github.com/WASdev/sample.consulservicediscovery). 

# More information 

* [A Blog post about microservices and this sample](https://developer.ibm.com/wasdev/blog/2016/06/01/putting-micro-microservices/)
* [The talk which accompanies this demo](http://www.slideshare.net/HollyCummins/microservices-from-dream-to-reality-in-an-hour")
* [Liberty and microservices](https://developer.ibm.com/wasdev/docs/microservices/)

