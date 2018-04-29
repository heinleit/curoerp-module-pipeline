package jar.curoerp.module.pipeline.demo.server;

import jar.curoerp.module.pipeline.shared.business.IApplePie;
import jar.curoerp.module.pipeline.shared.models.Apple;

public class ApplePie implements IApplePie {

	
	@Override
	public int getAmount() {
		return 12;
	}

	@Override
	public Apple getApple() {
		Apple apple = new Apple();
		apple.kind = "Elster";
		return apple;
	}

	@Override
	public void setApple(Apple apple, int amount) {
		// TODO Auto-generated method stub
		
	}

}
