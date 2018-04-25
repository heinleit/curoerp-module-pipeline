package jar.curoerp.module.pipeline.satellite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.yaml.snakeyaml.reader.StreamReader;

import de.curoerp.core.functionality.config.IConfigService;
import de.curoerp.core.functionality.info.ICoreInfo;
import de.curoerp.core.logging.LoggingService;
import jar.curoerp.module.pipeline.PipelineSession;
import jar.curoerp.module.pipeline.interfaces.IPipelineSatteliteService;

public class PipelineSatteliteService implements IPipelineSatteliteService {

	private PipelineSession _pipelineSession;
	private PipelineSatteliteConfig _config;
	private Socket _socket;
	
	public PipelineSatteliteService(ICoreInfo coreInfo, IConfigService configService) {
		this._pipelineSession = new PipelineSession(coreInfo.getApplicationName());
		this._config = configService.loadConfig("pipeline-sattelite", PipelineSatteliteConfig.class).instance;

		System.setProperty("javax.net.ssl.trustStore", this._config.KeyStore);
	}

	@Override
	public void connect(String host, int port) {
		try {
			TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}};

			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, trustAllCerts, new java.security.SecureRandom());

			SSLSocketFactory sslsocketfactory = context.getSocketFactory();
			this._socket = (SSLSocket) sslsocketfactory.createSocket(host, port);
			
			this.createReader().start();
		} catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean _isReader = false;
	private Thread createReader() {
		return new Thread(() -> {
			if(this._isReader) {
				return;
			}
			StringBuilder cache = new StringBuilder();
			int started = -1;
			
			while(this._socket != null) {
				this._isReader = true;
				
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(this._socket.getInputStream()));
					String line = null;
					while((line = reader.readLine()) != null) {
						if(line == null || line.trim().length() == 0) {
							continue;
						}
						
						if(started < 0) {
							if(line.matches("START:[0-9]*;")) {
								started = Integer.parseInt(line.substring(6, line.length() - 1));
							}
							continue;
						}
						
						if(line.matches("END:[0-9]*;")) {
							int end = Integer.parseInt(line.substring(4, line.length() - 1));
							if(started != end) {
								cache = new StringBuilder();
								throw new IOException("start(" + started + ") != end(" + end + ")");
							}
							
							this.receive(started, cache.toString());
							cache = new StringBuilder();
							continue;
						}
						
						cache.append(line);
					}
					
				} catch (IOException e) {
					LoggingService.error(e);
				}
				
			}
			cache = null;
			this._isReader = false;
		});
	}
	
	private void receive(int ident, String json) {
		
	}
	
	

}
