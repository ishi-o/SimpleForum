package com.dlut.simpleforum.common.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

/**
 * @author Ishi_O
 * @since
 */
public class HessianRedisSerializer<T> implements RedisSerializer<T> {
	@Override
	public byte[] serialize(@Nullable T arg0) throws SerializationException {
		if (arg0 == null) {
			return new byte[0];
		}
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			Hessian2Output hessian2Output = new Hessian2Output(os);
			hessian2Output.writeObject(arg0);
			hessian2Output.flush();
			hessian2Output.close();
			return os.toByteArray();
		} catch (IOException ex) {
			throw new SerializationException(ex.getMessage());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public @Nullable T deserialize(byte @Nullable [] arg0) throws SerializationException {
		if (arg0 == null || arg0.length == 0) {
			return null;
		}
		try (ByteArrayInputStream is = new ByteArrayInputStream(arg0);) {
			Hessian2Input hessian2Input = new Hessian2Input(is);
			Object obj = hessian2Input.readObject();
			hessian2Input.close();
			return (T) obj;
		} catch (IOException ex) {
			throw new SerializationException(ex.getMessage());
		}
	}

}
