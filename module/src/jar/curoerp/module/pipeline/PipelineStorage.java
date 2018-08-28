package jar.curoerp.module.pipeline;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

import de.curoerp.core.logging.LoggingService;

public class PipelineStorage {
	
	/*
	 * Socket
	 */
	private Socket socket;
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public void closeSocket() {
		try {
			this.socket.close();
		} catch (IOException e) {
			LoggingService.error(e);
		}
		this.socket = null;
	}
	
	public InputStream getSocketInputStream() throws IOException {
		return this.socket.getInputStream();
	}
	
	public OutputStream getSocketOutputStream() throws IOException {
		return this.socket.getOutputStream();
	}
	
	public boolean isSocketOpen() {
		return this.socket != null;
	}
	
	/*
	 * Output queue
	 * 
	 * FIFO (unperformant, but necessary)
	 */
	
	private ArrayList<String> output = new ArrayList<>();
	
	public void addOutput(String str) {
		this.output.add(str);
	}
	
	public String next() {
		if(this.output.size() == 0) {
			return null;
		}
		
		String first = this.output.get(0);
		this.output.remove(0);
		return first;
	}
	
	
	/*
	 * Trouble
	 */
	
	private Instant trouble;
	
	public void setTrouble() {
		this.trouble = Instant.now();
	}
	
	public void solveTrouble() {
		this.trouble = null;
	}
	
	public boolean trouble(int minutes) {
		if(trouble == null) {
			return false;
		}
		
		return trouble.plus(minutes, ChronoUnit.MINUTES).getEpochSecond() >= Instant.now().getEpochSecond();
	}
	
	/*
	 * Expect
	 */
	
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
	
	/*
	 * result
	 */
	
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
	
	public String getClientIdentifier() {
		return this.socket != null ? this.socket.toString() : "nav";
	}
	
}
