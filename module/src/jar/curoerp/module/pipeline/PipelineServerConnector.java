package jar.curoerp.module.pipeline;

import java.net.Socket;

import jar.curoerp.module.pipeline.receptionist.IPipelineReceptionist;

public class PipelineServerConnector {

	private PipelineService service;

	public PipelineServerConnector(Socket socket, IPipelineReceptionist receptionist) {

		this.service = new PipelineService(socket, receptionist);
		this.service.start();
		
	}

	public void close() {
		this.service.close();
	}

	public boolean canClose() {
		// TODO Auto-generated method stub
		return false;
	}

}
