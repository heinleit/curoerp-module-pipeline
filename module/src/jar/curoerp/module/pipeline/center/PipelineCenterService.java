package jar.curoerp.module.pipeline.center;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import de.curoerp.core.logging.LoggingService;
import jar.curoerp.module.pipeline.exceptions.PipelineAlreadyListenException;
import jar.curoerp.module.pipeline.exceptions.PipelinePortAlreadyInUseException;
import jar.curoerp.module.pipeline.interfaces.IPipelineCenterService;

public class PipelineCenterService implements IPipelineCenterService {

	private SSLServerSocket _serverSocket;
	private List<PipelineCenterConnectionDealer> _dealer = new ArrayList<>();

	@Override
	public void start(int port) throws PipelineAlreadyListenException, PipelinePortAlreadyInUseException {
		LoggingService.debug("Startup PipelineCenter ServerSocket on Port '" + port + "'");
		if(this._serverSocket != null) {
			LoggingService.debug("a Serversocket allready run (singleton!)");
			throw new PipelineAlreadyListenException();
		}

		try {
			SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			this._serverSocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(port);
			LoggingService.debug("ServerSocket on Port '" + port + "' successful started!");
		} catch (IOException e) {
			throw new PipelinePortAlreadyInUseException();
		}

		this.waitForConnections();
		this.socketCollector();
	}

	private void socketCollector() {
		new Thread(() -> {
			while(PipelineCenterService.this._serverSocket != null) {
				this.collect();
			}
		}).start();
	}
	
	private void collect() {
		for (PipelineCenterConnectionDealer dealer : this._dealer.toArray(new PipelineCenterConnectionDealer[this._dealer.size()])) {
			try {
				// Socket is shuted down?
				if(this._serverSocket == null) {
					this.close(dealer);
					continue;
				}
				
				Instant trouble = dealer.getTrouble();
				if(trouble != null) {
					if(trouble.plus(10, ChronoUnit.MINUTES).getEpochSecond() >= Instant.now().getEpochSecond()) {
						this.close(dealer);
					}
				}
			} catch (Exception e) {
				LoggingService.error(e);
			}
		}
	}
	
	

	private void waitForConnections() {
		new Thread(() -> {
			try {
				while(this._serverSocket != null) {
					Socket socket = this._serverSocket.accept();
					PipelineCenterConnectionDealer dealer = new PipelineCenterConnectionDealer(socket);
					this._dealer.add(dealer);
				}
			} catch (Exception e) {
				LoggingService.warn(e);
			}
		}).start();
	}

	@Override
	public void stop() {
		try {
			this._serverSocket.close();
		} catch (IOException e) {
			LoggingService.warn(e);
		}
		this._serverSocket = null;
		
		this.collect();
	}

	@Override
	public void keyFile(File keyStore, String password) {
		System.setProperty("javax.net.ssl.keyStore", keyStore.toString());
		System.setProperty("javax.net.ssl.keyStorePassword", password);
	}

	@Override
	public void close(PipelineCenterConnectionDealer dealer) {
		dealer.close();
		this._dealer.remove(dealer);
	}

}
