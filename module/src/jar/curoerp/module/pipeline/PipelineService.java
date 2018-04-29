package jar.curoerp.module.pipeline;

import java.io.IOException;
import java.net.Socket;

import jar.curoerp.module.pipeline.processor.IPipelineReceptionist;
import jar.curoerp.module.pipeline.processor.PipelineProcessor;
import jar.curoerp.module.pipeline.receptionist.PipelineReceptionist;
import jar.curoerp.module.pipeline.sender.PipelineSender;

import jar.curoerp.module.pipeline.transfer.IPipelineTransferReceiver;
import jar.curoerp.module.pipeline.transfer.PipelineTransfer;

public abstract class PipelineService {

	private PipelineStorage storage;
	private PipelineTransfer transfer;
	private PipelineProcessor processor;
	private IPipelineSender sender;
	private IPipelineReceptionist receptionist;

	public PipelineService(Socket socket) {
		// Storage (Helper)
		this.storage = new PipelineStorage();
		this.storage.setSocket(socket);

		// Transfer (1st Layer)
		this.transfer = new PipelineTransfer(this.storage);

		// Sender (2nd Layer)
		this.sender = new PipelineSender(this.transfer, this.storage);

		// Receptionist (3rd Layer)
		this.receptionist = new PipelineReceptionist();

		// Processor (4th Layer)
		this.processor = new PipelineProcessor(this.receptionist, this.sender, this.storage);
		this.transfer.addReceiver((IPipelineTransferReceiver) this.sender);
	}

	public void start() {
		this.transfer.start();

		new Thread(() -> {
			while(this.storage.isSocketOpen()) {
				this.processor.heartbeat();
			}
		}).start();
	}

	public void close() {
		try {
			this.storage.closeSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
