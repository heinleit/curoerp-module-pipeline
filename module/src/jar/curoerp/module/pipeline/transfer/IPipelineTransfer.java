package jar.curoerp.module.pipeline.transfer;

public interface IPipelineTransfer {
	public void send(String str);
	public void addReceiver(IPipelineTransferReceiver receiver);
}
