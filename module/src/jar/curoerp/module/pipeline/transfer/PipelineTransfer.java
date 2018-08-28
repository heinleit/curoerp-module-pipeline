package jar.curoerp.module.pipeline.transfer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.ArrayList;

import de.curoerp.core.logging.LoggingService;
import jar.curoerp.module.pipeline.PipelineStorage;

public class PipelineTransfer implements IPipelineTransfer {
	
	private PipelineStorage storage;
	private ArrayList<IPipelineTransferReceiver> receivers = new ArrayList<>();
	
	public PipelineTransfer(PipelineStorage storage) {
		// dependencies
		this.storage = storage;
		
	}
	
	public void start() {
		this.createReader().start();
		this.createWriter().start();
	}
	
	private boolean _isReader = false;
	protected Thread createReader() {
		return new Thread(() -> {
			if(this._isReader) {
				return;
			}
			
			while(this.storage.isSocketOpen()) {
				this.storage.solveTrouble();
				this._isReader = true;
				this.L("new reader");
				
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(this.storage.getSocketInputStream()));
					String line = null;
					//System.out.println("Wait for new line");
					while((line = reader.readLine()) != null) {
						//System.out.println("New Line: " + line);
						for (IPipelineTransferReceiver receiver : this.receivers) {
							receiver.receiveLine(line);
						}
					}
					
				}
				catch (SocketException e) {
					// client disconnected
					if(e.getMessage().contains("Connection reset")) {
						this.storage.closeSocket();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				this.storage.setTrouble();
			}
			
			this.L("connection closed");
			this._isReader = false;
		});
	}
	
	private boolean _isWriter = false;
	protected Thread createWriter() {
		return new Thread(() -> {
			if(this._isWriter) {
				return;
			}
			
			while(this.storage.isSocketOpen()) {
				this._isWriter = true;
				
				try {
					PrintWriter writer = new PrintWriter(this.storage.getSocketOutputStream());
					String next = null;
					while(true) {
						next = this.storage.next();
						if(next != null) {
							this.L("send line");
							writer.println(next);
							writer.flush();
							//System.out.println("Sended: " + next);
						}
						Thread.sleep(1000);
					}
				} catch (IOException | InterruptedException e) {
					//LoggingService.error(e);
					e.printStackTrace();
				}
				
			}
			this._isWriter = false;
		});
	}
	
	@Override
	public void send(String str) {
		this.storage.addOutput(str);
	}

	@Override
	public void addReceiver(IPipelineTransferReceiver receiver) {
		this.receivers.add(receiver);
	}

	private void L(String msg) {
		LoggingService.info(this.storage.getClientIdentifier() + ":" + msg);
	}
}
