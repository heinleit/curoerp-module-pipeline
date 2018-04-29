package jar.curoerp.module.pipeline.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;

import de.curoerp.core.logging.LoggingService;

public abstract class PipelineSocketBase implements IPipelineReceptionist, IPipelineSocket {

	protected Socket _socket;
	private PipelineReceiveParser _receiver;
	private ArrayList<String> _sendQueue = new ArrayList<>();
	protected PipelineStorage storage = new PipelineStorage();
	protected PipelineSendParser _sender;
	protected Instant _troubleSince;
	
	public PipelineSocketBase() {
		this._sender = new PipelineSendParser(this.storage, (IPipelineSocket) this);
		this._receiver = new PipelineReceiveParser(this.storage, this._sender, (IPipelineReceptionist) this);
	}
	
	private boolean _isReader = false;
	protected Thread createReader() {
		return new Thread(() -> {
			if(this._isReader) {
				return;
			}
			
			while(this._socket != null) {
				this._troubleSince = null;
				this._isReader = true;
				
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(this._socket.getInputStream()));
					String line = null;
					while((line = reader.readLine()) != null) {
						System.out.println("New Line: " + line);
						this._receiver.receiveLine(line);
					}
					
				} catch (IOException e) {
					LoggingService.error(e);
				}
				this._troubleSince = Instant.now();
			}
			this._isReader = false;
		});
	}
	
	private boolean _isWriter = false;
	protected Thread createWriter() {
		return new Thread(() -> {
			if(this._isWriter) {
				return;
			}
			
			while(this._socket != null) {
				this._isWriter = true;
				
				try {
					PrintWriter writer = new PrintWriter(this._socket.getOutputStream());
					while(true) {
						for (String string : this._sendQueue.toArray(new String[this._sendQueue.size()])) {
							writer.println(string);
							System.out.println("Send: " + string);
							this._sendQueue.remove(string);
						}
						Thread.sleep(1000);
					}
				} catch (IOException | InterruptedException e) {
					LoggingService.error(e);
				}
				
			}
			this._isWriter = false;
		});
	}
	
	@Override
	public void send(String content) {
		this._sendQueue.add(content);
	}

	public Instant getTrouble() {
		return this._troubleSince;
	}
}
