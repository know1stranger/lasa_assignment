# ContactListApp

> ContactListApp is a SpringBootApplication to perform search, view & edit contacts associated with an Organisation

## Installation

> Import archive  contactlistapp.zip project into workspace and run as SpringBootApplication.

## Software and Tools for Development

> JDK 1.8
>
> *ElasticSearch** 7.11.2
>
> Postman / Insomnia
>
> Browser (Chrome / Safari etc.)
>
> Eclipse IDE (SpringToolSuite)
>
> Flyway DB migration.
>
> H2 Database (In memory)
>
> Maven
> <h6> * want to proceed without ElasticServer? Please visit Troubleshooting & Known-issues for workaround.</h6>

## Usage

> Application: Once the SpringBoot application is running. Give these in different tabs for the address bar. <h5> (Please see ports mentioned in url are available.)</h5>
>
> [Contactlistapp](http://localhost:8080) *default port is 8080*
>
> [In memory Database](http://localhost:8080/h2-console) *accessing is 8080*
>
> Accessing your index on [Elasticserver](http://localhost:9200/contactstore/_search) or [For Pretty Response](http://localhost:9200/_search?pretty=true) *default port is 9200*

## To run application -  Maven commands

> How to run from Terminal/Console. (Assuming you have maven set-up ready.)
>
> To run contactlistapp:  `mvn spring-boot:run` (uses default spring profile local-dev.)
>
> To use different profile. `mvn spring-boot:run -Dspring-boot.run.arguments=--spring-boot.run.profiles=local-dev`. Default is local-dev, however, this application supports other profiles too like test. Basic application.properties and environment/profile specific properties would be used. If any overridden properties found they would applied to application instance.
>
> To skip tests and do installation: `mvn clean install -DskipTests=true`
>
> To skip ES `or` don’t have ES config ready. (default value to skip elasticsearch is false, setting it to true will stop the integration with it)
`mvn spring-boot:run -Dspring-boot.run.arguments=--elasticsearch.toggleFlagOn=false`
>
> To skip ES and to run on different http server port
`mvn spring-boot:run -Dspring-boot.run.arguments="--elasticsearch.toggleFlagOn=true --server.port=8084”`

## Functional Requirements <h6>~~closed~~ open</h6>

> ~~Add user name to welcome page~~
>
> ~~All screens have the navigation-bar~~
>
> ~~Search fields to consider case insensitive~~
>
>~~Support implicit trailing wildcard (*)~~
>  
> ~~Search text to support wildcard (*) in-between.~~
>
> ~~ABN to be displayed in format 99 999 999 999~~

`Pleas Note: To see full search features please configure ES. Searching with database supports only limited while using wildchar.`

## Optional Extensions <h6>~~closed~~ open</h6>

> ~~Adding automated testing~~
>
> ~~Finding~~ & fixing security vulnerability
>  
> ~~Use diff technology for searching~~
>
> ~~Imposing SLA on response times~~
>
> ~~Adding SPA tech React / Vue~~
>
> Showing ~~Infrastructure as Code deploying~~ or support cloud hosting.

## Roadmap

>- To server web pages with HTTPS.
>- Convert Spring MVC app to API (Rest-ful API). Gives flexibility to integrate with different view technologies. As it would become light-wright and gives leverage to spin multiple servers to host this as a service. It will help to handle traffic high-volumes.
>- Convert to REST based api and handle json messages. Query params can be reduced to give generic info rather giving out (secure) contact info.
>- Support distributed transactions when  delete and update are happening.
>- To add few more test source code to improve code coverage. (Write test for web layer.)
>- Add support for PIT mutation testing for having maintainable code.
>- To refine log level for all layers and environments.
>- To fully support toggle feature for ElasticSearch (getting flag from DB / another service).
> To use Spring AOP for keeping ElasticSearch index in sync with Database operation. (for CURD operation)

### Development Assumption

> 1) For better search results - Indexing for ElasticSearch is done at the time the application is booting.
> 2) When on Contacts tabs, aLl contacts to be listed before user performing any search.
> 3) Associated Created Date only to Contact entity.
> 4) Date stored in Local time not UTC to support timezone.
>5) Created Date is not the same as Updated Date, which is not a table column for now.
> 6) (Edit Screen) First Name and Last Name don't accept digits.
> 7) All updates/deletes performed in a transaction. To keep index in sync with DB.

