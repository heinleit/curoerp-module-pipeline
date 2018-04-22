package jar.curoerp.module.pipeline;

import java.time.LocalDateTime;
import java.util.Random;

public class PipelineSession {

	private String clientName;
	private String host;
	private int port = -1;
	private int _random;
	
	private static Random randomizer = new Random();

	public PipelineSession(String clientName) {
		this.clientName = clientName;
		this._random = randomizer.nextInt(10000);
	}

	public String getClientName() {
		return clientName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		if(this.host != null) return;
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		if(this.port > -1) return;
		this.port = port;
	}

	@Override
	public int hashCode() {
		return this.host.hashCode()
				+ (this.port * 10)
				+ (this.clientName.hashCode() * 100)
				+ (this._random * 1000)
				+ LocalDateTime.now().getSecond() * 10000;
	}
}
