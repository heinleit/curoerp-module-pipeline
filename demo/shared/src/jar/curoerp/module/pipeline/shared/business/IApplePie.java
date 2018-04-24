package jar.curoerp.module.pipeline.shared.business;

import jar.curoerp.module.pipeline.shared.models.Apple;

public interface IApplePie {
	
	public int getAmount();
	
	public Apple getApple();
	
	public void setApple(Apple apple, int amount);
	
}
