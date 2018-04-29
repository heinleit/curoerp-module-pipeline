package jar.curoerp.module.pipeline.processor;

import java.io.IOException;
import java.util.HashMap;

import jar.curoerp.module.pipeline.IPipelineSender;
import jar.curoerp.module.pipeline.PipelineStorage;
import jar.curoerp.module.pipeline.helper.PipelineHelper;
import jar.curoerp.module.pipeline.helper.PipelineRequest;
import jar.curoerp.module.pipeline.helper.PipelineTag;
import jar.curoerp.module.pipeline.helper.PipelineType;
import jar.curoerp.module.pipeline.transfer.IPipelineTransferReceiver;

public class PipelineProcessor implements IPipelineTransferReceiver {

	private StringBuilder _cache = new StringBuilder();
	private PipelineTag _start;
	private HashMap<PipelineTag, PipelineRequest> _received = new HashMap<>();

	private IPipelineReceptionist receptionist;
	private IPipelineSender sender;
	private PipelineStorage storage;

	public PipelineProcessor(IPipelineReceptionist receptionist, IPipelineSender sender, PipelineStorage storage) {
		this.receptionist = receptionist;
		this.sender = sender;
		this.storage = storage;
	}

	public void heartbeat() {
		for (PipelineTag id : this._received.keySet().stream().toArray(c -> new PipelineTag[c])) {
			PipelineRequest request = this._received.get(id);
			this._received.remove(id);

			switch (id.getType()) {
			default:
			case INFO:
				this.receptionist.info(request);
				break;
			case REQUEST:
				Object result = this.receptionist.request(request);
				this.sender.answer(id.getId(), result);
				break;
			}

		}
	}

	@Override
	public void receiveLine(String line) {
		try {
			if(line == null || line.trim().length() == 0) {
				return;
			}

			// not started
			if(_start == null) {
				PipelineTag start = PipelineHelper.parseTag("start", line);

				// start incompatible?
				if(start == null) {
					return;
				}

				// a result, that not requested? ignore..
				if(start.getType() == PipelineType.RESULT && !this.storage.isExpect(start.getId())) {
					return;
				}

				this._start = start;

				return;
			}

			// detect end
			PipelineTag end = PipelineHelper.parseTag("end", line);
			if(end != null) {
				// cleanup everything for garbage-collector
				String content = this._cache.toString();
				this._cache = new StringBuilder();
				PipelineTag start = this._start;
				this._start = null;

				if(!start.match(end)) {
					throw new IOException("start(" + start + ") != end(" + end + ")");
				}

				if(!start.validateContent(content)) {
					throw new IOException("hash doesn't match to content!");
				}

				switch(start.getType()) {
				default:
				case INFO:
					this._received.put(start, PipelineHelper.deserialize(PipelineRequest.class, content));
					break;
				case RESULT:
					this.storage.addResult(start.getId(), PipelineHelper.deserialize(start.getCls(), content));
					break;
				}

				return;
			}

			this._cache.append(line);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
