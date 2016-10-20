package redis.clients.jedis.exceptions;

import java.net.SocketTimeoutException;

import redis.clients.util.Observables;

public class JedisException extends RuntimeException {
	
  private static final long serialVersionUID = -2946266495682282677L;

  public JedisException(String message) {
    super(message);
    Observables.jedisExceptionObservable.notifyObservers();
  }

  public JedisException(Throwable e) {
    super(e);
    Observables.jedisExceptionObservable.notifyObservers();
    if(e instanceof SocketTimeoutException)
    	Observables.socketTimeoutExceptionObservable.notifyObservers();
  }

  public JedisException(String message, Throwable cause) {
    super(message, cause);
    Observables.jedisExceptionObservable.notifyObservers();
    if(cause instanceof SocketTimeoutException)
    	Observables.socketTimeoutExceptionObservable.notifyObservers();
  }
}
