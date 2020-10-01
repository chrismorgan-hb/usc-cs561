Christopher Morgan
7468272635
CSCI 561 Project 1

Compilation Instructions:
-------------------------
I used gmake and built a Makefile.  Just type "gmake" in the directory with the files and it will 
build proj1.

Execution Instructions:
-----------------------

> proj1 -mapFile <mapFilename> -heuristicFile <heuristicFilename> -startCity <start> -goalCity <goal>

Options:

	<mapFilename>:		Path/filename to map input file.  (default "map.txt")  Format: dist city1 city2
	<heuristicFilename>:	Path/filename to heuristic input file.  (default "heuristic.txt") Format: city h
	<start>:		Start City (default 'A')
	<goal>:			Goal City (default 'B', 'B' is always goal for A*)

Notes:
------------------------
map.txt and heuristic.txt are the map and heuristic files that were provided on the
class website.

The program handles the case where there is no solution by printing every possible path
and then saying "no solution" in the summary.

Solution format in the summary is (distance, path).
