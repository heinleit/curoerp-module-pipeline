package jar.curoerp.module.pipeline.interfaces;

import java.io.File;

import jar.curoerp.module.pipeline.center.PipelineCenterConnectionDealer;
import jar.curoerp.module.pipeline.exceptions.PipelineAlreadyListenException;
import jar.curoerp.module.pipeline.exceptions.PipelinePortAlreadyInUseException;

public interface IPipelineCenterService {
	
	public void keyFile(File keyStore, String password);
	public void start(int port) throws PipelineAlreadyListenException, PipelinePortAlreadyInUseException;
	public void stop();
	public void close(PipelineCenterConnectionDealer dealer);

}