>## Supported Features

1) All text fields will be considered case insensitive
2) All search inputs will have implicit trailing * as wildcard.
3) To search wildcard (*)  can be used in text field to search.
4) Organisation ABN is formatted `12 234 234 234`
5) Home (Tab)
    - Greets to `system user` with a welcome page. <h5>(Relying on `environment` rather hard-coding, as SPA is WIP)</h5>
6) Contact (Tab)
7) Search Screen:
     1) Allows user to enter first name, last name and Organisation name.
     2) To find contact(s) all form fields are will used as search criteria.
8) Search Results:
   1) Returns all matched contacts.
   2) Upon search if only one match found, the user will be navigated to contact view screen.
9) List View:
   1) Search Results displays contact's Fullname (Firstname Lastname) Organisation Name( ABN) and contact Created date. (Date formatted `dd/mm/yyyy`).
10) View Page:
    1) Displays First Name, Last Name, Organisation Name, Organisation ABN and Created date.
    2) Created date is formatted as `21/03/2021 16:17:11`
11) Edit Page:
    1) A valid edit saves to DB. Also updates the ElasticSearch Index.
    2) Successful save for a contact will take to View Contact Screen.
    3) Cancel: Navigates to the View Contact Screen.
    4) Organisation Dropdown will list Organisation name along with ABN in brackets. `THE AUSTRALIAN (99 867 121 121)`
    5) First Name and Last Name will be validated for length and form.
12) Error page: A global error page is displayed upon unexpected error is caused. For instance if ElasticSearch server connectivity is lost. User will be taken to custom application error page rather than the error page provided by app server or SpringBoot.
13) Supports Spring profile to build and deploy application. This will also help to in different environments. For CI-CD pipe to use and pick the properties. Can also have the cloud supported properties in seperate prop file without touching other configurations.
14) Only application properties needed are overridden. For instance not displaying banner. Can control connectivity like different database, Elastic server for those different environments.
15) Flyway support for DB migration.
16) Use application-`environment`.properties to configure or secure connections. For instance database access at environment level can be controlled / provided.

>## Issues
>- To find the user and read the values rather path variables.

## Troubleshooting & Known-issues

> 1)Log messages should help what's happening with the application.
>
> 2)Check the server is started without any alarming issues. Log files can help. contactlistapp-local-dev.log for local-dev profile. (Application can have multiple logs file and they are named same after appname and env they are running.)
>
> 4)Check Database is hosted and database updates are happening.
>
> 5)Check connectivity with Elastic Server and query for any index.
>
> 6)If Elasticserver is not available ContactRepositoryTestIT will fail. You may have the server or skip the test.
>
>7)To run app without ElasticServer set-up. Please make sure the below are set properly for environment. `application-*.properties` file has the property set to false `elasticsearch.toggleFlagOn=true`. The default `elasticsearch.toggleFlagOn` is set to `false` in `ContactElasticSearchService.java`. Log message
Caused by: java.net.ConnectException: Connection refused at org.elasticsearch.client.RestClient.extractAndWrapCause(RestClient.java:823) ~[elasticsearch-rest-client-7.6.2.jar:7.6.2]

## Project Status

>- All General requirements are supported.
>- Code coverage is 50%. Tests provided for Integration test from Service layer to database layer.
>- UI doesn't support add operation for Contact and Organisation.

### Version Support

 >   The archive (contactlistapp.zip) got ,git folder. Do git checkout to a new branch in your repo. For Git help [https://git-scm.com/book/en/v2/Appendix-C:-Git-Commands-Branching-and-Merging].

### How to setup ElasticSearch Server

>- [Download from](https://www.elastic.co/downloads/elasticsearch) the same page got to installation steps for your box (i.e ios/win) OS. Time it takes to setup < 15 mins.
>- When elasticsearch is up & running try this [link](http://localhost:9200). Response from it will show version, build info.
  
### Developer Info

> Chaitanya Kumar Karimajji
>
> email:kchaitanya.mail@gmail.com
