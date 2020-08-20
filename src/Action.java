public interface Action {
	public void performStep();
	public void addTokens(int tokens);
	public boolean hasTokens();
}