package com.hackquest.shenlong55.instances.instances;

public final class InstanceError extends Exception
{
	private static final long serialVersionUID = 1L;

	public InstanceError(final String errorMessage)
	{
		super(errorMessage);
	}
}