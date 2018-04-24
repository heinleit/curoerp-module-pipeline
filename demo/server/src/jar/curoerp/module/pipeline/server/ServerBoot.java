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
		this._pipelineService.keyFile(new File("resources/keystore.jks"), "password");
		try {
			this._pipelineService.start(2876); //C(2)U(8)R(7)O(6)
		} catch (PipelineAlreadyListenException | PipelinePortAlreadyInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
