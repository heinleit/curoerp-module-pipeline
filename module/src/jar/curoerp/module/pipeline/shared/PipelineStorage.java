package jar.curoerp.module.pipeline.shared;

import java.util.ArrayList;
import java.util.HashMap;

public class PipelineStorage {
	private final ArrayList<Integer> expect = new ArrayList<>();
	
	public boolean isExpect(int id) {
		return this.expect.contains(id);
	}
	
	public void removeExpect(int id) {
		this.expect.remove(this.expect.indexOf(id));
	}
	
	public void addExpect(int id) {
		if(id < 0 || this.isExpect(id))
			return;
		this.expect.add(id);
	}
	
	private final HashMap<Integer, Object> result = new HashMap<>();
	
	public void addResult(int id, Object obj) {
		this.result.put(id, obj);
	}
	
	public boolean isResult(int id) {
		return this.result.containsKey(id);
	}
	
	public Object getAndRemoveResult(int id) {
		this.removeExpect(id);
		Object object = this.result.get(id);
		this.result.remove(id);
		return object;
	}
	
}
