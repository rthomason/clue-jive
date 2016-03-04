# clue-jive

This project is forked from an early version of [clue](https://github.com/javasoze/clue) and made to work with Lucene 3.x with reduced functionality to prevent users from doing anything destructive to their index.

To run it:

> java -jar clue-jive.jar ~/var/data/contentSearch/contentSwappingSearch/`<tenant_id>`/dedupIndex-`<timestamp>`/

Example usage:

```

> help
directory - prints directory information
exit - exits program
explain - shows score explanation of a doc
fields - gets the fields of the search index
help - displays help
info - displays information about the index, including number of lucene docs
norm - displays norm values for a field for a document
readonly - puts clue in readonly mode
search - executes a query against the index: field:query
stored - displays stored data for a given field
terms - gets terms from the index, <field:term>, term can be a prefix
tv - shows term vector of a field for a doc

> fields
available fields are:
  tenantId
  objectIDandType
  objectType
  userID
  modificationDate
  dateForAge
  container
  source
  language
  parentIDandType
  subject_en
  subject_simple
  unstemmedSubject_en
  subject_ng
  untokenizedSubject
  body_en
  body_simple
  body_ng

> info
readonly mode: true
numdocs: 90
maxdoc: 90
num deleted docs: 0
current version: 1435771591613
segment count: 1
unique term count: 48659

> search subject_en:sp*
parsed query: subject_en:sp*
numhits: 2
time: 18ms
doc: 21, score: 1.0
doc: 75, score: 1.0

> stored objectIDandType 21
2|1001

```
