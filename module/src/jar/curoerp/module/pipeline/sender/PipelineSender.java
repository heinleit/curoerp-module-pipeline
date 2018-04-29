package jar.curoerp.module.pipeline.sender;

import jar.curoerp.module.pipeline.IPipelineSender;
import jar.curoerp.module.pipeline.PipelineStorage;
import jar.curoerp.module.pipeline.helper.PipelineHelper;
import jar.curoerp.module.pipeline.helper.PipelineRequest;
import jar.curoerp.module.pipeline.helper.PipelineTag;
import jar.curoerp.module.pipeline.helper.PipelineType;
import jar.curoerp.module.pipeline.transfer.IPipelineTransfer;

public class PipelineSender implements IPipelineSender {

	private IPipelineTransfer transfer;
	private PipelineStorage storage;
	private static final int TIMEOUT = 30;
	private int counter = 0;
	
	public PipelineSender(IPipelineTransfer transfer, PipelineStorage storage) {
		this.transfer = transfer;
		this.storage = storage;
	}

	@Override	
	public Object sendAndReceive(PipelineRequest request) {
		int id = this.send(request, PipelineType.REQUEST);
		this.storage.addExpect(id);
		int counter = TIMEOUT * 10;
		while(!this.storage.isResult(id)) {
			if(counter-- <= 0) {
				return null;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return this.storage.getAndRemoveResult(id);
	}

	@Override
	public void answer(int id, Object obj) {
		this.send(id, obj, PipelineType.RESULT);
	}

	@Override
	public int send(Object obj) {
		return this.send(obj, PipelineType.INFO);
	}

	private int send(Object obj, PipelineType type) {
		return this.send(-1, obj, type);
	}


	private int send(int id, Object obj, PipelineType type) {
		if(id < 0) {
			id = counter++;
		}
		String json = PipelineHelper.serialize(obj);
		this.transfer.send(PipelineHelper.parseStatement(new PipelineTag(id, type, PipelineHelper.sha256(json), obj.getClass()), json));
		return id;
	}

	/*@Override
	public void info(PipelineRequest request) {
		
	}

	@Override
	public Object request(PipelineRequest request) {
		// TODO Auto-generated method stub
		return null;
	}*/

}
