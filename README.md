# incident-management

For setup
mvn clean install

Following APIs are to manage crud operations for incident report

1. **Create incident-report**

API : http://localhost:8080/v1/incident-report

Http Method Type : POST

Request Header: creatorId <Integer Type>

Sample Request Body:

{
    "title": "Test Error 123",
    "description": "Test Desc",
    "comments": "Test Comment"
}

Sample Request Response:

{
    "incidentId": 1,
    "title": "Test Error 123",
    "status": "NEW",
    "creatorId": 1,
    "assigneeId": null,
    "description": "Test Desc",
    "comments": "Test Comment"
}

Http Status Code - 201


2. **Get all incident-report**

API: http://localhost:8080/v1/incident-reports

Http Method Type : GET

Sample Request Response:

[
    {
        "incidentId": 1,
        "title": "Test Error 123",
        "status": "NEW",
        "creatorId": 1,
        "assigneeId": null,
        "description": "Test Desc",
        "comments": "Test Comment"
    },
    {
        "incidentId": 2,
        "title": "Test Error 567",
        "status": "ASSIGNED",
        "creatorId": 1,
        "assigneeId": 4,
        "description": "Test Desc",
        "comments": "Test Comment"
    }
]

Http Status Code - 200


3. **Update incident-report**

API: http://localhost:8080/v1/incident-report/{incidentId}

Http Method Type : PATCH

Request Header: userId <Integer Type>

Sample Request Body:

{
    "title": "Test Error 567",
    "assigneeId": 4,
    "description": "Test Desc",
    "comments": "Test Comment"
}

Sample Request Response:

{
    "incidentId": 2,
    "title": "Test Error 567",
    "status": "ASSIGNED",
    "creatorId": 1,
    "assigneeId": 4,
    "description": "Test Desc",
    "comments": "Test Comment"
}

Http Status Code - 200

4. Delete incident-report

API: http://localhost:8080/v1/incident-report/{incidentId}

Http Method Type : PATCH

Request Header: userId <Integer Type>

Sample Request Response: No content

Http Status Code - 204


**Attached postman collection(incident-management) in the parent folder**


