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
 - decision: Eclipse. To increase my productivity in the projects I get paid for, I have to get more familiar with the IDE used there.
- Source Control
 - Problem: What source control to use?
 - options: SVN (know it already BUT ALSO have to set up my own server), Git (can commit while being offline, great support including a repository through github BUT ALSO github costs money)
 - decision: Git with github because its only 7$ per month and I want to get more familiar with Git.
- Package dependency management tool 
 - TODO (decision for Degraph)

Technology Stack
----------------

Important Design Decisions
----------------
