package jar.curoerp.module.pipeline.client;

import de.curoerp.core.modularity.module.IBootModule;
import jar.curoerp.module.pipeline.shared.business.IApplePie;

public class PipelineClient implements IBootModule {

	private IApplePie _applePie;

	public PipelineClient(IApplePie applePie) {
		this._applePie = applePie;
	}
	
	@Override
	public void boot() {
		this._applePie.getApple();
	}

}
