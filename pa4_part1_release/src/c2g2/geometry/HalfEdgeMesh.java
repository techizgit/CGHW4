package c2g2.geometry;

import java.util.ArrayList;

import org.joml.Vector3f;


import c2g2.engine.graph.Mesh;

/*
 * A mesh represented by HalfEdge data structure
 */
public class HalfEdgeMesh {

	private ArrayList<HalfEdge> halfEdges;
    private ArrayList<Vertex> vertices;


	public HalfEdgeMesh(Mesh mesh) {  
        float[] pos = mesh.getPos();
        float[] norms = mesh.getNorms();
        int[] inds = mesh.getInds();
        int[] vertexPositionInArray = new int[inds.length];
        int arrayPosition = 0;
        
        //Initialize vertex position array, -1 means not yet processed
        for(int i=0;i<inds.length;i++) vertexPositionInArray[i]=-1; 

        halfEdges = new ArrayList<>();
        vertices = new ArrayList<>();


        int vid=0;
        //Process every face
        for(int i=0;i<inds.length/3;i++){     
        	
        	Face face = new Face();
        	
        	HalfEdge HEdge_1 = new HalfEdge();
        	vid = inds[3*i];	
        	Vertex vertex_1 = null;
        	//If the vertex has already been processed, then do not generate a new vertex
        	if (vertexPositionInArray[vid] == -1){
            	vertex_1 = new Vertex(vid,pos[3*vid],pos[3*vid+1],pos[3*vid+2],norms[3*vid],norms[3*vid+1],norms[3*vid+2]);
            	vertices.add(vertex_1);        	 
            	vertexPositionInArray[vid] = arrayPosition;
            	arrayPosition = arrayPosition + 1;
        	} else {
        		vertex_1 = vertices.get(vertexPositionInArray[vid]);       		
        	}    
         	
        	HEdge_1.setlFace(face);
        	
        	//All three half edges should be processed
        	HalfEdge HEdge_2 = new HalfEdge();
        	vid = inds[3*i+1];
        	Vertex vertex_2 = null;
        	if (vertexPositionInArray[vid] == -1){
            	vertex_2 = new Vertex(vid,pos[3*vid],pos[3*vid+1],pos[3*vid+2],norms[3*vid],norms[3*vid+1],norms[3*vid+2]);
            	vertices.add(vertex_2);        	
            	vertexPositionInArray[vid] = arrayPosition;
            	arrayPosition = arrayPosition + 1;
        	} else {
        		vertex_2 = vertices.get(vertexPositionInArray[vid]);
        	}
        	HEdge_2.setlFace(face);
	
        	HalfEdge HEdge_3 = new HalfEdge();
        	vid = inds[3*i+2];
        	Vertex vertex_3 = null;
        	if (vertexPositionInArray[vid] == -1){
            	vertex_3 = new Vertex(vid,pos[3*vid],pos[3*vid+1],pos[3*vid+2],norms[3*vid],norms[3*vid+1],norms[3*vid+2]);
            	vertices.add(vertex_3);        	
            	vertexPositionInArray[vid] = arrayPosition;
            	arrayPosition = arrayPosition + 1;
        	} else {
        		vertex_3 = vertices.get(vertexPositionInArray[vid]);
        	}
        	HEdge_3.setlFace(face);
        	      	
           	face.setEdge(HEdge_1);
            
           	
           	//To find if this half edge has a flip(opposite) edge
           	//I did not use hash map method thus the time complexity for this query is not O(1)
           	HalfEdge currentFromEdge = new HalfEdge();     	
        	if (vertex_1.hasEdge()) {
        		
        		for(int j=0;j<vertex_1.fromEdgeSize();j++){
        			currentFromEdge = vertex_1.findFromEdge(j);
        			if (currentFromEdge.getNextV() == vertex_2) {
        				HEdge_1.setFlipE(currentFromEdge);
        				currentFromEdge.setFlipE(HEdge_1);
        			}
        		}
        	}
        	
        	if (vertex_2.hasEdge()) {
        		
        		for(int j=0;j<vertex_2.fromEdgeSize();j++){
        			currentFromEdge = vertex_2.findFromEdge(j);
        			if (currentFromEdge.getNextV() == vertex_3) {
        				HEdge_2.setFlipE(currentFromEdge);
        				currentFromEdge.setFlipE(HEdge_2);
        			}
        		}
        	}
        	
        	if (vertex_3.hasEdge()) {
        		
        		for(int j=0;j<vertex_3.fromEdgeSize();j++){
        			currentFromEdge = vertex_3.findFromEdge(j);
        			if (currentFromEdge.getNextV() == vertex_1) {
        				HEdge_3.setFlipE(currentFromEdge);
        				currentFromEdge.setFlipE(HEdge_3);
        			}
        		}
        	}
        	
        	
	       	
        	HEdge_1.setNextE(HEdge_2);//Setting the next half edge for half edges
        	HEdge_2.setNextE(HEdge_3);
        	HEdge_3.setNextE(HEdge_1);  
           	vertex_1.setEdge(HEdge_1);//Setting corresponding edges for vertices
           	vertex_2.setEdge(HEdge_2);
           	vertex_3.setEdge(HEdge_3);          	
           	vertex_1.addFromEdge(HEdge_3);//Maintaining an array which contains all edges pointing to this vertex
           	vertex_2.addFromEdge(HEdge_1);
           	vertex_3.addFromEdge(HEdge_2);
        	HEdge_1.setNextV(vertex_1);//Here I set the vertex as the "origin" of the half edge.
        	HEdge_2.setNextV(vertex_2);
        	HEdge_3.setNextV(vertex_3);
        	
        	halfEdges.add(HEdge_1);
        	halfEdges.add(HEdge_2);
        	halfEdges.add(HEdge_3);
        }
	}
	
