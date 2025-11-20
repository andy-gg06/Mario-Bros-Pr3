package tp1.logic;

public interface GameModel {
	public boolean isFinished();
	public void update();
	public void reset(int nLevel);
	public void exit();
	public void addAction(Action dir);
	public boolean addObject(String[] objWords);
}
