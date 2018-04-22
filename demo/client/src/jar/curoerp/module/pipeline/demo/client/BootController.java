package jar.curoerp.module.pipeline.demo.client;

import de.curoerp.core.modularity.module.IBootModule;

public class BootController implements IBootModule {

	private ApfelController _apfelController;

	public BootController(ApfelController apfelController) {
		this._apfelController = apfelController;
	}
	
	@Override
	public void boot() {
		this._apfelController.show();
	}
}
