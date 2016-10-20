package redis.clients.jedis.tests.utils;

import static org.junit.Assert.*;
import static redis.clients.jedis.tests.utils.AssertUtil.assertByteArrayListEquals;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.junit.Test;

import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisBusyException;
import redis.clients.jedis.exceptions.JedisClusterException;
import redis.clients.jedis.tests.FragmentedByteArrayInputStream;
import redis.clients.jedis.tests.commands.JedisCommandTestBase;
import redis.clients.util.Observables;
import redis.clients.util.RedisInputStream;
import redis.clients.util.RedisOutputStream;
import redis.clients.util.SafeEncoder;

public class ObservablesTest extends JedisCommandTestBase{

	private static class ObserverWithCounter implements Observer {
		public int numberOfTimeCalled = 0;

		@Override
		public void update(Observable o, Object arg) {
			numberOfTimeCalled++;
		}
	}

	private static class ObserverWithIntegerValue implements Observer {
		public int value = -1;

		@Override
		public void update(Observable o, Object arg) {
			value = (Integer) arg;
		}
	}

	@Test
	public void testJedisExceptionObservable() {
		Observables.ENABLE_OBSERVABLES = true;
		ObserverWithCounter observer = new ObserverWithCounter();
		Observables.jedisExceptionObservable.addObserver(observer);

		try {
			throw new JedisBusyException("");
		} catch (Exception e) {
		}
		try {
			throw new JedisClusterException(new Throwable());
		} catch (Exception e) {
		}
		try {
			throw new JedisClusterException("something", new Throwable());
		} catch (Exception e) {
		}

		assertEquals(3, observer.numberOfTimeCalled);
	}

	@Test
	public void testSocketTimeoutExceptionObservable() {
		Observables.ENABLE_OBSERVABLES = true;
		ObserverWithCounter observer = new ObserverWithCounter();
		Observables.socketTimeoutExceptionObservable.addObserver(observer);

		// Should not be counted as socketTimeoutException
		try {
			throw new JedisBusyException("");
		} catch (Exception e) {
		}
		// Should not be counted as socketTimeoutException
		try {
			throw new JedisClusterException(new Throwable());
		} catch (Exception e) {
		}
		// Should not be counted as socketTimeoutException
		try {
			throw new JedisClusterException("something", new Throwable());
		} catch (Exception e) {
		}

		try {
			throw new JedisClusterException(new SocketTimeoutException());
		} catch (Exception e) {
		}
		try {
			throw new JedisClusterException("something", new SocketTimeoutException());
		} catch (Exception e) {
		}

		assertEquals(2, observer.numberOfTimeCalled);

	}

	@Test
	public void buildACommand() throws IOException {
		Observables.ENABLE_OBSERVABLES = true;
		ObserverWithIntegerValue bytesSentObserver = new ObserverWithIntegerValue();
		Observables.bytesSendObservable.addObserver(bytesSentObserver);
		ObserverWithIntegerValue bytesReceivedObserver = new ObserverWithIntegerValue();
		Observables.bytesReceivedObservable.addObserver(bytesReceivedObserver);

		jedis.hset("foo", "bar", "car");
	    assertEquals(null, jedis.hget("bar", "foo"));
	    assertEquals(null, jedis.hget("foo", "car"));
	    assertEquals("car", jedis.hget("foo", "bar"));
	    
		assertEquals(0, bytesSentObserver.value);
		assertEquals(0, bytesReceivedObserver.value);
	}


}
