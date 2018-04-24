package jar.curoerp.module.pipeline.satellite;

import javax.net.ssl.SSLSocketFactory;

import de.curoerp.core.info.ICoreInfo;
import jar.curoerp.module.pipeline.PipelineSession;
import jar.curoerp.module.pipeline.interfaces.IPipelineSatteliteService;

public class PipelineSatteliteService implements IPipelineSatteliteService {
	
	private PipelineSession _pipelineSession;
	
	public PipelineSatteliteService(ICoreInfo coreInfo) {
		this._pipelineSession = new PipelineSession(coreInfo.getApplicationName());
	}

	@Override
	public void connect() {
		SSLSocketFactory.getDefault().createSocket(host, port)
	}

}
