package jar.curoerp.module.pipeline;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import de.curoerp.core.logging.LoggingService;
import jar.curoerp.module.pipeline.receptionist.IPipelineReceptionist;

public class PipelineServerService {

	private SSLServerSocket _serverSocket;
	private ArrayList<PipelineServerConnector> _connectors = new ArrayList<>();
	private IPipelineReceptionist receptionist;

	public PipelineServerService(IPipelineReceptionist receptionist) {
		this.receptionist = receptionist;
	}

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
		} 
		catch (IOException e) {
			LoggingService.debug(e);
			throw new PipelinePortAlreadyInUseException();
		}

		this.waitForConnections();
		this.socketCollector();
	}

	private void socketCollector() {
		new Thread(() -> {
			while(this._serverSocket != null) {
				this.collect();
			}
		}).start();
	}

	private void collect() {
		for (PipelineServerConnector connector : this._connectors.toArray(new PipelineServerConnector[this._connectors.size()])) {
			try {
				// Dealer is null?
				if(connector == null) {
					continue;
				}

				// Socket is shuted down?
				if(this._serverSocket == null) {
					this.close(connector);
					continue;
				}

				if(connector.canClose()) {
					this.close(connector);
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
					PipelineServerConnector connector = new PipelineServerConnector(socket, this.receptionist);
					this._connectors.add(connector);
				}
			} catch (Exception e) {
				LoggingService.warn(e);
			}
		}).start();
	}

	public void stop() {
		try {
			this._serverSocket.close();
		} catch (IOException e) {
			LoggingService.warn(e);
		}
		this._serverSocket = null;

		this.collect();
	}

	public void keyFile(File keyStore, String password) {
		System.out.println(keyStore.toString());
		System.out.println(password);
		System.setProperty("javax.net.ssl.keyStore", keyStore.toString());
		System.setProperty("javax.net.ssl.keyStorePassword", password);
	}

	public void close(PipelineServerConnector connector) {
		connector.close();
		this._connectors.remove(connector);
	}

}