	public Mesh toMesh() {
        // student code starts here
		int vertexNumber = halfEdges.size();
	
	    float[] pos = new float[vertexNumber*3];
	    float[] norms = new float[vertexNumber*3];
	    float[] textCoords = new float[vertexNumber*2];
	    int[] inds = new int[vertexNumber];
	    Vector3f posVec = new Vector3f();
	    Vector3f normVec = new Vector3f();
	    
	    //Process every half edge and create triangles for OpenGL
	    for(int i=0;i<vertexNumber;i++){
	    	inds[i] = halfEdges.get(i).getNextV().getId();
	    	posVec = halfEdges.get(i).getNextV().getPos();
	    	pos[3*inds[i]] = posVec.x;
	    	pos[3*inds[i]+1] = posVec.y;
	    	pos[3*inds[i]+2] = posVec.z;
	    	normVec = halfEdges.get(i).getNextV().getNorm();
	    	norms[3*inds[i]] = normVec.x;
	    	norms[3*inds[i]+1] = normVec.y;
	    	norms[3*inds[i]+2] = normVec.z;
	    }
	    
	    Mesh tempMesh = new Mesh(pos, textCoords, norms, inds);
		return tempMesh;
	}
	
	/*
	 * Remove the first vertex from the HalfEdgeMesh. 
	 */
	public void removeFirstVertex() {
        if (halfEdges.isEmpty()) return;
        
		Vertex vertex = halfEdges.get(0).getNextV();
		removeVertex(vertex);
	}
	
    /*
     * Collapse the first edge from the HalfEdgeMesh. 
     */
	public void collapseFirstEdge() {
        if (halfEdges.isEmpty()) return;
        
        HalfEdge edge = halfEdges.get(0);
        Vertex v = edge.getNextV();
        Vertex u = edge.getFlipE().getNextV();
        Vertex newV = u.getAverage(v);
        newV.getNorm().normalize();
		collapseEdge(edge, newV);
	}
	
