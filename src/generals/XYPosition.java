package generals;

public class XYPosition {
	private int posx;
	private int posy;

	public XYPosition() {
		this.posx = 0;
		this.posy = 0;
	}

	public XYPosition(int posx, int posy) {
		this.posx = posx;
		this.posy = posy;
	}

	public void updatePosition(int posx, int posy) {
		this.posx = posx;
		this.posy = posy;
	}

	public XYPosition getPosition() {
		return this;
	}

	public int getPosx() {
		return posx;
	}

	public int getPosy() {
		return posy;
	}

	public void setPosx(int posx) {
		this.posx = posx;
	}

	public void setPosy(int posy) {
		this.posy = posy;
	}

}
