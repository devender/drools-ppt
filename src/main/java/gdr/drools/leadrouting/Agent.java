package gdr.drools.leadrouting;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Agent {
	private double conversionRate;
	private Type type;
	private int fulfilled;
	private String name;
	private String state;

	public Agent(double conversionRate,Type type,int fulfilled, String name,String state) {
		this.conversionRate = conversionRate;
		this.type = type;
		this.fulfilled = fulfilled;
		this.name = name;
		this.state = state;
	}
	
	public double getConversionRate() {
		return conversionRate;
	}

	public String getType() {
		return type.name();
	}

	public int getFulfilled() {
		return fulfilled;
	}  
	
	public String getName() {
		return name;
	}

	public int hashCode() {
		return new HashCodeBuilder(27, 57).append(conversionRate).append(type).append(fulfilled).hashCode();
	}
	
	public String getState() {
		return state;
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
		Agent rhs = (Agent) obj;

		return new EqualsBuilder().append(conversionRate, rhs.conversionRate).append(type, rhs.type).append(fulfilled, rhs.fulfilled).isEquals();
	}
}
