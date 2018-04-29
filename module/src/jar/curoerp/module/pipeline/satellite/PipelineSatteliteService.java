package jar.curoerp.module.pipeline.satellite;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import de.curoerp.core.logging.LoggingService;
import jar.curoerp.module.pipeline.interfaces.IPipelineListener;
import jar.curoerp.module.pipeline.interfaces.IPipelineSatteliteService;
import jar.curoerp.module.pipeline.interfaces.IPipelineService;
import jar.curoerp.module.pipeline.shared.PipelineRequest;
import jar.curoerp.module.pipeline.shared.PipelineSendParser;
import jar.curoerp.module.pipeline.shared.PipelineSocketBase;

public class PipelineSatteliteService extends PipelineSocketBase implements IPipelineSatteliteService, IPipelineListener {
	

	//private PipelineSendParser _sender;
	private IPipelineService _service;

	public PipelineSatteliteService(IPipelineService service) {
		super();
		this._service = service;
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
			this.createWriter().start();
		} catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
			LoggingService.error(e);
		}
		
		this._service.setListener((IPipelineListener)this);
	}
	
	
	

	@Override
	public void info(PipelineRequest request) {
		//this is client: ignored currently
	}

	@Override
	public Object request(PipelineRequest request) {
		//this is client: ignored currently
		return null;
	}

	@Override
	public Object request(Method method, Object[] args) {
		PipelineRequest request = new PipelineRequest();
		request.arguments = args;
		request.method = method.getName();
		request.cls = method.getDeclaringClass().getName();
		
		return this._sender.sendAndReceive(request);
	}

}
