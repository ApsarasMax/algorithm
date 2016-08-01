/*
 * Name: <your name>
 * EID: <your EID>
 */

import java.util.Vector;
import java.util.Set;
import java.util.HashSet;
import java.util.Stack;
import java.util.Collections;

/* Your solution goes in this file.
 *
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */

public class Program2 extends VertexNetwork {
    /* DO NOT FORGET to add a graph representation and 
       any other fields and/or methods that you think 
       will be useful. 
       DO NOT FORGET to modify the constructors when you 
       add new fields to the Program2 class. */

    public double[][] adjLatencyMatrix=null;

    public void fillAdjMatrix(){
    	int n = super.location.size();
    	adjLatencyMatrix = new double[n][n];
    	for(int i=0;i<n;i++){
    		adjLatencyMatrix[i][i]=Double.MAX_VALUE;
    		for(int j=i+1;j<n;j++){
    			double curLatency = super.edges.get((int)(-i*i/2.0+(n-3.0/2)*i+j-1)).getW();
    			adjLatencyMatrix[i][j]=curLatency;
    			adjLatencyMatrix[j][i]=curLatency;
    		}
    	}
    }
    
    Program2() {
        super();
    }
    
    Program2(String locationFile) {
        super(locationFile);
    }
    
    Program2(String locationFile, double transmissionRange) {
        super(locationFile, transmissionRange);
    }
    
    Program2(double transmissionRange, String locationFile) {
        super(transmissionRange, locationFile);
    }

    //Class Node is used in Dijkstra Algorithm
    public class Node{

		private Vertex nodeLocation;
		private double latency;
		private Node fromNode;
		private int index;

		Node(){
		    this.nodeLocation = new Vertex(0.0, 0.0);
		    this.latency = 0.0;
		    this.fromNode = new Node(new Vertex(0.0, 0.0), Double.MAX_VALUE, null, 0);
		    this.index = 0;
		}

		Node(Vertex nodeLocation, double latency, Node fromNode, int index){
		    this.nodeLocation = nodeLocation;
		    this.latency = latency;
		    this.fromNode = fromNode;
		    this.index = index;
		}

		Node(Node node){
			this.nodeLocation = node.nodeLocation;
		    this.latency = node.latency;
		    this.fromNode = node.fromNode;
		    this.index = node.index;
		}

		public void setNodeLocation(Vertex nodeLocation){
		    this.nodeLocation = nodeLocation;
		}

		public Vertex getNodeLocation(){
		    return this.nodeLocation;
		}

		public void setLatency(double latency){
		    this.latency = latency;
		}

		public double getLatency(){
		    return this.latency;
		}

		public void setFromNode(Node fromNode){
		    this.fromNode = fromNode;
		}

		public Node getFromNode(){
		    return this.fromNode;
		}

		public void setIndex(int index){
			this.index = index;
		}

		public int getIndex(){
			return this.index;
		}
    }


    public Vector<Vertex> gpsrPath(int sourceIndex, int sinkIndex) {
        /* This method returns a path from a source at location sourceIndex 
           and a sink at location sinkIndex using the GPSR algorithm. An empty 
           path is returned if the GPSR algorithm fails to find a path. */
        /* The following code is meant to be a placeholder that simply 
           returns an empty path. Replace it with your own code that 
           implements the GPSR algorithm. */
	
		//Vector<Vertex> res1 = new Vector<Vertex>();
		Vector<Vertex> res = new Vector<Vertex>(0);
		int n=super.location.size();
		Vertex source = super.location.get(sourceIndex);
		Vertex sink = super.location.get(sinkIndex);
		double distanceToSink = source.distance(sink);
		double originalDistance = distanceToSink;
		res.add(source);
		
		while(source!=sink){
		    Vertex sourceCandidate = null;
		    for(int i=0;i<n;i++){
			Vertex tmpVertex = super.location.get(i);
			double tmpDistanceToSource = source.distance(tmpVertex);
			double tmpDistanceToSink = sink.distance(tmpVertex);
			if(tmpDistanceToSource<=super.transmissionRange && tmpDistanceToSink<distanceToSink){
			    distanceToSink = tmpDistanceToSink;
			    sourceCandidate = tmpVertex;
			}
		    }
		    if(sourceCandidate!=null){
			res.add(sourceCandidate);
			source = sourceCandidate;
		    }else{
			return new Vector<Vertex>(0);
		    }
		}
		//res.add(sink);
		return res;
    }
    
