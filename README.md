# Fire Ant
Http server for MongoDB

This is a fully fledged Http server that serve files to users. It accepts queries in the form of a JSON object and runs them on a Mongo db and returns the results in JSON format.

###Running the code.

To run the project you will need Java runtime environment installed and also potentially admin rights on the pc you are executing this on.

Download and extract the zip and run it using Java command as follows

`sudo java -jar FireAnt.jar`

The following are various options that you could use in conjunction to the above command.

Various options when invoking this are:
```
--portno  The port number where the server needs to run. Default is 80.
--mongoip The ip address where MongoDB is running. Default is localhost.
--mongoportno The port number where the mongo is running. Default is 27017.
--root The location from where I need to serve the requested files from. Default is "/".
--maxcachesize The maximum number of files that should be cached. Default is 25.
```
###Using the server.

To access MongoDB through my webserver send a POST request with the following information.
```
"database" name of the database.
"collections" name of the collection.
"data" query related data.
"operation" the operation you wish to perform, "select one", "select many", "update one", "update many", "delete one", "delete many", "insert"
```
For insertion it's just the document that you wish to insert.

For deletion its a document of the type {"field1": value1, "field2": value2} this document is used to find all the documents that have a matching values in the fields given by user and deletes it.

For updation, you need to pass two documents. {"selectionField": {document}, "replacement": {document}} The selection field is used to find documents just like in the case of deletion and the replacement field is use to replace the matched documents.

For selection, you need to pass three documents. {"findField": {document}, "sortField": {document}, "projectionField": {document}}
Find field is used to select documents and it works the same way as selectionField used for updation. projectionField looks like {"field1": 1, "field2": 0} etc., any field with value 1 is included in the result. sortField is used to sort the result by the specified field. You specify it just like you would specify it in a Mongo query.

This work is licensed under 3 clause BSD. 
