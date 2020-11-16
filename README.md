# Construction tender (tender-api)
Tender API for issuers and bidders. Yep... that's it.

## Running the application

### Running locally

Refer to [help page.](HELP.md)

### Where are other environments?

The assignment is created as an example only for local environment, 
but everything is (hopefully) already set up to make it easy to add new environments. Adding
a driver for another database (such as MySql) is as easy as adding a dependency (Thanks, Spring Boot!)
and creating a new environment configuration file (ie. for staging `application-staging.yml`)
in `src/main/resources/`.

Things are usually more complicated for production environments (all kinds of certificates etc.),
and sometimes setting things up isn't possible without some `@Configuration` classes, but this can
also be easily separated per environment with `@Profile`.

## Application information

### Database relation

There are 4 tables in total:

`"Issuer" 1-n "Tender" 1-n "Offer" n-1 "Bidder"`

Basically, a **tender** is bound to an **issuer**, meaning one issuer can have multiple tenders. 
An **offer** is bound to a **bidder**, meaning one bidder can have a lot of offers, 
for a lot of different (or same) tenders.

Connection (fetching type) between an issuer and tender and offer and bidder is **EAGER**, because, we'll always
need issuer information if we request data for a tender (same goes for offer-bidder), while
the connection between tender and offer is **LAZY** because it's not always worth fetching this
data from the database.

Dev should be able to access the embedded H2 console UI when running the application
with local profile by visiting [localhost:9080/tender-api/h2-console](http://localhost:9080/tender-api/h2-console)

### API

API documentation is available via swagger by visiting [localhost:9080/swagger-ui/index.html](http://localhost:9080/swagger-ui/index.html#/)

There are 2 controllers, `BidderController` and `IssuerController`. 
* **Bidder controller** being used to create offers and get information about offers.
* **Issuer controller** being used to create tenders, accept offers and get information about tenders and offers for a specific tender.

Notes about the controllers:
* Put/Post calls such as for *creating a tender*, *creating and offer* and *accepting an offer* require `bidder-name` or `issuer-name` headers.
* When accepting an offer a check is done if the accepted offer is for a specified bidder. This is just a validation step to prevent API user errors.
* Accepting an offer and creating an offer are mutually lockable if both are related to same tender ID. This means that a user can not create an offer 
while an issuer is accepting some offer and vise versa. This also means that if 2 users related to same issuer accept 2 different offers at the same time. 
The first user that "accepted" some offer should win the selection.
* For responses, Hateoas is used.
