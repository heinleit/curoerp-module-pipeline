package jar.curoerp.module.pipeline;

import java.net.Socket;

import jar.curoerp.module.pipeline.helper.PipelineRequest;
import jar.curoerp.module.pipeline.processor.PipelineProcessor;
import jar.curoerp.module.pipeline.proxy.IProxyRequestListener;
import jar.curoerp.module.pipeline.receptionist.IPipelineReceptionist;
import jar.curoerp.module.pipeline.sender.PipelineSender;
import jar.curoerp.module.pipeline.transfer.PipelineTransfer;

public class PipelineService implements IProxyRequestListener {

	private PipelineStorage storage;
	private PipelineTransfer transfer;
	private PipelineProcessor processor;
	private IPipelineSender sender;
	private IPipelineReceptionist receptionist;

	public PipelineService(Socket socket, IPipelineReceptionist receptionist) {
		// Storage (Helper)
		this.storage = new PipelineStorage();
		this.storage.setSocket(socket);
		
		// Receptionist (3rd Layer)
		this.receptionist = receptionist;

		// Transfer (1st Layer)
		this.transfer = new PipelineTransfer(this.storage);

		// Sender (2nd Layer)
		this.sender = new PipelineSender(this.transfer, this.storage);

		// Processor (4th Layer)
		this.processor = new PipelineProcessor(this.receptionist, this.sender, this.storage);
		this.transfer.addReceiver(this.processor);
	}

	public void start() {
		this.transfer.start();

		new Thread(() -> {
			while(this.storage.isSocketOpen()) {
				this.processor.heartbeat();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void close() {
		this.storage.closeSocket();
	}

	@Override
	public Object sendAndReceive(PipelineRequest request) {
		return this.sender.sendAndReceive(request);
	}

}
