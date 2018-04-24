package jar.curoerp.module.pipeline.satellite;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

import de.curoerp.core.info.ICoreInfo;
import jar.curoerp.module.pipeline.PipelineSession;
import jar.curoerp.module.pipeline.interfaces.IPipelineSatteliteService;

public class PipelineSatteliteService implements IPipelineSatteliteService {
	
	private PipelineSession _pipelineSession;
	
	public PipelineSatteliteService(ICoreInfo coreInfo) {
		this._pipelineSession = new PipelineSession(coreInfo.getApplicationName());
	}

	@Override
	public void connect(String host, int port) {
		try {
			SSLSocket socket = (SSLSocket) SSLContext.getInstance("TLS").getSocketFactory().createSocket(host, port);
			socket.startHandshake();
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			pw.println("HALLO SERVIERER");
		} catch (IOException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
