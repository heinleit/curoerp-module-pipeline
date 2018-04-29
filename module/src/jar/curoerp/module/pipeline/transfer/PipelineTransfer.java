package jar.curoerp.module.pipeline.transfer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

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
				
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(this.storage.getSocketInputStream()));
					String line = null;
					while((line = reader.readLine()) != null) {
						System.out.println("New Line: " + line);
						for (IPipelineTransferReceiver receiver : this.receivers) {
							receiver.receiveLine(line);
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.storage.setTrouble();
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
			
			while(this.storage.isSocketOpen()) {
				this._isWriter = true;
				
				try {
					PrintWriter writer = new PrintWriter(this.storage.getSocketOutputStream());
					String next = null;
					while(true) {
						next = this.storage.next();
						if(next != null) {
							writer.println(next);
							System.out.println("Send: " + next);
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

}
