package jar.curoerp.module.pipeline.center;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.Instant;

public class PipelineCenterConnectionDealer {

	private Socket _socket;
	private Instant _troubleSince;

	public PipelineCenterConnectionDealer(Socket socket) {
		this._socket = socket;
		this.deal();
	}

	public void deal() {
		try {
			BufferedReader bufReader = new BufferedReader(new InputStreamReader(this._socket.getInputStream()));
		} catch (IOException e) {
			this._troubleSince = Instant.now();
			return;
		}

		//1. TODO: send PipelineSession


	}

	public Instant getTrouble() {
		return this._troubleSince;
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

}
