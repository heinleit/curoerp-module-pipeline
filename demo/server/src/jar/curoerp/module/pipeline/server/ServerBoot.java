package jar.curoerp.module.pipeline.server;

import java.io.File;

import de.curoerp.core.functionality.config.IConfigService;
import de.curoerp.core.modularity.module.IBootModule;
import jar.curoerp.module.pipeline.PipelineAlreadyListenException;
import jar.curoerp.module.pipeline.PipelinePortAlreadyInUseException;
import jar.curoerp.module.pipeline.PipelineServerService;

public class ServerBoot implements IBootModule {

	private PipelineServerService server;
	private IConfigService _configService;

	public ServerBoot(PipelineServerService server, IConfigService configService) {
		this.server = server;
		this._configService = configService;
	}
	
	@Override
	public void boot() {
		try {
			PipelineServerConfig config = _configService.loadConfig("pipeline-server", PipelineServerConfig.class).instance;
			this.server.keyFile(new File(config.KeyFile), config.KeyPassword);
			this.server.start(config.Port); //C(2)U(8)R(7)O(6)
		} catch (PipelineAlreadyListenException | PipelinePortAlreadyInUseException | NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
