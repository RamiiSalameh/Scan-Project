# Scan-Project Application

This is a sample project showcasing a CyberScan application, which allows ingesting scan data and checking the status of the scans.

## Threading in Scan Processor

We scan is incoming a thread with the scan is created, it is waiting to run in the thread pool. 
The thread pool size is N=5 for and example but it can be change to any size needed. this will make system to run 5 scan one on each thread simultaneously. 

## APIs

### Ingest API

Endpoint: `/ingest`
Method: POST
Description: Use this API to ingest scan data into the application for processing.
Request Body: The scan data to be ingested.

### Status API

Endpoint: `/status/{scan-id}`
Method: GET
Description: Use this API to check the status of the scans processed by the application.
Response: The status of the scans, including information such as scan ID, status, and progress.

## Getting Started

To run the Scan-Project application, follow these steps:

1. Clone the repository.
2. Configure the application properties, such as the database connection details, in the `application.yml` file.
3. Build the project `mvn clean install`
4. Run the application using the command `java -jar <jar-file-name>.jar`.
5. The application will start, and you can access the APIs mentioned above.
