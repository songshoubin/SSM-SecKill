package com.gdufe.exception;

/**
 * 秒杀相关的所有业务异常
 * @author song
 */
public class SeckillException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7605046495037863705L;

	public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}