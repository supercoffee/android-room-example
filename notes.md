# First impressions

Your SQL statements are checked at compile time. No more runtime errors due to misspelled column
names or table names. 

Enforces no DB queries unless specifically overridden. 



# Snags

First time running, I had to fix about 10 compile errors related to DB annotations

The names of tables are not pluralized based on the entity name.  If you want a pluralized table 
name, you have to specify this yourself.

No relationships: while you may link entities with foreign keys, ROOM does not generate fancy 
methods to retrieve related models. You must query each related model manually.  

Basic data types suck. No automatic ID updates once the objects are inserted.
You must assign the id field manually after inserting using a basic data type. 

# Bugs

When editing a phone number, clicking + will wipe out all changes to the phone number field. 
The query results are reloaded when a new phone is inserted, unsaved changes
are immediately replaced with the original phone number from the database. 
Be careful how you wire up observables with adapters. 