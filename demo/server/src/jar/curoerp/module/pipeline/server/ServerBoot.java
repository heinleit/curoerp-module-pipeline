package jar.curoerp.module.pipeline.server;

import java.io.File;

import de.curoerp.core.functionality.config.IConfigService;
import de.curoerp.core.modularity.module.IBootModule;
import jar.curoerp.module.pipeline.exceptions.PipelineAlreadyListenException;
import jar.curoerp.module.pipeline.exceptions.PipelinePortAlreadyInUseException;
import jar.curoerp.module.pipeline.interfaces.IPipelineCenterService;

public class ServerBoot implements IBootModule {

	private IPipelineCenterService _pipelineService;
	private IConfigService _configService;

	public ServerBoot(IPipelineCenterService pipelineService, IConfigService configService) {
		this._pipelineService = pipelineService;
		this._configService = configService;
	}
	
	@Override
	public void boot() {
		try {
			PipelineServerConfig config = _configService.loadConfig("pipeline-server", PipelineServerConfig.class).instance;
			this._pipelineService.keyFile(new File(config.KeyFile), config.KeyPassword);
			this._pipelineService.start(config.Port); //C(2)U(8)R(7)O(6)
		} catch (PipelineAlreadyListenException | PipelinePortAlreadyInUseException | NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
