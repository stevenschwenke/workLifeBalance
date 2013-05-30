workLifeBalance
===============

Architecture
============

Definition
----------

My understanding of the termin "software architecture" is based on two definitions:

1. "That which is fundamental to understanding a system in its environment" (ISO/IEC/IEEE 42010)
2. "Things that people perceive as hard to change" (Fowler, M. (2003). "Design - Who needs an architect?")
 
According to these definitions, I want to document my system with the following aspects:

1. Tool Chain
2. Technology Stack
3. Important Design Decisions

As proposed by [Stefan Zörner](http://www.dokchess.de/), for every issue I note
- the problem to be solved,
- possible solutions / options and
- the decision / solution with an explanation.


Tool Chain
-----------
- IDE
 - Problem: What IDE to use?
 - options: Eclipse (free, I use it at work and could get more familiar with it, great plugins BUT ALSO slow / unstable), IDEA (fast, very nice build-in features BUT ALSO costs money)
 - decision: **Eclipse**. To increase my productivity in the projects I get paid for, I have to get more familiar with the IDE used there.
- Source Control
 - Problem: What source control to use?
 - options: SVN (know it already BUT ALSO have to set up my own server), Git (can commit while being offline, great support including a repository through github BUT ALSO github costs money)
 - decision: **Git** with github because its only 7$ per month and I want to get more familiar with Git.
- Package dependency management tool 
 - TODO (decision for Degraph)

Technology Stack
----------------
- **Persistence**
 - Problem: How to persist user-specific data?
 - options: 
 - Oracle-SQL-database: already experience BUT ALSO expensive and heavy-weight
 - HSQLDB: free, light-weight BUT ALSO no experience
 - Java DB: free, light-weight, already integrated in JDK BUT ALSO a relational DBMS that makes ORM necessary in manual or automated form
 - object-oriented databases: no gap between the objects in RAM and the persisted objects BUT ALSO there seems to be no good and widly-known implementation
 - decision: **Java DB**. I thought about using a NOSQL-DB but had no experience with that. Additionally, the gap between object oriented programming and realtional data storage can be bridget by ORMs or other techniques (see below).
- **object-relational mapping**
 - Problem: How to bridge the gap between object-oriented programming and relational persistence?
 - options: 
 - Hibernate: very good support, does 80% of the work with very less effort, good support for all CRUD-operations, have experience with it BUT ALSO the rest 20% of the work are quite cumbersome and Hibernate has a steep learning curve when it gets to this last 20% ([see this blog post](http://hallofthemountainking.wordpress.com/2011/11/11/now-why-am-i-so-much-in-love-with-mybatis/)
 - MyBatis: good separation of SQL-Queries to the rest of the code (reside in XML-files instead of in .java-files) and no error-generating "Change Tracking" like Hibernate BUT ALSO more made for requests than update/insert operations. Its focus lies on "just getting data out of the database", it's not an ORM in the first place. According to [Stackoverflow](http://programmers.stackexchange.com/questions/158109/what-are-the-advantages-of-mybatis-over-hibernate) best used with legacy databases.
 - decision: **MyBatis**. Although Hibernate is an allrounder than can provide for read- and write-operations and I have quite some experience with it, it has a boatload of functions and features such as change tracking that don't make life easier. Additionally, because I have the experience, I should learn a new ORM. MyBatis is lightweight in its functionalities and I can practice writing SQL statements.

Important Design Decisions
----------------
- Inter component communication in user interface code
 - Problem: Given that functionality is divided into smaller fragments that only have limited tasks and responsabilities, how should these modules communicate to each other? An example is one component that represents a dialog and another component that represents a table on another dialog. If data is inserted into the first component, how does it get to the table in the other component?
 - Options: Observer-Pattern (components can be registered in components and get notified if a change occurs) (widely known, have experience with it), eventbus (all components post events to a bus on via which all components are connectedand can be notified of change) (sounds easy enough to implement it BUT ALSO no expertise with it)
 - decision: **Inter component communication via Observer Pattern** because the UI components already form a tree. This tree structure can be used to bubble events up to the root and then communicate them down to every leave (W3C event model).
