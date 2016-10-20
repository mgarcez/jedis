package redis.clients.util;

import java.util.Observable;

public class Observables {

	public static boolean ENABLE_OBSERVABLES = false;

	public final static Observable jedisExceptionObservable = new Observable() {
		@Override
		public void notifyObservers() {
			if (ENABLE_OBSERVABLES) {
				setChanged();
				notifyObservers(null);
			}
		}
	};

	public final static Observable socketTimeoutExceptionObservable = new Observable() {
		@Override
		public void notifyObservers() {
			if (ENABLE_OBSERVABLES) {
				setChanged();
				notifyObservers(null);
			}
		}
	};

	public final static Observable bytesSendObservable = new Observable() {
		@Override
		public void notifyObservers(Object arg) {
			if (ENABLE_OBSERVABLES) {
				setChanged();
				notifyObservers(arg);
			}
		}
	};

	public final static Observable bytesReceivedObservable = new Observable() {
		@Override
		public void notifyObservers(Object arg) {
			if (ENABLE_OBSERVABLES) {
				setChanged();
				notifyObservers(arg);
			}
		}
	};

}
