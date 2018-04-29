package jar.curoerp.module.pipeline.demo.client;

import javax.swing.JOptionPane;

import jar.curoerp.module.pipeline.shared.business.IApplePie;
import jar.curoerp.module.pipeline.shared.models.Apple;

public class ApfelController  {

	private IApplePie _appleModel;
	private Apple _apple;

	public ApfelController(IApplePie apfelModel) {
		this._appleModel = apfelModel;
	}

	public void show() {
		this._apple = this._appleModel.getApple();
		this.showDialog();
	}

	private void showDialog() {
		JOptionPane.showMessageDialog(null, _apple.kind);
	}

}