	public void removeVertex(Vertex vtx) {
		//student code
		//Remove original vertex
		vertices.remove(vtx);

		HalfEdge firstEdge = vtx.getEdge();
		HalfEdge currentEdge = firstEdge;
		//Decide substituting new vertex
		Vertex newVertex = firstEdge.getNextE().getNextV();
		
		if (currentEdge.getNextE().getNextE().getFlipE() != null) {
			//Remove half edges of triangles faces adjacent to first edge we decided
			//Modify the corresponding vertex attached to the remaining half edges whose "origin" is the vertex we removed
			//Change the opposite half edges of some of those edges we are going to go through
			halfEdges.remove(currentEdge);
			currentEdge = currentEdge.getNextE();
			halfEdges.remove(currentEdge);
			currentEdge.getNextE().getFlipE().setFlipE(currentEdge.getFlipE());
			currentEdge.getFlipE().setFlipE(currentEdge.getNextE().getFlipE());			
			currentEdge = currentEdge.getNextE();			
			halfEdges.remove(currentEdge);
			currentEdge = currentEdge.getFlipE();
			while (currentEdge.getNextE().getNextE().getFlipE() != firstEdge) {
				currentEdge.setNextV(newVertex);
				newVertex.setEdge(currentEdge);
				currentEdge = currentEdge.getNextE();
				currentEdge = currentEdge.getNextE();
				currentEdge = currentEdge.getFlipE();
			}		
			currentEdge.getFlipE().setFlipE(currentEdge.getNextE().getFlipE());
			currentEdge.getNextE().getFlipE().setFlipE(currentEdge.getFlipE());		
			halfEdges.remove(currentEdge);
			currentEdge = currentEdge.getNextE();
			halfEdges.remove(currentEdge);
			currentEdge = currentEdge.getNextE();
			halfEdges.remove(currentEdge);
		}

	}
	
	public void collapseEdge(HalfEdge edge, Vertex newV) {
		//student code
		HalfEdge currentEdge = edge;

		HalfEdge e1;
		HalfEdge e2;
		HalfEdge e3;
		HalfEdge e4;
		
		//Remove 6 half edges of two triangle faces that is going to be eliminated
		//Modify the corresponding vertex of half edges respectively whose "origin"s are those two vertices of the edge we want to remove
		//Change the opposite half edges of some of those edges we are going to go through
		
		halfEdges.remove(currentEdge);	
		currentEdge = currentEdge.getNextE();
		e1 = currentEdge;
		vertices.remove(e1.getNextV());
		halfEdges.remove(currentEdge);
		currentEdge = currentEdge.getNextE();
		e2 = currentEdge;
		halfEdges.remove(currentEdge);
		
		currentEdge = edge.getFlipE();
		
		halfEdges.remove(currentEdge);
		currentEdge = currentEdge.getNextE();
		e3 = currentEdge;
		vertices.remove(e3.getNextV());
		halfEdges.remove(currentEdge);
		currentEdge = currentEdge.getNextE();
		e4 = currentEdge;
		halfEdges.remove(currentEdge);
		
		vertices.add(newV);
		
		currentEdge = e4.getFlipE();
		while (currentEdge != e1) {
			currentEdge.setNextV(newV);
			currentEdge = currentEdge.getNextE();
			currentEdge = currentEdge.getNextE();
			currentEdge = currentEdge.getFlipE();
		}
		e1.setNextV(newV);
		newV.setEdge(e1);
		
		
		currentEdge = e2.getFlipE();
		while (currentEdge != e3) {
			currentEdge.setNextV(newV);
			currentEdge = currentEdge.getNextE();
			currentEdge = currentEdge.getNextE();
			currentEdge = currentEdge.getFlipE();
		}
		e3.setNextV(newV);
		newV.setEdge(e3);
		
		e1.getFlipE().setFlipE(e2.getFlipE());
		e2.getFlipE().setFlipE(e1.getFlipE());
		
		e3.getFlipE().setFlipE(e4.getFlipE());
		e4.getFlipE().setFlipE(e3.getFlipE());
		
	}


    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public ArrayList<HalfEdge> getEdges() {
        return halfEdges;
    }	
}
