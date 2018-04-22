package jar.curoerp.module.pipeline.demo.client;

import javax.swing.JOptionPane;

import jar.curoerp.module.pipeline.demo.shared.Apple;
import jar.curoerp.module.pipeline.demo.shared.IAppleModel;

public class ApfelController  {

	private IAppleModel _appleModel;
	private Apple[] _apple;

	public ApfelController(IAppleModel apfelModel) {
		this._appleModel = apfelModel;
	}
	
	public void show() {
		this._apple = this._appleModel.getApples();
		this.showDialog();
	}

	private void showDialog() {
		for (Apple apfel : _apple) {
			// our 'view'^^ (thats real cheating)
			JOptionPane.showMessageDialog(null, apfel.kind);
		}
	}

}
