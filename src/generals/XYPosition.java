package generals;

public class XYPosition {
	private int posx;
	private int posy;
	
	public XYPosition (int posx, int posy){
		this.posx = posx;
		this.posy = posy;
	}
	
	public void updatePosition (int posx, int posy){
		this.posx=posx;
		this.posy = posy;
	}
	
	public XYPosition getPosition () {
		return this;
	}

	public int getPosx() {
		return posx;
	}
	
	public int getPosy(){
		return posy;
	}


}
