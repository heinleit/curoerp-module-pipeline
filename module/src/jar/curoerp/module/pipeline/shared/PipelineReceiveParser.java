package jar.curoerp.module.pipeline.shared;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import de.curoerp.core.logging.LoggingService;

public class PipelineReceiveParser {

	private StringBuilder _cache;
	private PipelineTag _start;
	private HashMap<PipelineTag, PipelineRequest> received;
	private PipelineStorage _storage;
	private IPipelineSender _sender;
	private IPipelineReceptionist _receptionist;
	
	public PipelineReceiveParser(PipelineStorage storage, IPipelineSender sender, IPipelineReceptionist receptionist) {
		this._cache = new StringBuilder();
		this.received = new HashMap<>();
		this._storage = storage;
		this._sender = sender;
		this._receptionist = receptionist;
	}
	
	//handle non-requestet received packages
	@SuppressWarnings("unchecked")
	public void heartbeat() {
		for (Entry<PipelineTag, PipelineRequest> receive : this.received.entrySet().stream().toArray(c -> new Entry[c])) {
			switch (receive.getKey().getType()) {
			default:
			case INFO:
				this._receptionist.info(receive.getValue());
				break;
			case REQUEST:
				Object result = this._receptionist.request(receive.getValue());
				this._sender.answer(receive.getKey().getId(), result);
				break;
			}
			
			this.received.remove(receive.getKey());
		}
	}

	public void receiveLine(String line) throws IOException {
		if(line == null || line.trim().length() == 0) {
			return;
		}
		
		// not started
		if(_start == null) {
			PipelineTag start = parseTag("start", line);
			
			// start incompatible?
			if(start == null) {
				return;
			}
			
			// a result, that not requested? ignore..
			if(start.getType() == PipelineType.RESULT && !this._storage.isExpect(start.getId())) {
				return;
			}
			
			this._start = start;
			
			return;
		}
		
		// detect end
		PipelineTag end = parseTag("end", line);
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
				this.received.put(start, PipelineSharedHelper.deserialize(PipelineRequest.class, content));
				break;
			case RESULT:
				this._storage.addResult(start.getId(), PipelineSharedHelper.deserialize(start.getCls(), content));
				break;
			}
			
			return;
		}
		
		this._cache.append(line);
	}
	
	private static PipelineTag parseTag(String tag, String line) {
		tag = tag.trim();
		line = line.trim();
		if(line.matches("#" + tag.toUpperCase() + ";[0-9]*;[a-zA-Z0-9]*;[a-fA-F0-9]{64};[a-zA-Z0-9_.]*;")) {
			String[] parts = line.split(";");
			int id = Integer.parseInt(parts[1]);
			PipelineType type = PipelineType.valueOf(parts[2]);
			String hash = parts[3];
			String cls = parts[4];
			try {
				return new PipelineTag(id, type, hash, Class.forName(cls));
			} catch (ClassNotFoundException e) {
				LoggingService.error(e);
			}
		}
		return null;
	}
	

	
}
