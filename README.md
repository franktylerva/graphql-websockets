This application uses the Netflix DGS GraphQL library to expose a GraphQL endpoint and Redis to scale out GraphQL subscriptions.  The subscription uses Redis Pub/Sub to broadcast subscriptions across different instances of the application.

### Start a Redis Docker Container listening on localhost port 6379

`docker run -d -p 6379:6379 redis
`

### Run the application

`./gradlew bootRun`

### Run another instance on a different port

`SERVER_PORT=8081 ./gradlew bootRun`

### Install GraphQL curl client

`npm install -g graphqurl`

### Query

`gq http://localhost:8080/graphql -q 'query { people { name age } }'`

### Mutate (Everytime you call this mutation, a message is published to the peopleUpdates subscription)

`gq http://localhost:8080/graphql -q 'mutation { addPerson(input: {name: "Frank", age: 24 }) { name }}'`

### Subscription

`gq ws://localhost:8080/subscriptions -q "subscription { peopleUpdates { name } }"`

### Subscribe to another instance with different fields

'`gq ws://localhost:8081/subscriptions -q "subscription { peopleUpdates { name age } }"`
