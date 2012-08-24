package nl.nekoconeko.glaciercmd.constants;

public enum AWSGlacierRegion {
	VIRGINIA("glacier.us-east-1.amazonaws.com"), OREGON("glacier.us-west-2.amazonaws.com"), CALIFORNIA("glacier.us-west-1.amazonaws.com"), IRELAND("glacier.eu-west-1.amazonaws.com"), TOKYO("glacier.ap-northeast-1.amazonaws.com");

	private String endpoint;

	private AWSGlacierRegion(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getEndpoint() {
		return this.endpoint;
	}
}
