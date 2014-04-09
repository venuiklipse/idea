package za.co.idea.ip.ws.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NumericCounter {
	private AtomicInteger intUUID;
	private AtomicLong longUUID;

	public NumericCounter() {
		intUUID = new AtomicInteger();
		longUUID = new AtomicLong();
	}

	public Long nextLong() {
		return longUUID.getAndIncrement();
	}

	public Integer nextInt() {
		return intUUID.getAndIncrement();
	}
}
