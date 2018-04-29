package jar.curoerp.module.pipeline.shared;

import de.curoerp.core.logging.LoggingService;

public class PipelineSendParser implements IPipelineSender {

	private int identifier = 0;

	private final static int TIMEOUT = 30; //seconds

	private PipelineStorage _storage;
	private IPipelineSocket _sender;

	public PipelineSendParser(PipelineStorage storage, IPipelineSocket sender) {
		this._storage = storage;
		this._sender = sender;
	}

	public Object sendAndReceive(PipelineRequest request) {
		int id = this.send(request, PipelineType.REQUEST);
		this._storage.addExpect(id);
		int counter = TIMEOUT * 10;
		while(!this._storage.isResult(id)) {
			if(counter-- <= 0) {
				return null;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				LoggingService.error(e);
			}
		}
		return this._storage.getAndRemoveResult(id);
	}

	public void answer(int id, Object obj) {
		this.send(id, obj, PipelineType.RESULT);
	}

	public int send(Object obj) {
		return this.send(obj, PipelineType.INFO);
	}

	private int send(Object obj, PipelineType type) {
		return this.send(-1, obj, type);
	}


	private int send(int id, Object obj, PipelineType type) {
		if(id < 0) {
			id = identifier++;
		}
		String json = PipelineSharedHelper.serialize(obj);
		this._sender.send(PipelineSharedHelper.parseStatement(new PipelineTag(id, type, PipelineSharedHelper.sha256(json), obj.getClass()), json));
		return id;
	}


}
