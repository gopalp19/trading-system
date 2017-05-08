package Messenger;

/**
 * Exception to be thrown when message or message text is invalid.
 * Created by Alan on 5/7/2017.
 */
public class MalformedMessageException extends RuntimeException {
    MalformedMessageException() {
        super();
    }

    MalformedMessageException(String exceptionMessage) {
        super(exceptionMessage);
    }

    MalformedMessageException(String exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    MalformedMessageException(String exceptionMessage, Throwable cause, boolean enableSuppression, boolean writeableStackTrace) {
        super(exceptionMessage, cause, enableSuppression, writeableStackTrace);
    }

    MalformedMessageException(Throwable cause) {
        super(cause);
    }
}
