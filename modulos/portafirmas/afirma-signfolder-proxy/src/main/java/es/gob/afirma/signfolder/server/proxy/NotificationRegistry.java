package es.gob.afirma.signfolder.server.proxy;

class NotificationRegistry {

	private final String deviceId;
	private final String platform;
	private final String idReg;

	NotificationRegistry(final String deviceId, final String platform, final String idReg) {
		this.deviceId = deviceId;
		this.platform = platform;
		this.idReg = idReg;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public String getPlatform() {
		return this.platform;
	}

	public String getIdRegistry() {
		return this.idReg;
	}
}
