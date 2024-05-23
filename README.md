## Environment-Harness TEST:
- Java version: 21
- Maven version: 3.8.1
- Spring Boot version: 3.1.4

## Data:
Example of a family data JSON object:
```
{
        "familyMemberId": 1,
        "familyMemberName": "Vidhan Chandra",
        "passportNumber": "R123456789",
        "dob": "1985-01-01"
    }
```
## Requirements:
Implementation of the Family model. The REST service must expose the `/familymembers` ,  `/familymember`  endpoints, which allow for managing the family records in the following way:

**POST** request to `/familymember`:

- creates a new family member
- expects a JSON family member object without an id property as a body payload. 
- adds the given family object to the collection of family members and assigns a unique integer id to it. The first created family member must have id 1, the second one 2, and so on.
- the response code is 201, and the response body is the created family member object

**GET** request to `/familymembers`:

- return a collection of all family members
- the response code is 200, and the response body is an array of all family member objects ordered by their ids in increasing order

**GET** request to `/familymember/<id>`:

- returns a family member with the given id
- if the matching family member exists, the response code is 200 and the response body is the matching family member object
- if there is no family member with the given id in the collection, the response code is 404

**DELETE**, **PUT**, **PATCH** request to `/familymember/<id>`:

- the response code is 405 because the API does not allow deleting or modifying family members for any id value

