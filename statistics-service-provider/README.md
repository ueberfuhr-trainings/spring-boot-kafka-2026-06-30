# Statistics Service Provider

## Run the sample

To run and use the app, we can use these commands:

```bash
# run the main class in your IDE or use Maven
mvn spring-boot:run
# Open http://localhost:8082 in the browser
# Invoke the API to read the statistics
curl \
  -X 'GET' \
  'http://localhost:8082/statistics/customers' \
  -H 'accept: application/json' \
  -i
```
