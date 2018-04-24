package jar.curoerp.module.pipeline.server;

import java.io.File;

import de.curoerp.core.modularity.module.IBootModule;
import jar.curoerp.module.pipeline.exceptions.PipelineAlreadyListenException;
import jar.curoerp.module.pipeline.exceptions.PipelinePortAlreadyInUseException;
import jar.curoerp.module.pipeline.interfaces.IPipelineCenterService;

public class ServerBoot implements IBootModule {

	private IPipelineCenterService _pipelineService;

	public ServerBoot(IPipelineCenterService pipelineService) {
		this._pipelineService = pipelineService;
	}
	
	@Override
	public void boot() {
		this._pipelineService.keyFile(new File("/home/hheinle/curoerp/crt/keystore.jks"), "GeheimesKennwort");
		try {
			this._pipelineService.start(12388);
		} catch (PipelineAlreadyListenException | PipelinePortAlreadyInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
