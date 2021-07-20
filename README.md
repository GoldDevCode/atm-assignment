ATM Assignment API

This ATM simulates the working of an ATM machine exposed through REST APIs. The project is developed in java(jdk 1.8) and framework is Spring Boot.

The project is build using maven. Use the simple mvn clean install command to build the jar artifact.

The project is containerized using Docker. Please find the Dockerfile in the project root folder.

You can issue the command : docker build -t <name_you_want_to_give>:latest to build the docker image of the project.

You can run the docker container by issuing below command : 

docker run -d -p 8080:9696 <name_of_the_docker_image>

-d : detached mode

-p : Port mapping, I am using 9696 port for embedded tomcat. You can change port number in application.properties file using server.port property. 
     8080 is the port that will be used for accessing the application on docker host.

You can see the Spring boot application starting using docker logs command.

There are two APIs in the project : 

1. API to checkBalance :  This API will return the balance amount available in the account. It is a POST API.

   API endpoint : /api/atm/checkBalance
   Request Body :  
   {
    "accountNumber" : "123456789",
    "pin" : "1234"
   }
   
   Response : 
   
   {
    "accountNumber": "123456789",
    "balanceInAccount": "800.0",
    "maximumWithdrawableAmount": "1000.0"
   }
   
   Error Case for eg. invalid pin will give response like below : 
   {
    "errorResponse": {
        "errorCode": "2002",
        "errorMessage": "Account Number Pin combination not valid.Card access will be locked after 2 more attempts"
    }
}

There are defined error code and error messages for handling different fault/error scenarios.

2. API to withdrawAmount :  This API will return a string representing the notes dispensed along with balance amount available in the account. It is a POST API.

   API endpoint : /api/atm/withDrawMoney
   Request Body :  
   
   {
    "accountNumber" : "123456789",
    "pin" : "1234",
    "amountToWithdraw" : 125
   }
   
   Response : 
   
   {
    "notesDispensed": "50€ x 2,20€ x 1,5€ x 1",
    "remainingBalance": "675.0"
   }
   
   Error Case for eg. Trying to withdraw amount like 127  will give response like below : 
   {
    "errorResponse": {
        "errorCode": "2007",
        "errorMessage": "Please enter amount in multiple of 10 or 5"
    }
  }

There are defined error code and error messages for handling different fault/error scenarios.

Unit tests will be run during project build using maven. Unit test file is available at src/test/java location inside the project.
   
   

