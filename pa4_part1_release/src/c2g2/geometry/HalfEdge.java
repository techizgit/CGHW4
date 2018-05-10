package c2g2.geometry;

/*
 * The main class that implements the half-edge structure.
 * 
 * Please add more code if needed. Feel free to change instance variables
 * to public if more convenient.
 */
public class HalfEdge {
	// id per full edge (not needed for part1)
	private int id;
	// A pointer to the next halfEdge 
	private HalfEdge nextE = null;
	// A pointer to the HalfEdge that is along the opposite direction
	private HalfEdge flipE = null;
	// A pointer to the next vertex
	private Vertex nextV = null;
	// A pointer to the left face
	private Face lFace=null;

	public HalfEdge() {};

	public HalfEdge(HalfEdge nextE, HalfEdge flipE, Vertex nextV, Face lFace) {
		this.nextE = nextE;
		this.flipE = flipE;
		this.nextV = nextV;
		this.lFace = lFace;
		this.id = -1;
	}
	
	public Face getlFace(){
		return lFace;
	}
	
	public HalfEdge getNextE(){
		return nextE;
	}
	
	public HalfEdge getFlipE(){
		return flipE;
	}
	
	public Vertex getNextV(){
		return nextV;
	}

	public int getId() {
		return id;
	}
	
	public void setlFace(Face f){
		lFace = f;
	}
	
	public void setNextE(HalfEdge e){
		nextE = e;
	}
	
	public void setFlipE(HalfEdge e){
		flipE = e;
	}
	
	public void setNextV(Vertex v) {
		nextV = v;
	}

	public void setId(int id) {
		this.id = id;
		this.flipE.id = id;
	}

	public String toString() {
		return "<" + flipE.getNextV().getId() + ", " + nextV.getId() + ">";
	}
}
