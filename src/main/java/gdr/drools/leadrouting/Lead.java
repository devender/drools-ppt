package gdr.drools.leadrouting;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Lead {
	private int credits = 0;
	private Type type;
	private String agent;

	public Lead(int credits, Type type) {
		this.credits = credits;
		this.type = type;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getAgent() {
		return agent;
	}

	public int getCredits() {
		return credits;
	}

	public String getType() {
		return type.name();
	}

	public int hashCode() {
		return new HashCodeBuilder(27, 57).append(credits).append(type).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Lead rhs = (Lead) obj;

		return new EqualsBuilder().append(credits, rhs.credits).append(type, rhs.type).isEquals();
	}
}
