package jar.curoerp.module.pipeline;

import java.util.Random;

public class PipelineSession {

	private String clientName;
	private int _random;
	
	private static int counter = 0;
	private int _counter = counter++;
	
	private static Random randomizer = new Random();

	public PipelineSession(String clientName) {
		this.clientName = clientName;
		this._random = randomizer.nextInt(10000);
	}

	public String getClientName() {
		return clientName;
	}

	@Override
	public int hashCode() {
		return (this.clientName.hashCode() * 100)
				+ (this._random * 1000)
				+ (this._counter * 10000);
	}
	
	@Override
	public boolean equals(Object obj) {
		try {
			PipelineSession other = (PipelineSession) obj;
			return other.hashCode() == this.hashCode();
		} catch(ClassCastException e) {
			return false;
		}
	}
}
