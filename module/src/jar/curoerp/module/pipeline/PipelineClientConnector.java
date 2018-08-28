package jar.curoerp.module.pipeline;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import de.curoerp.core.logging.LoggingService;
import jar.curoerp.module.pipeline.proxy.IPipelineSenderProxy;
import jar.curoerp.module.pipeline.receptionist.IPipelineReceptionist;

public class PipelineClientConnector {

	private Socket socket;
	private PipelineService service;
	private IPipelineReceptionist receptionist;
	private IPipelineSenderProxy senderProxy;

	public PipelineClientConnector(IPipelineReceptionist receptionist, IPipelineSenderProxy senderProxy) {
		this.receptionist = receptionist;
		this.senderProxy = senderProxy;
	}

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
			this.socket = (SSLSocket) sslsocketfactory.createSocket(host, port);
			// Service
			this.service = new PipelineService(this.socket, this.receptionist);
			this.service.start();

			this.senderProxy.setListener(this.service);
		} catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
			LoggingService.error(e);
		}

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