    public Vector<Vertex> dijkstraPathLatency(int sourceIndex, int sinkIndex) {
        /* This method returns a path (shortest in terms of latency) from a source at
           location sourceIndex and a sink at location sinkIndex using Dijkstra's algorithm.
           An empty path is returned if Dijkstra's algorithm fails to find a path. */
        /* The following code is meant to be a placeholder that simply 
           returns an empty path. Replace it with your own code that 
           implements Dijkstra's algorithm. */
        
        if(adjLatencyMatrix==null) fillAdjMatrix();

        int n = super.location.size();
		Vertex source = super.location.get(sourceIndex);
		Vertex sink = super.location.get(sinkIndex);
		
		//build nodes vector
		Vector<Node> nodes = new Vector<Node>(n);
		for(int i=0;i<n;i++){
		    nodes.add(new Node(super.location.get(i), Double.MAX_VALUE, null, i));
		}
		Node sourceNode = nodes.get(sourceIndex);
		Node originalNode = new Node(sourceNode); 
		sourceNode.setLatency(0.0);

		//use set to store visited nodes
		Set<Vertex> visitedNodes = new HashSet<>();

		//dijkstra
		while(sourceNode.getNodeLocation()!=sink){
			Node sourceCandidate = null;
			double sourceCandidateLatency = Double.MAX_VALUE;
			visitedNodes.add(sourceNode.getNodeLocation());
			for(int i=0;i<n;i++){
				Node tmpNode = nodes.get(i);
				if(visitedNodes.contains(tmpNode.getNodeLocation())) continue;
				double curTmpNodeLatency = tmpNode.getLatency();
			    if(sourceNode.getNodeLocation().distance(tmpNode.getNodeLocation()) <= super.transmissionRange){
					double edgeLatency = adjLatencyMatrix[i][sourceNode.getIndex()];
					double computedLatency = sourceNode.getLatency() + edgeLatency;
					if(computedLatency < curTmpNodeLatency){
						tmpNode.setLatency(computedLatency);
						tmpNode.setFromNode(sourceNode);
						if(computedLatency < sourceCandidateLatency){
							sourceCandidateLatency = computedLatency;
							sourceCandidate = tmpNode;
						}
					}
			    }
			    if(curTmpNodeLatency<sourceCandidateLatency){
					sourceCandidateLatency = curTmpNodeLatency;
					sourceCandidate = tmpNode;
				}
			}
			if(sourceCandidate==null) return new Vector<Vertex>(0);
			sourceNode = sourceCandidate;
		}

		//result
		Stack<Vertex> stack = new Stack<>();
		stack.push(sink);
		while(sourceNode.getNodeLocation()!=originalNode.getNodeLocation()){
			sourceNode = sourceNode.getFromNode();
			stack.push(sourceNode.getNodeLocation());
		}
		//return
		Vector<Vertex> res = new Vector<Vertex>(0);
		while(!stack.isEmpty()){
			res.add(stack.pop());
		}
		return res;
    }
    
    public Vector<Vertex> dijkstraPathHops(int sourceIndex, int sinkIndex) {
        /* This method returns a path (shortest in terms of hops) from a source at
           location sourceIndex and a sink at location sinkIndex using Dijkstra's algorithm.
           An empty path is returned if Dijkstra's algorithm fails to find a path. */
        /* The following code is meant to be a placeholder that simply 
           returns an empty path. Replace it with your own code that 
           implements Dijkstra's algorithm. */
        int n = super.location.size();
		Vertex source = super.location.get(sourceIndex);
		Vertex sink = super.location.get(sinkIndex);
		
		//build nodes vector
		Vector<Node> nodes = new Vector<Node>(n);
		for(int i=0;i<n;i++){
		    nodes.add(new Node(super.location.get(i), Double.MAX_VALUE, null, i));
		}
		Node sourceNode = nodes.get(sourceIndex);
		Node originalNode = new Node(sourceNode); 
		sourceNode.setLatency(0.0);

		//use set to store visited nodes
		Set<Vertex> visitedNodes = new HashSet<>();

		//dijkstra
		while(sourceNode.getNodeLocation()!=sink){
			Node sourceCandidate = null;
			double sourceCandidateLatency = Double.MAX_VALUE;
			visitedNodes.add(sourceNode.getNodeLocation());
			for(int i=0;i<n;i++){
				Node tmpNode = nodes.get(i);
				if(visitedNodes.contains(tmpNode.getNodeLocation())) continue;
				double curTmpNodeLatency = tmpNode.getLatency();
			    if(sourceNode.getNodeLocation().distance(tmpNode.getNodeLocation()) <= super.transmissionRange){
					double computedLatency = sourceNode.getLatency() + 1.0;//edgeLatency=1
					if(computedLatency < curTmpNodeLatency){
						tmpNode.setLatency(computedLatency);
						tmpNode.setFromNode(sourceNode);
						if(computedLatency < sourceCandidateLatency){
							sourceCandidateLatency = computedLatency;
							sourceCandidate = tmpNode;
						}
					}
			    }
			    if(curTmpNodeLatency<sourceCandidateLatency){
					sourceCandidateLatency = curTmpNodeLatency;
					sourceCandidate = tmpNode;
				}
			}
			if(sourceCandidate==null) return new Vector<Vertex>(0);
			sourceNode = sourceCandidate;
		}

		//result
		Stack<Vertex> stack = new Stack<>();
		stack.push(sink);
		while(sourceNode.getNodeLocation()!=originalNode.getNodeLocation()){
			sourceNode = sourceNode.getFromNode();
			stack.push(sourceNode.getNodeLocation());
		}
		//return
		Vector<Vertex> res = new Vector<Vertex>(0);
		while(!stack.isEmpty()){
			res.add(stack.pop());
		}

		return res;
    }
    
}

