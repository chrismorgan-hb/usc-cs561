#Makefile for Project 1
#CSCI 561 Fa2006
#Author: Christopher Morgan

CXX = g++
CXXFLAGS = -g 
WOBJS = AStarNode.o AStarSolver.o BFSolver.o DFSolver.o Node.o Pair.o Problem.o project1.o Solver.o Triplet.o 
WCC = AStarNode.cc AStarSolver.cc BFSolver.cc DFSolver.cc Node.cc Pair.cc Problem.cc project1.cc Solver.cc Triplet.cc 
WH = AStarNode.h AStarSolver.h BFSolver.h DFSolver.h Node.h Pair.h Problem.h Solver.h Triplet.h 
OTHER = Makefile README

all: $(WOBJS)
	$(CXX) $(CXXFLAGS) -o proj1 $(WOBJS)

AStarNode.o: Node.o AStarNode.h AStarNode.cc 
	$(CXX) $(CXXFLAGS) -c AStarNode.cc

AStarSolver.o: Solver.o AStarNode.o AStarSolver.h AStarSolver.cc
	$(CXX) $(CXXFLAGS) -c AStarSolver.cc

BFSolver.o: Solver.o Pair.o BFSolver.h BFSolver.cc
	$(CXX) $(CXXFLAGS) -c BFSolver.cc

DFSolver.o: Solver.o Pair.o DFSolver.h DFSolver.cc
	$(CXX) $(CXXFLAGS) -c DFSolver.cc

Node.o: Node.h Node.cc 
	$(CXX) $(CXXFLAGS) -c Node.cc

Pair.o: Pair.h Pair.cc 
	$(CXX) $(CXXFLAGS) -c Pair.cc

Problem.o: Triplet.o Pair.o Problem.h Problem.cc 
	$(CXX) $(CXXFLAGS) -c Problem.cc

project1.o: Problem.o Pair.o Triplet.o Solver.o BFSolver.o DFSolver.o AStarSolver.o project1.cc 
	$(CXX) $(CXXFLAGS) -c project1.cc

Solver.o: Node.o Solver.h Solver.cc 
	$(CXX) $(CXXFLAGS) -c Solver.cc

Triplet.o: Triplet.h Triplet.cc 
	$(CXX) $(CXXFLAGS) -c Triplet.cc
