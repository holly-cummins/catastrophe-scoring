# Cat-astrophe Microservices Sample
# Scoring Service


# About this sample

This sample is one microservice in a [larger sample 
microservices application](http://github.com/holly-cummins/catastrophe-microservices). If you’re wondering about the names – all good internet content involves cats. This sample was written
to be used as a live demo, and I have some experience of live demos, so I called it – of course – cat-astrophe.

This service evaluates the accuracy of a user drawing of 
a cat, using IBM Watson Visual Recognition service.  

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

The service should be testable on http://localhost:8081/rest/scoring/scoringalgorithm. 

### Deploying to a single board computer 

To create a zip with the application and all dependencies (including the server), run 

    gradle packageServer


### Deploying to Bluemix 

To create a war, run `gradle` from the catastrophe-scoring folder.

This can then be pushed to Bluemix with 

    cf push -p build/libs/catastrophe-web.war

# Setting up a Watson Visual Recognition Classifier

## Creating and binding the service 

You will also need to manually create an instance of the [Watson Visual Recognition Service](https://www.ibm.com/watson/developercloud/visual-recognition.html). Normally this could be done by the build pipeline, but the build pipeline generates a new set of credential every time an application is staged, and the Visual Recognition service limits the number of credentials that can be generated within a 24 hour period.  

Name the service "Visual Recognition". Then, using the `cf` cli, run `cf service-key "Visual Recognition" "Credentials-1"` to see the credentials.

To save them in an environment variable for the application, run 

VIS_REC_API_KEY=`cf service-key "Visual Recognition" "Credentials-1" | sed -En 's/.*api_key.*"(.*)".*/\1/p'`


## Training the Classifier

The Visual Recognition service comes with a default 
classifier which is optimised to understand a wide 
range of photographs. For this application, we 
instead need it to understand a small range of 
line drawings, so we need to create a custom classifier. Otherwise every image is identified as a weave. 

You will need to assemble zip files of images, each
with around 100 line drawings. For example, you could use 
cats.zip, octopods.zip, fish.zip, and elephants.zip. It's 
also a good idea to have a zip file of negative images.

Call the POST /v3/classifiers method with the following cURL command, which uploads the training data and creates the classifier "line-drawings":

For positive example files, the _positive_examples suffix is required. The prefix you choose (for example, apple) becomes the name of that class.

    curl -X POST -F "cat_positive_examples=@cats.zip" -F "fish_positive_examples=@fish.zip" -F "octopus_positive_examples=@octopods.zip" -F "elephant_positive_examples=@elephants.zip" -F "negative_examples=@negatives.zip" -F "name=line-drawings" "https://gateway-a.watsonplatform.net/visual-recognition/api/v3/classifiers?api_key=${VIS_REC_API_KEY}&version=2016-05-20"
    
The response includes a new classifier ID and status. For example:
{
    "classifier_id": "linedrawings_1646565112",
    "name": "line-drawings",
    "owner": "{owner}",
    "status": "training",
    "created": "2016-09-20T12:55:07.569Z",
    "classes": [
        {"class": "fish"},
        {"class": "elephant"},
        {"class": "cats"},
        {"class": "octopus"}
    ]

If you've already created a classifier, you may need to delete it before creating a second one, using 

    curl -X DELETE "https://gateway-a.watsonplatform.net/visual-recognition/api/v3/classifiers/{classifier_id}?api_key={api-key}&version=2016-05-20"
    
Training begins immediately and must finish before you can query the classifier. 

Once you have the classifier id, set a `CLASSIFIER_ID` environment variable locally or in Bluemix:

# Dependencies 

This sample uses [WebSphere Liberty](http://wasdev.net), Java EE interfaces, [IBM Watson Visual Recognition service](https://www.ibm.com/watson/developercloud/visual-recognition.html), and the [Liberty Consul Service Discovery sample](https://github.com/WASdev/sample.consulservicediscovery). 

# More information 

* [A Blog post about microservices and this sample](https://developer.ibm.com/wasdev/blog/2016/06/01/putting-micro-microservices/)
* [The talk which accompanies this demo](http://www.slideshare.net/HollyCummins/microservices-from-dream-to-reality-in-an-hour")
* [Liberty and microservices](https://developer.ibm.com/wasdev/docs/microservices/)

